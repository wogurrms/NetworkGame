package com.neet.DiamondHunter.Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ChatPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JTextField jt= new JTextField(12);		
	public static JTextArea ta;
	public ChatPanel() {
		BorderLayout bout= new BorderLayout();
		setLayout(bout);
		
		ta= new JTextArea(5,2);
		// 스크롤을 자동으로 내리게 해줌
		DefaultCaret caret = (DefaultCaret)ta.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		ta.setBackground(new Color(164,198,222));
		ta.setFont(new Font("나눔스퀘어", Font.BOLD, 15));

		ta.setFocusable(false);

		JScrollPane js= new JScrollPane(ta);
		add(js, BorderLayout.CENTER);
		
		jt.setFont(new Font("나눔스퀘어", Font.BOLD, 30));
		jt.setBackground(new Color(164,198,222));
		
		jt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 엔터치면 메시지 전송
				if(e!=null && jt.getText().length()!=0){
					String msg = null;
					msg = String.format("%s\t%s\t%s\t","msg",Game.client.id,e.getActionCommand());
					Game.client.send_Message(msg);
				}
				
				jt.setText(null);
				
				Game.gamePanel.requestFocus();
				

			}
		});
		
		add(jt, BorderLayout.SOUTH);

	}

}
