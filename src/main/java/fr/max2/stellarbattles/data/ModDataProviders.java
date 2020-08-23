package fr.max2.stellarbattles.data;

import fr.max2.stellarbattles.StellarBattlesMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = StellarBattlesMod.MOD_ID, bus = Bus.MOD)
public class ModDataProviders
{

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        if (event.includeServer())
        {
            gen.addProvider(new ModRecipeProvider(gen));
            gen.addProvider(new ModLootTableProvider(gen));
        }
        
        if (event.includeClient())
        {
        	ModBlockStateProvider blockStates = new ModBlockStateProvider(gen, StellarBattlesMod.MOD_ID, event.getExistingFileHelper());
            gen.addProvider(blockStates);
            gen.addProvider(new ModItemModelProvider(gen, StellarBattlesMod.MOD_ID, blockStates.getExistingFileHelper()));
            gen.addProvider(new ModLanguagesProvider(gen, StellarBattlesMod.MOD_ID));
        }
    }
	
}
