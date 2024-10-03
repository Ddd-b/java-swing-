package com.cqupt.lyy.Notepad;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GetSupport extends JFrame{
	public GetSupport() {
		
	}
	//实现发送反馈
	public void SendFeedback(ActionEvent e) {
		JOptionPane.showMessageDialog(null,"该功能尚未实现，有事请联系作者。\n联系方式：*****@qq.com","发送反馈" ,0);
	}
			
	//实现查看帮助
	public void Help(ActionEvent e) {
		//跳转到网站
		Desktop desk = Desktop.getDesktop();
		try {
			URI uri = new URI(
					"https://cn.bing.com/search?q=%E8%8E" + "%B7%E5%8F%96%E6%9C%89%E5%85%B3+windows+10+%E"
							+ "4%B8%AD%E7%9A%84%E8%AE%B0%E4%BA%8B%E6%9C%AC%E7%"
							+ "9A%84%E5%B8%AE%E5%8A%A9&filters=guid:%224466414-zh-h"
							+ "ans-dia%22%20lang:%22zh-hans%22&form=T00032&ocid=HelpPane-BingIA");
					desk.browse(uri);
			} catch (IOException e1) {
			// 处理读取文件时发生的其他IO异常，如读取错误、缓冲区溢出等
			e1.printStackTrace();
			}catch (URISyntaxException e1) {
			// 处理非法字符异常
			e1.printStackTrace();
			}
	}
			
	//实现关于
	public void About(ActionEvent e) {
		JOptionPane.showMessageDialog(null,
				"*********************************************\n" 
				+ " 编写者：姓名 学号\n"
				+ " 编写时间：2024-09\n" + "QQ：****\n"
				+ " 有些功能尚未实现，如有不足请提出建议\n" 
				+ "*********************************************\n",
				"关于",
				JOptionPane.INFORMATION_MESSAGE);
	}
}

