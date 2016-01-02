package foxie.bettersleeping.modules;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public abstract class Module {
   public void preinit(FMLPreInitializationEvent event) {
   }

   public void init(FMLInitializationEvent event) {
   }

   public void postinit(FMLPostInitializationEvent event) {
   }

   public void serverStarting(FMLServerStartingEvent event) {
   }

   public void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
   }

   public void playerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
   }
}
