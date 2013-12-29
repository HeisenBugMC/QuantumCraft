package quantumcraft.gui;

import net.minecraft.inventory.Container;
import quantumcraft.gui.abstractguis.GuiBase;
import quantumcraft.inventory.ContainerQEExtractor;

public class GuiQEExtractor extends GuiBase {

    public GuiQEExtractor(Container par1Container) {
        super(par1Container, 200, 170);
        tile = ((ContainerQEExtractor) par1Container).tile;
        setStripColor(0, 1, 0);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.mc.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        addHoverHandler(new HoverHandler(0, this), 189, 9, 9, 9);
        addHoverHandler(new HoverHandler(1, this), 206, 50, 12, 68);
        addClickHandler(new ClickHandler(0), 189, 9, 9, 9);
    }

    protected void drawBackground() {
        super.drawBackground();
        drawTwoSlot();
    }

    protected void drawForeground() {
        if (this.renderContents) {
            drawBaseForeground();

            drawBasePowerBar();

            this.fontRenderer.drawString("Quantum Energy Extractor", 15, 15, 0x000000);
            this.fontRenderer.drawString("Reserved for", 128, 55, 0x333333);
            this.fontRenderer.drawString("upgrades", 138, 65, 0x333333);

            handleHover();
        }

    }

}
