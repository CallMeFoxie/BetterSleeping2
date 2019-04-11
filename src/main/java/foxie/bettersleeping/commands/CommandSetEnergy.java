package foxie.bettersleeping.commands;

import foxie.bettersleeping.api.BetterSleepingAPI;
import foxie.bettersleeping.api.PlayerBSData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;

public class CommandSetEnergy extends CommandBase {

   @Override
   public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
      return super.getTabCompletions(server, sender, args, targetPos);
   }

   @Override
   public String getUsage(ICommandSender sender) {
       return "setenergy <new tiredness>";
   }

   @Override
   public String getName() {
       return "setenergy";
   }

   @Override
   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length == 0)
         return;

      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer) sender;

         PlayerBSData data = BetterSleepingAPI.getSleepingProperty(player);

         data.setEnergy(Long.decode(args[0]));
         sender.sendMessage(new TextComponentTranslation("message.energySet"));
      }
   }
}
