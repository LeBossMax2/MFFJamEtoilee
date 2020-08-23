package fr.max2.stellarbattles;

import java.util.List;
import java.util.stream.Collectors;

import fr.max2.stellarbattles.init.ModBlocks;
import fr.max2.stellarbattles.init.ModContainers;
import fr.max2.stellarbattles.init.ModEntities;
import fr.max2.stellarbattles.init.ModItems;
import fr.max2.stellarbattles.init.ModTileEntities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(StellarBattlesMod.MOD_ID)
public class StellarBattlesMod
{
	public static final String MOD_ID = "stellarbattles";
	
	
	public StellarBattlesMod()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModItems.REGISTRY.register(modEventBus);
		ModBlocks.REGISTRY.register(modEventBus);
		ModEntities.REGISTRY.register(modEventBus);
		ModTileEntities.REGISTRY.register(modEventBus);
		ModContainers.REGISTRY.register(modEventBus);
	}
	
	public static <T extends IForgeRegistryEntry<T>> List<T> filterRegistry(IForgeRegistry<T> registry)
	{
		return registry.getValues().stream().filter(entry -> entry.getRegistryName().getNamespace().equals(MOD_ID)).collect(Collectors.toList());
	}
	
}
