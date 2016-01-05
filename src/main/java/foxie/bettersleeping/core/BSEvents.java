package foxie.bettersleeping.core;

import foxie.bettersleeping.api.PlayerSleepEvent;
import foxie.bettersleeping.api.WorldSleepEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BSEvents {
   public static boolean isPlayerAllowedToSleep(EntityPlayer player) {
      PlayerSleepEvent.PlayerAllowedToSleepEvent allowedToSleep = new PlayerSleepEvent.PlayerAllowedToSleepEvent(player);
      MinecraftForge.EVENT_BUS.post(allowedToSleep);

      return !allowedToSleep.isCanceled(); // negate the isCanceled because of how the vanilla code is setup
   }

   public static void playerFallingAsleep(EntityPlayer player) {
      PlayerSleepEvent.PlayerFallingAsleepEvent fallingAsleep = new PlayerSleepEvent.PlayerFallingAsleepEvent(player);
      MinecraftForge.EVENT_BUS.post(fallingAsleep);
   }

   public static boolean canPlayerSleepOnTheGround(EntityPlayer player) {
      PlayerSleepEvent.SleepOnGroundEvent sleepOnGroundEvent = new PlayerSleepEvent.SleepOnGroundEvent(player);
      MinecraftForge.EVENT_BUS.post(sleepOnGroundEvent);

      return sleepOnGroundEvent.isCanceled();
   }

   public static long getSleepingTime(World world) {
      WorldSleepEvent.Pre event = new WorldSleepEvent.Pre(world);
      MinecraftForge.EVENT_BUS.post(event);

      return event.getSleptTime();
   }

   public static void playerSlept(EntityPlayer player, long timeSlept) {
      PlayerSleepEvent.PlayerSleptEvent event = new PlayerSleepEvent.PlayerSleptEvent(player, timeSlept);
      MinecraftForge.EVENT_BUS.post(event);
   }

   public static void worldSlept(World world, long timeSlept) {
      WorldSleepEvent.Post event = new WorldSleepEvent.Post(world, timeSlept);
      MinecraftForge.EVENT_BUS.post(event);
   }
}
