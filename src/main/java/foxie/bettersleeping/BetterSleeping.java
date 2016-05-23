package foxie.bettersleeping;

import foxie.bettersleeping.commands.CommandCollection;
import foxie.bettersleeping.modules.Modules;
import foxie.bettersleeping.proxy.ProxyCommon;
import foxie.lib.Config;
import foxie.lib.IFoxieMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

@Mod(modid = BetterSleeping.MODID, name = BetterSleeping.NAME, version = BetterSleeping.VERSION, dependencies = "required-after:FoxieLib;required-after:CalendarAPI")
public class BetterSleeping implements IFoxieMod {
   public static final String MODID = "bettersleeping";
   public static final String NAME = "Better Sleeping 2";
   public static final String AUTHOR = "CallMeFoxie";
   public static final String VERSION = "@VERSION@";

   @SidedProxy(clientSide = "foxie.bettersleeping.proxy.ProxyClient", serverSide = "foxie.bettersleeping.proxy.ProxyCommon")
   public static ProxyCommon proxy;

   @Mod.Instance(MODID)
   public static BetterSleeping INSTANCE;

   public HashMap<UUID, PlayerSyncStatus> syncData;
   private Config libConfig;
   private Modules modules;

   public BetterSleeping() {
      syncData = new HashMap<UUID, PlayerSyncStatus>();
   }

   public PlayerSyncStatus getSyncData(EntityPlayer player) {
      if (!syncData.containsKey(player.getUniqueID()))
         syncData.put(player.getUniqueID(), new PlayerSyncStatus());

      return syncData.get(player.getUniqueID());
   }

   @Mod.EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      //Config c = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
      // TODO fix oldschool config loading

      libConfig = new Config(new File(event.getModConfigurationDirectory().getPath() +
              "/" + BetterSleeping.MODID + "/baseconfig.cfg"));
      proxy.preinit(event);
      modules = new Modules();
      modules.preinit(event);
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
      modules.init(event);
   }

   @Mod.EventHandler
   public void postinit(FMLPostInitializationEvent event) {
      modules.postinit(event);
   }

   @Override
   public Config getConfig() {
      return libConfig;
   }

   @Override
   public String getModId() {
      return MODID;
   }

   @SubscribeEvent
   public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
      modules.playerJoined(event);
   }

   @SubscribeEvent
   public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
      modules.playerLeft(event);
   }

   @Mod.EventHandler
   public void onServerStarting(FMLServerStartingEvent event) {
      CommandCollection.register(event);
      modules.serverStarting(event);
      SaveHandler.emptyCache();
   }
}
