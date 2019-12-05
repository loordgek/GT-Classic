package gtclassic.api.pipe;

import gtclassic.api.interfaces.IGTDebuggableTile;
import ic2.core.fluid.IC2Tank;
import ic2.core.util.obj.ITankListener;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class GTTilePipeFluid extends GTTilePipeBase implements ITankListener, IGTDebuggableTile, ITickable {

	private IC2Tank tank;

	public GTTilePipeFluid() {
		this.tank = new IC2Tank(1000);
		this.tank.addListener(this);
		this.addGuiFields("tank");
	}

	@Override
	public boolean canConnect(TileEntity tile, EnumFacing dir) {
		if (tile == null) {
			return false;
		}
		if (tile instanceof GTTilePipeFluid) {
			return true;
		}
		return tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir);
	}

	public void onTankChanged(IFluidTank tank) {
		this.getNetwork().updateTileGuiField(this, "tank");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tank.readFromNBT(nbt.getCompoundTag("tank"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		this.tank.writeToNBT(this.getTag(nbt, "tank"));
		return nbt;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? true
				: super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank)
				: super.getCapability(capability, facing);
	}

	public IC2Tank getTankInstance() {
		return this.tank;
	}

	

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void update() {
		if (world.getTotalWorldTime() % 2 == 0) {
			for (EnumFacing side : this.connection.getRandomIterator()) {
				BlockPos sidePos = this.pos.offset(side);
				if (world.isBlockLoaded(sidePos) && side != lastIn) {
					IFluidHandler fluidTile = FluidUtil.getFluidHandler(world, sidePos, side);
					boolean canExport = fluidTile != null && this.tank.getFluid() != null;
					if (canExport) {
						if (FluidUtil.tryFluidTransfer(fluidTile, this.tank, this.tank.getCapacity()
								/ 10, true) != null) {
							TileEntity tile = world.getTileEntity(sidePos);
							if (tile instanceof GTTilePipeFluid) {
								((GTTilePipeFluid) tile).lastIn = side.getOpposite();
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean canDebugWithMagnifyingGlass() {
		return true;
	}
	
	@Override
	public String[] debugInfo() {
		FluidStack fluid = this.tank.getFluid();
		String in = this.lastIn != null ? this.lastIn.toString() : "Null";
		if (fluid != null) {
			return new String[] { fluid.amount + "mB of " + fluid.getLocalizedName(), "Last In: " + in};
		} else {
			return new String[] { "Empty", "Last In: " + in };
		}
	}

	
}
