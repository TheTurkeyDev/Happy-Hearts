package com.theprogrammingturkey.happyhearts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
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
	private static Map<String, Map<String, CustomDeco>> userDecos = new HashMap<>();

	private static final String[] displaySlots = {"right_hand", "left_hand", "hat", "front"};

	public static void init()
	{
		JsonArray json = getCustomDecos();
		for(JsonElement elem : json)
		{
			Map<String, CustomDeco> decos = new HashMap<>();
			JsonObject decoJson = elem.getAsJsonObject();

			String username = decoJson.get("username").getAsString();

			for(String slot : displaySlots)
			{
				try
				{
					if(decoJson.has(slot))
					{
						JsonObject slotJson = decoJson.getAsJsonObject(slot);
						CustomDeco dispSlot = new CustomDeco();
						dispSlot.stack = ItemStack.read(JsonToNBT.getTagFromJson(slotJson.getAsJsonObject("item").toString()));
						dispSlot.translation = jsonObjectToFloats(slotJson.getAsJsonObject("translation"));
						dispSlot.rotation = jsonObjectToFloats(slotJson.getAsJsonObject("rotation"));
						dispSlot.scale = jsonObjectToFloats(slotJson.getAsJsonObject("scale"));

						decos.put(slot, dispSlot);
					}
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}


			userDecos.put(username.toLowerCase(), decos);
		}
	}

	public static Map<String, CustomDeco> getDecosForUserName(String username)
	{
		return userDecos.getOrDefault(username.toLowerCase(), new HashMap<>());
	}

	private static float[] jsonObjectToFloats(JsonObject json)
	{
		float[] floats = new float[3];
		floats[0] = json.get("x").getAsFloat();
		floats[1] = json.get("y").getAsFloat();
		floats[2] = json.get("z").getAsFloat();
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

	public static class CustomDeco
	{
		public ItemStack stack = ItemStack.EMPTY;
		public float[] translation = new float[3];
		public float[] rotation = new float[3];
		public float[] scale = new float[3];
	}
}
