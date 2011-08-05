package me.patrickfreed.signcensor;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class SignCensorSignListener extends BlockListener {

	@Override
	public void onSignChange(SignChangeEvent event) {
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
				}
			}
		}
	}

}
