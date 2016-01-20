package foxie.bettersleeping.modules;

import foxie.bettersleeping.api.WorldSleepEvent;
import foxie.lib.Configurable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RandomModule extends Module {

   @Configurable(comment = "Allow setting spawn just on using a bed")
   private static boolean alwaysSetSpawn   = true;
   @Configurable(comment = "Chance to stop rain on world sleeping. 0 = never, 1 = always", min = "0", max = "1")
   private static double  chanceToStopRain = 0.7;

   @SubscribeEvent
   public void onPlayerUseBed(PlayerSleepInBedEvent event) {
      if (alwaysSetSpawn && !event.entityPlayer.worldObj.isRemote) {
         BlockPos blockpos = null;
         try {
            blockpos = event.entityPlayer.worldObj.getBlockState(event.entityPlayer.getPosition()).
                    getBlock().getBedSpawnPosition(event.entityPlayer.worldObj,
                    event.entityPlayer.playerLocation, event.entityPlayer);

         } catch (Exception ignored) {
         }

         if (blockpos == null) {
            blockpos = event.entityPlayer.getPosition().up();
         }

         event.entityPlayer.setSpawnPoint(blockpos, false);

         event.entityPlayer.addChatMessage(new ChatComponentTranslation("message.spawnSet"));
      }
   }

   @SubscribeEvent
   public void onWorldSlept(WorldSleepEvent.Post event) {
      if (event.world.rand.nextDouble() < chanceToStopRain)
         event.world.provider.resetRainAndThunder();
   }
}
