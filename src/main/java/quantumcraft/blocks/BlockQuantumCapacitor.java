package quantumcraft.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import quantumcraft.blocks.abstractblocks.BlockEnergySink;
import quantumcraft.net.IQEnergySource;
import quantumcraft.net.QuantumEnergyNet;
import quantumcraft.tile.TileQuantumCapacitor;
import quantumcraft.util.Coords;

public class BlockQuantumCapacitor extends BlockEnergySink implements IQEnergySource {
    private int maxEnergyMultiplier = 1;

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        QuantumEnergyNet.propagateSourceLocation(world, new Coords(x, y, z));
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        QuantumEnergyNet.onAddedLink(world, new Coords(x, y, z));
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        iconFront = iconTopR =
                iconTop = iconSide = iconBottom = iconBack = iconRegister.registerIcon("QuantumCraft:Capacitor" + maxEnergyMultiplier);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileQuantumCapacitor te = new TileQuantumCapacitor();
        te.setMaxEnergyMultiplier(maxEnergyMultiplier);
        return te;
    }

    public void setMaxEnergyMultiplier(int max) {
        maxEnergyMultiplier = max;
    }

    @Override
    public int requestQuantumEnergy(World w, Coords l, int request) {
        return ((TileQuantumCapacitor) w.getTileEntity(l.getXCoord(), l.getYCoord(), l.getZCoord()))
                .requestQuantumEnergy(l, request);
    }
}
