package foxie.bettersleeping.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSleepPls implements IMessage, IMessageHandler<MessageSleepPls, IMessage> {
   @Override
   public void fromBytes(ByteBuf buf) {

   }

   @Override
   public void toBytes(ByteBuf buf) {

   }

   @Override
   public IMessage onMessage(MessageSleepPls message, MessageContext ctx) {

      EntityPlayer player = ctx.getServerHandler().playerEntity;
      if (player.isPlayerSleeping())
         return null;

      BlockPos pos = player.getPosition();
      player.trySleep(pos);

      player.addChatMessage(new TextComponentString("foo hi bar " + FMLCommonHandler.instance().getEffectiveSide().toString()));

      return null;
   }
}
