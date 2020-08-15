package fr.max2.mffjametoilee.data;

import javax.annotation.Nonnull;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelProvider;

public class ModBlockStateProvider extends BlockStateProvider
{

	public ModBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper)
	{
		super(gen, modid, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		simpleBlock(ModBlocks.STABILIZED_STAR.get(), models().getExistingFile(modelName(ModBlocks.STABILIZED_STAR.get())));
	}
	
	@Override
	@Nonnull
	public String getName()
	{
		return MFFJamEtoileeMod.MOD_ID + " Block States";
	}
	
	public ExistingFileHelper getExistingFileHelper()
	{
		return models().existingFileHelper;
	}
	
	private static ResourceLocation modelName(Block block)
	{
		ResourceLocation name = block.getRegistryName();
		return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
	}
	
}
