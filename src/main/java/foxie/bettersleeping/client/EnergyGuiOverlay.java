package foxie.bettersleeping.client;

import foxie.bettersleeping.BetterSleeping;
import foxie.lib.Configurable;
import foxie.lib.Registrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnergyGuiOverlay extends GuiScreen {

   private static final int BAR_WIDTH = 32;
   private static final int BAR_HEIGHT = 8;
   private static final int BAR_MAXOFFSET = BAR_WIDTH - BAR_HEIGHT;

   @Configurable(category = "gui")
   private static int guiOffsetLeft = 4;
   @Configurable(category = "gui")
   private static int guiOffsetTop = 8;

   @Configurable(category = "gui")
   private static boolean renderBedIcon = true;

   public EnergyGuiOverlay() {
      Registrator.checkConfigurable(EnergyGuiOverlay.class);
      itemRender = Minecraft.getMinecraft().getRenderItem();
   }

   @SubscribeEvent
   public void onGuiRender(RenderGameOverlayEvent.Post event) {
      if (Minecraft.getMinecraft().gameSettings.showDebugInfo)
         return;

      if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || event.isCanceled()
              || ClientData.maxEnergy == -1
              || FMLClientHandler.instance().getClient().thePlayer.capabilities.isCreativeMode)
         return;

      OpenGlHelper.glBlendFunc(770, 771, 0, 1);

      TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
      textureManager.bindTexture(new ResourceLocation(BetterSleeping.MODID, "textures/gui/bar.png"));

      drawTexturedModalRect(guiOffsetLeft, guiOffsetTop, 0, 0, BAR_WIDTH, BAR_HEIGHT);

      double percent = 0;
      if (ClientData.maxEnergy != 0)
         percent = ((double) ClientData.energy / ClientData.maxEnergy) * BAR_MAXOFFSET;
      if (percent > BAR_MAXOFFSET)
         percent = BAR_MAXOFFSET;

      drawTexturedModalRect((int) (guiOffsetLeft + percent), guiOffsetTop, 0, 8, 8, 8);

      if (renderBedIcon)
         itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.BED), guiOffsetLeft + 36, guiOffsetTop - 4);

   }
}
