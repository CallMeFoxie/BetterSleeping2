package foxie.bettersleeping.core;

import net.minecraft.entity.player.EntityPlayer;

public class Core {
   public static void trySleepingPlayerOnTheGround(EntityPlayer player) {
      boolean allowed = BSEvents.canPlayerSleepOnTheGround(player);
      if (!allowed) {
         // failed :(
      } else {
         player.trySleep(player.getPosition());
      }
   }
}
