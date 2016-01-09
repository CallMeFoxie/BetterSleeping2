package foxie.bettersleeping.network;

import foxie.bettersleeping.client.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateTiredness implements IMessage, IMessageHandler<MessageUpdateTiredness, IMessage> {
   private long energy, maxEnergy;


   public MessageUpdateTiredness() {
   }

   /**
    * Create a new UpdateTiredness message. Requires maxEnergy to avoid client-server using different max values
    *
    * @param energy
    * @param maxEnergy
    */
   public MessageUpdateTiredness(long energy, long maxEnergy) {
      this.energy = energy;
      this.maxEnergy = maxEnergy;
   }

   public long getEnergy() {
      return energy;
   }

   public long getMaxEnergy() {
      return maxEnergy;
   }

   @Override
   public void fromBytes(ByteBuf buf) {
      energy = buf.readLong();
      maxEnergy = buf.readLong();
   }

   @Override
   public void toBytes(ByteBuf buf) {
      buf.writeLong(energy);
      buf.writeLong(maxEnergy);
   }

   @Override
   public IMessage onMessage(MessageUpdateTiredness message, MessageContext ctx) {
      ClientData.energy = message.energy;
      ClientData.maxEnergy = message.maxEnergy;

      return null;
   }
}
