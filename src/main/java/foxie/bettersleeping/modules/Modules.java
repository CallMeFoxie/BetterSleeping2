package foxie.bettersleeping.modules;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

import java.util.List;

public class Modules {
   public List<Module> modules;

   public Modules() {
      modules.add(new AlarmModule());
      modules.add(new BedModule());
      modules.add(new CoffeeModule());
      modules.add(new CompatModule());
      modules.add(new PeopleModule());
      modules.add(new PillModule());
      modules.add(new TirednessModule());
   }

   public void preinit(FMLPreInitializationEvent event) {
      for (Module module : modules)
         module.preinit(event);
   }

   public void init(FMLInitializationEvent event) {
      for (Module module : modules)
         module.init(event);
   }

   public void postinit(FMLPostInitializationEvent event) {
      for (Module module : modules)
         module.postinit(event);
   }

   public void serverStarting(FMLServerStartingEvent event) {
      for (Module module : modules)
         module.serverStarting(event);
   }

   public void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
      for (Module module : modules)
         module.playerJoined(event);
   }

   public void playerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
      for (Module module : modules)
         module.playerLeft(event);
   }
}
