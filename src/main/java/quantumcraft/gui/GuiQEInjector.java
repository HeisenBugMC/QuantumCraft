package quantumcraft.gui;

import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;
import quantumcraft.gui.abstractguis.GuiBase;
import quantumcraft.inventory.ContainerQEInjector;
import quantumcraft.tile.TileQEInjector;

public class GuiQEInjector extends GuiBase {

    public GuiQEInjector(Container par1Container) {
        super(par1Container, 200, 170);
        tile = ((ContainerQEInjector) par1Container).tile;
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
        if (this.renderContents) {
            bindImage(GuiTextures.GUI_2SLOT_BG);
            drawQuad(30, 50, 0, 1, 0, 1, 53, 18);
            bindImage(GuiTextures.GUI_DIVIDER_V);
            drawQuad(110, 31, 0, 1, 0, 1, 2, 59);

        }
    }

    protected void drawPowerBar() {
        float flt = (float) tile.getCurrentEnergy() / (float) tile.getMaxEnergy();
        int h = (int) (flt * 67);
        int tarx = 213 + 3;
        int tary = 40 + 11 + 8 + (67 - h);
        bindImage(GuiTextures.GUI_POWER_BAR);
        drawTexturedModalRect(213, 40, 8, 9, 17, 105);
        drawTexturedModalRect(tarx, tary, 51, 9 + (67 - h), 10, h);
        drawTexturedModalRect(tarx, 40 + 11 + 8, 33, 9, 10, 67);

        if (buffHT[1]) {
            drawTexturedModalRect(tarx, 40 + 11 + 8, 69, 9, 10, 67);
        }
    }

    protected void drawForeground() {
        if (this.renderContents) {
            bindImage(GuiTextures.GUI_BUTTON_CLOSE);
            GL11.glColor3f(1F, buffHT[0] ? 0F : 0.4F, buffHT[0] ? 0F : 0.4F);
            drawQuad(189, 9, 0, 1, 0, 1, 9, 9);
            GL11.glColor3f(1F, 1F, 1F);

            drawPowerBar();

            this.fontRenderer.drawString("Quantum Energy Injector", 15, 15, 0x000000);
            this.fontRenderer.drawString("Reserved for", 128, 55, 0x333333);
            this.fontRenderer.drawString("upgrades", 138, 65, 0x333333);

            handleHover();
        }

    }

}
