package foxie.bettersleeping;

import foxie.bettersleeping.api.PlayerBSData;
import foxie.lib.FoxieSavedData;
import net.minecraft.entity.player.EntityPlayer;

public class SaveHandler {

   public static PlayerBSData getDataFor(EntityPlayer player) {
      PlayerBSData prototype = new PlayerBSData();

      FoxieSavedData.instance().getPlayerData(player, prototype);

      return prototype;
   }
}
