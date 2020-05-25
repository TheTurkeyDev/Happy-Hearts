package com.theprogrammingturkey.happyhearts.deco;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeco
{
	public Style nameStyle = new Style().setColor(TextFormatting.WHITE);
	public List<CustomDeco> decos = new ArrayList<>();
}
