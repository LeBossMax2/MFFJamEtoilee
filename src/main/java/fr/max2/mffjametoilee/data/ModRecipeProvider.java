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
import net.minecraft.data.ShapelessRecipeBuilder;

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
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.MIGNATURE_STAR.get())
			.addIngredient(ModItems.FALLEN_STAR.get())
			.addIngredient(ModItems.FALLEN_STAR.get())
			.addIngredient(ModItems.FALLEN_STAR.get())
			.addIngredient(ModItems.FALLEN_STAR.get())
			.addCriterion("has_fallen_star", hasItem(ModItems.FALLEN_STAR.get()))
			.build(consumer);
		
		//Blocks
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.STABILIZED_STAR.get())
			.patternLine("GGG")
			.patternLine("GSG")
			.patternLine("OOO")
			.key('S', ModItems.MIGNATURE_STAR.get())
			.key('G', Blocks.GLASS)
			.key('O', Blocks.OBSIDIAN)
			.addCriterion("has_mignature_star", this.hasItem(ModItems.MIGNATURE_STAR.get()))
			.build(consumer);
	}
	
	@Override
	public String getName()
	{
		return MFFJamEtoileeMod.MOD_ID + " Recipes";
	}
	
}
