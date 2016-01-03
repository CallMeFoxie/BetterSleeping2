package foxie.bettersleeping.network;

import foxie.bettersleeping.BetterSleeping;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class Network {
   public static Network              instance;
   public static SimpleNetworkWrapper networkChannel;

   public Network() {
      instance = this;
      networkChannel = NetworkRegistry.INSTANCE.newSimpleChannel(BetterSleeping.MODID);
      init();
   }

   public static void init() {

   }

   public static void initClient() {

   }
}
