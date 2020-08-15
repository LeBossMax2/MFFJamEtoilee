package fr.max2.mffjametoilee.data;

import javax.annotation.Nonnull;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.init.ModBlocks;
import fr.max2.mffjametoilee.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModItemModelProvider extends ItemModelProvider
{

	public ModItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
	{
		super(generator, modid, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		simpleItem(ModItems.MINIATURE_STAR.get());
		simpleItem(ModItems.FALLEN_STAR.get());
		simpleBlock(ModBlocks.STABILIZED_STAR.get());
	}
	
	protected void simpleItem(IForgeRegistryEntry<?> entry)
	{
		singleTexture(name(entry), mcLoc("item/generated"), "layer0", itemTexture(entry));
	}
	
	protected void simpleBlock(Block block)
	{
		withExistingParent(name(block), blockModel(block));
	}

    protected ResourceLocation blockModel(Block block)
    {
        ResourceLocation name = block.getRegistryName();
        return new ResourceLocation(name.getNamespace(), BLOCK_FOLDER + "/" + name.getPath());
    }

    protected ResourceLocation itemTexture(IForgeRegistryEntry<?> entry)
    {
        ResourceLocation name = entry.getRegistryName();
        return new ResourceLocation(name.getNamespace(), (entry instanceof Block ? BLOCK_FOLDER : ITEM_FOLDER) + "/" + name.getPath());
    }

    protected String name(IForgeRegistryEntry<?> entry)
    {
        return entry.getRegistryName().getPath();
    }

	@Override
	public @Nonnull String getName()
	{
		return MFFJamEtoileeMod.MOD_ID + " Item Models";
	}
	
}
