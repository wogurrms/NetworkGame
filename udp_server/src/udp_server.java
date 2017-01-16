
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
	private JTextField textField; // ����� PORT��ȣ �Է�
	private JButton Start; // ������ �����Ų ��ư
	JTextArea textArea; // Ŭ���̾�Ʈ �� ���� �޽��� ���
	private int Port; // ��Ʈ��ȣ
	private static ArrayList<UserInfo> userinfos;

	// [0] - type , [1] - id , [2] - text
	private String protocol[] = new String[3];

	public static void main(String[] args) {
		udp_server frame = new udp_server(); // ������ ����.
		frame.setVisible(true);
	}

	public udp_server() {
		init();
	}

	private void init() { // GUI�� �����ϴ� �޼ҵ�
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
		Start = new JButton("���� ����");

		Myaction action = new Myaction();
		Start.addActionListener(action); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
		textField.addActionListener(action);
		Start.setBounds(0, 325, 264, 37);
		contentPane.add(Start);
		textArea.setEditable(false); // textArea�� ����ڰ� ���� ���ϰԲ� ���´�.

		userinfos = new ArrayList<UserInfo>();
	}

	class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		@Override
		public void actionPerformed(ActionEvent e) {

			// �׼� �̺�Ʈ�� sendBtn�϶� �Ǵ� textField ���� Enter key ġ��
			if (e.getSource() == Start || e.getSource() == textField) {
				if (textField.getText().equals("") || textField.getText().length() == 0) {
					textField.setText("��Ʈ��ȣ�� �Է����ּ���");
					textField.requestFocus(); // ��Ŀ���� �ٽ� textField�� �־��ش�
				} else {
					try {
						Port = Integer.parseInt(textField.getText());
						server_start(); // ����ڰ� ����ε� ��Ʈ��ȣ�� �־����� ���� ���������� �޼ҵ� ȣ��
					} catch (Exception er) {
						// ����ڰ� ���ڷ� �Է����� �ʾ����ÿ��� ���Է��� �䱸�Ѵ�
						textField.setText("���ڷ� �Է����ּ���");
						textField.requestFocus(); // ��Ŀ���� �ٽ� textField�� �־��ش�
					}
				} // else �� ��
			}
		}
	}

	public void addUser(DatagramPacket packet) {
		InetAddress ipAddress = packet.getAddress();
		int port = packet.getPort();
		UserInfo new_user = new UserInfo(ipAddress, port);
		userinfos.add(new_user);
		textArea.append(userinfos.size() + "p" + "���!\n");
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
		Thread th = new Thread(new Runnable() { // ����� ������ ���� ������
			@SuppressWarnings("resource")
			@Override
			public void run() {
				Start.setText("����������");
				Start.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				textField.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				try {
					socket = new DatagramSocket(Port); // ���� ��Ʈ�� ���� �κ�.
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
						socket.receive(udp_packet); // �� �κп��� ������ ��ٸ���.
						String receiveMSG = new String(bb);
						protocol = receiveMSG.split("\t");
						// type�� check ����
						if (protocol[0].equals("check")) {
							if (protocol[2].contains("join")) {
								addUser(udp_packet);
							}
							if (protocol[2].contains("id_check")) {
								broad_cast(socket, bb);
							}
						}
						// type�� msg ����
						if (protocol[0].equals("msg")) {
							broad_cast(socket, bb);
						}
						// type�� cmd ����
						if (protocol[0].equals("cmd")){
							// �Ѹ��� �����ϸ� ������ �ʱ�ȭ���ѹ���
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

	// ������ ���� ����.
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
