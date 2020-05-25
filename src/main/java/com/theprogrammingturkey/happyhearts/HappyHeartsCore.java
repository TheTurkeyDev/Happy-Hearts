package com.theprogrammingturkey.happyhearts;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(HappyHeartsCore.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HappyHeartsCore
{
	public static final String MODID = "happyhearts";

	public static HeartBlock HEART;
	public static TileEntityType<?> HEART_TE;

	private static int packetID = 0;
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(HappyHeartsCore.MODID, "packets")).clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true).networkProtocolVersion(() -> FMLNetworkConstants.NETVERSION).simpleChannel();


	public HappyHeartsCore()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientStart);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonStart);

		CustomDecosRegistry.init();
	}

	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(HEART = new HeartBlock());
	}

	@SubscribeEvent
	public static void onTileEntityRegistry(RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().register(HEART_TE = TileEntityType.Builder.create(HeartTE::new, HEART).build(null).setRegistryName(MODID, "tile_heart"));
	}

	@SubscribeEvent
	public static void onItemRegistry(RegistryEvent.Register<Item> e)
	{
		Item theItemBlock = new BlockItem(HEART, (new Item.Properties()).group(ItemGroup.DECORATIONS));
		theItemBlock.setRegistryName(HEART.getRegistryName());
		e.getRegistry().register(theItemBlock);
	}

	@SubscribeEvent
	public void onClientStart(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(HeartTE.class, new HeartTERenderer());
	}

	@SubscribeEvent
	public void onCommonStart(FMLCommonSetupEvent event)
	{
		CHANNEL.registerMessage(packetID++, PacketHHUpateName.class, PacketHHUpateName::encode, PacketHHUpateName::decode, PacketHHUpateName::handle);
	}
}
