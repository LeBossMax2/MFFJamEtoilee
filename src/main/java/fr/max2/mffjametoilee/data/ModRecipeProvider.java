package fr.max2.mffjametoilee.data;

import java.util.function.Consumer;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.init.ModBlocks;
import fr.max2.mffjametoilee.init.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

public class ModRecipeProvider extends RecipeProvider
{

	public ModRecipeProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
	{
		//Items
		ShapedRecipeBuilder.shapedRecipe(ModItems.MINIATURE_STAR.get())
			.patternLine(" S ")
			.patternLine("SDS")
			.patternLine(" S ")
			.key('S', ModItems.FALLEN_STAR.get())
			.key('D', Tags.Items.GEMS_DIAMOND)
			.addCriterion("has_fallen_star", hasItem(ModItems.FALLEN_STAR.get()))
			.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(ModItems.STAR_CANNON.get())
			.patternLine("ISI")
			.patternLine("OCO")
			.patternLine(" I ")
			.key('S', ModItems.MINIATURE_STAR.get())
			.key('O', Tags.Items.OBSIDIAN)
			.key('C', Items.CROSSBOW)
			.key('I', Tags.Items.INGOTS_IRON)
			.addCriterion("has_mignature_star", this.hasItem(ModItems.MINIATURE_STAR.get()))
			.addCriterion("has_crossbow", this.hasItem(Items.CROSSBOW))
			.build(consumer);
		
		//Blocks
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.STAR_BEACON.get())
			.patternLine("GGG")
			.patternLine("GSG")
			.patternLine("OOO")
			.key('S', ModItems.MINIATURE_STAR.get())
			.key('G', Blocks.GLASS)
			.key('O', Blocks.OBSIDIAN)
			.addCriterion("has_mignature_star", this.hasItem(ModItems.MINIATURE_STAR.get()))
			.build(consumer);
	}
	
	@Override
	public String getName()
	{
		return MFFJamEtoileeMod.MOD_ID + " Recipes";
	}
	
}
