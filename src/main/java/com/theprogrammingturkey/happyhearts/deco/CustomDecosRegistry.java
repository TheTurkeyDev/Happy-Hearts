package com.theprogrammingturkey.happyhearts.deco;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.theprogrammingturkey.happyhearts.HappyHeartsCore;
import com.theprogrammingturkey.happyhearts.client.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.ModList;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CustomDecosRegistry
{
	private static Map<String, PlayerDeco> userDecos = new HashMap<>();

	public static void init()
	{
		userDecos.clear();

		if(!Config.customDecos.get())
			return;

		JsonArray json = getCustomDecos();
		for(JsonElement elem : json)
		{
			PlayerDeco deco = new PlayerDeco();
			JsonObject decoJson = elem.getAsJsonObject();

			String username = decoJson.get("username").getAsString().toLowerCase();
			if(decoJson.has("name_color"))
			{
				TextFormatting format = TextFormatting.fromFormattingCode(decoJson.get("name_color").getAsString().charAt(0));
				if(format != null)
					deco.nameStyle.setColor(format);
			}

			if(decoJson.has("items"))
			{
				for(JsonElement slot : decoJson.get("items").getAsJsonArray())
				{
					try
					{
						JsonObject slotJson = slot.getAsJsonObject();
						CustomDeco dispSlot = new CustomDeco();
						dispSlot.stack = ItemStack.read(JsonToNBT.getTagFromJson(slotJson.getAsJsonObject("item").toString()));
						dispSlot.stack.setCount(1);
						dispSlot.translation = jsonObjectToFloats(slotJson.getAsJsonObject("translation"));
						dispSlot.rotation = jsonObjectToFloats(slotJson.getAsJsonObject("rotation"));
						dispSlot.scale = jsonObjectToFloats(slotJson.getAsJsonObject("scale"));

						deco.decos.add(dispSlot);
					} catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}

			if(userDecos.containsKey(username))
			{
				userDecos.get(username).nameStyle = deco.nameStyle;
				userDecos.get(username).decos.addAll(deco.decos);
			}
			else
			{
				userDecos.put(username, deco);
			}
		}
	}

	public static PlayerDeco getDecosForUserName(String username)
	{
		return userDecos.getOrDefault(username.toLowerCase(), new PlayerDeco());
	}

	private static float[] jsonObjectToFloats(JsonObject json)
	{
		float[] floats = new float[3];
		if(json != null)
		{
			floats[0] = json.get("x").getAsFloat();
			floats[1] = json.get("y").getAsFloat();
			floats[2] = json.get("z").getAsFloat();
		}
		return floats;
	}

	public static JsonArray getCustomDecos()
	{
		try
		{
			HttpURLConnection con = (HttpURLConnection) new URL("http://api.theprogrammingturkey.com/happy_hearts/HappyHeartsAPI.php").openConnection();
			con.setDoInput(true);
			con.setReadTimeout(5000);
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");
			con.setRequestMethod("POST");

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes("version=" + ModList.get().getModContainerById(HappyHeartsCore.MODID).get().getModInfo().getVersion().toString());
			wr.flush();
			wr.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

			StringBuilder buffer = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null)
				buffer.append(line);

			con.disconnect();

			String page = buffer.toString();

			return (new JsonParser()).parse(page).getAsJsonArray();
		} catch(Exception e)
		{
			return new JsonArray();
		}
	}
}
