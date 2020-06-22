package com.theprogrammingturkey.happyhearts.client;

import com.theprogrammingturkey.happyhearts.HappyHeartsCore;
import com.theprogrammingturkey.happyhearts.block.HeartTE;
import com.theprogrammingturkey.happyhearts.deco.CustomDecosRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.concurrent.CompletableFuture;

public class ClientProxy
{
	public ClientProxy()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientStart);

		IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
		if(resourceManager instanceof IReloadableResourceManager)
			((IReloadableResourceManager) resourceManager).addReloadListener((stage, resourceManager1, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> CompletableFuture.runAsync(CustomDecosRegistry::init, backgroundExecutor).thenCompose(stage::markCompleteAwaitingOthers));

		CustomDecosRegistry.init();
	}

	@SubscribeEvent
	public void clientStart(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(HappyHeartsCore.HEART_TE, HeartTERenderer::new);
		RenderTypeLookup.setRenderLayer(HappyHeartsCore.HEART, RenderType.getCutoutMipped());
	}

	public static void openNameInputGUI(HeartTE te)
	{
		Minecraft.getInstance().displayGuiScreen(new HeartInputGui(te));
	}
}
