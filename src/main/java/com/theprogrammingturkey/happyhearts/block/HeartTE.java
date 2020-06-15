package com.theprogrammingturkey.happyhearts.block;

import com.theprogrammingturkey.happyhearts.HappyHeartsCore;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public class HeartTE extends TileEntity implements INameable
{
	private String username = "";
	private TextFormatting color = TextFormatting.WHITE;
	private int blockColor = 0xC73C9D;

	public HeartTE()
	{
		super(HappyHeartsCore.HEART_TE);
	}


	@Override
	public ITextComponent getName()
	{
		return new StringTextComponent(username).setStyle(new Style().setColor(color));
	}

	public String getNameRaw()
	{
		return username;
	}

	public void setName(String name)
	{
		this.username = name;
	}

	public int getHeartColor()
	{
		return blockColor;
	}

	public void setHeartColor(int color)
	{
		this.blockColor = color;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putString("name", this.username);
		compound.putInt("color", this.blockColor);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.username = compound.getString("name");
		this.blockColor = compound.getInt("color");
	}

	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		this.read(tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(pkt.getNbtCompound());
	}
}