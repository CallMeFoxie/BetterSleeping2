package foxie.bettersleeping.api;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

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
   public static class PlayerAllowedToSleepEvent extends PlayerSleepEvent {
      public PlayerAllowedToSleepEvent(EntityPlayer player) {
         super(player);
      }
   }

   @Cancelable
   public static class PlayerFallingAsleepEvent extends PlayerSleepEvent {
      public PlayerFallingAsleepEvent(EntityPlayer player) {
         super(player);
      }
   }

   /**
    * This event is fired before a person is supposed to fall on a ground outside of bed - when he reaches 0 on his Sleepybar.
    * This event is cancelable. When cancelled the sleeping will be denied.
    */
   @Cancelable
   public static class SleepOnGroundEvent extends PlayerSleepEvent {
      public SleepOnGroundEvent(EntityPlayer player) {
         super(player);
      }
   }

   public static class PlayerSleptEvent extends PlayerSleepEvent {
      private long time;

      public PlayerSleptEvent(EntityPlayer player, long time) {
         super(player);
         this.time = time;
      }

      public long getTime() {
         return this.time;
      }
   }
}
