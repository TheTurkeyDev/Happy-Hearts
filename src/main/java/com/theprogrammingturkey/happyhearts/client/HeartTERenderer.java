package com.theprogrammingturkey.happyhearts.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.theprogrammingturkey.happyhearts.block.HeartBlock;
import com.theprogrammingturkey.happyhearts.block.HeartTE;
import com.theprogrammingturkey.happyhearts.deco.CustomDeco;
import com.theprogrammingturkey.happyhearts.deco.CustomDecosRegistry;
import com.theprogrammingturkey.happyhearts.deco.PlayerDeco;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;

public class HeartTERenderer extends TileEntityRenderer<HeartTE>
{
	@Override
	public void render(HeartTE tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		PlayerDeco playerDeco = CustomDecosRegistry.getDecosForUserName(tile.getNameRaw());

		if(!tile.getNameRaw().isEmpty())
			this.drawNameplate(tile, tile.getDisplayName().setStyle(playerDeco.nameStyle).getFormattedText(), x, y - 0.1, z, 12);

		Direction facing = tile.getBlockState().get(HeartBlock.FACING);

		GlStateManager.pushMatrix();
		double d0 = (double) tile.getPos().getX() - tile.getPos().getX() + x;
		double d1 = (double) tile.getPos().getY() - tile.getPos().getY() + y;
		double d2 = (double) tile.getPos().getZ() - tile.getPos().getZ() + z;
		GlStateManager.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
		GlStateManager.rotatef(getRotAdj(facing), 0.0F, 1.0F, 0.0F);
		GlStateManager.enableLighting();

		GlStateManager.pushMatrix();
		GlStateManager.rotatef(90, 0.0F, 1.0F, 0.0F);

		for(CustomDeco deco : playerDeco.decos)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translatef(deco.translation[0], deco.translation[1], deco.translation[2]);
			GlStateManager.rotatef(deco.rotation[0], 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(deco.rotation[1], 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(deco.rotation[2], 0.0F, 0.0F, 1.0F);
			GlStateManager.scalef(deco.scale[0], deco.scale[1], deco.scale[2]);
			Minecraft.getInstance().getItemRenderer().renderItem(deco.stack, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	public float getRotAdj(Direction dir)
	{
		switch(dir)
		{
			case SOUTH:
				return 0;
			case WEST:
				return 270;
			case EAST:
				return 90;
			default:
				return 180;
		}
	}
}
