package fr.max2.mffjametoilee.init;

import java.util.function.Supplier;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.client.screen.StarBeaconScreen;
import fr.max2.mffjametoilee.tileentity.StarBeaconContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
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
public class ModContainers
{
	public static final DeferredRegister<ContainerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, MFFJamEtoileeMod.MOD_ID);
	
	public static final RegistryObject<ContainerType<StarBeaconContainer>> STAR_BEACON = register("star_beacon", () -> StarBeaconContainer::new);

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerGuis(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(STAR_BEACON.get(), StarBeaconScreen::new);
	}
	
	private static <C extends Container> RegistryObject<ContainerType<C>> register(String name, Supplier<ContainerType.IFactory<C>> factory)
	{
		return REGISTRY.register(name, () -> new ContainerType<>(factory.get()));
	}
}
