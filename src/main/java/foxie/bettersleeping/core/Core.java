package foxie.bettersleeping.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Core {
   public static void trySleepingPlayerOnTheGround(EntityPlayer player) {
      boolean allowed = BSEvents.canPlayerSleepOnTheGround(player);
      if (!allowed) {
         // failed :(
      } else {
         if (trySleep(player) == EntityPlayer.SleepResult.OK) {
            BSEvents.playerSleptOnTheGround(player);
         }
      }
   }

   private static EntityPlayer.SleepResult trySleep(EntityPlayer player) {
      BlockPos bedLocation = player.getPosition();

      EntityPlayer.SleepResult ret = net.minecraftforge.event.ForgeEventFactory.onPlayerSleepInBed(player, player.getPosition());
      if (ret != null) return ret;

      if (player.world.isRemote && player.bedLocation == null) {
         player.bedLocation = new BlockPos(0, 1, 0); // vanilla check to avoid hidden NPE with animation
      }

      if (!player.world.isRemote) {
         if (player.isPlayerSleeping() || !player.isEntityAlive()) {
            return EntityPlayer.SleepResult.OTHER_PROBLEM;
         }

         if (!player.world.provider.isSurfaceWorld()) {
            return EntityPlayer.SleepResult.NOT_POSSIBLE_HERE;
         }

         double d0 = 8.0D;
         double d1 = 5.0D;
         List<EntityMob> list = player.world.<EntityMob>getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB((double) bedLocation.getX() - 8.0D, (double) bedLocation.getY() - 5.0D, (double) bedLocation.getZ() - 8.0D, (double) bedLocation.getX() + 8.0D, (double) bedLocation.getY() + 5.0D, (double) bedLocation.getZ() + 8.0D));

         if (!list.isEmpty()) {
            return EntityPlayer.SleepResult.NOT_SAFE;
         }
      }

      if (player.isRiding()) {
         player.dismountRidingEntity();
      }

      try {
         Class<?> clazz = player.getClass();
         Method foundMethod;
         while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
               // Test any other things about it beyond the name...
               if (method.getName().equals("setSize") && method.getParameterTypes().length == 2) {
                  method.setAccessible(true);
                  method.invoke(player, 0.2F, 0.2F);
               }
            }
            clazz = clazz.getSuperclass();
         }
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }

      //player.setSize(0.2F, 0.2F);

      IBlockState state = null;
      player.setPosition((double) ((float) bedLocation.getX() + 0.5F), (double) ((float) bedLocation.getY() + 0.6875F), (double) ((float) bedLocation.getZ() + 0.5F));

      player.bedLocation = bedLocation;
      player.sleeping = true;
      player.sleepTimer = 0;
      player.motionX = 0.0D;
      player.motionY = 0.0D;
      player.motionZ = 0.0D;

      if (!player.world.isRemote) {
         player.world.updateAllPlayersSleepingFlag();
      }

      return EntityPlayer.SleepResult.OK;
   }

   public static void wakeUpPlayerFix(EntityPlayer player) {
      if (player.bedLocation == null)
         player.bedLocation = new BlockPos(0, 1, 0);
   }
}
