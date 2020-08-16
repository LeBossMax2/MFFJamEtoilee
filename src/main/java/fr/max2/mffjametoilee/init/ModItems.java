package fr.max2.mffjametoilee.init;

import java.util.function.Supplier;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.item.FallenStarItem;
import fr.max2.mffjametoilee.item.StarCannonItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MFFJamEtoileeMod.MOD_ID);
	
	public static final RegistryObject<Item>
		FALLEN_STAR = register("fallen_star", () -> new FallenStarItem(prop())),
		MINIATURE_STAR = register("miniature_star", () -> new Item(prop().rarity(Rarity.RARE))),
		STAR_CANNON = register("star_cannon", () -> new StarCannonItem(prop().maxDamage(500)));
	
	private static Properties prop()
	{
		return new Properties().group(ModItemGroups.MAIN);
	}
	
	private static <I extends Item> RegistryObject<I> register(String name, Supplier<I> item)
	{
		return REGISTRY.register(name, item);
	}
	
}
