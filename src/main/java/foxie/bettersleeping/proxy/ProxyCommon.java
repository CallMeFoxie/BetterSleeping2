package foxie.bettersleeping.proxy;

import foxie.bettersleeping.EventHandlers;
import foxie.bettersleeping.network.Network;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyCommon {
   public void preinit(FMLPreInitializationEvent event) {
      new Network();
      new EventHandlers();
   }

   public void init(FMLInitializationEvent event) {
      MinecraftForge.EVENT_BUS.register(EventHandlers.INSTANCE);
   }

   public EntityPlayer getPlayer() {
      return null;
   }
}
