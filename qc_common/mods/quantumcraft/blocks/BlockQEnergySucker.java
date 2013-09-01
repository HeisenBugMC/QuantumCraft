package mods.quantumcraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.quantumcraft.blocks.abstractblocks.BlockEnergySink;
import mods.quantumcraft.machine.TileQEnergySucker;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQEnergySucker extends BlockEnergySink {

    public BlockQEnergySucker(int id) {
        super(id, Material.iron);
        setHardness(10F);
        setResistance(5F);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileQEnergySucker();
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        iconFront = iconRegister.registerIcon("QuantumCraft:machineQES_front");
        iconTop = iconRegister.registerIcon("QuantumCraft:machineQES_top");
        iconTopR = iconRegister.registerIcon("QuantumCraft:machineQES_top_r");
        iconSide = iconRegister.registerIcon("QuantumCraft:machineQES_side");
        iconBottom = iconRegister.registerIcon("QuantumCraft:machineQES_bottom");
        iconSide = iconRegister.registerIcon("QuantumCraft:machineQES_side");
        iconBack = iconRegister.registerIcon("QuantumCraft:machineQES_back");
    }

}
