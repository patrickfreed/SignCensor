package me.patrickfreed.signcensor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignCensorSignListener implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		for (String lines : event.getLines()){
			for (String words : SignCensor.words){
				if (lines.toLowerCase().contains(words.toLowerCase()) && !event.getPlayer().hasPermission("SignCensor.exempt")){
					int line = 0;	
					while (line < 4){
						event.setLine(line, SignCensor.Line[line]);
						line++;
					}
					if (SignCensor.isKickMode){
						event.getPlayer().kickPlayer(SignCensor.CensorMessage);
					}
					else {
						event.getPlayer().sendMessage(SignCensor.CensorMessage);
					}
					
					if (SignCensor.isLoggingCensors){
						File log = new File("plugins/SignCensor", "censor-log.yml");
						YamlConfiguration logdata = YamlConfiguration.loadConfiguration(log);
						if (!log.exists()){
							try {
								log.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						for(int x = 1; x < lines.length() + 1; x++){
							String xstring = Integer.toString(x);
							String locx = event.getBlock().getLocation().getX() + "-";
							String locy = event.getBlock().getLocation().getY() + "-";
							String locz = Double.toString(event.getBlock().getLocation().getZ());
							logdata.set(Utilities.getDateTime() + ": " + player.getName() + ": " + locx + locy + locz + ".Line " + xstring, event.getLine(x-1));
							if(x == lines.length()){
								if(SignCensor.isKickMode){
									logdata.set(Utilities.getDateTime() + ": " + player.getName() + ": " + locx + locy + locz + ".Action", "Kicked");
								}else{
									logdata.set(Utilities.getDateTime() + ": " + player.getName() + ": " + locx + locy + locz + ".Action", "Warned");
								}
							}
						}
					}
				}
				if (SignCensor.isLoggingAll){
					File log = new File("plugins/SignCensor", "sign-log.yml");
					YamlConfiguration logdata = YamlConfiguration.loadConfiguration(log);
					if (!log.exists()){
						try {
							log.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					for(int x = 1; x < lines.length() + 1; x++){
						if(event.getLine(x-1) != null){
							String xstring = Integer.toString(x);
							try {
								logdata.load(log);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (InvalidConfigurationException e) {
								e.printStackTrace();
							}
							String locx = event.getBlock().getLocation().getX() + "-";
							String locy = event.getBlock().getLocation().getY() + "-";
							String locz = Double.toString(event.getBlock().getLocation().getZ());
							logdata.set(Utilities.getDateTime() + ": " + player.getName() + ": " + locx + locy + locz + ".Line " + xstring, event.getLine(x-1));
							
							try {
								logdata.save(log);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}