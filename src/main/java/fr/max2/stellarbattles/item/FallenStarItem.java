package fr.max2.stellarbattles.item;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class FallenStarItem extends Item
{
	public FallenStarItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity)
	{
		float particleChance = entity.onGround ? 0.002F : 0.2F; 
		if (random.nextFloat() < particleChance)
		{
			Vec3d motion = entity.getMotion();
			entity.world.addOptionalParticle(ParticleTypes.FIREWORK, true, entity.getPosX(), entity.getPosY() + 0.3D, entity.getPosZ(), -0.5D * motion.x + random.nextFloat() * 0.1D - 0.05D, -0.5D * motion.y + random.nextFloat() * 0.1D + 0.02D, -0.5D * motion.z + random.nextFloat() * 0.1D - 0.05D);
		}
		return false;
	}
}
