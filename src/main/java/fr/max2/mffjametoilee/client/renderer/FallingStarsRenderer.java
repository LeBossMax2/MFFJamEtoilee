package fr.max2.mffjametoilee.client.renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.EvictingQueue;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fr.max2.mffjametoilee.FallingStarNightHandler;
import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = MFFJamEtoileeMod.MOD_ID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class FallingStarsRenderer
{
	private static final List<FallingStar> STARS = new ArrayList<>();
	private static final Random STAR_RAND = new Random();
	private static final float SPAWN_CHANCE = 0.4f;
	private static final int MAX_STARS = 5;
	
	@SubscribeEvent
	public static void tick(ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.world != null && !mc.isGamePaused())
		{
			// Tick stars
			Iterator<FallingStar> it = STARS.iterator();
			while (it.hasNext())
			{
				if (!it.next().tick())
				{
					it.remove();
				}
			}
			
			// Add stars
			if (FallingStarNightHandler.areStarsFalling() && STARS.size() < MAX_STARS && STAR_RAND.nextFloat() < SPAWN_CHANCE)
			{
				STARS.add(new FallingStar());
			}
		}
	}
	
	@SubscribeEvent
	public static void renderFallingStars(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (!STARS.isEmpty() && mc.gameSettings.renderDistanceChunks >= 4 && mc.world.dimension.getType() == DimensionType.OVERWORLD)
		{
			float partialTicks = event.getPartialTicks();
			float factor = 1.0F - mc.world.getRainStrength(partialTicks);
			float starBrightness = mc.world.getStarBrightness(partialTicks) * factor * 1.5f;
			
			if (starBrightness > 0.0F)
			{
				if (starBrightness > 1.0F)
					starBrightness = 1.0F;
				final float sb = starBrightness;
				
				RenderSystem.depthMask(false);
				RenderSystem.disableFog();
				RenderSystem.disableAlphaTest();
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				RenderSystem.disableTexture();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				
                MatrixStack stack = event.getMatrixStack();

                Tessellator tess = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tess.getBuffer();
                bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                
                STARS.forEach(star -> star.render(stack, bufferBuilder, sb, partialTicks));
                
                tess.draw();
				
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.enableTexture();
				RenderSystem.disableBlend();
				RenderSystem.enableAlphaTest();
				RenderSystem.defaultBlendFunc();
				RenderSystem.depthMask(true);
			}
		}
	}
	
	
	private static class FallingStar
	{
		private static final int MAX_TRAIL_LENGTH = 8;
		private final EvictingQueue<Vec3d> trail = EvictingQueue.create(MAX_TRAIL_LENGTH);
		private double x, y, z, roll, prevX, prevY, prevZ, prevRoll;
		private final double speed, angularSpeed, baseSize;
		private int age;
		private final int maxAge;
		
		public FallingStar()
		{
			double bx, by, bz, lenSqr;
			do // Find a random non zero vector inside the unit sphere
			{
				bx = STAR_RAND.nextFloat() * 2.0F - 1.0F;
				by = STAR_RAND.nextFloat() * 2.0F - 1.0F;
				bz = STAR_RAND.nextFloat() * 2.0F - 1.0F;
				lenSqr = bx * bx + by * by + bz * bz;
			}
			while (lenSqr > 1.0D || lenSqr < 0.0001D);
			
			// Normalize the vector
			double len = Math.sqrt(lenSqr);
			this.x = bx / len;
			this.y = by / len;
			this.z = bz / len;
			
			this.baseSize = 0.4F + STAR_RAND.nextFloat() * 0.3F;
			this.roll = STAR_RAND.nextDouble() * Math.PI * 2.0D;
			this.speed = 0.01f * STAR_RAND.nextFloat() + 0.005f;
			this.angularSpeed = 0.08f * STAR_RAND.nextFloat() - 0.04f;
			this.maxAge = 15 + STAR_RAND.nextInt(11);
			this.age = 0;

			this.prevX = x;
			this.prevY = y;
			this.prevZ = z;
			this.prevRoll = roll;
		}
		
		public boolean tick()
		{
			// Save previous state
			this.prevX = this.x;
			this.prevY = this.y;
			this.prevZ = this.z;
			this.prevRoll = this.roll;
			
			this.trail.add(new Vec3d(this.x, this.y, this.z));
			
			// Apply velocity
			double yaw = Math.atan2(this.x, this.z);
			double ysin = Math.sin(yaw);
			double ycos = Math.cos(yaw);
			
			double pitch = Math.atan2(Math.sqrt(this.x * this.x + this.z * this.z), this.y);
			double psin = Math.sin(pitch);
			double pcos = Math.cos(pitch);
			
			double rsin = Math.sin(this.roll);
			double rcos = Math.cos(this.roll);
			
			double u = this.speed;
			double v = 0.0D;
			
			double rv = v * rcos - u * rsin;
			double ru = u * rcos + v * rsin;
			
			double dxz = 0.0D * psin - rv * pcos;
			double dy = rv * psin + 0.0D * pcos;
			
			double dx = dxz * ysin - ru * ycos;
			double dz = ru * ysin + dxz * ycos;
			
			this.x += dx;
			this.y += dy;
			this.z += dz;
			
			// Renormalize the vector
			double len = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
			this.x /= len;
			this.y /= len;
			this.z /= len;
			
			// Apply rotation
			this.roll += this.angularSpeed;
			
			// Ageing
			this.age++;
			return this.age < this.maxAge;
		}
		
		private static double interp(double v, double prevV, float partialTicks)
		{
			return prevV + (v - prevV) * partialTicks;
		}
		
		public void render(MatrixStack stack, IVertexBuilder vertices, float starBrightness, float partialTicks)
		{
			double x = interp(this.x, this.prevX, partialTicks);
			double y = interp(this.y, this.prevY, partialTicks);
			double z = interp(this.z, this.prevZ, partialTicks);
			double size = (1.0F - (this.age + partialTicks) / this.maxAge) * baseSize; // Calculate the size proportionally to the remaining age
			double roll = interp(this.roll, this.prevRoll, partialTicks);
			
			double yaw = Math.atan2(x, z);
			double ysin = Math.sin(yaw);
			double ycos = Math.cos(yaw);
			
			double pitch = Math.atan2(Math.sqrt(x * x + z * z), y);
			double psin = Math.sin(pitch);
			double pcos = Math.cos(pitch);
			
			double rsin = Math.sin(roll);
			double rcos = Math.cos(roll);
			
			// Draw star
			for (int vertexIndex = 0; vertexIndex < 4; ++vertexIndex)
			{
				// Rotate the quad towards the camera
				double u = (((vertexIndex + 1) & 2) - 1) * size;
				double v = ((vertexIndex & 2) - 1) * size;
				
				double rv = v * rcos - u * rsin;
				double ru = u * rcos + v * rsin;
				
				double dxz = 0.0D * psin - rv * pcos;
				double dy = rv * psin + 0.0D * pcos;
				
				double dx = dxz * ysin - ru * ycos;
				double dz = ru * ysin + dxz * ycos;
				vertices.pos(stack.getLast().getMatrix(), (float)(x * 100.0D + dx), (float)(y * 100.0D + dy), (float)(z * 100.0D + dz)).color(starBrightness, starBrightness * 0.5F, starBrightness * 0.5F, starBrightness).endVertex();
			}
			
			// Draw trail
			int i = 0;
			for (Vec3d t : this.trail)
			{
				int c = i == 0 ? 1 : 2;
				for (int j = 0; j < c; j++)
				{
					for (int k = 0; k < 2; k++)
					{
						double u = -size;
						double v = ((j ^ k) * 2 - 1) * (i * size / MAX_TRAIL_LENGTH);
						
						double rv = v * rcos - u * rsin;
						double ru = u * rcos + v * rsin;
						
						double dxz = 0.0D * psin - rv * pcos;
						double dy = rv * psin + 0.0D * pcos;
						
						double dx = dxz * ysin - ru * ycos;
						double dz = ru * ysin + dxz * ycos;
						vertices.pos(stack.getLast().getMatrix(), (float)(t.x * 100.0D + dx), (float)(t.y * 100.0D + dy), (float)(t.z * 100.0D + dz)).color(starBrightness, starBrightness, starBrightness, (i * starBrightness / MAX_TRAIL_LENGTH)).endVertex();
					}
				}
				i++;
			}
			if (i != 0)
			{
				for (int j = 0; j < 2; j++)
				{
					double u = -size;
					double v = (j * 2 - 1) * size;
					
					double rv = v * rcos - u * rsin;
					double ru = u * rcos + v * rsin;
					
					double dxz = 0.0D * psin - rv * pcos;
					double dy = rv * psin + 0.0D * pcos;
					
					double dx = dxz * ysin - ru * ycos;
					double dz = ru * ysin + dxz * ycos;
					vertices.pos(stack.getLast().getMatrix(), (float)(x * 100.0D + dx), (float)(y * 100.0D + dy), (float)(z * 100.0D + dz)).color(starBrightness, starBrightness, starBrightness, starBrightness).endVertex();
				}
			}
		}
	}
	
}
