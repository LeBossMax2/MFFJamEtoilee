package fr.max2.mffjametoilee.init;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModItemGroups
{
	public static final ItemGroup MAIN = new ItemGroup(MFFJamEtoileeMod.MOD_ID + ".main")
	{
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon()
		{
			return new ItemStack(ModBlocks.NONE);
		}
	};
}
