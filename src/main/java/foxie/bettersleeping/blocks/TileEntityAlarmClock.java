package foxie.bettersleeping.blocks;

import foxie.calendar.api.CalendarAPI;
import foxie.calendar.api.ICalendarProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAlarmClock extends TileEntity {
   private ICalendarProvider clock;

   public TileEntityAlarmClock() {
      clock = CalendarAPI.getCalendarInstance(worldObj);
   }

   @Override
   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      clock.setMinute(compound.getInteger("minute"));
      clock.setHour(compound.getInteger("hour"));
   }

   @Override
   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setInteger("minute", clock.getMinute());
      compound.setInteger("hour", clock.getHour());
   }

   public ICalendarProvider getWakeUpTime() {
      return clock;
   }
}
