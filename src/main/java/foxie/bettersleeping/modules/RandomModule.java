package foxie.bettersleeping.modules;

import foxie.bettersleeping.api.WorldSleepEvent;
import foxie.lib.Configurable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RandomModule extends Module {

   @Configurable(comment = "Allow setting spawn just on using a bed")
   private static boolean alwaysSetSpawn = true;
   @Configurable(comment = "Chance to stop rain on world sleeping. 0 = never, 1 = always", min = "0", max = "1")
   private static double chanceToStopRain = 0.7;

   @SubscribeEvent
   public void onPlayerUseBed(PlayerSleepInBedEvent event) {
      if (alwaysSetSpawn && !event.getEntityPlayer().world.isRemote) {
         BlockPos blockpos = null;
         try {
            blockpos = event.getEntityPlayer().world.getBlockState(event.getEntityPlayer().getPosition()).
                    getBlock().getBedSpawnPosition(null, event.getEntityPlayer().world,
                    event.getEntityPlayer().getPosition(), event.getEntityPlayer());

         } catch (Exception ignored) {
         }

         if (blockpos == null) {
            blockpos = event.getEntityPlayer().getPosition().up();
         }

         event.getEntityPlayer().setSpawnPoint(blockpos, false);

         event.getEntityPlayer().sendMessage(new TextComponentTranslation("message.spawnSet"));
      }
   }

   @SubscribeEvent
   public void onWorldSlept(WorldSleepEvent.Post event) {
      if (event.getWorld().isRemote)
         return;

      if (event.getWorld().rand.nextDouble() < chanceToStopRain)
         event.getWorld().provider.resetRainAndThunder();
   }
}
