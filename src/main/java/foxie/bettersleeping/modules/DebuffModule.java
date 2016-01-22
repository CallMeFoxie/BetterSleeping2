package foxie.bettersleeping.modules;

import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.api.BetterSleepingAPI;
import foxie.bettersleeping.api.PlayerDebuff;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class DebuffModule extends Module {
   @Override
   public void preinit(FMLPreInitializationEvent event) {
      super.preinit(event);
      Configuration cfg = new Configuration(new File(event.getModConfigurationDirectory().getPath() +
              "/" + BetterSleeping.MODID + "/debuffs.cfg"));

      String[] debuffNames = {"moveSlowdown", "digSlowdown", "harm", "confusion", "blindness", "hunger", "weakness", "poison", "wither"};
      boolean[] defaultEnable = {true, true, false, false, true, false, true, false, false};
      int[] defaultTiredLevel = {800, 800, 800, 800, 800, 800, 800, 800, 800};
      int[] defaultMaxScale = {3, 3, 1, 1, 2, 1, 3, 1, 1};
      int[] potionEffect = {Potion.moveSlowdown.getId(), Potion.digSlowdown.getId(), Potion.harm.getId(), Potion.confusion.getId(),
              Potion.blindness.getId(), Potion.hunger.getId(), Potion.weakness.getId(), Potion.poison.getId(), Potion.wither.getId()};

      PlayerDebuff[] debuffs = new PlayerDebuff[debuffNames.length];

      for (int i = 0; i < debuffNames.length; i++) {
         String baseName = debuffNames[i];
         PlayerDebuff debuff = new PlayerDebuff();
         debuff.potion = Potion.potionTypes[potionEffect[i]];
         debuff.enable = cfg.getBoolean(baseName + "_enable", "debuffs", defaultEnable[i], "Enable this debuff");
         debuff.maxScale = cfg.getInt(baseName + "_maxScale", "debuffs", defaultMaxScale[i], 0, 5, "Maximum scaling of this debuff");
         debuff.tiredLevel = cfg.getInt(baseName + "_level", "debuffs", defaultTiredLevel[i], 1, 23999, "At which level is this debuff " +
                 "applied");
         debuffs[i] = debuff;
      }

      for (PlayerDebuff debuff : debuffs) {
         BetterSleepingAPI.addDebuff(debuff);
      }

      if (cfg.hasChanged())
         cfg.save();
   }
}
