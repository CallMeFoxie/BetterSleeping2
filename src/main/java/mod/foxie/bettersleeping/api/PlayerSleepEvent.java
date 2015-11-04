package mod.foxie.bettersleeping.api;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public abstract class PlayerSleepEvent extends PlayerEvent {

   private EntityPlayer player;

   public PlayerSleepEvent(EntityPlayer player) {
      super(player);
      this.player = player;
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }

   @Cancelable
   public static class PlayerAllowedToSleep extends PlayerSleepEvent {
      public PlayerAllowedToSleep(EntityPlayer player) {
         super(player);
      }
   }
}
