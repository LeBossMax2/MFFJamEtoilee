package fr.max2.mffjametoilee.init;

import java.util.function.Supplier;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MFFJamEtoileeMod.MOD_ID);
	
	public static final RegistryObject<Item>
		FALLEN_STAR = register("fallen_star", () -> new Item(prop())),
		MIGNATURE_STAR = register("mignature_star", () -> new Item(prop()));
	
	private static Properties prop()
	{
		return new Properties().group(ModItemGroups.MAIN);
	}
	
	private static <I extends Item> RegistryObject<I> register(String name, Supplier<I> item)
	{
		return REGISTRY.register(name, item);
	}
	
}
