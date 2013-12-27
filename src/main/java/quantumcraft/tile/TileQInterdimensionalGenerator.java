package quantumcraft.tile;

import net.minecraft.nbt.NBTTagCompound;
import quantumcraft.tile.abstracttiles.TileEnergySource;
import quantumcraft.util.BasicUtils;
import quantumcraft.util.Coords;
import quantumcraft.util.QInterdimensionalGeneratorDataObject;
import quantumcraft.util.QInterdimensionalGeneratorUtil;

public class TileQInterdimensionalGenerator extends TileEnergySource {

    private int generatorsInChunk = 1;

    @Override
    public int getMaxEnergy() {
        return 300;
    }

    @Override
    public int guiID() {
        // TODO - Change this
        return -1;
    }

    @Override
    public void onBlockBreak() {

    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.getWorldTime() % 20 == 0) this.addEnergy(10 / generatorsInChunk);
    }

    public void onQIGChange() {
        int xChunk = BasicUtils.getChunk(xCoord);
        int zChunk = BasicUtils.getChunk(zCoord);

        generatorsInChunk = 0;

        for (QInterdimensionalGeneratorDataObject generator : QInterdimensionalGeneratorUtil.getGenerators()) {
            int localXChunk = BasicUtils.getChunk(generator.getCoords().x);
            int localZChunk = BasicUtils.getChunk(generator.getCoords().z);
            if (xChunk == localXChunk && zChunk == localZChunk && generator.getWorld() == worldObj) generatorsInChunk++;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        QInterdimensionalGeneratorUtil
                .addGenerator(new QInterdimensionalGeneratorDataObject(worldObj, new Coords(xCoord, yCoord, zCoord)));
        QInterdimensionalGeneratorUtil.updateAllGenerators();
    }
}
