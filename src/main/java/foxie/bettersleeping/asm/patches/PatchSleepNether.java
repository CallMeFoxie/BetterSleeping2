package foxie.bettersleeping.asm.patches;

import foxie.bettersleeping.Config;
import foxie.bettersleeping.asm.MethodToPatch;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PatchSleepNether extends ClassPatch {

   public PatchSleepNether(ClassWriter writer) {
      super(writer);
      matchingMethods.add(new MethodToPatch("isSurfaceWorld", "()Z"));
      matchingMethods.add(new MethodToPatch("d", "()Z"));
   }

   // TODO make better
   public static boolean isSurfaceWorld(WorldProvider provider) {
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
         return false;

      StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
      if ((stackTraceElements[3].getMethodName().equals("sleepInBedAt") || stackTraceElements[3].getMethodName().equals("a")) &&
              stackTraceElements[3].getClassName().equals("net.minecraft.entity.player.EntityPlayer") || stackTraceElements[3].getClassName()
              .equals("yz")) {
         if (Loader.isModLoaded("harvestthenether") && provider instanceof WorldProviderHell && Config.enableSleepingNether) {
            return true;
         }
      }

      return false;
   }

   @Override
   public MethodVisitor patchedVisitor(MethodVisitor parent) {
      return new PatchHarvestTheNetherVisitor(parent);
   }

   public class PatchHarvestTheNetherVisitor extends MethodVisitor {
      public PatchHarvestTheNetherVisitor(MethodVisitor visitor) {
         super(Opcodes.ASM4, visitor);
      }

      @Override
      public void visitCode() {
         mv.visitCode();
         mv.visitVarInsn(Opcodes.ALOAD, 0);
         mv.visitMethodInsn(Opcodes.INVOKESTATIC, "foxie/bettersleeping/asm/patches/PatchSleepNether", "isSurfaceWorld",
                 "(Lnet/minecraft/world/WorldProvider;)Z", false);
         mv.visitInsn(Opcodes.IRETURN);
      }
   }
}
