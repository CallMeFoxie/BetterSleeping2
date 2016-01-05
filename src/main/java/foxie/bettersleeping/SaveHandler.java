package foxie.bettersleeping;

import foxie.bettersleeping.api.PlayerBSData;
import foxie.bettersleeping.modules.TirednessModule;
import foxie.lib.FoxieSavedData;
import net.minecraft.entity.player.EntityPlayer;

public class SaveHandler {

   public static PlayerBSData getDataFor(EntityPlayer player) {
      PlayerBSData prototype = new PlayerBSData(player);
      prototype.setEnergy(TirednessModule.getSpawnEnergy());

      FoxieSavedData.instance().getPlayerData(player, prototype);

      return prototype;
   }

   public static void saveDataFor(EntityPlayer player, PlayerBSData data) {
      FoxieSavedData.instance().setPlayerData(player, data);
   }
}
