package fr.max2.mffjametoilee.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.max2.mffjametoilee.init.ModItems;
import fr.max2.mffjametoilee.tileentity.StabilizedStarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class StabilizedStarRenderer extends TileEntityRenderer<StabilizedStarTileEntity>
{
	private final ItemRenderer itemRenderer;

	public StabilizedStarRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
	}

	@Override
	public void render(StabilizedStarTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
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
	}
	
}
