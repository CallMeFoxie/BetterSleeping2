package foxie.bettersleeping.core;

import foxie.bettersleeping.api.PlayerSleepEvent;
import foxie.bettersleeping.api.WorldSleepEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Event {
   public static boolean isPlayerAllowedToSleep(EntityPlayer player) {
      PlayerSleepEvent.PlayerAllowedToSleepEvent allowedToSleep = new PlayerSleepEvent.PlayerAllowedToSleepEvent(player);

      return !allowedToSleep.isCanceled(); // negate the isCanceled because of how the vanilla code is setup
   }

   public static boolean canPlayerFallSleep(EntityPlayer player) {
      PlayerSleepEvent.PlayerFallingAsleepEvent fallingAsleep = new PlayerSleepEvent.PlayerFallingAsleepEvent(player);

      return fallingAsleep.isCanceled();
   }

   public static boolean canPlayerSleepOnTheGround(EntityPlayer player) {
      PlayerSleepEvent.SleepOnGroundEvent sleepOnGroundEvent = new PlayerSleepEvent.SleepOnGroundEvent(player);

      return sleepOnGroundEvent.isCanceled();
   }

   public static long getSleepingTime(World world) {
      WorldSleepEvent.Pre event = new WorldSleepEvent.Pre(world);

      return event.getSleptTime();
   }

   public static void playerSlept(EntityPlayer player, long timeSlept) {
      PlayerSleepEvent.PlayerSleptEvent event = new PlayerSleepEvent.PlayerSleptEvent(player, timeSlept);
   }
}
