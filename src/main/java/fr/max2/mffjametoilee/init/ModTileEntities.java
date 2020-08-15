package fr.max2.mffjametoilee.init;

import java.util.function.Supplier;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.client.renderer.tileentity.StabilizedStarRenderer;
import fr.max2.mffjametoilee.tileentity.StabilizedStarTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = MFFJamEtoileeMod.MOD_ID, bus = Bus.MOD)
public class ModTileEntities
{
public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MFFJamEtoileeMod.MOD_ID);
	
	public static final RegistryObject<TileEntityType<StabilizedStarTileEntity>> STABILIZED_STAR = register("stabilized_star", () -> TileEntityType.Builder.<StabilizedStarTileEntity>create(StabilizedStarTileEntity::new, ModBlocks.STABILIZED_STAR.get()));
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenders(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(STABILIZED_STAR.get(), StabilizedStarRenderer::new);
	}
	
	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<TileEntityType.Builder<T>> tile)
	{
		return REGISTRY.register(name, () -> tile.get().build(null));
	}
	
}
