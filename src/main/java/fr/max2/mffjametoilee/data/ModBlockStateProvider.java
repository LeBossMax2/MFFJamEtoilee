package fr.max2.mffjametoilee.data;

import static net.minecraftforge.client.model.generators.ModelProvider.*;

import javax.annotation.Nonnull;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
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
	
	
	protected void randomMirror(Block block)
	{
		ModelFile normalModel = cubeAll(block);
		ModelFile mirrorModel = models().singleTexture(block.getRegistryName().getPath() + "_mirrored", mcLoc(BLOCK_FOLDER + "/cube_mirrored_all"), "all", blockTexture(block));
		simpleBlock(block,
			new ConfiguredModel(normalModel),
			new ConfiguredModel(mirrorModel),
			new ConfiguredModel(normalModel, 0, 180, false),
			new ConfiguredModel(mirrorModel, 0, 180, false));
	}
	
	private static String name(Block block)
	{
		return block.getRegistryName().getPath();
	}
	
	private static ResourceLocation modelName(Block block)
	{
		ResourceLocation name = block.getRegistryName();
		return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
	}
	
	private static ResourceLocation extend(ResourceLocation rl, String suffix)
	{
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
	}
	
}
