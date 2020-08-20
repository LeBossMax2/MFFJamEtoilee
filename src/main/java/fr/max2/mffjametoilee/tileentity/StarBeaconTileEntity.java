package fr.max2.mffjametoilee.tileentity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import fr.max2.mffjametoilee.init.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LockCode;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StarBeaconTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity
{
	
	/** List of effects that Beacons can apply */
	public static final Effect[][] EFFECTS_LIST = new Effect[][] { { Effects.NIGHT_VISION, Effects.BLINDNESS }, { Effects.SLOW_FALLING, Effects.LEVITATION }, { Effects.INVISIBILITY, Effects.GLOWING } };
	private static final Set<Effect> VALID_EFFECTS = Arrays.stream(EFFECTS_LIST).flatMap(Arrays::stream).collect(Collectors.toSet());
	/** A list of beam segments for this beacon */
	private boolean beamReady = false;
	private boolean checkBeamReady = false;
	/** Level of this beacon's pyramid. */
	private int levels;
	private int beamTop = -1;
	/** Primary potion effect given by this beacon */
	@Nullable
	private Effect primaryEffect;
	@Nullable
	private ITextComponent customName;
	private LockCode lockCode = LockCode.EMPTY_CODE;
	private final IIntArray dataArray = new IIntArray()
	{
		
		@Override
		public int get(int index)
		{
			switch (index)
			{
			case 0:
				return StarBeaconTileEntity.this.levels;
			case 1:
				return Effect.getId(StarBeaconTileEntity.this.primaryEffect);
			default:
				return 0;
			}
		}
		
		@Override
		public void set(int index, int value)
		{
			switch (index)
			{
			case 0:
				StarBeaconTileEntity.this.levels = value;
				break;
			case 1:
				if (!StarBeaconTileEntity.this.world.isRemote && StarBeaconTileEntity.this.beamReady)
				{
					StarBeaconTileEntity.this.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT);
				}
				
				StarBeaconTileEntity.this.primaryEffect = isBeaconEffect(value);
				break;
			}
			
		}
		
		@Override
		public int size()
		{
			return 3;
		}
	};
	
	public StarBeaconTileEntity()
	{
		super(ModTileEntities.STAR_BEACON.get());
	}
	
	public boolean isBeamReady()
	{
		return beamReady;
	}
	
	@Override
	public void tick()
	{
		int posX = this.pos.getX();
		int posY = this.pos.getY();
		int posZ = this.pos.getZ();
		BlockPos checkPos;
		if (this.beamTop < posY)
		{
			checkPos = this.pos;
			this.checkBeamReady = false;
			this.beamTop = posY - 1;
		}
		else
		{
			checkPos = new BlockPos(posX, this.beamTop + 1, posZ);
		}
		
		int topBlockY = this.world.getHeight(Heightmap.Type.WORLD_SURFACE, posX, posZ);
		
		for (int checkStep = 0; checkStep < 10 && checkPos.getY() <= topBlockY; ++checkStep)
		{
			BlockState checkState = this.world.getBlockState(checkPos);
			Block block = checkState.getBlock();
			float[] color = checkState.getBeaconColorMultiplier(this.world, checkPos, getPos());
			if (color != null)
			{
				this.checkBeamReady = true;
			}
			else if (!this.checkBeamReady || checkState.getOpacity(this.world, checkPos) >= 15 && block != Blocks.BEDROCK)
			{
				this.checkBeamReady = false;
				this.beamTop = topBlockY;
				break;
			}
			
			checkPos = checkPos.up();
			++this.beamTop;
		}
		
		int prevLevel = this.levels;
		if (this.world.getGameTime() % 80L == 0L)
		{
			if (this.beamReady)
			{
				this.checkBeaconLevel(posX, posY, posZ);
			}
			
			if (this.levels > 0 && this.beamReady)
			{
				this.addEffectToPlayers();
				this.playSound(SoundEvents.BLOCK_BEACON_AMBIENT);
			}
		}
		
		if (this.beamTop >= topBlockY)
		{
			this.beamTop = -1;
			boolean prevConstructed = prevLevel > 0;
			this.beamReady = this.checkBeamReady;
			if (!this.world.isRemote)
			{
				boolean constructed = this.levels > 0;
				if (!prevConstructed && constructed)
				{
					this.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE);
				}
				else if (prevConstructed && !constructed)
				{
					this.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
				}
			}
		}
		
	}
	
	private void checkBeaconLevel(int x, int y, int z)
	{
		this.levels = 0;
		
		for (int stage = 1; stage <= 3; this.levels = stage++)
		{
			int stateY = y - stage;
			if (stateY < 0)
				return;
			
			for (int k = x - stage; k <= x + stage; ++k)
			{
				for (int l = z - stage; l <= z + stage; ++l)
				{
					if (!this.world.getBlockState(new BlockPos(k, stateY, l)).isBeaconBase(this.world, new BlockPos(k, stateY, l), getPos()))
						return;
				}
			}
		}
		
	}
	
	/**
	 * invalidates a tile entity
	 */
	@Override
	public void remove()
	{
		this.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
		super.remove();
	}
	
	private void addEffectToPlayers()
	{
		if (!this.world.isRemote && this.primaryEffect != null)
		{
			double range = this.levels * 10 + 10;
			
			int effectDuration = (9 + this.levels * 2) * 20;
			AxisAlignedBB effectArea = (new AxisAlignedBB(this.pos)).grow(range).expand(0.0D, this.world.getHeight(), 0.0D);
			
			for (PlayerEntity p : this.world.getEntitiesWithinAABB(PlayerEntity.class, effectArea))
			{
				p.addPotionEffect(new EffectInstance(this.primaryEffect, effectDuration, 0, true, true));
			}
			
		}
	}
	
	public void playSound(SoundEvent soind)
	{
		this.world.playSound(null, this.pos, soind, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
	
	public int getLevels()
	{
		return this.levels;
	}
	
	/**
	 * Retrieves packet to send to the client whenever this Tile Entity is
	 * resynced via World.notifyBlockUpdate. For modded TE's, this packet comes
	 * back to you clientside in {@link #onDataPacket}
	 */
	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
	}
	
	/**
	 * Get an NBT compound to sync to the client with SPacketChunkData, used for
	 * initial loading of the chunk or when many blocks change at once. This
	 * compound comes back to you clientside in {@link handleUpdateTag}
	 */
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536.0D;
	}
	
	@Nullable
	private static Effect isBeaconEffect(int p_184279_0_)
	{
		Effect effect = Effect.get(p_184279_0_);
		return VALID_EFFECTS.contains(effect) ? effect : null;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.primaryEffect = isBeaconEffect(compound.getInt("Primary"));
		if (compound.contains("CustomName", 8))
		{
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
		}
		
		this.lockCode = LockCode.read(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putInt("Primary", Effect.getId(this.primaryEffect));
		compound.putInt("Levels", this.levels);
		if (this.customName != null)
		{
			compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
		}
		
		this.lockCode.write(compound);
		return compound;
	}
	
	/**
	 * Sets the custom name for this beacon.
	 */
	public void setCustomName(@Nullable ITextComponent aname)
	{
		this.customName = aname;
	}
	
	@Override
	@Nullable
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player)
	{
		return LockableTileEntity.canUnlock(player, this.lockCode, this.getDisplayName())
			? new StarBeaconContainer(id, playerInv, this.dataArray, IWorldPosCallable.of(this.world, this.getPos()))
			: null;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return this.customName != null ? this.customName : new TranslationTextComponent("container.beacon");
	}
	
	@Override
    @OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
}
