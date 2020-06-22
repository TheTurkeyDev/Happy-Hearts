package com.theprogrammingturkey.happyhearts.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.theprogrammingturkey.happyhearts.block.HeartBlock;
import com.theprogrammingturkey.happyhearts.block.HeartTE;
import com.theprogrammingturkey.happyhearts.deco.CustomDeco;
import com.theprogrammingturkey.happyhearts.deco.CustomDecosRegistry;
import com.theprogrammingturkey.happyhearts.deco.PlayerDeco;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;

public class HeartTERenderer extends TileEntityRenderer<HeartTE>
{
	public HeartTERenderer(TileEntityRendererDispatcher teRenderer)
	{
		super(teRenderer);
	}

	@Override
	public void render(HeartTE tile, float v, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		PlayerDeco playerDeco = CustomDecosRegistry.getDecosForUserName(tile.getNameRaw());

		if(!tile.getNameRaw().isEmpty())
			this.drawNameplate(tile, tile.getDisplayName().setStyle(playerDeco.nameStyle).getFormattedText(), matrixStack, bufferIn, combinedLightIn);

		// Heart Item rendering
		Direction facing = tile.getBlockState().get(HeartBlock.FACING);

		matrixStack.push();
		double d0 = (double) tile.getPos().getX() - tile.getPos().getX();
		double d1 = (double) tile.getPos().getY() - tile.getPos().getY();
		double d2 = (double) tile.getPos().getZ() - tile.getPos().getZ();
		matrixStack.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(getRotAdj(facing)));
		matrixStack.rotate(Vector3f.YP.rotationDegrees(90));

		for(CustomDeco deco : playerDeco.decos)
		{
			matrixStack.push();
			matrixStack.translate(deco.translation[0], deco.translation[1], deco.translation[2]);
			matrixStack.rotate(Vector3f.XP.rotationDegrees(deco.rotation[0]));
			matrixStack.rotate(Vector3f.YP.rotationDegrees(deco.rotation[1]));
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(deco.rotation[2]));
			matrixStack.scale(deco.scale[0], deco.scale[1], deco.scale[2]);
			Minecraft.getInstance().getItemRenderer().renderItem(deco.stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
			matrixStack.pop();
		}

		matrixStack.pop();
	}

	protected void drawNameplate(HeartTE te, String str, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		PlayerEntity player = Minecraft.getInstance().player;
		if(player == null)
			return;

		double d0 = te.getDistanceSq(player.getPosX(), player.getPosY(), player.getPosZ());
		if(!(d0 > 4096.0D))
		{
			matrixStackIn.push();
			matrixStackIn.translate(0.5D, 1.25F, 0.5D);
			matrixStackIn.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
			matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
			int j = (int) (f1 * 255.0F) << 24;
			FontRenderer fontrenderer = renderDispatcher.getFontRenderer();
			float f2 = (float) (-fontrenderer.getStringWidth(str) / 2);
			fontrenderer.renderString(str, f2, 0, 553648127, false, matrix4f, bufferIn, false, j, packedLightIn);
			fontrenderer.renderString(str, f2, 0, -1, false, matrix4f, bufferIn, false, 0, packedLightIn);

			matrixStackIn.pop();
		}
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
