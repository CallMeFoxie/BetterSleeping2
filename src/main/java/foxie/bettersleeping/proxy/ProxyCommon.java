package foxie.bettersleeping.proxy;

import foxie.bettersleeping.network.Network;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProxyCommon {
   public void preinit(FMLPreInitializationEvent event) {
      new Network();
   }

   public void init(FMLInitializationEvent event) {

   }

   public EntityPlayer getPlayer() {
      return null;
   }

   public IThreadListener getThreadListener(MessageContext ctx) {
      return (IThreadListener) ctx.getServerHandler().playerEntity.worldObj;
   }
}
