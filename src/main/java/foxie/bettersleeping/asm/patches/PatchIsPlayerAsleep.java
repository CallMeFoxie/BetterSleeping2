package foxie.bettersleeping.asm.patches;

import foxie.bettersleeping.asm.MethodToPatch;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PatchIsPlayerAsleep extends ClassPatch {
   public PatchIsPlayerAsleep(ClassWriter writer) {
      super(writer);
      matchingMethods.add(new MethodToPatch("isPlayerFullyAsleep", "()Z"));
      matchingMethods.add(new MethodToPatch("cf", "()Z"));
   }

   @Override
   public MethodVisitor patchedVisitor(MethodVisitor parent) {
      return new PatchIsPlayerFullyAsleep(parent);
   }

   public class PatchIsPlayerFullyAsleep extends MethodVisitor {
      private String className;
      private String fieldName;

      public PatchIsPlayerFullyAsleep(MethodVisitor visitor) {
         super(Opcodes.ASM4, visitor);
         if (!ClassPatch.isDeobfuscatedEnvironment()) {
            className = "wn";
            fieldName = "b";
         } else {
            className = "net/minecraft/entity/player/EntityPlayer";
            fieldName = "sleepTimer";
         }
      }

      @Override
      public void visitCode() {
         mv.visitCode();

         mv.visitVarInsn(Opcodes.ALOAD, 0);
         mv.visitVarInsn(Opcodes.ALOAD, 0);
         mv.visitFieldInsn(Opcodes.GETFIELD, className, fieldName, "I");

         mv.visitMethodInsn(Opcodes.INVOKESTATIC, "foxie/bettersleeping/core/BSEvents", "isPlayerFullyAsleep",
                 "(Lnet/minecraft/entity/player/EntityPlayer;I)Z", false);
         mv.visitInsn(Opcodes.IRETURN);
      }
   }
}
