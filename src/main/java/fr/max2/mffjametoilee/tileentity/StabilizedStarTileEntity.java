package fr.max2.mffjametoilee.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fr.max2.mffjametoilee.init.ModTileEntities;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class StabilizedStarTileEntity extends TileEntity implements ITickableTileEntity
{
	private final StarEnergy content;
	private final LazyOptional<IEnergyStorage> optional;

	public StabilizedStarTileEntity(TileEntityType<?> tileEntityTypeIn)
	{
		super(tileEntityTypeIn);
		this.content = new StarEnergy(1000);
		this.optional = LazyOptional.of(() -> this.content);
	}
	
	public StabilizedStarTileEntity()
	{
		this(ModTileEntities.STABILIZED_STAR.get());
	}

	@Override
	public void tick()
	{
		this.content.tick();
	}
	
	@Override
	protected void invalidateCaps()
	{
		super.invalidateCaps();
		this.optional.invalidate();
	}
	
	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if (cap == CapabilityEnergy.ENERGY)
		{
			return this.optional.cast();
		}
		return super.getCapability(cap, side);
	}
	
	private static class StarEnergy extends EnergyStorage
	{
		public StarEnergy(int maxExtract)
		{
			super(maxExtract, 0, maxExtract);
		}
		
		public void tick()
		{
			this.energy = this.capacity; // Fully recharge each tick
		}
	}
	
}
