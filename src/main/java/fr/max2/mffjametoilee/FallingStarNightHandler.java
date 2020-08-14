package fr.max2.mffjametoilee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.max2.mffjametoilee.init.ModItems;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = MFFJamEtoileeMod.MOD_ID)
public class FallingStarNightHandler
{
	private static final List<IChunk> OVERWORLD_CHUNKS = new ArrayList<>();
	private static final Random STAR_RAND = new Random();
	private static final float EVENT_CHANGE = 0.2f;
	private static final float STAR_SPAWN_CHANCE = 0.001f;
	private static boolean wasNight = false;
	private static boolean areStarsFalling = false; //TODO save / update on server start / quit

	@SubscribeEvent
	public static void OnChunkLoaded(ChunkEvent.Load event)
	{
		IChunk chunk = event.getChunk();
		IWorld world = chunk.getWorldForge();
		if (world != null && !world.isRemote() && world.getDimension().getType() == DimensionType.OVERWORLD)
		{
			OVERWORLD_CHUNKS.add(chunk);
		}
	}

	@SubscribeEvent
	public static void OnChunkUnloaded(ChunkEvent.Unload event)
	{
		IChunk chunk = event.getChunk();
		IWorld world = chunk.getWorldForge();
		if (world != null && !world.isRemote() && world.getDimension().getType() == DimensionType.OVERWORLD)
		{
			OVERWORLD_CHUNKS.remove(chunk);
		}
	}
	
	public static boolean areStarsFalling()
	{
		return areStarsFalling;
	}
	
	private static void updateFallingStarsState()
	{
		World world = ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD);
		boolean isNight = world.isNightTime();
		if (wasNight && !isNight)
		{
			areStarsFalling = false;
		}
		else if (!wasNight && isNight)
		{
			areStarsFalling = STAR_RAND.nextFloat() < EVENT_CHANGE;
		}
		wasNight = isNight;
	}
	
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event)
	{
		updateFallingStarsState();
		if (areStarsFalling())
		{
			for (IChunk chunk : OVERWORLD_CHUNKS)
			{
				if (STAR_RAND.nextFloat() < STAR_SPAWN_CHANCE)
				{
					spawnStar(chunk);
				}
			}
		}
	}
	
	private static boolean spawnStar(IChunk chunk)
	{
		int x = STAR_RAND.nextInt(16) + chunk.getPos().getXStart();
		int z = STAR_RAND.nextInt(16) + chunk.getPos().getZStart();
		
		World world = chunk.getWorldForge().getWorld();
		
		ItemEntity starItem = new ItemEntity(world, x, world.getActualHeight(), z, new ItemStack(ModItems.FALLEN_STAR.get()));
		
		return world.addEntity(starItem);
	}
}
