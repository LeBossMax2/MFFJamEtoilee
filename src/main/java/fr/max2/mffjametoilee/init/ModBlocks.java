package fr.max2.mffjametoilee.init;

import java.util.function.Supplier;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.block.StabilizedStarBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks
{
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MFFJamEtoileeMod.MOD_ID);
	
	public static final RegistryObject<Block>
		STABILIZED_STAR = register("stabilized_star", () -> new StabilizedStarBlock(Properties.create(Material.ROCK).hardnessAndResistance(50.0F, 200.0F)));
	
	static
	{
		item(STABILIZED_STAR);
	}
	
	private static <B extends Block> RegistryObject<B> register(String name, Supplier<B> block)
	{
		return REGISTRY.register(name, block);
	}
	
	private static RegistryObject<Item> item(RegistryObject<? extends Block> block)
	{
		return ModItems.REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), itemProp()));
	}
	
	private static Item.Properties itemProp()
	{
		return new Item.Properties().group(ModItemGroups.MAIN);
	}
	
}
