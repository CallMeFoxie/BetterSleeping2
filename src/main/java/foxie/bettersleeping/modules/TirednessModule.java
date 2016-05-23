package foxie.bettersleeping.modules;

import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.PlayerSyncStatus;
import foxie.bettersleeping.api.BetterSleepingAPI;
import foxie.bettersleeping.api.PlayerBSData;
import foxie.bettersleeping.api.PlayerSleepEvent;
import foxie.bettersleeping.api.WorldSleepEvent;
import foxie.bettersleeping.core.BSEvents;
import foxie.bettersleeping.core.Core;
import foxie.bettersleeping.network.MessageUpdateTiredness;
import foxie.bettersleeping.network.Network;
import foxie.calendar.api.CalendarAPI;
import foxie.calendar.api.ICalendarProvider;
import foxie.lib.Configurable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class TirednessModule extends Module {
   public static DamageSource tirednessDamage = new DamageSource("tiredness").setDamageBypassesArmor();
   @Configurable(comment = "How much energy is regained per slept tick", min = "0")
   private static double regainedEnergyPerSleptTick = 3;
   @Configurable(comment = "Disable overcharging energy")
   private static boolean capEnergy = true;
   @Configurable(comment = "Maximum energy to allow sleeping at")
   private static long maximumEnergy = 24000;
   @Configurable(comment = "Minimum energy to allow sleeping at")
   private static long minimumEnergy = 8000;
   @Configurable(comment = "At which energy the player falls asleep on the ground (-1 to disable)")
   private static long sleepOnGroundAt = 200;
   @Configurable(comment = "How much energy is lost per awake tick", min = "0")
   private static long energyPerAwakeTick = 1;
   @Configurable(comment = "Energy to spawn with", min = "0")
   private static long energyToSpawnWith = 48000;
   @Configurable(comment = "Should player die when they reach zero energy? (if sleeping on the ground at == 0 then they will die first)")
   private static boolean dieOnExhaustion = true;
   @Configurable(comment = "Wake up time (24h day cycle, morning = 6h)")
   private static int wakeupHour = 6;
   @Configurable(comment = "Wake up with sleeping cap")
   private static boolean wakeupOnCap = false;
   @Configurable(comment = "Ticks before a player falls asleep", min = "0", max = "100")
   private static int dozingTimer = 100;
   @Configurable(comment = "How much food is taken by every slept tick", min = "0")
   private static double hungerPerSleptTick = 0.00001d;
   @Configurable(comment = "Do players get tired? (Disables everything related)")
   private static boolean playersGetTired = true;

   public static long getSpawnEnergy() {
      return energyToSpawnWith;
   }

   public static long getGranularity() {
      return maximumEnergy / 24;
   }

   public static boolean doPlayersGetTired() {
      return playersGetTired;
   }

   @SubscribeEvent
   public void playerSlept(PlayerSleepEvent.PlayerSleptEvent event) {
      if (event.getPlayer().worldObj.isRemote)
         return;

      PlayerBSData data = BetterSleepingAPI.getSleepingProperty(event.getPlayer());

      if (playersGetTired) {
         long currentEnergy = data.getEnergy();
         if (capEnergy)
            data.setEnergy((long) Math.min(maximumEnergy, currentEnergy + event.getTime() * regainedEnergyPerSleptTick));
         else
            data.addEnergy((long) (event.getTime() * regainedEnergyPerSleptTick));
      }

      // take care of hunger eating
      double decreaseHunger = Math.max(0, event.getTime() * hungerPerSleptTick);
      event.getPlayer().getFoodStats().addStats((int) -decreaseHunger, 0f);
   }

   @SubscribeEvent
   public void playerTick(TickEvent.PlayerTickEvent event) {
      if (event.player.worldObj.isRemote || !playersGetTired || event.player.capabilities.isCreativeMode)
         return;

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
      } else if (event.player.isPlayerSleeping()) {
         data.addEnergy((long) regainedEnergyPerSleptTick);
      }

      // check for resyncing
      PlayerSyncStatus status = BetterSleeping.INSTANCE.getSyncData(event.player);
      if (Math.abs(status.lastSyncEnergy - data.getEnergy()) > getGranularity() && event.player instanceof EntityPlayerMP) {
         Network.networkChannel.sendTo(new MessageUpdateTiredness(data.getEnergy(), maximumEnergy), (EntityPlayerMP) event.player);
         status.lastSyncEnergy = data.getEnergy();
      }
   }

   @SubscribeEvent
   public void sleepOnGroundEvent(PlayerSleepEvent.SleepOnGroundEvent event) {
      if (event.getPlayer().worldObj.isRemote)
         return;

      if (sleepOnGroundAt < 0 && event.isCancelable())
         event.setCanceled(true);
   }

   @SubscribeEvent
   public void onWorldTick(TickEvent.WorldTickEvent event) {
      if (event.phase != TickEvent.Phase.START || event.world.isRemote)
         return;

      // take care of possible sleeping
      List<EntityPlayer> asleepPlayers = new ArrayList<EntityPlayer>();
      for (EntityPlayer player : event.world.playerEntities) {
         if (!player.isEntityAlive())
            continue;

         if (player.isPlayerFullyAsleep() && !BSEvents.isPlayerAllowedToSleep(player)) {
            asleepPlayers.add(player);
         }
      }

      // take care of legit sleeping
      if (asleepPlayers.size() == event.world.playerEntities.size() && asleepPlayers.size() > 0) {
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
      if (event.getWorld().isRemote)
         return;

      if (!wakeupOnCap) {
         ICalendarProvider calendar = CalendarAPI.getCalendarInstance(event.getWorld());
         // wake up by normal day cycles
         if (calendar.getHour() > wakeupHour) {
            calendar.setDay(calendar.getDay() + 1);
         }

         calendar.setHour(wakeupHour);

         event.setSleptTime(calendar.getTime() - CalendarAPI.getCalendarInstance(event.getWorld()).getTime(), WorldSleepEvent.PRIORITY_BUILTIN);

      } else {
         long ticks = event.getWorld().provider.getWorldTime();

         long maxSleep = 0;

         for (EntityPlayer player : event.getWorld().playerEntities) {
            PlayerBSData data = BetterSleepingAPI.getSleepingProperty(player);
            if (data.getEnergy() > maxSleep)
               maxSleep = data.getEnergy();
         }

         // possibly sleep? (maxEnergy - currentEnergy) / regainedEnergyPerSleptTick
         ticks += (maximumEnergy - maxSleep) / regainedEnergyPerSleptTick;

         event.setSleptTime(ticks - event.getWorld().provider.getWorldTime(), WorldSleepEvent.PRIORITY_BUILTIN);
      }
   }

   @SubscribeEvent
   public void isPlayerAllowedToSleep(PlayerSleepInBedEvent event) {
      if (event.getEntityPlayer().worldObj.isRemote || !playersGetTired)
         return;

      PlayerBSData data = BetterSleepingAPI.getSleepingProperty(event.getEntityPlayer());

      if (data.getEnergy() > maximumEnergy && capEnergy || data.getEnergy() > minimumEnergy) {
         event.getEntityPlayer().addChatMessage(new TextComponentTranslation("message.notTired"));
         event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
      }
   }

   @SubscribeEvent
   public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
      if (event.player.worldObj.isRemote)
         return;

      PlayerBSData data = BetterSleepingAPI.getSleepingProperty(event.player);
      data.setEnergy(energyToSpawnWith);
   }

   @SubscribeEvent
   public void isPlayerFullyAsleep(PlayerSleepEvent.IsPlayerFullyAsleepEvent event) {
      if (event.getEntityPlayer().worldObj.isRemote)
         return;

      if (!event.getEntityPlayer().isPlayerSleeping() || event.getTimer() < dozingTimer)
         event.setCanceled(true);
   }

   @Override
   public void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
      super.playerJoined(event);
      if (!doPlayersGetTired() && event.player instanceof EntityPlayerMP) {
         Network.networkChannel.sendTo(new MessageUpdateTiredness(0, -1), (EntityPlayerMP) event.player); // send the client info that we are not going to use this
      }
   }
}
