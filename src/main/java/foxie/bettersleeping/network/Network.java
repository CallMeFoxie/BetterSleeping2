package foxie.bettersleeping.network;

import foxie.bettersleeping.BetterSleeping;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Network {
   public static Network              instance;
   public static SimpleNetworkWrapper networkChannel;

   public Network() {
      instance = this;
      networkChannel = NetworkRegistry.INSTANCE.newSimpleChannel(BetterSleeping.MODID);
      init();
   }

   public static void init() {
      networkChannel.registerMessage(MessageUpdateTiredness.class, MessageUpdateTiredness.class, 1, Side.CLIENT);
   }
}
