package com.theprogrammingturkey.happyhearts.client;

import com.theprogrammingturkey.happyhearts.HappyHeartsCore;
import com.theprogrammingturkey.happyhearts.block.HeartTE;
import com.theprogrammingturkey.happyhearts.network.PacketHHUpateName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

public class HeartInputGui extends Screen
{
	private static final ResourceLocation guiTextures = new ResourceLocation(HappyHeartsCore.MODID + ":textures/gui/gui_name_input.png");
	private static final int IMAGE_WIDTH = 176;
	private static final int IMAGE_HEIGHT = 54;

	private TextFieldWidget rewardField;
	private HeartTE heartTE;

	public HeartInputGui(HeartTE heartTE)
	{
		super(new StringTextComponent("Happy Heart"));
		this.heartTE = heartTE;
	}

	@Override
	public void init()
	{
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
		int i = (this.width - IMAGE_WIDTH) / 2;
		int j = (this.height - IMAGE_HEIGHT) / 2;
		this.rewardField = new TextFieldWidget(this.font, i + 17, j + 10, 143, 12, "Test");
		this.rewardField.setTextColor(-1);
		this.rewardField.setDisabledTextColour(-1);
		this.rewardField.setEnableBackgroundDrawing(true);
		this.rewardField.setMaxStringLength(100);
		this.rewardField.setText(heartTE.getNameRaw());

		this.children.add(this.rewardField);
	}

	@Override
	public void onClose()
	{
		this.heartTE.setName(this.rewardField.getText());
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
	}
}
