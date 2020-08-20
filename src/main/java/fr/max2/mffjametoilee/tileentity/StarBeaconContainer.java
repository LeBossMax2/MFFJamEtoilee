package fr.max2.mffjametoilee.tileentity;

import javax.annotation.Nullable;

import fr.max2.mffjametoilee.init.ModBlocks;
import fr.max2.mffjametoilee.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.BeaconContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.potion.Effect;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;

public class StarBeaconContainer extends BeaconContainer
{
	private final IWorldPosCallable pos;
	
	public StarBeaconContainer(int id, IInventory beacon, IIntArray beaconData, IWorldPosCallable pos)
	{
		super(id, beacon, beaconData, pos);
		this.pos = pos;
	}

	public StarBeaconContainer(int id, IInventory beacon)
	{
		super(id, beacon);
		this.pos = IWorldPosCallable.DUMMY;
	}

	@Override
	public ContainerType<?> getType()
	{
		return ModContainers.STAR_BEACON.get();
	}
	
	@Override
	@Nullable
	public Effect func_216968_g() // Secondary effect
	{
		return null;
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(this.pos, playerIn, ModBlocks.STAR_BEACON.get());
	}
	
}
