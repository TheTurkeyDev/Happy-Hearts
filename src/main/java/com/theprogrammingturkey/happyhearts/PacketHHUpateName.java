package com.theprogrammingturkey.happyhearts;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
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
				ctx.get().getSender().getServerWorld().getTileEntity(msg.pos).read(msg.nbt));
		ctx.get().setPacketHandled(true);
	}
}
