package foxie.bettersleeping.commands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandCollection {
   public static void register(FMLServerStartingEvent event) {
      event.registerServerCommand(new CommandSetEnergy());
      event.registerServerCommand(new CommandGetEnergy());
   }
}
