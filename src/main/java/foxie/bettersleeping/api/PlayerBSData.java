package foxie.bettersleeping.api;

// TODO make API not call the base mod

import foxie.lib.IPlayerData;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerBSData implements IPlayerData {

   private long energy;
   private long pillness;
   private long caffeine;

   public PlayerBSData(NBTTagCompound compound) {
      readFromNBT(compound);
   }

   public PlayerBSData() {
   }

   public void readFromNBT(NBTTagCompound compound) {
      energy = compound.getLong("energy");
      pillness = compound.getLong("pillness");
      caffeine = compound.getLong("caffeine");
   }

   public void writeToNBT(NBTTagCompound compound) {
      compound.setLong("energy", energy);
      compound.setLong("pillness", pillness);
      compound.setLong("caffeine", caffeine);
   }

   @Override
   public String getMODID() {
      return "BetterSleeping";
   }

   public long getEnergy() {
      return energy;
   }

   public void setEnergy(long energy) {
      this.energy = Math.max(energy, 0);
   }

   public long getPillness() {
      return pillness;
   }

   public void setPillness(long pillness) {
      this.pillness = Math.max(pillness, 0);
   }

   public long getCaffeine() {
      return caffeine;
   }

   public void setCaffeine(long caffeine) {
      this.caffeine = Math.max(caffeine, 0);
   }
}
