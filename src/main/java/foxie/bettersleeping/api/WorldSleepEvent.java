package foxie.bettersleeping.api;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldSleepEvent extends WorldEvent {
   public static final int PRIORITY_BUILTIN = 10;
   public static final int PRIORITY_ALARM   = 20;

   public WorldSleepEvent(World world) {
      super(world);
   }

   /**
    * This event is used to gather the time you want to sleep each world.
    */
   public static class Pre extends WorldSleepEvent {
      long sleptTime    = 0;
      int  lastPriority = -1;

      public Pre(World world) {
         super(world);
      }

      /**
       * Attempts to set a new slept time. If the priority is higher than the one set it overwrites the value.
       *
       * @param sleptTime new calculated slept time
       * @param priority  priority of this calculation. Base priority is -1
       * @return if succeeded
       */
      public boolean setSleptTime(long sleptTime, int priority) {
         if (sleptTime < 0) // please...
            return false;

         if (priority > lastPriority) {
            this.sleptTime = sleptTime;
            return true;
         }

         return false;
      }

      public long getSleptTime() {
         return sleptTime;
      }

      public int getLastPriority() {
         return lastPriority;
      }
   }

   /**
    * This even is fired after all of the calculating new TOD, players have been awoken etc has been done.
    * This event is NOT cancelable.
    */
   public static class Post extends WorldSleepEvent {
      private final long sleptTicks;

      public Post(World world, long sleptTicks) {
         super(world);
         this.sleptTicks = sleptTicks;
      }

      public long getSleptTicks() {
         return sleptTicks;
      }
   }
}
