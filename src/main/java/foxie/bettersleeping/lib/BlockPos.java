package foxie.bettersleeping.lib;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Support class to make 1.8 update easier :P
 */
public class BlockPos {
   private int x, y, z;
   private ForgeDirection direction;

   public BlockPos(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.direction = ForgeDirection.UNKNOWN;
   }

   public BlockPos(int x, int y, int z, ForgeDirection direction) {
      this(x, y, z);
      this.direction = direction;
   }

   public static BlockPos readFromNBT(NBTTagCompound compound) {
      if (compound == null)
         return null;

      return new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
   }

   public int getX() {
      return x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getZ() {
      return z;
   }

   public void setZ(int z) {
      this.z = z;
   }

   public ForgeDirection getDirection() {
      return direction;
   }

   public void setDirection(ForgeDirection direction) {
      this.direction = direction;
   }

   public BlockPos getLeft(int blocks) {
      if (direction == ForgeDirection.EAST) {
         return new BlockPos(x, y, z - blocks, direction);
      }

      if (direction == ForgeDirection.WEST) {
         return new BlockPos(x, y, z + blocks, direction);
      }

      if (direction == ForgeDirection.NORTH) {
         return new BlockPos(x - blocks, y, z, direction);
      }

      if (direction == ForgeDirection.SOUTH) {
         return new BlockPos(x + blocks, y, z, direction);
      }

      return this;
   }

   public BlockPos getRight(int blocks) {
      if (direction == ForgeDirection.EAST) {
         return new BlockPos(x, y, z + blocks, direction);
      }

      if (direction == ForgeDirection.WEST) {
         return new BlockPos(x, y, z - blocks, direction);
      }

      if (direction == ForgeDirection.NORTH) {
         return new BlockPos(x + blocks, y, z, direction);
      }

      if (direction == ForgeDirection.SOUTH) {
         return new BlockPos(x - blocks, y, z, direction);
      }

      return this;
   }

   public BlockPos getFarther(int blocks) {
      if (direction == ForgeDirection.EAST) {
         return new BlockPos(x + blocks, y, z, direction);
      }

      if (direction == ForgeDirection.WEST) {
         return new BlockPos(x - blocks, y, z, direction);
      }

      if (direction == ForgeDirection.NORTH) {
         return new BlockPos(x, y, z - blocks, direction);
      }

      if (direction == ForgeDirection.SOUTH) {
         return new BlockPos(x, y, z + blocks, direction);
      }

      return this;
   }

   public BlockPos getCloser(int blocks) {
      if (direction == ForgeDirection.EAST) {
         return new BlockPos(x - blocks, y, z, direction);
      }

      if (direction == ForgeDirection.WEST) {
         return new BlockPos(x + blocks, y, z, direction);
      }

      if (direction == ForgeDirection.NORTH) {
         return new BlockPos(x, y, z + blocks, direction);
      }

      if (direction == ForgeDirection.SOUTH) {
         return new BlockPos(x, y, z - blocks, direction);
      }

      return this;
   }

   public BlockPos getBelow(int blocks) {
      return new BlockPos(x, y - 1, z, direction);
   }

   public BlockPos getAbove(int blocks) {
      return new BlockPos(x, y + 1, z, direction);
   }

   public BlockPos getLeft() {
      return getLeft(1);
   }

   public BlockPos getRight() {
      return getRight(1);
   }

   public BlockPos getFarther() {
      return getFarther(1);
   }

   public BlockPos getCloser() {
      return getCloser(1);
   }

   public BlockPos getBelow() {
      return getBelow(1);
   }

   public BlockPos getAbove() {
      return getAbove(1);
   }

   public Block getBlock(World world) {
      return world.getBlock(x, y, z);
   }

   public TileEntity getTE(World world) {
      return world.getTileEntity(x, y, z);
   }

   public int getMeta(World world) {
      return world.getBlockMetadata(x, y, z);
   }

   public void writeToNBT(NBTTagCompound compound) {
      compound.setInteger("x", getX());
      compound.setInteger("y", getY());
      compound.setInteger("z", getZ());
   }

}
