package fr.max2.stellarbattles.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import fr.max2.stellarbattles.init.ModItems;
import fr.max2.stellarbattles.tileentity.StarBeaconTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Matrix3f;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class StarBeaconRenderer extends TileEntityRenderer<StarBeaconTileEntity>
{
	public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");
	private static final int BEAM_HEIGHT = 1024; 
	private final ItemRenderer itemRenderer;

	public StarBeaconRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
	}

	@Override
	public void render(StarBeaconTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		ItemStack stack = new ItemStack(ModItems.MINIATURE_STAR.get());
		
		float time = tileEntity.getWorld().getGameTime() + partialTicks;
		
		matrixStack.push();
		
		matrixStack.translate(0.5F, 0.58F + Math.sin((time * 2.0F) % 360F / 360F * (Math.PI * 2.0D)) * 0.1F, 0.5F);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(time * 8.0F));
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(time * 4.0F));
		matrixStack.scale(0.5F, 0.5F, 0.5f);
		this.itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
		
		if (buffer instanceof IRenderTypeBuffer.Impl)
            ((IRenderTypeBuffer.Impl) buffer).finish();
		
		matrixStack.pop();
		
		if (tileEntity.isBeamReady())
		{
			renderBeamSegment(matrixStack, buffer, TEXTURE_BEACON_BEAM, time % 40.0f, BEAM_HEIGHT, DyeColor.WHITE.getColorComponentValues(), 0.2F, 0.25F);
		}
	}
	
	public static void renderBeamSegment(MatrixStack matrixStack, IRenderTypeBuffer buffer, ResourceLocation textureLocation, float worldTime, int height, float[] colors, float beamRadius, float glowRadius)
	{
		matrixStack.push();
		matrixStack.translate(0.5D, 0.0D, 0.5D);
		float v2 = 1.0F - (worldTime * 0.2F) % 1.0F;
		float r = colors[0];
		float g = colors[1];
		float b = colors[2];
		matrixStack.push();
		matrixStack.rotate(Vector3f.YP.rotationDegrees(worldTime * 2.25F - 45.0F));
		float v1 = height * 0.5F / beamRadius + v2;
		renderPart(matrixStack, buffer.getBuffer(RenderType.getBeaconBeam(textureLocation, true)), r, g, b, 0.5F, 0, height,
			0.0F, beamRadius,
			beamRadius, 0.0F,
			-beamRadius, 0.0F,
			0.0F, -beamRadius,
			0.0F, 1.0F, v1, v2);
		matrixStack.pop();
		v1 = height + v2;
		renderPart(matrixStack, buffer.getBuffer(RenderType.getBeaconBeam(textureLocation, true)), r, g, b, 0.125F, 0, height,
			-glowRadius, -glowRadius,
			glowRadius, -glowRadius,
			-glowRadius, glowRadius,
			glowRadius, glowRadius,
			0.0F, 1.0F, v1, v2);
		matrixStack.pop();
	}
	
	private static void renderPart(MatrixStack matrixStack, IVertexBuilder buffer, float r, float g, float b, float a, float yMin, float yMax, float x1, float z1, float x3, float z3, float x4, float z4, float x2, float z2, float u1, float u2, float v1, float v2)
	{
		MatrixStack.Entry matrixState = matrixStack.getLast();
		Matrix4f matrix = matrixState.getMatrix();
		Matrix3f normal = matrixState.getNormal();
		addQuad(matrix, normal, buffer, r, g, b, a, yMin, yMax, x1, z1, x3, z3, u1, u2, v1, v2);
		addQuad(matrix, normal, buffer, r, g, b, a, yMin, yMax, x2, z2, x4, z4, u1, u2, v1, v2);
		addQuad(matrix, normal, buffer, r, g, b, a, yMin, yMax, x3, z3, x2, z2, u1, u2, v1, v2);
		addQuad(matrix, normal, buffer, r, g, b, a, yMin, yMax, x4, z4, x1, z1, u1, u2, v1, v2);
	}
	
	private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder buffer, float r, float g, float b, float a, float yMin, float yMax, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2)
	{
		addVertex(matrixPos, matrixNormal, buffer, r, g, b, a, yMax, x1, z1, u2, v1);
		addVertex(matrixPos, matrixNormal, buffer, r, g, b, a, yMin, x1, z1, u2, v2);
		addVertex(matrixPos, matrixNormal, buffer, r, g, b, a, yMin, x2, z2, u1, v2);
		addVertex(matrixPos, matrixNormal, buffer, r, g, b, a, yMax, x2, z2, u1, v1);
	}
	
	private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red, float green, float blue, float alpha, float y, float x, float z, float u, float v)
	{
		bufferIn.pos(matrixPos, x, y, z).color(red, green, blue, alpha).tex(u, v).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880).normal(matrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
	}
	
	@Override
	public boolean isGlobalRenderer(StarBeaconTileEntity te)
	{
		return true;
	}
	
}
