package foxie.bettersleeping.modules;

import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.network.MessageSleepPls;
import foxie.bettersleeping.network.Network;
import foxie.lib.Configurable;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class BedModule extends Module {
   @Configurable(comment = "Minimum day time to go to sleep at (-1 for anytime)", min = "0", max = "23999")
   public static int minTime = 0;
   @Configurable(comment = "Maximum day time to go to sleep at (-1 for anytime)", min = "0", max = "23999")
   public static int maxTime = 23999;

   public static KeyBinding keyGotoSleep;

   @Override
   public void init(FMLInitializationEvent event) {
      super.init(event);
      keyGotoSleep = new KeyBinding("key.gotosleep", Keyboard.KEY_J, "key.categories.movement");
      ClientRegistry.registerKeyBinding(keyGotoSleep);
   }

   @SubscribeEvent
   public void isProperTime(PlayerSleepInBedEvent event) {
      if (event.getEntityPlayer().worldObj.isRemote)
         return;

      long time = event.getEntityPlayer().worldObj.getWorldTime() % 24000;
      if (time < minTime && minTime >= -1 || time > maxTime && minTime >= -1) {
         event.getEntityPlayer().addChatComponentMessage(new TextComponentTranslation("message.notSleepNow"));
         event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
      }
   }

   // new Forge stuff
   @SubscribeEvent
   public void onSomething(SleepingLocationCheckEvent event) {
      event.setResult(Event.Result.ALLOW); // TODO more checks possibly? :P
   }

   @SubscribeEvent
   public void onKeyPress(InputEvent.KeyInputEvent event) {
      if(!keyGotoSleep.isPressed())
         return;

      if(BetterSleeping.proxy.getPlayer().isPlayerSleeping())
         return;

      Network.networkChannel.sendToServer(new MessageSleepPls());
   }
}
