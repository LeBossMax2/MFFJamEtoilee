package fr.max2.mffjametoilee.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.LootTable.Builder;
import net.minecraft.world.storage.loot.ValidationTracker;
import net.minecraftforge.registries.ForgeRegistries;

public class ModLootTableProvider extends LootTableProvider
{
	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> tables = ImmutableList.of(
		Pair.of(() -> this::fishingLootTables, LootParameterSets.FISHING),
		Pair.of(() -> this::chestLootTables, LootParameterSets.CHEST),
		Pair.of(ModEntityLootTables::new, LootParameterSets.ENTITY),
		Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK));


	public ModLootTableProvider(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}
	
	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables()
	{
		return tables;
	}
	
	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker tracker)
	{
		map.forEach((name, table) ->
		{
			LootTableManager.func_227508_a_(tracker, name, table);
		});
	}
	
	private void fishingLootTables(BiConsumer<ResourceLocation, Builder> tableConsumer)
	{
		
	}
	
	private void chestLootTables(BiConsumer<ResourceLocation, Builder> tableConsumer)
	{
		
	}
	
	private static class ModEntityLootTables extends EntityLootTables
	{
		@Override
		protected void addTables()
		{
			
		}
		
		@Override
		protected Iterable<EntityType<?>> getKnownEntities()
		{
			return MFFJamEtoileeMod.filterRegistry(ForgeRegistries.ENTITIES);
		}
	}
	
	private static class ModBlockLootTables extends BlockLootTables
	{
		@Override
		protected void addTables()
		{
			
		}
		
		@Override
		protected Iterable<Block> getKnownBlocks()
		{
			return MFFJamEtoileeMod.filterRegistry(ForgeRegistries.BLOCKS).stream().filter(block -> block.getLootTable().getNamespace().equals(MFFJamEtoileeMod.MOD_ID)).collect(Collectors.toList());
		}
	}
	
	@Override
	public String getName()
	{
		return MFFJamEtoileeMod.MOD_ID + " LootTables";
	}
	
}
