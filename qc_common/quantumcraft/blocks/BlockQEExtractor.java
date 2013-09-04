package quantumcraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import quantumcraft.blocks.abstractblocks.BlockEnergySource;
import quantumcraft.tile.TileQEExtractor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockQEExtractor extends BlockEnergySource {

    public BlockQEExtractor(int id) {
        super(id, Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileQEExtractor();
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        iconFront = iconRegister.registerIcon("QuantumCraft:machineQEE_front");
        iconTop = iconRegister.registerIcon("QuantumCraft:machineQEE_top");
        iconTopR = iconRegister.registerIcon("QuantumCraft:machineQEE_top_r");
        iconSide = iconRegister.registerIcon("QuantumCraft:machineQEE_side");
        iconBottom = iconRegister.registerIcon("QuantumCraft:machineQEE_bottom");
        iconSide = iconRegister.registerIcon("QuantumCraft:machineQEE_side");
        iconBack = iconRegister.registerIcon("QuantumCraft:machineQEE_back");
    }

}