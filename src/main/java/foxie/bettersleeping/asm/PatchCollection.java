package foxie.bettersleeping.asm;

import foxie.bettersleeping.asm.patches.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatchCollection implements IClassTransformer {
   public static Map<String, List<Class<? extends ClassPatch>>> patchClasses;

   static {
      addPatch("net.minecraft.entity.player.EntityPlayer", PatchIsDay.class);
      addPatch("net.minecraft.entity.player.EntityPlayer", PatchIsInBed.class);
      addPatch("net.minecraft.world.WorldProviderHell", PatchSleepNether.class);
      addPatch("net.minecraft.entity.player.EntityPlayer", PatchIsPlayerAsleep.class);
      addPatch("net.minecraft.entity.player.EntityPlayer", PatchWakeUpPlayer.class);

      addPatch("aax", PatchIsDay.class);
      addPatch("aax", PatchIsInBed.class);
      addPatch("avf", PatchSleepNether.class);
      addPatch("aax", PatchIsPlayerAsleep.class);
      addPatch("aax", PatchWakeUpPlayer.class);
   }

   public static void addPatch(String classname, Class<? extends ClassPatch> patch) {
      if (patchClasses == null)
         patchClasses = new HashMap<String, List<Class<? extends ClassPatch>>>();

      if (patchClasses.get(classname) != null) {
         patchClasses.get(classname).add(patch);
      } else {
         List<Class<? extends ClassPatch>> tmp = new ArrayList<Class<? extends ClassPatch>>();
         tmp.add(patch);
         patchClasses.put(classname, tmp);
      }
   }

   @Override
   public byte[] transform(String origName, String newName, byte[] bytecode) {
      if (patchClasses.containsKey(origName)) {
         ClassReader rd;
         ClassWriter wr;
         for (Class<? extends ClassPatch> patch : patchClasses.get(origName)) {
            try {
               rd = new ClassReader(bytecode);
               wr = new ClassWriter(ClassWriter.COMPUTE_MAXS);
               ClassVisitor patcher = patch.getDeclaredConstructor(ClassWriter.class).newInstance(wr);
               rd.accept(patcher, ClassReader.EXPAND_FRAMES);
               bytecode = wr.toByteArray();
               FMLLog.info("Successfully patched " + origName + " with " + patch.toString());
            } catch (Exception e) {
               FMLLog.warning("Failed to patch class " + origName + " with " + patch.toString() + " :( - things will not work properly!");
            }
         }
      }

      return bytecode;
   }
}
