package fr.max2.mffjametoilee.init;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.tileentity.TileEntityType;
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
public class ModTileEntities
{
	//public static final TileEntityType<EnergyStorageSpyTileEntity>
	//	ENERGY_STORAGE_SPY = null;

	@SubscribeEvent
	public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().registerAll(
			//name("energy_storage_spy", TileEntityType.Builder.create(EnergyStorageSpyTileEntity::new, ModBlocks.NONE))
		);
		
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(FMLClientSetupEvent event)
	{
		
	}
	
	private static TileEntityType<?> name(String name, TileEntityType.Builder<?> builder)
	{
		TileEntityType<?> type = builder.build(null);
	    type.setRegistryName(MFFJamEtoileeMod.MOD_ID, name);
	    return type;
	}
	
}
