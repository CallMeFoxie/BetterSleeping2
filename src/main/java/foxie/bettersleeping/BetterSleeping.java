package foxie.bettersleeping;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.relauncher.Side;
import foxie.bettersleeping.proxy.ProxyCommon;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

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

   BSSavedData playerData;

   @EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      Config c = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
      proxy.preinit(event);
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
   }

   @EventHandler
   public void postinit(FMLPostInitializationEvent event) {

   }


   @EventHandler
   public void onServerStarted(FMLServerStartedEvent event) {
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
         return;

      World world = MinecraftServer.getServer().worldServers[0];

      playerData = (BSSavedData) world.loadItemData(BSSavedData.class, BetterSleeping.MODID);
      if (playerData == null) {
         playerData = new BSSavedData();
         world.setItemData(BetterSleeping.MODID, playerData);
      }
   }


}
