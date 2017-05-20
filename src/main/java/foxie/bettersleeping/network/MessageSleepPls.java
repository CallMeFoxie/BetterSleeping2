package foxie.bettersleeping.network;

import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.core.Core;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
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
   public IMessage onMessage(final MessageSleepPls message, final MessageContext ctx) {

      final EntityPlayer player = ctx.getServerHandler().player;
      final BlockPos pos = player.getPosition();
      if (player.isPlayerSleeping())
         return null;

      BetterSleeping.proxy.getThreadListener(ctx).addScheduledTask(new Runnable() {
         @Override
         public void run() {
            Core.trySleepingPlayerOnTheGround(player);
         }
      });

      return null;
   }
}
