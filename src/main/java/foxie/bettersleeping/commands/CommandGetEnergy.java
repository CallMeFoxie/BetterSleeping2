package foxie.bettersleeping.commands;

import foxie.bettersleeping.api.BetterSleepingAPI;
import foxie.bettersleeping.api.PlayerBSData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandGetEnergy extends CommandBase {
   @Override
   public String getCommandName() {
      return "getenergy";
   }

   @Override
   public String getCommandUsage(ICommandSender sender) {
      return "getenergy";
   }

   @Override
   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (!(sender instanceof EntityPlayer))
         return;

      EntityPlayer player = (EntityPlayer) sender;

      PlayerBSData data = BetterSleepingAPI.getSleepingProperty(player);

      player.addChatMessage(new TextComponentTranslation("message.energyGet", data.getEnergy()));

   }
}
