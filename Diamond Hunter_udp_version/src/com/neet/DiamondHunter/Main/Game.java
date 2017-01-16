// The entry point of the game.
// This class loads up a JFrame window and
// puts a GamePanel into it.

package com.neet.DiamondHunter.Main;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.neet.DiamondHunter.Client.UDP_Client;
import com.neet.DiamondHunter.GameState.PlayState;

public class Game {
	public static JFrame window;
	public static GamePanel gamePanel = new GamePanel();
	public static ChatPanel chatpanel = new ChatPanel();

	public static UDP_Client client;

	public static void main(String[] args) throws IOException {

		window = new JFrame("Diamond Hunter");

		window.add(gamePanel);

		window.add(chatpanel, BorderLayout.SOUTH);

		window.setResizable(false);
		window.pack();

		client = new UDP_Client("localhost", 30000);

		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.NORMAL);

		window.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowevent) {
				if (JOptionPane.showConfirmDialog(window, "Are you sure to close this window?", "Really Closing?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

					String msg = null;
					try {
						msg = String.format("%s\t%s\t%s\t", "cmd", PlayState.player.nickname, "exit");
						Game.client.send_Message(msg);
					} catch (Exception e) {
						System.exit(0);
					}
					System.exit(0);
				}
			}
		});

	}

}
