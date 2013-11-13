package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class mod_ResurrectionFeather extends BaseMod {

	public static int resurrectionFeatherID = 17650;
	public static int onDeathTimeDrop = 20;
	public static int onDeathTimeDespawn = 300;
	public static boolean useOriginalIcon = true;
	public static boolean DebugMessage = false;

	public static Item resurrectionFeather;
	public static mod_ResurrectionFeather mod_resurrectionFeather;
	private static final File cfgdir = new File(Minecraft.getMinecraft().mcDataDir, "/config/");
	private static File whiteListfile = new File(cfgdir, ("ResurrectionFeather_whiteList.cfg"));
	private static File ngListfile = new File(cfgdir, ("ResurrectionFeather_NGList.cfg"));
	public static List<String> whiteList = new ArrayList<String>();
	public static List<String> ngList = new ArrayList<String>();
	private static File mainCfgfile = new File(cfgdir, ("ResurrectionFeather.cfg"));
	public static String itemName;
	private String packageName;
	public static boolean isForge;

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
		return getPackage() == null;
	}

	public String getVersion() {
		return "1.6.2-5";
	}

	public void load() {
		mod_resurrectionFeather = this;
		loadcfg();
		itemName = "ResurrectionFeather";
		resurrectionFeather = new ItemResurrectionFeather(
				resurrectionFeatherID - 256).setUnlocalizedName(
						itemName).setCreativeTab(CreativeTabs.tabMaterials);
		ModLoader.addName(resurrectionFeather, itemName);
		ModLoader.addRecipe(new ItemStack(resurrectionFeather, 1),
				new Object[] { " Y ", "YXY", " Y ", Character.valueOf('X'),
						Item.feather, Character.valueOf('Y'), Item.ingotGold });
		loadList();
	}

	public void modsLoaded()
	{
		EntityCreature.whiteList = whiteList;
		EntityCreature.ngList = ngList;
		EntityCreature.onDeathTimeDrop = onDeathTimeDrop;
		EntityCreature.onDeathTimeDespawn = onDeathTimeDespawn;
	}

	public static void writerList(String[] s, File file, List<String> list) {
		//List�t�@�C����������
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
		// List�ǂݍ���
		if (cfgdir.exists()) {
			if (!whiteListfile.exists()) {
				// whiteList�t�@�C�������� = �V�K�쐬
				String s[] = {
						"net.minecraft.entity.EntityCreature", "EntityCreature", "[om]", "Ozelot", "Wolf",
						"Cow", "Chicken", "Pig", "Sheep", "Golem",
						"Squid", "Villager"
				};
				writerList(s, whiteListfile, whiteList);
			} else {
				// whiteList�t�@�C��������
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
				// NGList�t�@�C�������� = �V�K�쐬
				String s[] = {
						"net.minecraft.entity.monster.EntityMob", "EntityMob", "[tl]"
				};
				writerList(s, ngListfile, ngList);
			} else {
				// NGList�t�@�C��������
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

	public static void loadcfg() {
		// cfg�ǂݍ���
		if (cfgdir.exists()) {
			if (!mainCfgfile.exists()) {
				// cfg�t�@�C�������� = �V�K�쐬
				String s[] = {
						"resurrectionFeatherID=17650",
						"onDeathTimeDrop=20",
						"onDeathTimeDespawn=300",
						"useOriginalIcon=true",
						"DebugMessage=false"
				};
				ResurrectionFeatherConfig.writerConfig(mainCfgfile, s);
			} else {
				// cfg�t�@�C��������
				resurrectionFeatherID = Integer.valueOf((ResurrectionFeatherConfig.loadConfig(mainCfgfile, "resurrectionFeatherID", resurrectionFeatherID)).toString());
				onDeathTimeDrop = Integer.valueOf((ResurrectionFeatherConfig.loadConfig(mainCfgfile, "onDeathTimeDrop", onDeathTimeDrop)).toString());
				onDeathTimeDespawn = Integer.valueOf((ResurrectionFeatherConfig.loadConfig(mainCfgfile, "onDeathTimeDespawn", onDeathTimeDespawn)).toString());
				useOriginalIcon = Boolean.valueOf((ResurrectionFeatherConfig.loadConfig(mainCfgfile, "useOriginalIcon", useOriginalIcon)).toString());
				DebugMessage = Boolean.valueOf((ResurrectionFeatherConfig.loadConfig(mainCfgfile, "DebugMessage", DebugMessage)).toString());
				cfgMaxMinCheck();
				String k[] = {
						"resurrectionFeatherID",
						"onDeathTimeDrop",
						"onDeathTimeDespawn",
						"useOriginalIcon",
						"DebugMessage"
				};
				String k1[] = {
						""+resurrectionFeatherID,
						""+onDeathTimeDrop,
						""+onDeathTimeDespawn,
						""+useOriginalIcon,
						""+DebugMessage
				};
				ResurrectionFeatherConfig.writerSupplementConfig(mainCfgfile, k, k1);
			}
		}
	}

	public static void cfgMaxMinCheck() {
		if (resurrectionFeatherID < 0) resurrectionFeatherID = 0;
		if (resurrectionFeatherID > 32000) resurrectionFeatherID = 32000;
		if (onDeathTimeDrop < 0) onDeathTimeDrop = 0;
		if (onDeathTimeDrop > 2147483647) onDeathTimeDrop = 2147483647;
		if (onDeathTimeDespawn < 0) onDeathTimeDespawn = 0;
		if (onDeathTimeDespawn > 2147483647) onDeathTimeDespawn = 2147483647;
	}

	public String getClassName(String s) {
		if (s == null) return null;
		if (s.indexOf(".") > -1) return s;
		String s1 = getPackage();
		if (s1 != null) return s1.concat(".").concat(s);
		return s;
	}

	public String getPackage() {
		if (packageName != null
				| isForge) return packageName;
		try {
			String s = ""+Class.forName("net.minecraft.src.FMLRenderAccessLibrary");
			if (s != null) {
				isForge = true;
				packageName = null;
				return null;
			}
		} catch (Exception e) {
		}
		Package pack = getClass().getPackage();
		if (pack != null) packageName = pack.getName();
		else packageName = null;
		return packageName;
	}
}