package fr.max2.stellarbattles.init;

import java.util.function.Supplier;

import fr.max2.stellarbattles.StellarBattlesMod;
import fr.max2.stellarbattles.client.renderer.tileentity.StarBeaconRenderer;
import fr.max2.stellarbattles.tileentity.StarBeaconTileEntity;
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

@EventBusSubscriber(modid = StellarBattlesMod.MOD_ID, bus = Bus.MOD)
public class ModTileEntities
{
public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, StellarBattlesMod.MOD_ID);
	
	public static final RegistryObject<TileEntityType<StarBeaconTileEntity>> STAR_BEACON = register("star_beacon", () -> TileEntityType.Builder.create(StarBeaconTileEntity::new, ModBlocks.STAR_BEACON.get()));
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenders(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(STAR_BEACON.get(), StarBeaconRenderer::new);
	}
	
	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<TileEntityType.Builder<T>> tile)
	{
		return REGISTRY.register(name, () -> tile.get().build(null));
	}
	
}
