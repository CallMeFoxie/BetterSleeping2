package foxie.bettersleeping;

import foxie.bettersleeping.proxy.ProxyCommon;
import foxie.lib.FoxieSavedData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BetterSleeping.MODID, name = BetterSleeping.NAME, version = BetterSleeping.VERSION)
public class BetterSleeping {
   public static final String MODID   = "bettersleeping";
   public static final String NAME    = "Better Sleeping 2";
   public static final String AUTHOR  = "CallMeFoxie";
   public static final String VERSION = "@VERSION@";

   @SidedProxy(clientSide = "foxie.bettersleeping.proxy.ProxyClient", serverSide = "foxie.bettersleeping.proxy.ProxyCommon")
   public static ProxyCommon proxy;

   @Mod.Instance(MODID)
   public static BetterSleeping INSTANCE;

   FoxieSavedData playerData;

   @Mod.EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      Config c = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
      proxy.preinit(event);
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
   }

   @Mod.EventHandler
   public void postinit(FMLPostInitializationEvent event) {

   }


   @Mod.EventHandler
   public void onServerStarted(FMLServerStartedEvent event) {
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
         return;

      World world = MinecraftServer.getServer().worldServers[0];

      playerData = (FoxieSavedData) world.loadItemData(FoxieSavedData.class, BetterSleeping.MODID);
      if (playerData == null) {
         playerData = new FoxieSavedData();
         world.setItemData(BetterSleeping.MODID, playerData);
      }
   }


}
