package fr.max2.mffjametoilee.block;

import javax.annotation.Nullable;

import fr.max2.mffjametoilee.tileentity.StabilizedStarTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class StabilizedStarBlock extends Block
{

	public StabilizedStarBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new StabilizedStarTileEntity();
	}
	
}
