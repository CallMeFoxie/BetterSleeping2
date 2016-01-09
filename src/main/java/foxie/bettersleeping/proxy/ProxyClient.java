package foxie.bettersleeping.proxy;

import foxie.bettersleeping.client.EnergyGuiOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
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
      MinecraftForge.EVENT_BUS.register(new EnergyGuiOverlay());
   }

   @Override
   public EntityPlayer getPlayer() {
      return FMLClientHandler.instance().getClientPlayerEntity();
   }
}
