package fr.max2.stellarbattles.item;

import java.util.Collection;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableList;

import fr.max2.stellarbattles.entity.StarProjectileEntity;
import fr.max2.stellarbattles.init.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class StarCannonItem extends ShootableItem
{

	public StarCannonItem(Properties builder)
	{
		super(builder);
	}
	
	@Override
	public Predicate<ItemStack> getAmmoPredicate()
	{
		return stack -> 
				stack.getItem() == ModItems.FALLEN_STAR.get()
			 || stack.getItem() == ModItems.MINIATURE_STAR.get()
			 || stack.getItem() == Items.NETHER_STAR;
	}

	@Override
	public Predicate<ItemStack> getInventoryAmmoPredicate()
	{
		return stack -> stack.getItem() == ModItems.FALLEN_STAR.get();
	}
	
	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 10;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BOW;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		return new ActionResult<>(this.onStartCharge(player, hand, stack), stack);
	}
	
	protected ActionResultType onStartCharge(PlayerEntity player, Hand hand, ItemStack stack)
	{
		boolean hasAmmo = !player.findAmmo(stack).isEmpty();
		
		if (!player.abilities.isCreativeMode && !hasAmmo)
		{
			return ActionResultType.FAIL;
		}
		
		player.setActiveHand(hand);
		return ActionResultType.CONSUME;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entity;
			
			Hand activeHand = player.getActiveHand();
			shoot(world, player, stack, activeHand);
		}
		return stack;
	}
	
	protected void shoot(World world, PlayerEntity shooter, ItemStack stack, Hand activeHand)
	{
		boolean infinityEnch = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
		ItemStack ammoStack = shooter.findAmmo(stack);
		
		if (!ammoStack.isEmpty() || shooter.abilities.isCreativeMode || infinityEnch)
		{
			if (ammoStack.isEmpty())
				ammoStack = new ItemStack(ModItems.FALLEN_STAR.get());
			
			boolean infiniteAmmo = shooter.abilities.isCreativeMode || (ammoStack.getItem() == ModItems.FALLEN_STAR.get() && infinityEnch);
			if (!world.isRemote)
			{
				StarProjectileEntity projectile;
				int itemDamage = 1;
				/*if (ammoStack.getItem() == Items.NETHER_STAR)
				{
					projectile = new FireworkRocketEntity(world, new ItemStack(Items.FIREWORK_ROCKET), shooter.getPosX(), shooter.getPosYEye() - 0.15F, shooter.getPosZ(), true);
					itemDamage = 3;
				}
				else
				{
					ArrowItem arrowItem = (ArrowItem)Items.ARROW;
					AbstractArrowEntity arrowEntity = arrowItem.createArrow(world, ammoStack, shooter);
					arrowEntity.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
					projectile = arrowEntity;
				}*/
				StarProjectileEntity.StarType type = StarProjectileEntity.StarType.FALLEN;
				if (ammoStack.getItem() == Items.NETHER_STAR)
				{
					type = StarProjectileEntity.StarType.NETHER;
					itemDamage = 3;
				}
				else if (ammoStack.getItem() == ModItems.MINIATURE_STAR.get())
				{
					type = StarProjectileEntity.StarType.MINIATURE;
					itemDamage = 2;
				}
				Vec3d dir = shooter.getLook(1.0F);
				projectile = new StarProjectileEntity(shooter, dir.x * 10.0D, dir.y * 10.0D, dir.z * 10.0D, type, world);
				
				//projectile.shoot(dir.x, dir.y, dir.z, 3.2F, 0.5F);
				
				stack.damageItem(itemDamage, shooter, p -> p.sendBreakAnimation(activeHand));
				world.addEntity((Entity) projectile);
				world.playSound((PlayerEntity) null, shooter.getPosX(), shooter.getPosY(), shooter.getPosZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 0.5F);
			}
			
			if (!infiniteAmmo && !shooter.abilities.isCreativeMode)
			{
				ammoStack.shrink(1);
				if (ammoStack.isEmpty())
				{
					shooter.inventory.deleteStack(ammoStack);
				}
			}
			
			shooter.addStat(Stats.ITEM_USED.get(this));
		}
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.INFINITY;
	}
	
	@Override
	public Collection<ItemGroup> getCreativeTabs()
	{
		if (this.getGroup() == ItemGroup.COMBAT)
		{
			return super.getCreativeTabs();
		}
		return ImmutableList.of(this.getGroup(), ItemGroup.COMBAT);
	}
	
}
