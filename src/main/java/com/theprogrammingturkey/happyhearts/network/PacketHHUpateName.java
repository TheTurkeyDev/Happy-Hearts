package com.theprogrammingturkey.happyhearts.network;

import com.theprogrammingturkey.happyhearts.block.HeartBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHHUpateName
{
	private CompoundNBT nbt;
	private BlockPos pos;

	public PacketHHUpateName(BlockPos pos, CompoundNBT nbt)
	{
		this.pos = pos;
		this.nbt = nbt;
	}

	public static void encode(PacketHHUpateName msg, PacketBuffer buf)
	{
		buf.writeBlockPos(msg.pos);
		buf.writeCompoundTag(msg.nbt);
	}

	public static PacketHHUpateName decode(PacketBuffer buf)
	{
		return new PacketHHUpateName(buf.readBlockPos(), buf.readCompoundTag());
	}

	public static void handle(PacketHHUpateName msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerWorld world = ctx.get().getSender().getServerWorld();
			TileEntity te = world.getTileEntity(msg.pos);
			if(te != null)
			{
				te.read(msg.nbt);
				BlockState current = world.getBlockState(te.getPos());
				world.setBlockState(te.getPos(), current.with(HeartBlock.TOGGLE, !current.get(HeartBlock.TOGGLE)));
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
