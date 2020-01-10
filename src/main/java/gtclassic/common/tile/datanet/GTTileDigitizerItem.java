package gtclassic.common.tile.datanet;

import java.util.ArrayList;

import gtclassic.common.util.datanet.GTDataNet.DataType;
import gtclassic.common.util.datanet.GTFilterItemDigitizer;
import ic2.core.inventory.filters.BasicItemFilter;
import ic2.core.inventory.filters.IFilter;
import ic2.core.inventory.filters.InvertedFilter;
import ic2.core.inventory.transport.IItemTransporter;
import ic2.core.inventory.transport.TransporterManager;
import net.minecraft.item.ItemStack;

public class GTTileDigitizerItem extends GTTileInputNodeBase {

	public ArrayList<ItemStack> blacklist = new ArrayList<>();

	/** Transmits Items from the facing pos to valid output nodes on the network **/
	public GTTileDigitizerItem() {
		super(0);
	}

	@Override
	public boolean onDataNetTick(GTTileOutputNodeBase node) {
		if (node.dataType() != DataType.ITEM) {
			return false;
		}
		IItemTransporter slave = TransporterManager.manager.getTransporter(world.getTileEntity(this.pos.offset(this.getFacing())), true);
		if (slave == null) {
			return false;
		}
		IItemTransporter nodeTile = TransporterManager.manager.getTransporter(world.getTileEntity(node.inventoryPos()), true);
		if (nodeTile == null) {
			return false;
		}
		int limit = slave.getSizeInventory(getFacing());
		for (int i = 0; i < limit; ++i) {
			if (i == limit - 1) {
				blacklist.clear();
			}
			IFilter filter = node.inventoryFilter() != null ? node.inventoryFilter()
					: new InvertedFilter(new GTFilterItemDigitizer(this));
			ItemStack stack = slave.removeItem(filter, this.getFacing().getOpposite(), 64, false);
			ItemStack added = nodeTile.addItem(stack, node.inventoryFacing(), true);
			if (added.getCount() <= 0) {
				if (!stack.isEmpty()) {
					blacklist.add(stack);
				}
			} else {
				slave.removeItem(new BasicItemFilter(added), this.getFacing().getOpposite(), added.getCount(), true);
				blacklist.clear();
			}
		}
		return false;
	}
}