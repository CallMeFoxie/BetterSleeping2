package foxie.bettersleeping.modules;

import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.network.MessageSleepPls;
import foxie.bettersleeping.network.Network;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class ClientModule extends Module {
   public static KeyBinding keyGotoSleep;

   @Override
   public void init(FMLInitializationEvent event) {
      super.init(event);
      keyGotoSleep = new KeyBinding("key.gotosleep", Keyboard.KEY_J, "key.categories.movement");
      ClientRegistry.registerKeyBinding(keyGotoSleep);
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
