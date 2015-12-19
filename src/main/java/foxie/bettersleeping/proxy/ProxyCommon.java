package foxie.bettersleeping.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxie.bettersleeping.EventHandlers;
import foxie.bettersleeping.network.Network;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ProxyCommon {
   public void preinit(FMLPreInitializationEvent event) {
      new Network();
      Network.initClient();

      new EventHandlers();
   }

   public void init(FMLInitializationEvent event) {
      FMLCommonHandler.instance().bus().register(EventHandlers.INSTANCE);
      MinecraftForge.EVENT_BUS.register(EventHandlers.INSTANCE);
   }

   public EntityPlayer getPlayer() {
      return null;
   }
}
