package foxie.bettersleeping.modules;

import foxie.lib.Configurable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RandomModule extends Module {

   @Configurable(comment = "Allow setting spawn just on using a bed")
   private static boolean alwaysSetSpawn = true;

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
}
