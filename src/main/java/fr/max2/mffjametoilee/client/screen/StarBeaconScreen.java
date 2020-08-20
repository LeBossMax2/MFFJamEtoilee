package fr.max2.mffjametoilee.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import fr.max2.mffjametoilee.MFFJamEtoileeMod;
import fr.max2.mffjametoilee.tileentity.StarBeaconContainer;
import fr.max2.mffjametoilee.tileentity.StarBeaconTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraft.network.play.client.CUpdateBeaconPacket;
import net.minecraft.potion.Effect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;

@OnlyIn(Dist.CLIENT)
public class StarBeaconScreen extends ContainerScreen<StarBeaconContainer>
{
	
	private static final ResourceLocation BEACON_GUI_TEXTURES = new ResourceLocation(MFFJamEtoileeMod.MOD_ID, "textures/gui/container/star_beacon.png");
	private ConfirmButton confirmButton;
	private final List<PowerButton> powerButtons = new ArrayList<>(); 
	private boolean refreshButtons;
	private Effect mainEffect;
	
	public StarBeaconScreen(final StarBeaconContainer container, PlayerInventory playerInv, ITextComponent title)
	{
		super(container, playerInv, title);
		this.xSize = 230; //176;
		this.ySize = 219;
		container.addListener(new IContainerListener()
		{
			/**
			 * update the crafting window inventory with the items in the list
			 */
			@Override
			public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
			{}
			
			/**
			 * Sends the contents of an inventory slot to the client-side
			 * Container. This doesn't have to match the actual contents of that
			 * slot.
			 */
			@Override
			public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
			{}
			
			/**
			 * Sends two ints to the client-side Container. Used for furnace
			 * burning time, smelting progress, brewing progress, and enchanting
			 * level. Normally the first int identifies which variable to
			 * update, and the second contains the new value. Both are truncated
			 * to shorts in non-local SMP.
			 */
			@Override
			public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue)
			{
				StarBeaconScreen.this.mainEffect = container.func_216967_f();
				StarBeaconScreen.this.refreshButtons = true;
			}
		});
	}
	
	@Override
	protected void init()
	{
		super.init();
		this.confirmButton = this.addButton(new ConfirmButton(this.guiLeft + 157, this.guiTop + 109));
		this.addButton(new CancelButton(this.guiLeft + 179, this.guiTop + 109));
		this.refreshButtons = true;
		this.confirmButton.active = false;
		
		for (int row = 0; row <= 2; ++row)
		{
			int effectsCount = StarBeaconTileEntity.EFFECTS_LIST[row].length;
			int rowWidth = effectsCount * 22 + (effectsCount - 1) * 2;
			
			for (int col = 0; col < effectsCount; ++col)
			{
				Effect effect = StarBeaconTileEntity.EFFECTS_LIST[row][col];
				PowerButton button = new PowerButton(this.guiLeft + 101 + col * 24 - rowWidth / 2 + 28, this.guiTop + 22 + row * 25, row + 1, effect);
				this.powerButtons.add(button);
				this.addButton(button);
			}
		}
	}
	
	@Override
	public void tick()
	{
		super.tick();
		int beaconLevel = this.container.func_216969_e();
		if (this.refreshButtons && beaconLevel >= 0)
		{
			this.refreshButtons = false;
			
			for (PowerButton button : this.powerButtons)
			{
				if (button.requiredLevel > beaconLevel)
				{
					button.active = false;
				}
				else
				{
					button.active = true;
					if (button.effect == this.mainEffect)
					{
						button.setSelected(true);
					}
				}
			}
		}
		
		this.confirmButton.active = this.container.func_216970_h() && this.mainEffect != null;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.drawCenteredString(this.font, I18n.format("block.minecraft.beacon.primary"), 87 + 28, 10, 14737632);
		
		for (Widget widget : this.buttons)
		{
			if (widget.isHovered())
			{
				widget.renderToolTip(mouseX - this.guiLeft, mouseY - this.guiTop);
				break;
			}
		}
		
	}
	
	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(BEACON_GUI_TEXTURES);
		this.blit((this.width - 176) / 2 + 1, this.guiTop, 0, 0, 176, this.ySize);
		this.itemRenderer.zLevel = 100.0F;
		this.itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(Items.EMERALD), this.guiLeft + 42, this.guiTop + 109);
		this.itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(Items.DIAMOND), this.guiLeft + 42 + 22, this.guiTop + 109);
		this.itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(Items.GOLD_INGOT), this.guiLeft + 42 + 44, this.guiTop + 109);
		this.itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(Items.IRON_INGOT), this.guiLeft + 42 + 66, this.guiTop + 109);
		this.itemRenderer.zLevel = 0.0F;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@OnlyIn(Dist.CLIENT)
	abstract static class Button extends AbstractButton
	{
		private boolean selected;
		
		protected Button(int x, int y, int size)
		{
			super(x, y, size, size, "");
		}
		
		@Override
		public void renderButton(int mouseX, int mouseY, float partialTicks)
		{
			int j = 0;
			if (!this.active)
			{
				j += 22 * 2;
			}
			else if (this.selected)
			{
				j += 22 * 1;
			}
			else if (this.isHovered())
			{
				j += 22 * 3;
			}
			
			GuiUtils.drawContinuousTexturedBox(BEACON_GUI_TEXTURES, this.x, this.y, j, 219, this.width, this.height, 22, 22, 2, this.getBlitOffset());
			this.renderForeground();
		}
		
		protected abstract void renderForeground();
		
		public boolean isSelected()
		{
			return this.selected;
		}
		
		public void setSelected(boolean selectedIn)
		{
			this.selected = selectedIn;
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	class CancelButton extends SpriteButton
	{
		public CancelButton(int x, int y)
		{
			super(x, y, 112, 220);
		}
		
		@Override
		public void onPress()
		{
			StarBeaconScreen.this.minecraft.player.connection
				.sendPacket(new CCloseWindowPacket(StarBeaconScreen.this.minecraft.player.openContainer.windowId));
			StarBeaconScreen.this.minecraft.displayGuiScreen((Screen) null);
		}
		
		@Override
		public void renderToolTip(int mouseX, int mouseY)
		{
			StarBeaconScreen.this.renderTooltip(I18n.format("gui.cancel"), mouseX, mouseY);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	class ConfirmButton extends SpriteButton
	{
		public ConfirmButton(int x, int y)
		{
			super(x, y, 90, 220);
		}
		
		@Override
		public void onPress()
		{
			StarBeaconScreen.this.minecraft.getConnection().sendPacket(
				new CUpdateBeaconPacket(Effect.getId(StarBeaconScreen.this.mainEffect), Effect.getId(null)));
			StarBeaconScreen.this.minecraft.getConnection()
				.sendPacket(new CCloseWindowPacket(StarBeaconScreen.this.minecraft.player.openContainer.windowId));
			StarBeaconScreen.this.minecraft.displayGuiScreen(null);
		}
		
		@Override
		public void renderToolTip(int mouseX, int mouseY)
		{
			StarBeaconScreen.this.renderTooltip(I18n.format("gui.done"), mouseX, mouseY);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	class PowerButton extends Button
	{
		private final int requiredLevel;
		private final Effect effect;
		private final TextureAtlasSprite sprite;
		
		public PowerButton(int x, int y, int requiredLevel, Effect effect)
		{
			super(x, y, 22);
			this.requiredLevel = requiredLevel;
			this.effect = effect;
			this.sprite = Minecraft.getInstance().getPotionSpriteUploader().getSprite(effect);
		}
		
		@Override
		public void onPress()
		{
			if (!this.isSelected())
			{
				StarBeaconScreen.this.mainEffect = this.effect;
				StarBeaconScreen.this.buttons.clear();
				StarBeaconScreen.this.children.clear();
				StarBeaconScreen.this.init();
				StarBeaconScreen.this.tick();
			}
		}
		
		@Override
		public void renderToolTip(int mouseX, int mouseY)
		{
			String tooltip = I18n.format(this.effect.getName());
			
			StarBeaconScreen.this.renderTooltip(tooltip, mouseX, mouseY);
		}
		
		@Override
		protected void renderForeground()
		{
			Minecraft.getInstance().getTextureManager().bindTexture(this.sprite.getAtlasTexture().getTextureLocation());
			blit(this.x + 2, this.y + 2, this.getBlitOffset(), 18, 18, this.sprite);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	abstract static class SpriteButton extends Button
	{
		private final int spriteU;
		private final int spriteV;
		
		protected SpriteButton(int x, int y, int u, int v)
		{
			super(x, y, 18);
			this.spriteU = u;
			this.spriteV = v;
		}
		
		@Override
		protected void renderForeground()
		{
			this.blit(this.x, this.y, this.spriteU, this.spriteV, 18, 18);
		}
	}
}
