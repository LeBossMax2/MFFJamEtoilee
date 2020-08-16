package fr.max2.mffjametoilee.entity;

import fr.max2.mffjametoilee.init.ModEntities;
import fr.max2.mffjametoilee.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class StarProjectileEntity extends DamagingProjectileEntity implements IEntityAdditionalSpawnData, IRendersAsItem
{
	private StarType starType;

	public StarProjectileEntity(EntityType<? extends StarProjectileEntity> type, World world)
	{
		super(type, world);
	}

	public StarProjectileEntity(double x, double y, double z, double accelX, double accelY, double accelZ, StarType starType, World world)
	{
		super(ModEntities.STAR_PROJECTILE.get(), x, y, z, accelX, accelY, accelZ, world);
		this.starType = starType;
	}

	public StarProjectileEntity(LivingEntity shooter, double accelX, double accelY, double accelZ, StarType starType, World world)
	{
		super(ModEntities.STAR_PROJECTILE.get(), shooter, accelX, accelY, accelZ, world);
		this.setPosition(shooter.getPosX(), shooter.getPosYEye() - 0.15F, shooter.getPosZ());
		this.starType = starType;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem()
	{
		return this.starType.item.get();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}
	
	@Override
	protected boolean isFireballFiery()
	{
		return false;
	}
	
	@Override
	protected IParticleData getParticle()
	{
		return ParticleTypes.FIREWORK;
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		super.onImpact(result);
		if (!this.world.isRemote)
		{
			if (result.getType() == RayTraceResult.Type.ENTITY)
			{
				float damage = starType == StarType.FALLEN ? 6.0F : 10.0F;
				Entity target = ((EntityRayTraceResult) result).getEntity();
				target.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), damage);
				if (starType == StarType.NETHER && target instanceof LivingEntity)
				{
					((LivingEntity)target).addPotionEffect(new EffectInstance(Effects.WITHER, 100)); // 5 sec
				}
				this.applyEnchantments(this.shootingEntity, target);
			}
			
			if (starType != StarType.FALLEN)
			{
				boolean destroyBlocks = starType == StarType.NETHER && ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
				float radius = starType == StarType.NETHER ? 4.0F : 2.0F;
				this.world.createExplosion((Entity) null, this.getPosX(), this.getPosY(), this.getPosZ(), radius, false, destroyBlocks ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
			}
			this.remove();
		}
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeDouble(this.accelerationX);
		buffer.writeDouble(this.accelerationY);
		buffer.writeDouble(this.accelerationZ);
		buffer.writeByte(this.starType.ordinal());
	}

	@Override
	public void readSpawnData(PacketBuffer buffer)
	{
		this.accelerationX = buffer.readDouble();
		this.accelerationY = buffer.readDouble();
		this.accelerationZ = buffer.readDouble();
		this.starType = StarType.values()[buffer.readByte()];
	}
	
	public static enum StarType
	{
		FALLEN(ModItems.FALLEN_STAR::get),
		MINIATURE(ModItems.MINIATURE_STAR::get),
		NETHER(() -> Items.NETHER_STAR);
		
		private final NonNullLazy<ItemStack> item;

		private StarType(NonNullSupplier<Item> item)
		{
			this.item = NonNullLazy.of(() -> new ItemStack(item.get()));
		}
	}
	
}
