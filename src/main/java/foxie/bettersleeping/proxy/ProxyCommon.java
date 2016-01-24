package foxie.bettersleeping.proxy;

import foxie.bettersleeping.network.Network;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyCommon {
   public void preinit(FMLPreInitializationEvent event) {
      new Network();
   }

   public void init(FMLInitializationEvent event) {

   }

   public EntityPlayer getPlayer() {
      return null;
   }
}
