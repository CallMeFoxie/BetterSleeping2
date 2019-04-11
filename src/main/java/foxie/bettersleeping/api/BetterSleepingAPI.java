package foxie.bettersleeping.api;

import foxie.bettersleeping.SaveHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.FMLLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BetterSleepingAPI {
   private static List<PlayerDebuff> debuffs;

   public static void addDebuff(PlayerDebuff debuff) {
      if (debuffs == null)
         debuffs = new ArrayList<PlayerDebuff>();

      if (debuff.tiredLevel == 0) {
         FMLLog.warning("[Better Sleeping API] Tried adding debuff with tired level of 0. That is not how it is meant to be used! Use " +
                 "enabled=0 instead! Skipping this debuff...");
         return;
      }


      debuffs.add(debuff);
   }

   public static List<PlayerDebuff> getDebuffs() {
      return debuffs;
   }

   public static boolean removeDebuff(Potion potion) {
      boolean removed = false;
      Iterator iterator = debuffs.iterator();
      while (iterator.hasNext()) {
         PlayerDebuff debuff = (PlayerDebuff) iterator.next();
         if (debuff.potion.equals(potion)) {
            removed = true;
            iterator.remove();
         }
      }

      return removed;
   }


   // TODO make API not call the base mod
   public static PlayerBSData getSleepingProperty(EntityPlayer player) {
      return SaveHandler.getDataFor(player);
   }

   public static void setSleepingProperty(EntityPlayer player, PlayerBSData data) {
      SaveHandler.saveDataFor(player, data);
   }
}
