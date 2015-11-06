package foxie.bettersleeping.core;

import foxie.bettersleeping.api.PlayerSleepEvent;
import net.minecraft.entity.player.EntityPlayer;

public class Event {
   public static boolean isPlayerAllowedToSleep(EntityPlayer player) {
      PlayerSleepEvent.PlayerAllowedToSleep allowedToSleep = new PlayerSleepEvent.PlayerAllowedToSleep(player);

      return !allowedToSleep.isCanceled(); // negate the isCanceled because of how the vanilla code is setup
   }
}
