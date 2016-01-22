package foxie.bettersleeping;

import foxie.bettersleeping.api.PlayerBSData;
import foxie.bettersleeping.modules.TirednessModule;
import foxie.lib.FoxieSavedData;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveHandler {

   private static Map<UUID, PlayerBSData> cachedData = new HashMap<UUID, PlayerBSData>();

   public static PlayerBSData getDataFor(EntityPlayer player) {
      if (cachedData.containsKey(player.getUniqueID())) // caching layer to avoid going into registry/NBT and avoid extra constructor
         return cachedData.get(player.getUniqueID());

      PlayerBSData prototype = new PlayerBSData(player, TirednessModule.getSpawnEnergy());

      FoxieSavedData.instance().getPlayerData(player, prototype);
      cachedData.put(player.getUniqueID(), prototype);

      return prototype;
   }

   public static void saveDataFor(EntityPlayer player, PlayerBSData data) {
      FoxieSavedData.instance().setPlayerData(player, data);
      cachedData.put(player.getUniqueID(), data);
   }
}
