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
	// Client.Java Java Chatting Client 의 Nicknam, IP, Port 번호 입력하고 접속하는 부분
	// MainView.java : Java Chatting Client 의 핵심부분
	// read keyboard --> write to network (Thread 로 처리)
	// read network --> write to textArea

	public static String id = " ";
	private InetAddress ip_addr;
	private int port;
	private DatagramSocket udp_socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	// [0] - type , [1] - id , [2] - text
	private String protocol[] = new String[3];

	public UDP_Client(String ip,int port) throws UnknownHostException// 생성자
	{
		this.id = "";
		this.ip_addr = InetAddress.getByName(ip);
		this.port = 30000;
		// textArea.append("매개 변수로 넘어온 값 : " + id + " " + "127.0.0.1" + " " +
		// port + "\n");
		network();
	}

	public void network() {
		// 서버에 접속
		try {
			udp_socket = new DatagramSocket();
			if (udp_socket != null) // socket이 null값이 아닐때 즉! 연결되었을때
			{
				Connection(); // 연결 메소드를 호출
			}
		} catch (IOException e) {
			// textArea.append("소켓 접속 에러!!\n");
		}
	}

	public void Connection() { // 실직 적인 메소드 연결부분
		Thread th = new Thread(new Runnable() { // 스레드를 돌려서 서버로부터 메세지를 수신
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
					
					// type이 check일때
					if (protocol[0].equals("check")) {
						if (!protocol[1].equals(id) && !protocol[1].equals("server"))
							PlayState.player2.setNickName(protocol[1]);

						if (protocol[2].contains("start"))
							PlayState.start = true;
					}

					// type이 msg일때
					if (protocol[0].equals("msg")) {
						String msg_tmp = "[" + protocol[1] + "]" + "  " + protocol[2];
						Game.chatpanel.ta.append(msg_tmp + "\n");
					}
					
					// type이 cmd일때
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
								// 종료 신호가 오면 다시 처음부터 시작됨.
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
					
				} // while문 끝
			}// run메소드 끝
		});
		th.start();
	}

	public void send_Message(String str) { // 서버로 메세지를 보내는 메소드
		byte[] bb = new byte[128];
		bb = str.getBytes();
		DatagramPacket udp_packet = new DatagramPacket(bb, bb.length, ip_addr, port);
		try {
			udp_socket.send(udp_packet);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
		}
	}

}
