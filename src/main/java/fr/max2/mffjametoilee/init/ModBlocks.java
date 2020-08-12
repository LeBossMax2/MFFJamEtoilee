package fr.max2.mffjametoilee.init;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = MFFJamEtoileeMod.MOD_ID, bus = Bus.MOD)
@ObjectHolder(MFFJamEtoileeMod.MOD_ID)
public class ModBlocks
{
	public static final Block
		NONE = null;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
		);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(items(ModItemGroups.MAIN));
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenders(FMLClientSetupEvent event)
	{
		//RenderTypeLookup.setRenderLayer(NONE, RenderType.getCutout());
	}
	
	
	private static <B extends Block> B name(String name, B block)
	{
	    block.setRegistryName(MFFJamEtoileeMod.MOD_ID, name);
	    return block;
	}
	
	private static Item[] items(ItemGroup group, Block... blocks)
	{
		Item[] items = new Item[blocks.length];
		
		for (int i = 0; i < blocks.length; i++)
		{
			items[i] = item(group, blocks[i]);
		}
		
		return items;
	}
	
	private static Item item(ItemGroup group, Block block)
	{
		return item(block, new BlockItem(block, new Item.Properties().group(group)));
	}
	
	private static <I extends Item> I item(Block block, I item)
	{
		item.setRegistryName(block.getRegistryName());
		return item;
	}
	
}
