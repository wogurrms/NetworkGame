// The main menu GameState.
//메인메뉴 게임스테이트
//스타트, 종료 선택 가능.

package com.neet.DiamondHunter.GameState;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.neet.DiamondHunter.Entity.Player;
import com.neet.DiamondHunter.Main.Game;
import com.neet.DiamondHunter.Manager.Content;
import com.neet.DiamondHunter.Manager.GameStateManager;
import com.neet.DiamondHunter.Manager.JukeBox;
import com.neet.DiamondHunter.Manager.Keys;

public class MenuState extends GameState {
	
	private BufferedImage bg;
	private BufferedImage diamond;
	
	private int currentOption = 0;
	private String[] options = {
		"START",
		"QUIT",
	};
	private GameStateManager gsm;
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
		this.gsm= gsm;
	}
	
	public void init() {
		bg = Content.MENUBG[0][0];
		diamond = Content.DIAMOND[0][0];
		JukeBox.load("/SFX/collect.wav", "collect");
		JukeBox.load("/SFX/menuoption.wav", "menuoption");
	}
	
	public void update() {
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(bg, 0, 0, null);
		
		//글자 출력
		Content.drawString(g, options[0], 42, 90);
		Content.drawString(g, options[1], 45, 100);
		
		if(currentOption == 0) g.drawImage(diamond, 22, 86, null);
		else if(currentOption == 1) g.drawImage(diamond, 22, 96, null);
		
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.DOWN) && currentOption < options.length - 1) {
			JukeBox.play("menuoption");
			currentOption++;
		}
		if(Keys.isPressed(Keys.UP) && currentOption > 0) {
			JukeBox.play("menuoption");
			currentOption--;
		}
		if(Keys.isPressed(Keys.ENTER)) {
			JukeBox.play("collect");
			selectOption();
		}
	}
	
	private void selectOption() {
		if(currentOption == 0) {
			new LoginDialog(Game.window, "LOGIN", gsm);
			gsm.setState(GameStateManager.PLAY);
		}
		if(currentOption == 1) {
			System.exit(0);
		}
	}
	
	public class LoginDialog extends JDialog {
		private JTextField tf= new JTextField(10);
		private JButton loginButton= new JButton("LOGIN");
		private GameStateManager gsm;
		private LoginDialog(JFrame frame, String title, GameStateManager gsm){
			super(frame, title);
			this.gsm= gsm;
			setLayout(new FlowLayout());
			setBackground(new Color(164,198,222));
			JLabel textLabel1= new JLabel("press nickname");
			add(textLabel1);
			JLabel textLabel2= new JLabel("only English or Intager");
			add(textLabel2);
			add(tf);
			add(loginButton);
			setSize(200, 170);
			this.setLocationRelativeTo(frame);
			setVisible(true);
			
			tf.addActionListener(new LoginActionListener());
			loginButton.addActionListener(new LoginActionListener());
		}
		
		public class LoginActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlayState.player.setNickName(e.getActionCommand().toString());
				Game.client.id = e.getActionCommand().toString();
				
				String msg = null;
				msg = String.format("%s\t%s\t%s","check",Game.client.id,"join");
				Game.client.send_Message(msg);
				
				setVisible(false);
			}
		}

	}
	
}
