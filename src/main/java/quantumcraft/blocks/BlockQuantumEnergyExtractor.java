package quantumcraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import quantumcraft.blocks.abstractblocks.BlockEnergySource;
import quantumcraft.tile.TileQuantumEnergyExtractor;

public class BlockQuantumEnergyExtractor extends BlockEnergySource {

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileQuantumEnergyExtractor();
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        iconFront = iconRegister.registerIcon("QuantumCraft:machine_quantum_energy_extractor_front");
        iconTop = iconRegister.registerIcon("QuantumCraft:machine_quantum_energy_extractor_top");
        iconTopR = iconRegister.registerIcon("QuantumCraft:machine_quantum_energy_extractor_top_r");
        iconSide = iconRegister.registerIcon("QuantumCraft:machine_quantum_energy_extractor_side");
        iconBottom = iconRegister.registerIcon("QuantumCraft:machine_quantum_energy_extractor_bottom");
        iconSide = iconRegister.registerIcon("QuantumCraft:machine_quantum_energy_extractor_side");
        iconBack = iconRegister.registerIcon("QuantumCraft:machine_quantum_energy_extractor_back");
    }

}