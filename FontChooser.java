package com.cqupt.lyy.Notepad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class FontChooser extends JPanel
{
	private Font thisFont;
	private JList jFace, jStyle, jSize;

	private JDialog dialog;
	private JButton okButton;

	JTextArea tf;

	private boolean ok;// 操作成功标志

	public FontChooser(Font withFont)
	{
		thisFont=withFont;//目前字体


		String[] fontNames=
				GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();//获取可用字体名称
		jFace=new JList(fontNames); 
		jFace.setSelectedIndex(0);// 默认选择第一个字体

		jFace.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent ev){
				tf.setFont(createFont());}// 更新预览字体
			});

		String[] fontStyles={"常规","斜体","加粗","加粗+斜体"};
		jStyle=new JList(fontStyles);
		jStyle.setSelectedIndex(0); 

		jStyle.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent ev){
				tf.setFont(createFont());}// 更新预览字体
			});

		String[] fontSizes=new String[30];
		for(int j=0; j<30; j++)
			fontSizes[j]=new String(10+j*2+"");
		jSize=new JList(fontSizes); jSize.setSelectedIndex(0); 

		jSize.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent ev){
				tf.setFont(createFont());}// 更新预览字体
			});

		JPanel jpLabel=new JPanel();
		jpLabel.setLayout(new GridLayout(1,3));

		jpLabel.add(new JLabel("字体",JLabel.CENTER));
		jpLabel.add(new JLabel("字体风格",JLabel.CENTER));
		jpLabel.add(new JLabel("大小",JLabel.CENTER));

		JPanel jpList=new JPanel();
		jpList.setLayout(new GridLayout(1,3));

		jpList.add(new JScrollPane(jFace));
		jpList.add(new JScrollPane(jStyle));
		jpList.add(new JScrollPane(jSize));

		okButton=new JButton("确认");
		JButton cancelButton=new JButton("取消");

		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				ok=true;
				FontChooser.this.thisFont=FontChooser.this.createFont();
				dialog.setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				dialog.setVisible(false);
			}
		});

		JPanel jpButton=new JPanel();
		jpButton.setLayout(new FlowLayout());
		jpButton.add(okButton);
		jpButton.add(new JLabel("          "));//占位标签
		jpButton.add(cancelButton);

		tf=new JTextArea(5,30);
		JPanel jpTextField=new JPanel();
		jpTextField.add(new JScrollPane(tf));

		JPanel centerPanel=new JPanel();
		centerPanel.setLayout(new GridLayout(2,1));
		centerPanel.add(jpList);
		centerPanel.add(jpTextField);

		setLayout(new BorderLayout());
		add(jpLabel,BorderLayout.NORTH);
		add(centerPanel,BorderLayout.CENTER);
		add(jpButton,BorderLayout.SOUTH);
		add(new JLabel("  "),BorderLayout.EAST);//占位标签
		add(new JLabel("  "),BorderLayout.WEST);//占位标签
			
		tf.setFont(thisFont);
		tf.append("\n字体选择演示");
		tf.append("\n0123456789");
		tf.append("\n~!@#$%^&*()_+|?><\n");
	}

	public Font createFont()
	{
		Font fnt=thisFont;
		int fontstyle=Font.PLAIN;
		int x=jStyle.getSelectedIndex();

		switch(x)
		{
		case 0:
			fontstyle=Font.PLAIN;	break;
		case 1:
			fontstyle=Font.ITALIC;	break;
		case 2:
			fontstyle=Font.BOLD;	break;
		case 3:
			fontstyle=Font.BOLD+Font.ITALIC;	break;
		}

		int fontsize=Integer.parseInt((String)jSize.getSelectedValue());
		String fontname=(String)jFace.getSelectedValue();

		fnt=new Font(fontname,fontstyle,fontsize);

		return fnt;

	}

	public boolean showDialog(Component parent, String title)
	{
		ok=false;

		Frame owner=null;
		if(parent instanceof Frame) 
			owner=(Frame)parent;
		else
			owner=(Frame)SwingUtilities.getAncestorOfClass(Frame.class,parent);
		if(dialog==null || dialog.getOwner()!=owner)
		{
			dialog=new JDialog(owner,true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.setSize(400,325);
			dialog.setLocationRelativeTo(null);//居中显示
		}

		dialog.setTitle(title);
		dialog.setVisible(true);
		return ok;
	}

}

