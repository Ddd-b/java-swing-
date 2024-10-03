package com.cqupt.lyy.Notepad;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetNotePadState extends JFrame implements ActionListener {
	private NotePadMain notepad;
	private boolean isIncrease;
	
	//接受NotePadMain类的实例
	public SetNotePadState(NotePadMain NotePadMain,boolean isIncrease) {
		this.notepad=NotePadMain;
		this.isIncrease=isIncrease;
	}
	
	public void actionPerformed(ActionEvent e) {//方法的名字，必须为actionPerformed
		String name = notepad.textArea.getFont().getFontName();// 返回字体外观
		int style = notepad.textArea.getFont().getStyle();// 获得字体的样式
		int size = notepad.textArea.getFont().getSize();// 获得字体的大小
		if(isIncrease) {
			notepad.textArea.setFont(new Font(name, style, size + 1));// 设置字体大小+1
		}
		else {
			notepad.textArea.setFont(new Font(name, style, size - 1));// 设置字体大小-1
		}
		
	}
	
}
