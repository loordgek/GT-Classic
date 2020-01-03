package gtclassic.common.tile;

import java.util.Map;

import gtclassic.api.helpers.int3;
import gtclassic.api.interfaces.IGTDebuggableTile;
import gtclassic.common.GTBlocks;
import gtclassic.common.util.GTIBlockFilter;
import ic2.core.RotationList;
import ic2.core.block.base.tile.TileEntityMachine;
import ic2.core.util.helpers.AabbUtil;
import ic2.core.util.helpers.AabbUtil.BoundingBox;
import ic2.core.util.helpers.AabbUtil.Processor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class GTTilePipelineItemEnd extends TileEntityMachine implements ITickable, IGTDebuggableTile {

	private Processor task = null;
	private AabbUtil.IBlockFilter filter = new GTIBlockFilter(GTBlocks.pipelineItem);
	private int blockCount;

	public GTTilePipelineItemEnd() {
		super(0);
		this.blockCount = 0;
	}

	@Override
	public void update() {
		if (task != null && world.isAreaLoaded(pos, 16)) {
			task.update();
			if (!task.isFinished()) {
				return;
			}
			this.blockCount = task.getResults().size();
			int i = 0;
			for (BlockPos pPos : task.getResults()) {
				i++;
				if (i > 256) {
					break;
				}
				TileEntity worldTile = world.getTileEntity(pPos);
				if (worldTile instanceof GTTilePipelineItem && ((GTTilePipelineItem) worldTile).targetPos == null) {
					((GTTilePipelineItem) worldTile).targetPos = this.getExportTilePos();
				}
			}
		}
		if (world.getTotalWorldTime() % 128 == 0) {
			if (!world.isAreaLoaded(pos, 16))
				return;
			task = AabbUtil.createBatchTask(world, new BoundingBox(this.pos, 256), this.pos, RotationList.ALL, filter, 64, false, false, false);
			task.update();
		}
	}

	public BlockPos getExportTilePos() {
		int3 dir = new int3(getPos(), getFacing());
		return dir.forward(1).asBlockPos();
	}

	@Override
	public boolean canRemoveBlock(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean canSetFacing(EntityPlayer player, EnumFacing facing) {
		return facing != getFacing();
	}

	@Override
	public void getData(Map<String, Boolean> data) {
		data.put("Connected Pipes: " + this.blockCount, false);
	}
}
