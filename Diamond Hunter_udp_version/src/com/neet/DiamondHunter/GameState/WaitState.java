// The pause GameState can only be activated
// by calling GameStateManager#setPaused(true).

package com.neet.DiamondHunter.GameState;

import java.awt.Graphics2D;

import com.neet.DiamondHunter.Main.Game;
import com.neet.DiamondHunter.Manager.Content;
import com.neet.DiamondHunter.Manager.GameStateManager;
import com.neet.DiamondHunter.Manager.JukeBox;
import com.neet.DiamondHunter.Manager.Keys;

public class WaitState extends GameState {
	
	public WaitState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {}
	
	public void update() {
		if(PlayState.start) {
			gsm.setWait(false);
			JukeBox.resumeLoop("music1");
			

			String msg = null;
			msg = String.format("%s\t%s\t%s","check",Game.client.id,"id_check");
			Game.client.send_Message(msg);
		}
	}
	
	public void draw(Graphics2D g) {
		
		Content.drawString(g, "paused", 40, 30);
		
		Content.drawString(g, "Wait", 48, 50);
		Content.drawString(g, "For", 52, 60);
		Content.drawString(g, "Other", 44, 70);
		Content.drawString(g, "Connection", 25, 80);

		
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
	
}
