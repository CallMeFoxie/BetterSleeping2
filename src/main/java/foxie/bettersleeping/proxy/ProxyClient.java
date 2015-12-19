package foxie.bettersleeping.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.player.EntityPlayer;

public class ProxyClient extends ProxyCommon {

   public void registerTESR() {
   }

   @Override
   public void preinit(FMLPreInitializationEvent event) {
      super.preinit(event);
   }

   @Override
   public void init(FMLInitializationEvent event) {
      super.init(event);
   }

   @Override
   public EntityPlayer getPlayer() {
      return FMLClientHandler.instance().getClientPlayerEntity();
   }
}
