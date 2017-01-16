package com.neet.DiamondHunter.Client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.neet.DiamondHunter.GameState.PlayState;
import com.neet.DiamondHunter.Main.Game;
import com.neet.DiamondHunter.Main.GamePanel;
import com.neet.DiamondHunter.Manager.GameStateManager;
import com.neet.DiamondHunter.Manager.JukeBox;

public class UDP_Client extends JFrame {
	// Client.Java Java Chatting Client �� Nicknam, IP, Port ��ȣ �Է��ϰ� �����ϴ� �κ�
	// MainView.java : Java Chatting Client �� �ٽɺκ�
	// read keyboard --> write to network (Thread �� ó��)
	// read network --> write to textArea

	public static String id = " ";
	private InetAddress ip_addr;
	private int port;
	private DatagramSocket udp_socket; // �������
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	// [0] - type , [1] - id , [2] - text
	private String protocol[] = new String[3];

	public UDP_Client(String ip,int port) throws UnknownHostException// ������
	{
		this.id = "";
		this.ip_addr = InetAddress.getByName(ip);
		this.port = 30000;
		// textArea.append("�Ű� ������ �Ѿ�� �� : " + id + " " + "127.0.0.1" + " " +
		// port + "\n");
		network();
	}

	public void network() {
		// ������ ����
		try {
			udp_socket = new DatagramSocket();
			if (udp_socket != null) // socket�� null���� �ƴҶ� ��! ����Ǿ�����
			{
				Connection(); // ���� �޼ҵ带 ȣ��
			}
		} catch (IOException e) {
			// textArea.append("���� ���� ����!!\n");
		}
	}

	public void Connection() { // ���� ���� �޼ҵ� ����κ�
		Thread th = new Thread(new Runnable() { // �����带 ������ �����κ��� �޼����� ����
			@Override
			public void run() {
				byte[] bb = new byte[128];
				DatagramPacket udp_packet = new DatagramPacket(bb, bb.length);
				while (true) {
					for (int i = 0; i < bb.length; i++) {
						bb[i] = 0;
					}
					try {
						udp_socket.receive(udp_packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String msg = new String(bb);
					msg = msg.trim();
					protocol = msg.split("\t");
					
					// type�� check�϶�
					if (protocol[0].equals("check")) {
						if (!protocol[1].equals(id) && !protocol[1].equals("server"))
							PlayState.player2.setNickName(protocol[1]);

						if (protocol[2].contains("start"))
							PlayState.start = true;
					}

					// type�� msg�϶�
					if (protocol[0].equals("msg")) {
						String msg_tmp = "[" + protocol[1] + "]" + "  " + protocol[2];
						Game.chatpanel.ta.append(msg_tmp + "\n");
					}
					
					// type�� cmd�϶�
					if (protocol[0].equals("cmd")) {
						if (!protocol[1].equals(id)) {
							if (protocol[2].contains("left"))
								PlayState.player2.setLeft();
							if (protocol[2].contains("right"))
								PlayState.player2.setRight();
							if (protocol[2].contains("up"))
								PlayState.player2.setUp();
							if (protocol[2].contains("down"))
								PlayState.player2.setDown();
							if (protocol[2].contains("act"))
								PlayState.player2.setAction();
							if (protocol[2].contains("exit") || protocol[2].contains("gameover")){
								// ���� ��ȣ�� ���� �ٽ� ó������ ���۵�.
								PlayState.start = false;
								GamePanel.gsm.setState(GameStateManager.INTRO);
							}
							if (protocol[2].contains("pause")){
								JukeBox.stop("music1");
								GamePanel.gsm.setPaused(true);
							}
							if (protocol[2].contains("restart")){
								GamePanel.gsm.setPaused(false);
								JukeBox.resumeLoop("music1");
							}
						}
					}
					
				} // while�� ��
			}// run�޼ҵ� ��
		});
		th.start();
	}

	public void send_Message(String str) { // ������ �޼����� ������ �޼ҵ�
		byte[] bb = new byte[128];
		bb = str.getBytes();
		DatagramPacket udp_packet = new DatagramPacket(bb, bb.length, ip_addr, port);
		try {
			udp_socket.send(udp_packet);
		} catch (IOException e) {
			// textArea.append("�޼��� �۽� ����!!\n");
		}
	}

}
