package com.theprogrammingturkey.happyhearts.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.theprogrammingturkey.happyhearts.HappyHeartsCore;
import com.theprogrammingturkey.happyhearts.block.HeartTE;
import com.theprogrammingturkey.happyhearts.network.PacketHHUpateName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.opengl.GL11;

public class HeartInputGui extends Screen
{
	private static final ResourceLocation guiTextures = new ResourceLocation(HappyHeartsCore.MODID + ":textures/gui/gui_name_input.png");
	private static final int IMAGE_WIDTH = 176;
	private static final int IMAGE_HEIGHT = 159;

	private TextFieldWidget rewardField;
	private HeartTE heartTE;

	private GuiSlider redSlider;
	private GuiSlider greenSlider;
	private GuiSlider blueSlider;

	private int left;
	private int top;

	public HeartInputGui(HeartTE heartTE)
	{
		super(new StringTextComponent("Happy Heart"));
		this.heartTE = heartTE;
	}

	@Override
	public void init()
	{
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
		left = (this.width - IMAGE_WIDTH) / 2;
		top = (this.height - IMAGE_HEIGHT) / 2;
		this.rewardField = new TextFieldWidget(this.font, left + 17, top + 10, 143, 12, "Test");
		this.rewardField.setTextColor(-1);
		this.rewardField.setDisabledTextColour(-1);
		this.rewardField.setEnableBackgroundDrawing(true);
		this.rewardField.setMaxStringLength(100);
		this.rewardField.setText(heartTE.getNameRaw());

		this.children.add(this.rewardField);

		int color = heartTE.getHeartColor();
		this.children.add(redSlider = new GuiSlider(left + 17, top + 35, 100, 25, "Red ", "", 0, 255, (color >> 16) & 0x000000FF, false, true, p_onPress_1_ ->
		{
		}));
		this.children.add(greenSlider = new GuiSlider(left + 17, top + 70, 100, 25, "Green ", "", 0, 255, (color >> 8) & 0x000000FF, false, true, p_onPress_1_ ->
		{
		}));
		this.children.add(blueSlider = new GuiSlider(left + 17, top + 105, 100, 25, "Blue ", "", 0, 255, (color) & 0x000000FF, false, true, p_onPress_1_ ->
		{
		}));
	}

	public int getIntColor()
	{
		return (0xFF << 24) | ((redSlider.getValueInt() & 0xFF) << 16) | ((greenSlider.getValueInt() & 0xFF) << 8) | (blueSlider.getValueInt() & 0xFF);
	}

	@Override
	public void onClose()
	{
		this.heartTE.setName(this.rewardField.getText());
		this.heartTE.setHeartColor(this.getIntColor());
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
		HappyHeartsCore.CHANNEL.sendToServer(new PacketHHUpateName(heartTE.getPos(), heartTE.write(new CompoundNBT())));
		super.onClose();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		this.minecraft.getTextureManager().bindTexture(guiTextures);
		this.blit((this.width - IMAGE_WIDTH) / 2, (this.height - IMAGE_HEIGHT) / 2, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		super.render(mouseX, mouseY, partialTicks);
		this.rewardField.render(mouseX, mouseY, partialTicks);
		this.redSlider.render(mouseX, mouseY, partialTicks);
		this.greenSlider.render(mouseX, mouseY, partialTicks);
		this.blueSlider.render(mouseX, mouseY, partialTicks);
		int gradientX = left + 135;
		int gradientY = top + 40;
		//drawGradientRect(gradientX - 5, gradientY - 5, gradientX + 25, gradientY + 90, -1, -1);
		int color = getIntColor();
		drawGradientRect(gradientX, gradientY, gradientX + 32, gradientY + 80, color, color);
	}

	public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
	{
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture();
		GlStateManager.enableBlend();
		GlStateManager.disableAlphaTest();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(right, top, 0d).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos(left, top, 0d).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos(left, bottom, 0d).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos(right, bottom, 0d).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlphaTest();
		GlStateManager.enableTexture();
	}
}
