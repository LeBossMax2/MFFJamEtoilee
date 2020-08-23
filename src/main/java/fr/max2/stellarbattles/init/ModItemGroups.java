package fr.max2.stellarbattles.init;

import fr.max2.stellarbattles.StellarBattlesMod;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModItemGroups
{
	public static final ItemGroup MAIN = new ItemGroup(StellarBattlesMod.MOD_ID + ".main")
	{
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon()
		{
			return new ItemStack(ModItems.MINIATURE_STAR.get());
		}
	};
}
