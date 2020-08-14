package fr.max2.mffjametoilee.data;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = MFFJamEtoileeMod.MOD_ID, bus = Bus.MOD)
public class ModDataProviders
{

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        if (event.includeServer())
        {
            gen.addProvider(new ModBlockTagsProvider(gen));
            gen.addProvider(new ModItemTagsProvider(gen));
            gen.addProvider(new ModRecipeProvider(gen));
            gen.addProvider(new ModLootTableProvider(gen));
        }
        
        if (event.includeClient())
        {
        	ModBlockStateProvider blockStates = new ModBlockStateProvider(gen, MFFJamEtoileeMod.MOD_ID, event.getExistingFileHelper());
            gen.addProvider(blockStates);
            gen.addProvider(new ModItemModelProvider(gen, MFFJamEtoileeMod.MOD_ID, blockStates.getExistingFileHelper()));
            gen.addProvider(new ModLanguagesProvider(gen, MFFJamEtoileeMod.MOD_ID));
        }
    }
	
}