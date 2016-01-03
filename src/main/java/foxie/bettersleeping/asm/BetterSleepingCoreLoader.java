package foxie.bettersleeping.asm;


import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.Name(BetterSleepingCoreLoader.NAME)
public class BetterSleepingCoreLoader implements IFMLLoadingPlugin {
   public static final String NAME = "BetterSleeping Core";

   @Override
   public String[] getASMTransformerClass() {
      return new String[]{PatchCollection.class.getName()};
   }

   @Override
   public String getModContainerClass() {
      return BetterSleepingCore.class.getName();
   }

   @Override
   public String getSetupClass() {
      return null;
   }

   @Override
   public void injectData(Map<String, Object> data) {

   }

   @Override
   public String getAccessTransformerClass() {
      return null;
   }
}
