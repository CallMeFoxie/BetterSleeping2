package foxie.bettersleeping.core;

import net.minecraft.entity.player.EntityPlayer;

public class Core {
   public static void trySleepingPlayerOnTheGround(EntityPlayer player) {
      boolean allowed = BSEvents.canPlayerSleepOnTheGround(player);
      if (!allowed) {
         // failed :(
      } else {
         if (player.trySleep(player.getPosition()) == EntityPlayer.SleepResult.OK) {
            BSEvents.playerSleptOnTheGround(player);
         }
      }
   }
}
