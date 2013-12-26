package quantumcraft.util;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ForgeDirection;
import quantumcraft.core.Config;
import quantumcraft.core.interfaces.IMultiTool;
import quantumcraft.core.interfaces.IUpgrade;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicUtils {

    // Cardinal Orientation
    public static final int dirBottom = 0;
    public static final int dirTop = 1;
    public static final int dirEast = 2;
    public static final int dirWest = 3;
    public static final int dirNorth = 4;
    public static final int dirSouth = 5;
    // Axial Orientation
    public static final int dirYNeg = 0;
    public static final int dirYPos = 1;
    public static final int dirZNeg = 2;
    public static final int dirZPos = 3;
    public static final int dirXNeg = 4;
    public static final int dirXPos = 5;
    public static Random rand = new Random();

    public static Block getBlockInstance(IBlockAccess world, int x, int y, int z) {
        return Block.blocksList[world.getBlockId(x, y, z)];
    }

    public static Block getBlockAtTarget(DrawBlockHighlightEvent event) {
        return getBlockInstance(event.player.worldObj, event.target.blockX, event.target.blockY, event.target.blockZ);
    }

    public static TileEntity getTileEntityAtTarget(DrawBlockHighlightEvent event) {
        return event.player.getEntityWorld()
                .getBlockTileEntity(event.target.blockX, event.target.blockY, event.target.blockZ);
    }

    public static int overclockMultiplier(int[] uids) {
        int res = 0;
        for (int u : uids) {
            if (u == 1) res++;
        }
        return res;
    }

    public static boolean isRedstonePowered(World world, int x, int y, int z) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z)) {
            return true;
        }
        for (BlockPosition bp : new BlockPosition(x, y, z).getAdjacent(false)) {
            int blockId = world.getBlockId(bp.x, bp.y, bp.z);
            if (blockId == Block.redstoneWire.blockID &&
                    Block.blocksList[blockId].isProvidingStrongPower(world, bp.x, bp.y, bp.z, 1) > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRedstonePowered(TileEntity te) {
        return isRedstonePowered(te.worldObj, te.xCoord, te.yCoord, te.zCoord);
    }

    public static boolean isServer(World world) {
        return world != null && !world.isRemote;
    }

    public static boolean isClient(World world) {
        return world != null && world.isRemote;
    }

    public static boolean isHoldingWrench(EntityPlayer player) {
        return player.inventory.getCurrentItem() != null &&
                Item.itemsList[player.inventory.getCurrentItem().itemID] instanceof IMultiTool;
    }

    public static boolean isHoldingUpgrade(EntityPlayer player) {
        return player.inventory.getCurrentItem() != null &&
                Item.itemsList[player.inventory.getCurrentItem().itemID] instanceof IUpgrade;
    }

    public static void sendPacketToServer(Packet packet) {
        PacketDispatcher.sendPacketToServer(packet);
    }

    public static void sendPacketToPlayer(Packet packet, Player player) {
        PacketDispatcher.sendPacketToPlayer(packet, player);
    }

    public static void sendPacketToPlayerList(Packet packet, List<EntityPlayer> players) {
        for (EntityPlayer player : players) {
            PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
        }
    }

    public static void sendPacketToNearbyPlayers(Packet packet, int x, int y, int z, int dimID) {
        PacketDispatcher.sendPacketToAllAround(x, y, z, Config.networkUpdateRange.getDouble(50), dimID, packet);
    }

    public static void sendPacketToAllPlayers(Packet packet) {
        PacketDispatcher.sendPacketToAllPlayers(packet);
    }

    public static List<EntityPlayer> getPlayerArround(World worldObj, int xCoord, int yCoord, int zCoord,
                                                      int distance) {
        List<EntityPlayer> list = new ArrayList<EntityPlayer>();
        if (worldObj != null) {
            for (Object playerObject : worldObj.playerEntities) {
                EntityPlayer player = (EntityPlayer) playerObject;
                if (Math.hypot(player.posX - xCoord, Math.hypot(player.posY - yCoord, player.posZ - zCoord)) <
                        distance) {
                    list.add(player);
                }
            }
        }
        return list;
    }

    public static void dropItem(World world, int x, int y, int z, ItemStack itemStack) {
        if (!isClient(world)) {
            double var5 = 0.7D;
            double var7 = (double) world.rand.nextFloat() * var5 + (1.0D - var5) * 0.5D;
            double var9 = (double) world.rand.nextFloat() * var5 + (1.0D - var5) * 0.5D;
            double var11 = (double) world.rand.nextFloat() * var5 + (1.0D - var5) * 0.5D;
            EntityItem var13 =
                    new EntityItem(world, (double) x + var7, (double) y + var9, (double) z + var11, itemStack);
            var13.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(var13);
        }
    }

    /**
     * Notify all neighbors, including diagonals.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param blockId
     */
    public static void updateIndirectNeighbors(World world, int x, int y, int z, int blockId) {
        if (!isClient(world)) {
            for (int var5 = -3; var5 <= 3; ++var5) {
                for (int var6 = -3; var6 <= 3; ++var6) {
                    for (int var7 = -3; var7 <= 3; ++var7) {
                        int var8 = var5 < 0 ? -var5 : var5;
                        var8 += var6 < 0 ? -var6 : var6;
                        var8 += var7 < 0 ? -var7 : var7;
                        if (var8 <= 3) {
                            Block localBlock = Block.blocksList[world.getBlockId(x, y, z)];
                            if (localBlock != null) {
                                localBlock.onNeighborBlockChange(world, x, y, z, blockId);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Notify all blocks around of neighbor change. If flag is true, also notify the actual block given.
     *
     * @param world
     * @param coord
     * @param id
     * @param flag
     */
    public static void updateAllNeighbors(World world, Coords coord, int id, boolean flag) {
        world.notifyBlocksOfNeighborChange(coord.x, coord.y, coord.z, id);
        if (flag) {
            world.notifyBlockOfNeighborChange(coord.x, coord.y, coord.z, id);
        }
    }

    public static TileEntity getTileEntity(IBlockAccess access, Coords coords, Class clazz) {
        TileEntity te = access.getBlockTileEntity(coords.x, coords.y, coords.z);
        return !clazz.isInstance(te) ? null : te;
    }

    public static MovingObjectPosition retraceBlock(World world, EntityPlayer player, int x, int y, int z) {
        Vec3 headVec = Vec3.createVectorHelper(player.posX, (player.posY + 1.62) - player.yOffset, player.posZ);
        Vec3 lookVec = player.getLook(1.0F);
        double reach = world.isRemote ? Minecraft.getMinecraft().playerController.getBlockReachDistance() :
                ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        return Block.blocksList[world.getBlockId(x, y, z)].collisionRayTrace(world, x, y, z, headVec, endVec);
    }

    public static void placeNoise(World world, int x, int y, int z, int id) {
        Block var5 = Block.blocksList[id];
        world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F),
                "step.stone", (var5.stepSound.getVolume() + 1.0F) / 2.0F, var5.stepSound.getPitch() * 0.8F);
    }

    /**
     * Add itemstack to an NBTTagList and return it. Maxvalue is usually 64.
     *
     * @param items
     * @param maxQuantity
     */
    public static NBTTagList writeItemStacksToTag(ItemStack[] items, int maxQuantity) {
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setShort("Slot", (short) i);
                items[i].writeToNBT(tag);
                if (maxQuantity > Short.MAX_VALUE) {
                    tag.setInteger("Quantity", items[i].stackSize);
                } else if (maxQuantity > Byte.MAX_VALUE) {
                    tag.setShort("Quantity", (short) items[i].stackSize);
                }
                tagList.appendTag(tag);
            }
        }
        return tagList;
    }

    public static void readItemStacksFromTag(ItemStack[] items, NBTTagList tagList) {
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            int b = tag.getShort("Slot");
            items[b] = ItemStack.loadItemStackFromNBT(tag);
            if (tag.hasKey("Quantity")) {
                NBTBase qtag = tag.getTag("Quantity");
                if (qtag instanceof NBTTagInt) {
                    items[b].stackSize = ((NBTTagInt) qtag).data;
                } else if (qtag instanceof NBTTagShort) {
                    items[b].stackSize = ((NBTTagShort) qtag).data;
                }
            }
        }
    }

    public static double getPlayerReach(EntityPlayer player) {
        return Minecraft.getMinecraft().playerController.getBlockReachDistance();
    }

    public static boolean areStacksTheSame(ItemStack is1, ItemStack is2) {
        if (is1 == null || is2 == null) {
            return false;
        }
        return is1.itemID == is2.itemID && is1.getItemDamage() == is2.getItemDamage();
    }

    public static void writeItemStackToData(ItemStack[] stack, DataOutputStream data) throws IOException {
        for (int i = 0; i < stack.length; i++) {
            // Itemstack
            if (stack[i] == null) {
                data.writeShort(-1);
            } else {
                data.writeShort(stack[i].itemID);
                data.writeByte(stack[i].stackSize);
                data.writeShort(stack[i].getItemDamage());

                // NBT
                NBTTagCompound compound = stack[i].stackTagCompound;
                if (compound == null) {
                    data.writeShort(-1);
                } else {
                    byte[] var3 = CompressedStreamTools.compress(compound);
                    data.writeShort((short) var3.length);
                    data.write(var3);
                }
            }
        }
    }

    public static void readItemStackFromData(ItemStack[] stack, DataInputStream data) throws IOException {
        for (int i = 0; i < stack.length; i++) {
            // Itemstack
            short itemID = data.readShort();
            if (itemID >= 0) {
                int stackSize = data.readByte();
                short damage = data.readShort();
                stack[i] = new ItemStack(itemID, stackSize, damage);

                // NBT
                short legnth = data.readShort();
                if (legnth > 0) {
                    byte[] nbtArray = new byte[legnth];
                    data.readFully(nbtArray, 0, legnth);
                    NBTTagCompound nbt = CompressedStreamTools.decompress(nbtArray);
                    if (nbt != null) {
                        stack[i].stackTagCompound = nbt;
                    }
                }
            } else {
                stack[i] = null;
            }
        }
    }

    public static void writeNBTToData(NBTBase nbt, DataOutputStream data) throws IOException {
        NBTBase.writeNamedTag(nbt, data);
    }

    public static NBTBase readNBTFromData(DataInputStream data) throws IOException {
        return NBTBase.readNamedTag(data);
    }

    public int getDimensionForWorld(World world) {
        if (world instanceof WorldServer) {
            return ((WorldServer) world).provider.dimensionId;
        }
        if (world instanceof WorldClient) {
            return ((WorldClient) world).provider.dimensionId;
        }
        return world.getWorldInfo().getVanillaDimension();

    }

    void ejectItem(World worldObj, ItemStack is, boolean violent, EntityPlayer player, int to_side, Coords coord) {
        if (worldObj.isRemote) {
            return;
        }
        if ((is == null) || (is.stackSize == 0)) {
            return;
        }
        if (player == null) {
            to_side = -1;
        }
        double mult = 0.02D;
        if (violent) {
            mult = 0.2D;
        }
        Vec3 pos = worldObj.getWorldVec3Pool().getVecFromPool(coord.x + 0.5D, coord.y + 0.5D, coord.z + 0.5D);
        Vec3 vel = worldObj.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D, 0.0D);
        if (to_side != -1) {
            ForgeDirection dir = ForgeDirection.getOrientation(to_side);

            double d = 0.75D;
            pos.xCoord += dir.offsetX * d;
            pos.yCoord += dir.offsetY * d;
            pos.zCoord += dir.offsetZ * d;
            vel.xCoord = dir.offsetX;
            vel.yCoord = dir.offsetY;
            vel.zCoord = dir.offsetZ;
        } else if (player != null) {
            Vec3 vec = Vec3.createVectorHelper(player.posX - coord.x, player.posY - coord.y, player.posZ - coord.z);

            vec = vec.normalize();
            vel = vec;
            double d = 0.25D;

            pos.xCoord += vec.xCoord * d;
            pos.yCoord += vec.yCoord * d;
            pos.zCoord += vec.zCoord * d;
        } else {
            vel.xCoord = rand.nextGaussian();
            vel.yCoord = rand.nextGaussian();
            vel.zCoord = rand.nextGaussian();
        }
        EntityItem ent = new EntityItem(worldObj, pos.xCoord, pos.yCoord, pos.zCoord, is);
        ent.motionX = (vel.xCoord * mult);
        ent.motionY = (vel.yCoord * mult);
        ent.motionZ = (vel.zCoord * mult);
        worldObj.spawnEntityInWorld(ent);
    }


}