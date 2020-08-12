package fr.max2.mffjametoilee.init;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.inventory.container.ContainerType;
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
public class ModContainers
{
	public static final ContainerType<?> NONE = null;
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<ContainerType<?>> event)
	{
		//event.getRegistry().register(name("none", IForgeContainerType.create(NONE::new)));
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerGuis(FMLClientSetupEvent event)
	{
		//ScreenManager.registerFactory(NONE, NONE::new);
	}
	
	private static ContainerType<?> name(String name, ContainerType<?> container)
	{
	    return container.setRegistryName(MFFJamEtoileeMod.MOD_ID, name);
	}
}
