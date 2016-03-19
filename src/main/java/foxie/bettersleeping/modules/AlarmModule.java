package foxie.bettersleeping.modules;

import foxie.bettersleeping.api.WorldSleepEvent;
import foxie.bettersleeping.blocks.TileEntityAlarmClock;
import foxie.calendar.api.ICalendarProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class AlarmModule extends Module {
   public static List<TileEntityAlarmClock> getAllAlarms(List<EntityPlayer> playerEntities, List<EntityPlayer> outPlayers) {
      List<TileEntityAlarmClock> alarms = new ArrayList<TileEntityAlarmClock>();

      for (EntityPlayer player : playerEntities) {
         TileEntityAlarmClock alarm = findNearbyAlarm(player);
         if (alarm != null) {
            alarms.add(alarm);
            outPlayers.add(player);
         }
      }

      return alarms;
   }

   public static TileEntityAlarmClock findNearbyAlarm(EntityPlayer player) {
      World world = player.worldObj;
      for (double x = player.posX - 2; x < player.posX + 2; x++) {
         for (double y = player.posY - 1; y < player.posY + 2; y++) {
            for (double z = player.posZ - 2; z < player.posZ + 2; z++) {
               TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
               if (tileEntity instanceof TileEntityAlarmClock)
                  return (TileEntityAlarmClock) tileEntity;
            }
         }
      }

      return null;
   }

   @SubscribeEvent
   public void onWorldSleepPre(WorldSleepEvent.Pre event) {
      if (event.world.isRemote)
         return;

      // collect all the alarm clocks
      List<EntityPlayer> playersWithAlarms = new ArrayList<EntityPlayer>();
      List<TileEntityAlarmClock> alarms = getAllAlarms(event.world.playerEntities, playersWithAlarms);

      if (alarms.size() == 0)
         return;

      ICalendarProvider earliestAlarm = alarms.get(0).getWakeUpTime();
      for (int i = 1; i < alarms.size(); i++) {
         if (earliestAlarm.getHour() > alarms.get(i).getWakeUpTime().getHour()) {
            earliestAlarm = alarms.get(i).getWakeUpTime();
         }
      }

      if (!TirednessModule.isWakeupOnCap()) {
         event.setSleptTime(1, WorldSleepEvent.PRIORITY_ALARM); // TODO
      } else {
         event.setSleptTime(1, WorldSleepEvent.PRIORITY_ALARM); // TODO
      }
   }
}
