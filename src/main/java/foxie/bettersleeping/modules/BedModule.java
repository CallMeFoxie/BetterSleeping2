package foxie.bettersleeping.modules;

import foxie.lib.Configurable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BedModule extends Module {
   @Configurable(comment = "Minimum day time to go to sleep at", min = "0", max = "23999")
   public static int minTime = 18000;
   @Configurable(comment = "Maximum day time to go to sleep at", min = "0", max = "23999")
   public static int maxTime = 23999;

   @SubscribeEvent
   public void isProperTime(PlayerSleepInBedEvent event) {
      if (event.entityPlayer.worldObj.isRemote)
         return;

      long time = event.entityPlayer.worldObj.getWorldTime() % 24000;
      if (time < minTime && minTime >= -1 || time > maxTime && minTime >= -1) {
         event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("message.notSleepNow"));
         event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
      }
   }
}
