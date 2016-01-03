package foxie.bettersleeping.modules;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
