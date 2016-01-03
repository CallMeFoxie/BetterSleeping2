package foxie.bettersleeping.asm;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import foxie.bettersleeping.BetterSleeping;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class BetterSleepingCore extends DummyModContainer {

   public BetterSleepingCore() {
      super(new ModMetadata());
      ModMetadata metadata = getMetadata();
      metadata.modId = BetterSleeping.MODID + "core";
      metadata.name = BetterSleeping.NAME + " Core";
      metadata.version = "2.0";
      metadata.authorList.add(BetterSleeping.AUTHOR);
   }

   @Override
   public boolean registerBus(EventBus bus, LoadController controller) {
      bus.register(this);
      return true;
   }

   @Subscribe
   public void modConstruction(FMLConstructionEvent event) {

   }

   @Mod.EventHandler
   public void preinit(FMLPreInitializationEvent event) {

   }

   @Mod.EventHandler
   public void postInit(FMLPostInitializationEvent event) {
   }
}
