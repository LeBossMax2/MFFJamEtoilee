package fr.max2.mffjametoilee.network;

import java.util.function.Supplier;

import fr.max2.mffjametoilee.FallingStarNightHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class FallingStarsStateMessage
{
	private boolean starsFalling;

	public FallingStarsStateMessage(boolean starsFalling)
	{
		this.starsFalling = starsFalling;
	}
	
	public void encode(PacketBuffer buf)
	{
		buf.writeBoolean(this.starsFalling);
	}
	
	public static FallingStarsStateMessage decode(PacketBuffer buf)
	{
		return new FallingStarsStateMessage(buf.readBoolean());
	}
	
	public void onReceived(Supplier<NetworkEvent.Context> ctxSup)
	{
		NetworkEvent.Context ctx = ctxSup.get();
		ctx.enqueueWork(() -> FallingStarNightHandler.setStarsFalling(this.starsFalling));
		ctx.setPacketHandled(true);
	}
}
