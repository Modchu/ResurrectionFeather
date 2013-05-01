package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

public class mod_ResurrectionFeather extends BaseMod {

	@MLProp(min = 256.0D, max = 32000.0D)
	public static int resurrectionFeatherID = 17650;
	@MLProp(min = 0, max = 2147483647)
	public static int onDeathTimeDrop = 20;
	@MLProp(min = 0, max = 2147483647)
	public static int onDeathTimeDespawn = 300;
	public static Item resurrectionFeather;
	private static boolean DebugMessage = true;
	private static mod_ResurrectionFeather mod_resurrectionFeather;
	private static final File cfgdir = new File(Minecraft.getMinecraftDir(), "/config/");
	private static File whiteListfile = new File(cfgdir, ("ResurrectionFeather_whiteList.cfg"));
	private static File ngListfile = new File(cfgdir, ("ResurrectionFeather_NGList.cfg"));
	public static List<String> whiteList = new ArrayList<String>();
	public static List<String> ngList = new ArrayList<String>();

	public static void Debug(String s)
	{
		if (DebugMessage)
		{
			System.out.println((new StringBuilder()).append("resurrectionFeather-").append(s).toString());
		}
	}

	public static void mDebug(String s)
	{
		if (DebugMessage
				&& !mod_resurrectionFeather.isRelease())
		{
			System.out.println((new StringBuilder()).append("resurrectionFeather_").append(s).toString());
		}
	}

	public boolean isRelease() {
		return getClass().getPackage() == null;
	}

	public String getVersion() {
		return "1.2.4~5-3";
	}

	public void load() {
		mod_resurrectionFeather = this;
		resurrectionFeather = new ItemResurrectionFeather(
				resurrectionFeatherID - 256).setIconCoord(8, 1).setItemName(
				"ResurrectionFeather");
		ModLoader.addName(resurrectionFeather, "resurrectionFeather");
		ModLoader.addRecipe(new ItemStack(resurrectionFeather, 1),
				new Object[] { " Y ", "YXY", " Y ", Character.valueOf('X'),
						Item.feather, Character.valueOf('Y'), Item.ingotGold });
		loadList();
	}

	public static void writerList(String[] s, File file, List<String> list) {
		//Listファイル書き込み
		try {
			BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < s.length ; i++)
			{
				//mDebug("s[i]="+s[i]);
				if (s[i] != null) {
					bwriter.write(s[i]);
					list.add(s[i]);
					bwriter.newLine();
				}
			}
			bwriter.close();
			Debug("file new file create.");
		} catch (Exception e) {
			Debug("file writer fail.");
			e.printStackTrace();
			Debug(" ");
		}
	}

	public static void loadList() {
		// List読み込み
		if (cfgdir.exists()) {
			if (!whiteListfile.exists()) {
				// whiteListファイルが無い = 新規作成
				String s[] = {
						"EntityCreature", "aaa", "ozelot", "wolf", "cow",
						"chicken", "pig", "sheep", "golem", "squid",
						"villager"
				};
				writerList(s, whiteListfile, whiteList);
			} else {
				// whiteListファイルがある
				try {
					BufferedReader breader = new BufferedReader(new FileReader(
							whiteListfile));
					String rl;
					int i = 0;
					while ((rl = breader.readLine()) != null) {
						whiteList.add(rl);
						i++;
					}
					breader.close();
					mDebug("whiteList load end.");
				} catch (Exception e) {
					Debug("whiteList file load fail.");
		            e.printStackTrace();
					Debug(" ");
				}
			}
			if (!ngListfile.exists()) {
				// NGListファイルが無い = 新規作成
				String s[] = {
						"EntityMob", "yy"
				};
				writerList(s, ngListfile, ngList);
			} else {
				// NGListファイルがある
				try {
					BufferedReader breader = new BufferedReader(new FileReader(
							ngListfile));
					String rl;
					int i = 0;
					while ((rl = breader.readLine()) != null) {
						ngList.add(rl);
						i++;
					}
					breader.close();
					mDebug("NGList load end.");
				} catch (Exception e) {
					Debug("NGList file load fail.");
		            e.printStackTrace();
					Debug(" ");
				}
			}
		}
	}

}