package fr.max2.mffjametoilee.data;

import javax.annotation.Nonnull;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.init.ModBlocks;
import fr.max2.mffjametoilee.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
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
		cannonItem(ModItems.STAR_CANNON.get());
		simpleBlock(ModBlocks.STABILIZED_STAR.get());
	}
	
	protected ItemModelBuilder simpleItem(IForgeRegistryEntry<?> entry)
	{
		return singleTexture(name(entry), mcLoc("item/generated"), "layer0", itemTexture(entry));
	}
	
	protected ItemModelBuilder cannonItem(IForgeRegistryEntry<?> entry)
	{
		return singleTexture(name(entry), mcLoc("item/handheld"), "layer0", itemTexture(entry))
			.transforms()
				.transform(Perspective.THIRDPERSON_RIGHT).rotation(0,-90, 45).translation(0.0F, 3.0F, 0.5F).scale(1.0F, 1.0F, 1.0F).end()
				.transform(Perspective.THIRDPERSON_LEFT ).rotation(0, 90,-45).translation(0.0F, 3.0F, 0.5F).scale(1.0F, 1.0F, 1.0F).end()
			.end();
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
