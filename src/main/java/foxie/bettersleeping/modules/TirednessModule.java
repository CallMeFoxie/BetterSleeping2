package foxie.bettersleeping.modules;

import foxie.bettersleeping.api.BetterSleepingAPI;
import foxie.bettersleeping.api.PlayerBSData;
import foxie.bettersleeping.api.PlayerSleepEvent;
import foxie.bettersleeping.core.Core;
import foxie.lib.Configurable;
import net.minecraft.util.DamageSource;
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

      if (!event.player.isPlayerSleeping()) {
         data.addEnergy(-energyPerAwakeTick);

         if (sleepOnGroundAt < 0)
            return;

         if (data.getEnergy() <= sleepOnGroundAt) {
            Core.trySleepingPlayerOnTheGround(event.player);
         }
      } else {
         data.addEnergy((long) regainedEnergyPerSleptTick);
      }
   }
}
