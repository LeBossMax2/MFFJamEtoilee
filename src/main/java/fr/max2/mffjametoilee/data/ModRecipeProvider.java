package fr.max2.mffjametoilee.data;

import java.util.function.Consumer;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

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
		
		
		//Blocks
		
	}
	
	@Override
	public String getName()
	{
		return MFFJamEtoileeMod.MOD_ID + " Recipes";
	}
	
}
