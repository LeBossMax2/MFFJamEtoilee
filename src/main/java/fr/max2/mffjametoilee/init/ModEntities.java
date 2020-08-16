package fr.max2.mffjametoilee.init;

import java.util.function.Supplier;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.entity.StarProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = MFFJamEtoileeMod.MOD_ID, bus = Bus.MOD)
public class ModEntities
{
public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, MFFJamEtoileeMod.MOD_ID);
	
	public static final RegistryObject<EntityType<StarProjectileEntity>> STAR_PROJECTILE = register("star_projectile", () -> EntityType.Builder.<StarProjectileEntity>create(StarProjectileEntity::new, EntityClassification.MISC).size(0.3125F, 0.3125F).setTrackingRange(4).setUpdateInterval(10).setShouldReceiveVelocityUpdates(true));
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenders(FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(STAR_PROJECTILE.get(), manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer(), 1.0F, true));
	}
	
	private static <E extends Entity> RegistryObject<EntityType<E>> register(String name, Supplier<EntityType.Builder<E>> tile)
	{
		return REGISTRY.register(name, () -> tile.get().build(MFFJamEtoileeMod.MOD_ID + ":" + name));
	}
	
}