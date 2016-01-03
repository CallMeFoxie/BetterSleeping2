package foxie.bettersleeping.modules;


import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
