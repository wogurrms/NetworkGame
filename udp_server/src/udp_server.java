
// Java Chatting Server

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class udp_server extends JFrame {
	private static int uCount = 0;
	private static DatagramSocket socket;
	private static DatagramPacket in, out;
	private JPanel contentPane;
	private JTextField textField; // 사용할 PORT번호 입력
	private JButton Start; // 서버를 실행시킨 버튼
	JTextArea textArea; // 클라이언트 및 서버 메시지 출력
	private int Port; // 포트번호
	private static ArrayList<UserInfo> userinfos;

	// [0] - type , [1] - id , [2] - text
	private String protocol[] = new String[3];

	public static void main(String[] args) {
		udp_server frame = new udp_server(); // 프레임 생성.
		frame.setVisible(true);
	}

	public udp_server() {
		init();
	}

	private void init() { // GUI를 구성하는 메소드
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 280, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane js = new JScrollPane();

		textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setRows(5);
		js.setBounds(0, 0, 264, 254);
		contentPane.add(js);
		js.setViewportView(textArea);

		textField = new JTextField();
		textField.setBounds(98, 264, 154, 37);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(12, 264, 98, 37);
		contentPane.add(lblNewLabel);
		Start = new JButton("서버 실행");

		Myaction action = new Myaction();
		Start.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
		textField.addActionListener(action);
		Start.setBounds(0, 325, 264, 37);
		contentPane.add(Start);
		textArea.setEditable(false); // textArea를 사용자가 수정 못하게끔 막는다.

		userinfos = new ArrayList<UserInfo>();
	}

	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {

			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == Start || e.getSource() == textField) {
				if (textField.getText().equals("") || textField.getText().length() == 0) {
					textField.setText("포트번호를 입력해주세요");
					textField.requestFocus(); // 포커스를 다시 textField에 넣어준다
				} else {
					try {
						Port = Integer.parseInt(textField.getText());
						server_start(); // 사용자가 제대로된 포트번호를 넣었을때 서버 실행을위헤 메소드 호출
					} catch (Exception er) {
						// 사용자가 숫자로 입력하지 않았을시에는 재입력을 요구한다
						textField.setText("숫자로 입력해주세요");
						textField.requestFocus(); // 포커스를 다시 textField에 넣어준다
					}
				} // else 문 끝
			}
		}
	}

	public void addUser(DatagramPacket packet) {
		InetAddress ipAddress = packet.getAddress();
		int port = packet.getPort();
		UserInfo new_user = new UserInfo(ipAddress, port);
		userinfos.add(new_user);
		textArea.append(userinfos.size() + "p" + "등록!\n");
	}

	private static void broad_cast(DatagramSocket sk, byte[] msg) {
		DatagramSocket socket = sk;
		byte[] sendData = new byte[128];
		sendData = msg;

		for (int i = 0; i < userinfos.size(); i++) {
			DatagramPacket sendPack = new DatagramPacket(sendData, sendData.length, userinfos.get(i).getAddr(),
					userinfos.get(i).getPort());
			try {
				socket.send(sendPack);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// @SuppressWarnings("resource")
	private void server_start() {
		Thread th = new Thread(new Runnable() { // 사용자 접속을 받을 스레드
			@SuppressWarnings("resource")
			@Override
			public void run() {
				Start.setText("서버실행중");
				Start.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				textField.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				try {
					socket = new DatagramSocket(Port); // 서버 포트를 여는 부분.
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				byte[] bb = new byte[128];
				DatagramPacket udp_packet = new DatagramPacket(bb, bb.length);

				while (true) {
					for (int i = 0; i < bb.length; i++) {
						bb[i] = 0;
					}
					try {
						socket.receive(udp_packet); // 이 부분에서 접속을 기다린다.
						String receiveMSG = new String(bb);
						protocol = receiveMSG.split("\t");
						// type이 check 인지
						if (protocol[0].equals("check")) {
							if (protocol[2].contains("join")) {
								addUser(udp_packet);
							}
							if (protocol[2].contains("id_check")) {
								broad_cast(socket, bb);
							}
						}
						// type이 msg 인지
						if (protocol[0].equals("msg")) {
							broad_cast(socket, bb);
						}
						// type이 cmd 인지
						if (protocol[0].equals("cmd")){
							// 한명이 종료하면 완전히 초기화시켜버림
							if(protocol[2].contains("exit") || protocol[2].contains("gameover")){	
								broad_cast(socket, bb);
								userinfos.clear();
							}
							else {
								broad_cast(socket, bb);
							}
						}
						if (userinfos.size() >= 2) {
							String msg = null;
							msg = String.format("%s\t%s\t%s\t", "check", "server", "start");
							broad_cast(socket, msg.getBytes());
						} else {

						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String msgStr = new String(bb);
					textArea.append(msgStr + "\n");
					textArea.setCaretPosition(textArea.getText().length());

				}
			}
		});
		th.start();
	}

	// 유저의 정보 저장.
	class UserInfo {
		InetAddress addr;
		int port;

		public UserInfo(InetAddress addr, int port) {
			this.addr = addr;
			this.port = port;
		}

		public int getPort() {
			return port;
		}

		public InetAddress getAddr() {
			return addr;
		}

	}
}
