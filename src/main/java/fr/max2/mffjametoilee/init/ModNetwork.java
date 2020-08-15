package fr.max2.mffjametoilee.init;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.network.FallingStarsStateMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@EventBusSubscriber(modid = MFFJamEtoileeMod.MOD_ID, bus = Bus.MOD)
public class ModNetwork
{
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(MFFJamEtoileeMod.MOD_ID, "main_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	@SubscribeEvent
	public static void registerPackets(FMLCommonSetupEvent event)
	{
		registerClient(FallingStarsStateMessage.class, FallingStarsStateMessage::encode, FallingStarsStateMessage::decode, FallingStarsStateMessage::onReceived);
	}
	
	private static int lastIndex = 0;
	
	private static <MSG> void registerServer(Class<MSG> messageClass,  BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
	{
		lastIndex++;
		CHANNEL.registerMessage(lastIndex, messageClass, encoder, decoder, messageConsumer, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
	
	private static <MSG> void registerClient(Class<MSG> messageClass,  BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
	{
		lastIndex++;
		CHANNEL.registerMessage(lastIndex, messageClass, encoder, decoder, messageConsumer, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
	
	/**
	 * Send a packet to a player.
	 * Must be called Server side. 
	 */
	public static void sendTo(Object msg, ServerPlayerEntity player)
	{
		if (!(player instanceof FakePlayer))
		{
			CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
		}
	}
}
