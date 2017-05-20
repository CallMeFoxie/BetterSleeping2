package foxie.bettersleeping.asm.patches;

import foxie.bettersleeping.asm.MethodToPatch;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PatchWakeUpPlayer extends ClassPatch {

   public PatchWakeUpPlayer(ClassWriter writer) {
      super(writer);
      matchingMethods.add(new MethodToPatch("wakeUpPlayer", "(ZZZ)V"));
      matchingMethods.add(new MethodToPatch("a", "(ZZZ)V"));
   }

   @Override
   public MethodVisitor patchedVisitor(MethodVisitor parent) {
      return new PatchWakeUpPlayerClass(parent);
   }

   public class PatchWakeUpPlayerClass extends MethodVisitor {

      public PatchWakeUpPlayerClass(MethodVisitor visitor) {
         super(Opcodes.ASM4, visitor);
      }

      @Override
      public void visitCode() {
         mv.visitCode();

         mv.visitVarInsn(Opcodes.ALOAD, 0);

         mv.visitMethodInsn(Opcodes.INVOKESTATIC, "foxie/bettersleeping/core/Core", "wakeUpPlayerFix",
                 "(Lnet/minecraft/entity/player/EntityPlayer;)V", false);

         super.visitCode();
      }
   }
}
