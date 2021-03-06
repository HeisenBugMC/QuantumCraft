package quantumcraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import quantumcraft.inventory.abstractinv.ContainerUpdatedBase;
import quantumcraft.tile.TileQuantumEnergyExtractor;

public class ContainerQuantumEnergyExtractor extends ContainerUpdatedBase {

    public TileQuantumEnergyExtractor tile;

    public ContainerQuantumEnergyExtractor(InventoryPlayer ip, TileQuantumEnergyExtractor te) {
        super(ip);
        maxStackSize = 1;
        tile = te;
        this.addSlotToContainer(new Slot(tile, 0, 40, 60));
        this.addSlotToContainer(new SlotOutput(tile, 1, 75, 60));
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return this.tile.isUseableByPlayer(par1EntityPlayer);
    }

}
