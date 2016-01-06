package foxie.bettersleeping.modules;

import foxie.bettersleeping.api.BetterSleepingAPI;
import foxie.bettersleeping.api.PlayerBSData;
import foxie.bettersleeping.api.PlayerSleepEvent;
import foxie.bettersleeping.api.WorldSleepEvent;
import foxie.bettersleeping.core.BSEvents;
import foxie.bettersleeping.core.Core;
import foxie.lib.Configurable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TirednessModule extends Module {
   public static DamageSource tirednessDamage = new DamageSource("tiredness").setDamageBypassesArmor();

   @Configurable(comment = "How much energy is regained per slept tick", min = "0")
   private static double  regainedEnergyPerSleptTick = 3;
   @Configurable(comment = "Disable overcharging energy")
   private static boolean capEnergy                  = true;
   @Configurable(comment = "Maximum energy to allow sleeping at")
   private static long    maximumEnergy              = 24000;
   @Configurable(comment = "At which energy the player falls asleep on the ground (-1 to disable)")
   private static long    sleepOnGroundAt            = 200;
   @Configurable(comment = "How much energy is lost per awake tick", min = "0")
   private static long    energyPerAwakeTick         = 1;
   @Configurable(comment = "Energy to spawn with", min = "0")
   private static long    energyToSpawnWith          = 48000;
   @Configurable(comment = "Should player die when they reach zero energy? (if sleeping on the ground at == 0 then they will die first)")
   private static boolean dieOnExhaustion            = true;

   public static long getSpawnEnergy() {
      return energyToSpawnWith;
   }

   @SubscribeEvent
   public void playerSlept(PlayerSleepEvent.PlayerSleptEvent event) {
      PlayerBSData data = BetterSleepingAPI.getSleepingProperty(event.getPlayer());

      long currentEnergy = data.getEnergy();

      if (capEnergy)
         data.setEnergy((long) Math.min(maximumEnergy, currentEnergy + event.getTime() * regainedEnergyPerSleptTick));
      else
         data.addEnergy((long) (event.getTime() * regainedEnergyPerSleptTick));
   }

   @SubscribeEvent
   public void playerTick(TickEvent.PlayerTickEvent event) {
      PlayerBSData data = BetterSleepingAPI.getSleepingProperty(event.player);

      if (!event.player.isPlayerSleeping() && event.player.isEntityAlive()) {
         data.addEnergy(-energyPerAwakeTick);

         if (data.getEnergy() <= 0 && dieOnExhaustion) {
            event.player.attackEntityFrom(tirednessDamage, 999999);
            return;
         }

         if (sleepOnGroundAt < 0)
            return;

         if (data.getEnergy() <= sleepOnGroundAt) {
            Core.trySleepingPlayerOnTheGround(event.player);
         }
      } else {
         data.addEnergy((long) regainedEnergyPerSleptTick);
      }
   }

   @SubscribeEvent
   public void sleepOnGroundEvent(PlayerSleepEvent.SleepOnGroundEvent event) {
      if (sleepOnGroundAt < 0 && event.isCancelable())
         event.setCanceled(true);
   }

   @SubscribeEvent
   public void onWorldTick(TickEvent.WorldTickEvent event) {
      if (event.phase != TickEvent.Phase.START || event.world.isRemote)
         return;

      // take care of possible sleeping
      boolean allPlayersAsleep = true;
      int playersAsleep = 0;
      for (EntityPlayer player : event.world.playerEntities) {
         if (!player.isEntityAlive())
            continue;

         if (!player.isPlayerFullyAsleep() || !BSEvents.isPlayerAllowedToSleep(player)) {
            allPlayersAsleep = false;
            break;
         } else {
            playersAsleep++;
         }
      }

      // take care of legit sleeping
      if (allPlayersAsleep && playersAsleep > 0) {
         for (EntityPlayer player : event.world.playerEntities) {
            BSEvents.playerFallingAsleep(player);
         }

         long timeSlept = BSEvents.getSleepingTime(event.world);

         for (EntityPlayer player : event.world.playerEntities) {
            BSEvents.playerSlept(player, timeSlept);
            // wake them up to prevent vanilla code kicking in
            player.wakeUpPlayer(true, true, true);
         }

         // set new world time
         event.world.provider.setWorldTime(event.world.provider.getWorldTime() + timeSlept);

         BSEvents.worldSlept(event.world, timeSlept);
      }
   }

   @SubscribeEvent
   public void onWorldSleepPre(WorldSleepEvent.Pre event) {
      // TODO calculate time
   }

   @SubscribeEvent
   public void isPlayerAllowedToSleep(PlayerSleepInBedEvent event) {
      PlayerBSData data = BetterSleepingAPI.getSleepingProperty(event.entityPlayer);

      if (data.getEnergy() > maximumEnergy && capEnergy) {
         event.entityPlayer.addChatMessage(new ChatComponentTranslation("message.notTired"));
         event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
      }
   }
}
