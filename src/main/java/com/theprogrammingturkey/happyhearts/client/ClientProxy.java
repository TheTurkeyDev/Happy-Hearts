package com.theprogrammingturkey.happyhearts.client;

import com.theprogrammingturkey.happyhearts.block.HeartTE;
import com.theprogrammingturkey.happyhearts.deco.CustomDecosRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.concurrent.CompletableFuture;

public class ClientProxy
{
	// This is needed to be moved here due to bytecode fun. CPW would approve...
	public static void initClientStuff()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(HeartTE.class, new HeartTERenderer());
		IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
		if(resourceManager instanceof IReloadableResourceManager)
			((IReloadableResourceManager) resourceManager).addReloadListener((stage, resourceManager1, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> CompletableFuture.runAsync(CustomDecosRegistry::init, backgroundExecutor).thenCompose(stage::markCompleteAwaitingOthers));

		CustomDecosRegistry.init();
	}

	public static void openNameInputGUI(HeartTE te)
	{
		Minecraft.getInstance().displayGuiScreen(new HeartInputGui(te));
	}
}
