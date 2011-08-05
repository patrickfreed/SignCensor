package me.patrickfreed.signcensor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 * @author PatrickFreed
 */

public class SignCensor extends JavaPlugin {
	public Configuration config;
	public static List<String>words;
	public static String CensorMessage;
	public static boolean isKickMode; 
	public static String[] Line = new String[4];

	@Override
	public void onDisable() {
		System.out.println("[" + this.getDescription().getName()+ "] Disabled!");
	}

	@Override
	public void onEnable() {
		System.out.println("[" + this.getDescription().getName() + "] Enabled!");
		PluginManager pm = getServer().getPluginManager();
		pm.addPermission(new Permission("SignCensor.exempt"));
		pm.registerEvent(Event.Type.SIGN_CHANGE, new SignCensorSignListener(),Priority.Normal, this);
		File conf = makeConfig(new File(getDataFolder(), "config.yml"));
		if (conf.exists()) {
			config = getConfiguration();
			List<String>words = config.getStringList("Banned-Words", null);		
			SignCensor.words = words;
			CensorMessage = config.getString("Censor-Message", "You put a banned word on that sign! Censoring...");
			isKickMode = config.getBoolean("Auto-Kick", false);
			Line[0] = config.getString("Replacements.Line-One", "Censored!");
			Line[1] = config.getString("Replacements.Line-Two", "Censored!");
			Line[2] = config.getString("Replacements.Line-Three", "Censored!");
			Line[3] = config.getString("Replacements.Line-Four", "Censored!");
			System.out.println("[" + this.getDescription().getName() + "] Config loaded successfully!");
		}
		else {
			System.out.println("Config loading failed, disabling...");
			pm.disablePlugin(this);
		}

	}
	//makeConfig by krinsdeath
	private File makeConfig(File file) {
		if (!file.exists()) {
			System.out.println("[" + this.getDescription().getName() + "] Generating config...");
			new File(file.getParent()).mkdirs();
			InputStream in = SignCensor.class
			.getResourceAsStream("/resources/" + file.getName());
			if (in != null) {
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					byte[] buffer = new byte[2048];
					int length = 0;
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						in.close();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return file;
	}
}
	
	