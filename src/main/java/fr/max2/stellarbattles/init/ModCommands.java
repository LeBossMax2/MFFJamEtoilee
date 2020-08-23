package fr.max2.stellarbattles.init;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import fr.max2.stellarbattles.FallingStarNightHandler;
import fr.max2.stellarbattles.StellarBattlesMod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@EventBusSubscriber(modid = StellarBattlesMod.MOD_ID)
public class ModCommands
{
	@SubscribeEvent
	public static void serverStartingEvent(FMLServerStartingEvent event)
	{
		event.getCommandDispatcher().register(
			LiteralArgumentBuilder.<CommandSource>literal("setstarsfalling")
			.requires(src -> src.hasPermissionLevel(2))
			.executes(ctx -> setFallingStars(ctx.getSource(), true))
			.then(
				Commands.argument("falling", BoolArgumentType.bool())
				.executes(ctx -> setFallingStars(ctx.getSource(), BoolArgumentType.getBool(ctx, "falling")))
			)
		);
	}
	
	private static int setFallingStars(CommandSource src, boolean falling)
	{
		if (!src.getServer().getWorld(DimensionType.OVERWORLD).isNightTime())
		{
			src.sendErrorMessage(new TranslationTextComponent(StellarBattlesMod.MOD_ID + ".command.setstarsfalling.error.day"));
			return 0;
		}
		else
		{
			if (FallingStarNightHandler.updateFallingState(falling))
			{
				src.sendFeedback(new TranslationTextComponent(StellarBattlesMod.MOD_ID + ".command.setstarsfalling." + (falling ? "start" : "stop")), true);
				return Command.SINGLE_SUCCESS;
			}
			else
			{
				src.sendErrorMessage(new TranslationTextComponent(StellarBattlesMod.MOD_ID + ".command.setstarsfalling.error." + (falling ? "alreadystarted" : "alreadystopped")));
				return 0;
			}
		}
	}
}
