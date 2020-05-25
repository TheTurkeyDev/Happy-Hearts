package com.theprogrammingturkey.happyhearts.client;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config
{

	public static final ForgeConfigSpec configSpec;
	public static final Config CONFIG;

	public static ForgeConfigSpec.BooleanValue customDecos;

	static
	{
		final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
		configSpec = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	public Config(ForgeConfigSpec.Builder builder)
	{
		customDecos = builder
				.comment("By default this mod fetches custom decorations from an outside url, so that decorations can be changed and added without a mod update. To aid in development efforts the mod version you are using is logged by the server, but nothing else. Set this to false to disable this.")
				.define("custom_decos", true);
	}
}
