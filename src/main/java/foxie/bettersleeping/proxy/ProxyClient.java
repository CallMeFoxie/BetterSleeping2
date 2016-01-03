package foxie.bettersleeping.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
