package foxie.bettersleeping.asm.patches;

import foxie.bettersleeping.asm.MethodToPatch;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PatchIsDay extends ClassPatch {

   public PatchIsDay(ClassWriter writer) {
      super(writer);
      matchingMethods.add(new MethodToPatch("trySleep", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/player/EntityPlayer$SleepResult;"));
      matchingMethods.add(new MethodToPatch("a", "(Lcm;)Lzs$a;"));
      matchingMethods.add(new MethodToPatch("onUpdate", "()V"));
      matchingMethods.add(new MethodToPatch("m", "()V"));
   }

   @Override
   public MethodVisitor patchedVisitor(MethodVisitor parent) {
      return new PatchIsDayMethodVisitor(parent);
   }

   public class PatchIsDayMethodVisitor extends MethodVisitor {
      public PatchIsDayMethodVisitor(MethodVisitor visitor) {
         super(Opcodes.ASM4, visitor);
      }

      @Override
      public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
         if (desc.equals("()Z") && (name.equals("isDaytime") || name.equals("B"))) {
            super.visitInsn(Opcodes.POP);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "foxie/bettersleeping/core/BSEvents", "isPlayerAllowedToSleep",
                    "(Lnet/minecraft/entity/player/EntityPlayer;)Z", false);
         } else {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
         }
      }
   }
}
