package foxie.bettersleeping.api;

// TODO make API not call the base mod

import foxie.lib.IPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerBSData implements IPlayerData {

   private long energy;
   private long pillness;
   private long caffeine;

   private EntityPlayer player;

   public PlayerBSData(EntityPlayer player, NBTTagCompound compound) {
      this(player);
      readFromNBT(compound);
   }

   public PlayerBSData(EntityPlayer player) {
      this.player = player;
   }

   public PlayerBSData(EntityPlayer player, long spawnEnergy) {
      this(player);
      this.energy = spawnEnergy;
   }

   public EntityPlayer getPlayer() {
      return player;
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

   public void markDirty() {
      BetterSleepingAPI.setSleepingProperty(player, this);
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
      markDirty();
   }

   public void addEnergy(long energy) {
      this.energy += energy;
      if (this.energy < 0)
         this.energy = 0;

      markDirty();
   }

   public long getPillness() {
      return pillness;
   }

   public void setPillness(long pillness) {
      this.pillness = Math.max(pillness, 0);
      markDirty();
   }

   public void addPillness(long pillness) {
      this.pillness += pillness;
      if (this.pillness < 0)
         this.pillness = 0;

      markDirty();
   }

   public long getCaffeine() {
      return caffeine;
   }

   public void setCaffeine(long caffeine) {
      this.caffeine = Math.max(caffeine, 0);
      markDirty();
   }

   public void addCaffeine(long caffeine) {
      this.caffeine += caffeine;
      if (this.caffeine < 0)
         this.caffeine = 0;

      markDirty();
   }
}
