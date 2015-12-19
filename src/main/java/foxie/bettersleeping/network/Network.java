package foxie.bettersleeping.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import foxie.bettersleeping.BetterSleeping;

public class Network {
   public static Network instance;
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
