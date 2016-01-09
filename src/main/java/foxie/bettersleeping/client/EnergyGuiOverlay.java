package foxie.bettersleeping.client;

import foxie.bettersleeping.BetterSleeping;
import foxie.lib.Configurable;
import foxie.lib.Registrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnergyGuiOverlay extends Gui {

   private static final int BAR_WIDTH     = 32;
   private static final int BAR_HEIGHT    = 8;
   @Configurable(category = "gui")
   private static       int guiOffsetLeft = 4;
   @Configurable(category = "gui")
   private static       int guiOffsetTop  = 8;

   public EnergyGuiOverlay() {
      Registrator.checkConfigurable(EnergyGuiOverlay.class);
   }

   @SubscribeEvent
   public void onGuiRender(RenderGameOverlayEvent event) {
      if (event.type != RenderGameOverlayEvent.ElementType.ALL || event.isCanceled())
         return;

      OpenGlHelper.glBlendFunc(770, 771, 0, 1);

      TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
      textureManager.bindTexture(new ResourceLocation(BetterSleeping.MODID, "textures/gui/bar.png"));

      drawTexturedModalRect(guiOffsetLeft, guiOffsetTop, 0, 0, BAR_WIDTH, BAR_HEIGHT);

   }
}
