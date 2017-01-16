// Contains a reference to the Player.
// Draws all relevant information at the
// bottom of the screen.

package com.neet.DiamondHunter.HUD;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JTextField;

import com.neet.DiamondHunter.Entity.Diamond;
import com.neet.DiamondHunter.Entity.Player;
import com.neet.DiamondHunter.GameState.PlayState;
import com.neet.DiamondHunter.Main.GamePanel;
import com.neet.DiamondHunter.Manager.Content;

public class Hud {
	
	private int yoffset;
	
	private BufferedImage bar;
	private BufferedImage diamond;
	private BufferedImage boat;
	private BufferedImage axe;
	
	private Player player;
	
	private int numDiamonds;
	
	private Font font;
	private Color textColor;
	// 플레이어2 게이지 칼러
	private Color textColor2;
	
	public Hud(Player p, ArrayList<Diamond> d) {
		
		player = p;
		numDiamonds = d.size();
		yoffset = GamePanel.HEIGHT;
		
		bar = Content.BAR[0][0];
		diamond = Content.DIAMOND[0][0];
		boat = Content.ITEMS[0][0];
		axe = Content.ITEMS[0][1];
		
		font = new Font("Arial", Font.PLAIN, 10);
		//게이지 색 채우기(1~15 / 15)
		textColor = new Color(47, 64, 126);
	    textColor2= new Color(239, 108, 0);
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw hud (다이아, 아이템 몇개먹었나 창 전체)
		g.drawImage(bar, 0, yoffset, null);
		
		// draw diamond bar (다이아 먹을때마다 차는 게이지)
		g.setColor(textColor);
		g.fillRect(8, yoffset + 6, (int)(28.0 * player.numDiamonds() / numDiamonds), 4);
		
		// 플레이어 2가 다이아먹을때 차는 게이지.
		g.setColor(textColor2);
	    g.fillRect(36-(int)(28.0 * PlayState.player2.numDiamonds() / numDiamonds), yoffset + 6, (int)(28.0 * PlayState.player2.numDiamonds() / numDiamonds), 4);
		
		// draw diamond amount (다이아 몇개먹었나 갯수)
		g.setColor(textColor);
		g.setFont(font);
		String s = player.numDiamonds() + "/" + numDiamonds;
		Content.drawString(g, s, 40, yoffset + 3);
		if(player.numDiamonds() >= 10) g.drawImage(diamond, 80, yoffset, null);
		else g.drawImage(diamond, 72, yoffset, null);
		
		// draw items
		if(player.hasBoat()) g.drawImage(boat, 100, yoffset, null);
		if(player.hasAxe()) g.drawImage(axe, 112, yoffset, null);
		
		// draw time
		int minutes = (int) (player.getTicks() / 1800);
		int seconds = (int) ((player.getTicks() / 30) % 60);
		if(minutes < 10) {
			if(seconds < 10) Content.drawString(g, "0" + minutes + ":0" + seconds, 85, 3);
			else Content.drawString(g, "0" + minutes + ":" + seconds, 85, 3);
		}
		else {
			if(seconds < 10) Content.drawString(g, minutes + ":0" + seconds, 85, 3);
			else Content.drawString(g, minutes + ":" + seconds, 85, 3);
		}
		

	}
	
}
