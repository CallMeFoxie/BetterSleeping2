package foxie.bettersleeping.modules;

import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.api.BetterSleepingAPI;
import foxie.bettersleeping.api.PlayerBSData;
import foxie.bettersleeping.api.PlayerDebuff;
import foxie.bettersleeping.api.PlayerSleepEvent;
import foxie.lib.Configurable;
import foxie.lib.FoxLog;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.util.List;

public class DebuffModule extends Module {
   private static final int POTION_DURATION = 80;

   @Configurable(comment = "Potion IDs of random effects when sleeping on the ground")
   private static int[] sleepOnGroundPotionIds    = {2, 9};
   @Configurable(comment = "Effect length of sleeping on the ground")
   private static int   sleepOnGroundPotionLength = 100;
   @Configurable(comment = "Potion IDs of random effects when sleeping")
   private static int[] sleepInBedPotionIds       = {6, 21, 10, 11};
   @Configurable(comment = "Effect length of sleeping")
   private static int   sleepInBedPotionLength    = 100;

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

   @SubscribeEvent
   public void playerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.START || event.player.worldObj.isRemote || !TirednessModule.doPlayersGetTired())
         return;

      PlayerBSData data = BetterSleepingAPI.getSleepingProperty(event.player);

      List<PlayerDebuff> debuffs = BetterSleepingAPI.getDebuffs();

      for (PlayerDebuff debuff : debuffs) {
         if (debuff.enable && data.getEnergy() < debuff.tiredLevel) {
            double percentTired = (debuff.tiredLevel - data.getEnergy()) / (double) (debuff.tiredLevel);
            int scale = (int) Math.ceil(percentTired * debuff.maxScale) - 1;
            event.player.addPotionEffect(
                    new PotionEffect(debuff.potion.getId(), POTION_DURATION * 2, scale));
         }
      }
   }

   @SubscribeEvent
   public void sleepOnGround(PlayerSleepEvent.SleepOnGroundEvent event) {
      if (sleepOnGroundPotionIds.length == 0 || sleepOnGroundPotionLength == 0)
         return;

      int potionId = sleepOnGroundPotionIds[event.getPlayer().worldObj.rand.nextInt(sleepOnGroundPotionIds.length)];
      if (Potion.potionTypes.length >= potionId || Potion.potionTypes[potionId] == null) {
         FoxLog.error("Tried applying bad potion type while sleeping on the ground. Potion ID: " + potionId);
         return;
      }

      event.getPlayer().addPotionEffect(new PotionEffect(potionId, sleepOnGroundPotionLength));
   }

   @SubscribeEvent
   public void sleepInBed(PlayerSleepEvent.PlayerSleptEvent event) {
      if (sleepInBedPotionIds.length == 0 || sleepInBedPotionLength == 0)
         return;

      int potionId = sleepInBedPotionIds[event.getPlayer().worldObj.rand.nextInt(sleepInBedPotionIds.length)];
      if (Potion.potionTypes.length >= potionId || Potion.potionTypes[potionId] == null) {
         FoxLog.error("Tried applying bad potion type while sleeping on the ground. Potion ID: " + potionId);
         return;
      }

      event.getPlayer().addPotionEffect(new PotionEffect(potionId, sleepInBedPotionLength));
   }
}
