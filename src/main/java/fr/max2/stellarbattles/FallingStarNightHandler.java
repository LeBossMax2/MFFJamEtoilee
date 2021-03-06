package fr.max2.stellarbattles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.max2.stellarbattles.init.ModItems;
import fr.max2.stellarbattles.init.ModNetwork;
import fr.max2.stellarbattles.network.FallingStarsStateMessage;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = StellarBattlesMod.MOD_ID)
public class FallingStarNightHandler
{
	private static final List<IChunk> OVERWORLD_CHUNKS = new ArrayList<>();
	private static final Random STAR_RAND = new Random();
	private static final float EVENT_CHANGE = 0.2f;
	private static final float STAR_SPAWN_CHANCE = 0.0004f;
	private static boolean wasNight = false;
	private static ThreadLocal<Boolean> areStarsFalling = ThreadLocal.withInitial(() -> false); //TODO save / update on server start / quit

	@SubscribeEvent
	public static void onChunkLoaded(ChunkEvent.Load event)
	{
		IChunk chunk = event.getChunk();
		IWorld world = chunk.getWorldForge();
		if (world != null && !world.isRemote() && world.getDimension().getType() == DimensionType.OVERWORLD)
		{
			OVERWORLD_CHUNKS.add(chunk);
		}
	}

	@SubscribeEvent
	public static void onChunkUnloaded(ChunkEvent.Unload event)
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
		return areStarsFalling.get();
	}
	
	public static void setStarsFalling(boolean falling)
	{
		areStarsFalling.set(falling);
	}
	
	public static boolean updateFallingState(boolean falling)
	{
		if (areStarsFalling() != falling)
		{
			setStarsFalling(falling);
			ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new FallingStarsStateMessage(falling));
			return true;
		}
		return false;
	}
	
	private static void updateFallingStarsState()
	{
		World world = ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD);
		boolean isNight = world.isNightTime();
		if (wasNight && !isNight)
		{
			updateFallingState(false);
		}
		else if (!wasNight && isNight)
		{
			updateFallingState(STAR_RAND.nextFloat() < EVENT_CHANGE);
		}
		wasNight = isNight;
	}
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (event.getPlayer() instanceof ServerPlayerEntity)
		{
			ModNetwork.sendTo(new FallingStarsStateMessage(areStarsFalling()), (ServerPlayerEntity)event.getPlayer());
		}
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
		starItem.setNoPickupDelay();
		starItem.lifespan = 2000; // 1 min
		
		return world.addEntity(starItem);
	}
}
