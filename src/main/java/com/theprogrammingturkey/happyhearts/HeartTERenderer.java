package com.theprogrammingturkey.happyhearts;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;

import java.util.Map;

public class HeartTERenderer extends TileEntityRenderer<HeartTE>
{
	@Override
	public void render(HeartTE tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if(!tile.getNameRaw().isEmpty())
			this.drawNameplate(tile, tile.getDisplayName().getFormattedText(), x, y - 0.1, z, 12);

		Direction facing = tile.getBlockState().get(HeartBlock.FACING);

		GlStateManager.pushMatrix();
		double d0 = (double) tile.getPos().getX() - tile.getPos().getX() + x;
		double d1 = (double) tile.getPos().getY() - tile.getPos().getY() + y;
		double d2 = (double) tile.getPos().getZ() - tile.getPos().getZ() + z;
		GlStateManager.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
		GlStateManager.rotatef(getRotAdj(facing), 0.0F, 1.0F, 0.0F);
		GlStateManager.enableLighting();

		Map<String, CustomDecosRegistry.CustomDeco> decos = CustomDecosRegistry.getDecosForUserName(tile.getNameRaw());

		GlStateManager.pushMatrix();
		GlStateManager.rotatef(90, 0.0F, 1.0F, 0.0F);

		//Right "Hand"
		if(decos.containsKey("right_hand"))
		{
			CustomDecosRegistry.CustomDeco rh = decos.get("right_hand");
			GlStateManager.pushMatrix();
			GlStateManager.translatef(-0.1f + rh.translation[0], 0.1f + rh.translation[1], -0.51f + rh.translation[2]);
			GlStateManager.rotatef(rh.rotation[0], 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(rh.rotation[1], 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(rh.rotation[2], 0.0F, 0.0F, 1.0F);
			GlStateManager.scalef(rh.scale[0], rh.scale[1], rh.scale[2]);
			Minecraft.getInstance().getItemRenderer().renderItem(rh.stack, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.popMatrix();
		}

		//Left "Hand"
		if(decos.containsKey("left_hand"))
		{
			CustomDecosRegistry.CustomDeco lh = decos.get("right_hand");
			GlStateManager.pushMatrix();
			GlStateManager.translatef(-0.1f + lh.translation[0], 0.1f + lh.translation[1], 0.51f + lh.translation[2]);
			GlStateManager.rotatef(lh.rotation[0], 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(lh.rotation[1], 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(lh.rotation[2], 0.0F, 0.0F, 1.0F);
			GlStateManager.scalef(lh.scale[0], lh.scale[1], lh.scale[2]);
			Minecraft.getInstance().getItemRenderer().renderItem(lh.stack, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.popMatrix();
		}

		//Hat
		if(decos.containsKey("hat"))
		{
			CustomDecosRegistry.CustomDeco hat = decos.get("hat");
			GlStateManager.pushMatrix();
			GlStateManager.translatef(hat.translation[0], 0.43f + hat.translation[1], hat.translation[2]);
			GlStateManager.rotatef(hat.rotation[0], 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(hat.rotation[1], 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(hat.rotation[2], 0.0F, 0.0F, 1.0F);
			GlStateManager.scalef(hat.scale[0], hat.scale[1], hat.scale[2]);
			Minecraft.getInstance().getItemRenderer().renderItem(hat.stack, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.popMatrix();
		}

		//Front
		if(decos.containsKey("front"))
		{
			CustomDecosRegistry.CustomDeco front = decos.get("front");
			GlStateManager.pushMatrix();
			GlStateManager.translatef(-0.33f + front.translation[0], -0.1f + front.translation[1], front.translation[2]);
			GlStateManager.rotatef(front.rotation[0], 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(front.rotation[1], 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(front.rotation[2], 0.0F, 0.0F, 1.0F);
			GlStateManager.scalef(front.scale[0], front.scale[1], front.scale[2]);
			Minecraft.getInstance().getItemRenderer().renderItem(front.stack, ItemCameraTransforms.TransformType.FIXED);
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
