package com.cqupt.lyy.Notepad;

import javax.swing.undo.UndoManager;
import java.text.SimpleDateFormat;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Date;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NotePadMain extends JFrame  {
	private File file;// 打开和保存共同的成员变量
	private boolean changed;// 用作关闭时判断文件是否变化
	private UndoManager undom;// 撤销管理器
	private GetSupport gs = new GetSupport();//获取帮助
	private FindDialog findReplaceDialog=null;//查找器
	private FontChooser fontDialog=null;//字体选择器
	private JLabel statusLabel;//状态栏
	private JCheckBoxMenuItem chckbxmntmNewCheckItem;//状态栏有无
	public JTextArea textArea;
	public JFrame frame;
	private JPopupMenu popu;// 弹出式菜单
	private JColorChooser bcolorChooser=null;//背景颜色
	private JColorChooser fcolorChooser=null;//字体颜色
	private JDialog backgroundDialog=null;
	private JDialog foregroundDialog=null;

	
	public NotePadMain() {
		setTitle("无标题-记事本");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 702, 439);
		
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {// 窗体打开时设置大小
				Toolkit toolkit = getToolkit();
				Dimension dimen = toolkit.getScreenSize();// 获得屏幕大小
				int width = (int) (dimen.getWidth() * 0.5);
				int height = (int) (dimen.getHeight() * 0.6);
				setSize(width, height);
			}

			public void windowClosing(WindowEvent e) {// 窗体关闭时，询问是否保存文件
				do_this_windowClosing(e);
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu m1 = new JMenu("文件");
		m1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		menuBar.add(m1);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("新建标签页");
		mntmNewMenuItem.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewTab();
			}
		});
		m1.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("新建窗口");
		mntmNewMenuItem_1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NotePadMain m = new NotePadMain();
				m.setVisible(true);
			}
		});
		m1.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("打开");
		mntmNewMenuItem_2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenOP();
			}
		});
		m1.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("保存");
		mntmNewMenuItem_3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveOP();
			}
		});
		m1.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("另存为");
		mntmNewMenuItem_4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveAsOP();
			}
		});
		m1.add(mntmNewMenuItem_4);
		
		JSeparator separator = new JSeparator();
		m1.add(separator);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("页面设置");
		mntmNewMenuItem_5.setEnabled(false);
		mntmNewMenuItem_5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		m1.add(mntmNewMenuItem_5);
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("打印");
		mntmNewMenuItem_6.setEnabled(false);
		mntmNewMenuItem_6.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		m1.add(mntmNewMenuItem_6);
		
		JSeparator separator_2 = new JSeparator();
		m1.add(separator_2);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("退出");
		mntmNewMenuItem_7.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		m1.add(mntmNewMenuItem_7);
		
		JSeparator separator_1 = new JSeparator();
		m1.add(separator_1);
		
		JMenu m2 = new JMenu("编辑");
		m2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		menuBar.add(m2);
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("撤销");
		mntmNewMenuItem_8.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_8.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mntmNewMenuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (undom.canUndo()) {// 如果可以撤销
					undom.undo();// 撤销
				}
			}
		});
		m2.add(mntmNewMenuItem_8);
		
		JSeparator separator_3 = new JSeparator();
		m2.add(separator_3);
		
		JMenuItem mntmNewMenuItem_9 = new JMenuItem("剪切");
		mntmNewMenuItem_9.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_9.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mntmNewMenuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.cut();// 剪切
			}
		});
		m2.add(mntmNewMenuItem_9);
		
		JMenuItem mntmNewMenuItem_10 = new JMenuItem("复制");
		mntmNewMenuItem_10.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_10.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mntmNewMenuItem_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.copy();// 复制
			}
		});
		m2.add(mntmNewMenuItem_10);
		
		JMenuItem mntmNewMenuItem_11 = new JMenuItem("粘贴");
		mntmNewMenuItem_11.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_11.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mntmNewMenuItem_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.paste();// 粘贴
			}
		});
		m2.add(mntmNewMenuItem_11);
		
		JMenuItem mntmNewMenuItem_12 = new JMenuItem("删除");
		mntmNewMenuItem_12.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.replaceSelection("");//用空替换选中的文本
			}
		});
		m2.add(mntmNewMenuItem_12);
		
		JSeparator separator_4 = new JSeparator();
		m2.add(separator_4);
		
		JMenuItem mntmNewMenuItem_13 = new JMenuItem("查找");
		mntmNewMenuItem_13.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_13.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		mntmNewMenuItem_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findReplaceDialog=new FindDialog(textArea);
				findReplaceDialog.showDialog(frame,true);//查找
			}
		});
		m2.add(mntmNewMenuItem_13);
		
		JMenuItem mntmNewMenuItem_14 = new JMenuItem("查找下一个");
		mntmNewMenuItem_14.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_14.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		mntmNewMenuItem_14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FindNext();
			}
		});
		m2.add(mntmNewMenuItem_14);
		
		JMenuItem mntmNewMenuItem_15 = new JMenuItem("查找上一个");
		mntmNewMenuItem_15.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_15.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_MASK));
		mntmNewMenuItem_15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FindLast();
			}
		});
		m2.add(mntmNewMenuItem_15);
		
		JMenuItem mntmNewMenuItem_16 = new JMenuItem("替换");
		mntmNewMenuItem_16.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_16.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mntmNewMenuItem_16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findReplaceDialog=new FindDialog(textArea);
				findReplaceDialog.showDialog(frame,false);//替换
			}
		});
		m2.add(mntmNewMenuItem_16);
		
		JMenuItem mntmNewMenuItem_17 = new JMenuItem("转到");
		mntmNewMenuItem_17.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_17.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		mntmNewMenuItem_17.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Goto();//转到
			}
		});
		m2.add(mntmNewMenuItem_17);
		
		JSeparator separator_5 = new JSeparator();
		m2.add(separator_5);
		
		JMenuItem mntmNewMenuItem_18 = new JMenuItem("全选");
		mntmNewMenuItem_18.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_18.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mntmNewMenuItem_18.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.selectAll();// 全部选中
			}
		});
		m2.add(mntmNewMenuItem_18);
		
		JMenuItem mntmNewMenuItem_19 = new JMenuItem("时间/日期");
		mntmNewMenuItem_19.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_19.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mntmNewMenuItem_19.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();//获得当前日期
				//日期格式化SimpleDateFormat h小时，m分钟 y年份 M月份 d天数
				SimpleDateFormat sdf = new SimpleDateFormat("hh:mm yyyy-MM-dd");
				textArea.append(sdf.format(date));//追加到文本
			}
		});
		m2.add(mntmNewMenuItem_19);
		
		JMenu m3 = new JMenu("查看");
		m3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		menuBar.add(m3);
		
		JMenu mnNewMenu = new JMenu("缩放");
		mnNewMenu.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		m3.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_20 = new JMenuItem("放大");
		mntmNewMenuItem_20.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_20.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_MASK));
		//功能委托给 SetNotePadState类
		SetNotePadState e=new SetNotePadState(this,true);
		mntmNewMenuItem_20.addActionListener(e);
		mnNewMenu.add(mntmNewMenuItem_20);
		
		JMenuItem mntmNewMenuItem_21 = new JMenuItem("缩小");
		mntmNewMenuItem_21.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_21.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK));
		SetNotePadState e1=new SetNotePadState(this,false);
		mntmNewMenuItem_21.addActionListener(e1);
		mnNewMenu.add(mntmNewMenuItem_21);
		
		chckbxmntmNewCheckItem = new JCheckBoxMenuItem("状态栏");
		chckbxmntmNewCheckItem.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		chckbxmntmNewCheckItem.setSelected(true);
		chckbxmntmNewCheckItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusLabel.setVisible(chckbxmntmNewCheckItem.isSelected());
			}
		});
		m3.add(chckbxmntmNewCheckItem);
		
		
		JCheckBoxMenuItem chckbxmntmNewCheckItem_1 = new JCheckBoxMenuItem("自动换行");
		chckbxmntmNewCheckItem_1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		chckbxmntmNewCheckItem_1.setSelected(false);
		chckbxmntmNewCheckItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setLineWrap(chckbxmntmNewCheckItem_1.isSelected());
			}
		});
		m3.add(chckbxmntmNewCheckItem_1);
		
		JMenu m5 = new JMenu("格式");
		m5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		menuBar.add(m5);
		
		JMenuItem mntmNewMenuItem_25 = new JMenuItem("字体");
		mntmNewMenuItem_25.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_25.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fontDialog==null)
					fontDialog=new FontChooser(textArea.getFont());

				if(fontDialog.showDialog(frame,"字体选择"))//如果修改成功
					textArea.setFont(fontDialog.createFont());//修改字体
			}
		});
		m5.add(mntmNewMenuItem_25);
		
		JMenuItem mntmNewMenuItem_26 = new JMenuItem("字体颜色");
		mntmNewMenuItem_26.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_26.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showForegroundColorDialog();
			}
		});
		m5.add(mntmNewMenuItem_26);
		
		JMenuItem mntmNewMenuItem_27 = new JMenuItem("背景颜色");
		mntmNewMenuItem_27.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_27.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBackgroundColorDialog();
			}
		});
		m5.add(mntmNewMenuItem_27);
		
		JMenu m4 = new JMenu("设置");
		m4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 17));
		menuBar.add(m4);
		
		JMenuItem mntmNewMenuItem_22 = new JMenuItem("发送反馈");
		mntmNewMenuItem_22.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gs.SendFeedback(e);//发送反馈
			}
		});
		m4.add(mntmNewMenuItem_22);
		
		JMenuItem mntmNewMenuItem_23 = new JMenuItem("帮助");
		mntmNewMenuItem_23.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_23.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gs.Help(e);//帮助
			}
		});
		m4.add(mntmNewMenuItem_23);
		
		JMenuItem mntmNewMenuItem_24 = new JMenuItem("关于此应用");
		mntmNewMenuItem_24.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		mntmNewMenuItem_24.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gs.About(e);//关于
			}
		});
		m4.add(mntmNewMenuItem_24);
		
		//初始状态栏
		statusLabel = new JLabel("Ln 1,Col 1, number 0");
		statusLabel.setFont(new Font("宋体", Font.PLAIN, 15));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(statusLabel, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		//状态栏变化，状态栏不显示的情况下，也要自动计数
		textArea.addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent e)
			{
				int lineNumber=0, column=0, pos=0,number=0;
				try
				{
					pos=textArea.getCaretPosition();//获取光标位置
					lineNumber=textArea.getLineOfOffset(pos);//监听光标所在行
					column=pos-textArea.getLineStartOffset(lineNumber);//监听光标所在列
					number=textArea.getText().length();//统计字符数
					
					if(textArea.getText().length()==0){
						lineNumber=0; column=0;number=0;
					}
					statusLabel.setText("Ln "+(lineNumber+1)+", Col "+(column+1)+", number "+(number));
				}catch(Exception excp){
					excp.printStackTrace();
				}
				
			}
		});
		
		undom = new UndoManager();// 创建撤销管理器
		textArea.getDocument().addUndoableEditListener(undom);// 为文本注册监听器	
		
		//检测文本框是否被改变
		textArea.addKeyListener(new KeyAdapter() {// 给文本添加键盘监听
			public void keyTyped(KeyEvent e) {// 发生击键事件触发
				changed = true;
			}
		});
		
		
	}
	
	
	//窗口关闭时，监测文件是否被修改和保存
	protected void do_this_windowClosing(WindowEvent e) {
		String nowtext = textArea.getText();
		if (changed) { // 如果文本改动过则弹出对话框
			Object[] options = { "保存(S)", "不保存(N)", "取消" }; // 按钮
			int m = JOptionPane.showOptionDialog(this, "你想将更改保存到 无标题 吗？", "记事本", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (m == JOptionPane.YES_OPTION) { // 点击保存
				Save();// 调用保存文本的方法
				this.dispose();// 销毁窗体
			} else if (m == JOptionPane.NO_OPTION) {
				this.dispose();// 销毁窗体
			}
		} else {
			this.dispose();// 销毁窗体
		}
	}
	
	//新建标签页时，监测文件是否被修改和保存
	public void NewTab() {
		this.file = null;// 设置为null
		changed = false;
		String text = textArea.getText();// 获取文本内容
		if (text.isEmpty()) {// 如果文本为空
			setTitle("无标题-记事本");
			textArea.setText("");// 清空文本域
		} else {
			Object[] options = { "保存(S)", "不保存(N)", "取消" }; //按钮
			int m = JOptionPane.showOptionDialog(this, "你想将更改保存到 无标题 吗？", "记事本", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (m == JOptionPane.YES_OPTION) {// 点击保存
				Save();// 调用保存的方法
				setTitle("无标题-记事本");
				textArea.setText("");// 清空文本域
			} else if (m == JOptionPane.NO_OPTION) {// 点击不保存
				setTitle("无标题-记事本");
				textArea.setText("");// 清空文本域
			}
		}
		
	}
	
	//打开文件，监测文件是否被修改和保存
	public void OpenOP() {
		String text = textArea.getText();// 获取文本内容
		changed = false;
		if (text.isEmpty()||text==" ") {// 如果文本为空,那么直接打开文件选择器
			Open();

		} else {
			Object[] options = { "保存(S)", "不保存(N)", "取消" }; // 按钮
			int m = JOptionPane.showOptionDialog(this, "你想将更改保存到 无标题 吗？", "记事本", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (m == JOptionPane.YES_OPTION || m == JOptionPane.NO_OPTION) {// 判断是否打开文件
				if (m == JOptionPane.YES_OPTION) {// 如果要保存，否则直接跳过
					Save();// 调用保存文本的方法
				}
				Open();// 调用打开文件的方法
			}
		}
		
	}
	
	//保存文件
	public void SaveOP() {
		String text = textArea.getText();// 获取文本域的内容
		changed = false;
		if (this.file != null) {// 判断文件是否存在，如果已经存在直接保存
			
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file));// 创建文件的缓存输出流
				bw.write(text);// 把文本保存导文件
				bw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					bw.close();// 关闭流
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		} else {// 文件不存在，打开文件选择器进行保存
			Save();
		}
		
	}
	
	//文件另存为
	public void SaveAsOP() {
		String text = textArea.getText();// 获取文本内容
		changed = false;
		if (text.isEmpty()) {// 如果为空
			JOptionPane.showMessageDialog(this, "没有需要保存的文本");
			return;
		} else {
			FileDialog fd = new FileDialog(this, "另存为", FileDialog.SAVE);// 创建文件保存对话框
			fd.setFile(this.getTitle() + ".txt");
			fd.setVisible(true);// 显示对话框
			
			String parent = fd.getDirectory();// 获得父路径
			String child = fd.getFile();// 获得文件名
			if (parent != null && child != null) {// 判断是否要保存，或者取消
				File f = new File(parent, child);// 创建一个文件对象
				PrintStream ps = null;// 打印流
				try {
					ps = new PrintStream(f);
					ps.write(text.getBytes());
					ps.flush();
				} catch (FileNotFoundException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} finally {
					if (ps != null) {
						ps.close();
					}
				}
			}
		}		
	}
	
	private void Open() {// 打开文件的方法
		JFileChooser filechooser = new JFileChooser();// 创建一个文件选择器
		filechooser.setFileFilter(new FileNameExtensionFilter("java文档(*.java)", "java"));
		filechooser.setFileFilter(new FileNameExtensionFilter("文本文档(*.txt)", "txt"));// 设置文件过滤器
		filechooser.setCurrentDirectory(new File("."));//默认打开，当前文件的目录
		int i = filechooser.showOpenDialog(this);// 显示对话框
		if (i == filechooser.APPROVE_OPTION) {// 如果点击了打开按钮
			this.file = filechooser.getSelectedFile();// 获取选中的文件
			this.setTitle(this.file.getName());// 设置标题
			BufferedReader br = null;// 声明缓存输入流
			try {
				br = new BufferedReader(new FileReader(this.file));// 创建文件的输入流
				String str = null;
				textArea.setText("");// 清空文本
				while ((str = br.readLine()) != null) {
					textArea.append(str + "\n");// 追加写入到文本中
				}

			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();// 关闭流
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	public void Save() {
		String text = textArea.getText();// 获取文本域的内容
		if (text.isEmpty()) {// 如果为空
			JOptionPane.showMessageDialog(this, "没有需要保存的文本");
			return;
		} else {
			JFileChooser filechooser = new JFileChooser();// 创建文件选择框
			filechooser.setFileFilter(new FileNameExtensionFilter("java文档(*.java)", "java"));
			filechooser.setFileFilter(new FileNameExtensionFilter("文本文档(*.txt)", "txt"));// 设置文件过滤器
			
			filechooser.setCurrentDirectory(new File("."));//默认保存在，当前文件的目录
			int option = filechooser.showSaveDialog(this);// 打开文件选择框
			if (option == filechooser.APPROVE_OPTION) {// 判断用户单击是否为打开按钮
				File file = filechooser.getSelectedFile();// 获取用户选择的文件
				String fname=filechooser.getName(file);//从文件名输入框获取文件名
				//假如用户填写的文件名不带后缀名，默认添上后缀.txt
				if(fname.indexOf(".txt")==-1){
					file=new File(filechooser.getCurrentDirectory(),fname+".txt");
				}
				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new FileWriter(file));// 创建文件的缓存输出流
					bw.write(text);// 把文本保存导文件
					bw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					try {
						bw.close();// 关闭流
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			}
		}
		
	}
	
	//转到行
	public void Goto() {
		JDialog dialog = new JDialog(this, "转到指定行");
		JButton define = new JButton("转到");
		JButton off = new JButton("取消");
		dialog.getContentPane().setLayout(null);
		Container c = dialog.getContentPane();
		JLabel l = new JLabel("行号(L)：");
		JTextField field = new JTextField();
		l.setBounds(10, 12, 400, 30);
		field.setBounds(10, 42, 350, 28);
		define.setBounds(180, 84, 80, 28);
		define.setContentAreaFilled(false);
		off.setBounds(270, 84, 80, 28);
		off.setContentAreaFilled(false);

		dialog.setBounds(200, 200, 400, 160);
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		c.add(l);
		c.add(field);
		c.add(define);
		c.add(off);
		dialog.setVisible(true);
		define.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int r = textArea.getLineCount();// 确定行数
				String str[] = textArea.getText().split("\n");
				int count = 0;
				try {
					count = Integer.parseInt(field.getText().trim());
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "请输入数字！");
				}
				if (r >= count) {
					int sum = 0;
					for (int i = 0; i < count - 1; i++) {
						sum += str[i].length() + 1;
					}
					dialog.dispose();
					textArea.setCaretPosition(sum);
				} else {
					JOptionPane.showMessageDialog(null, "行数超过了总行数！");
				}
			}
		});
		off.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
	}
	
	//查找下一个
	public void FindNext() {
		String areastr = textArea.getText();// 获取文本区文本
		String fieldstr = textArea.getSelectedText();// 获取选中的文本
		if (fieldstr != null) {
			String toupparea = areastr.toUpperCase();// 转为大写，用做区分大小写判断方便查找
			String touppfield = fieldstr.toUpperCase();
			String A;// 用做查找的文本域内容
			String B;// 用作查找的文本框内容
			A = toupparea;
			B = touppfield;
			
			int n = textArea.getCaretPosition();// 获取光标的位置
			int m = 0;
			// 开始查找
			m = A.indexOf(B, n);
			if (m != -1) {
				textArea.setCaretPosition(m + fieldstr.length());
				textArea.select(m, m + fieldstr.length());
			} else {
				m = A.indexOf(B);// 从头开始找
				if (m != -1) {
					textArea.setCaretPosition(m + fieldstr.length());
					textArea.select(m, m + fieldstr.length());
				} else {
					JOptionPane.showMessageDialog(null, "找不到 “" + fieldstr + "“", "查找",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "请先选择字符，再进行查找操作！","查找下一个",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	//查找上一个
	public void FindLast() {
		String areastr = textArea.getText();// 获取文本区文本
		String fieldstr = textArea.getSelectedText();// 获取文本框文本
		if (fieldstr != null) {
			String toupparea = areastr.toUpperCase();// 转为大写，用做区分大小写判断方便查找
			String touppfield = fieldstr.toUpperCase();
			String A;// 用做查找的文本域内容
			String B;// 用作查找的文本框内容
			A = toupparea;
			B = touppfield;
			int n = textArea.getCaretPosition();// 获取光标的位置
			int m = 0;
			// 开始向上查找
			if (textArea.getSelectedText() == null) {
				m = A.lastIndexOf(B, n - 1);
			} else {
				m = A.lastIndexOf(B, n - fieldstr.length() - 1);
			}
			if (m != -1) {
				textArea.setCaretPosition(m);
				textArea.select(m, m + fieldstr.length());
			} else {
				m = A.lastIndexOf(B);// 从后面开始找
				if (m != -1) {
					textArea.setCaretPosition(m);
					textArea.select(m, m + fieldstr.length());
				} else {
					JOptionPane.showMessageDialog(null, "找不到 “" + fieldstr + "“", "查找",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "请先选择字符，再进行查找操作！","查找上一个",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	//设置背景颜色
	void showBackgroundColorDialog()
	{
		if(bcolorChooser==null)
			bcolorChooser=new JColorChooser();
		if(backgroundDialog==null)
			backgroundDialog=JColorChooser.createDialog
			(NotePadMain.this.frame,
					"背景颜色",
					false,
					bcolorChooser,
					new ActionListener()
			{public void actionPerformed(ActionEvent evvv){
				textArea.setBackground(bcolorChooser.getColor());}},
			null);		
		backgroundDialog.setVisible(true);
	}
	
	//设置字体颜色
	void showForegroundColorDialog()
	{
		if(fcolorChooser==null)
			fcolorChooser=new JColorChooser();
		if(foregroundDialog==null)
			foregroundDialog=JColorChooser.createDialog
			(NotePadMain.this.frame,
					"字体颜色", false,
					fcolorChooser,
					new ActionListener()
			{public void actionPerformed(ActionEvent evvv){
				textArea.setForeground(fcolorChooser.getColor());}},
			null);		
		foregroundDialog.setVisible(true);
	}
		
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NotePadMain frame = new NotePadMain();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);//在屏幕中居中显示
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}		

}

