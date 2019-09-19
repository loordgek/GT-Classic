package gtclassic.gui;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import gtclassic.tile.GTTileUUMAssembler;
import ic2.core.inventory.gui.GuiIC2;
import ic2.core.inventory.gui.components.GuiComponent;
import ic2.core.platform.registry.Ic2GuiComp;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTGuiCompUUMAssembler extends GuiComponent {

	GTTileUUMAssembler block;

	public GTGuiCompUUMAssembler(GTTileUUMAssembler tile) {
		super(Ic2GuiComp.nullBox);
		this.block = tile;
	}

	@Override
	public List<ActionRequest> getNeededRequests() {
		return Arrays.asList(ActionRequest.FrontgroundDraw);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawFrontground(GuiIC2 gui, int mouseX, int mouseY) {
		gui.drawString("EU: " + this.block.energy, 12, 18, Color.cyan.hashCode());
		int stored = this.block.getStoredUU();
		gui.drawString("UU: " + stored, 12, 28, Color.cyan.hashCode());
		int cost = this.block.getCurrentCost();
		gui.drawString("Cost: " + cost, 12, 38, Color.cyan.hashCode());
		int per = this.block.getAmountPer();
		gui.drawString("Produced: " + per, 12, 48, Color.cyan.hashCode());
	}
}
