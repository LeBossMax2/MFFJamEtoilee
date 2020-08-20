package fr.max2.mffjametoilee.init;

import java.util.function.Supplier;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.block.StarBeaconBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = MFFJamEtoileeMod.MOD_ID, bus = Bus.MOD)
public class ModBlocks
{
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MFFJamEtoileeMod.MOD_ID);
	
	public static final RegistryObject<Block>
		STAR_BEACON = register("star_beacon", () -> new StarBeaconBlock(Properties.create(Material.GLASS, MaterialColor.DIAMOND).hardnessAndResistance(3.0F).lightValue(15).notSolid()));
	
	static
	{
		item(STAR_BEACON);
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerRenders(FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(STAR_BEACON.get(), RenderType.getCutout());
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
