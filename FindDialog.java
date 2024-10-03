package com.cqupt.Notepad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class FindDialog extends JPanel implements ActionListener
{

	JTextArea jta;//目标文本区域
	public int lastIndex;//最后查找位置
	JLabel replaceLabel;

	private TextField findWhat;
	private JTextField replaceWith;

	private JCheckBox matchCase;//区分大小写

	JRadioButton up, down;

	JButton findNextButton, replaceButton, replaceAllButton, cancelButton;

	JPanel direction,buttonPanel, findButtonPanel, replaceButtonPanel;
	CardLayout card;

	private boolean ok;//是否成功标志
	private JDialog dialog;
	
	public FindDialog(JTextArea jta)
	{

		this.jta=jta;//目标文本区域
		findWhat=new TextField(20);
		replaceWith=new JTextField(20);

		matchCase=new JCheckBox("区分大小写");

		up=new JRadioButton("向上");
		down=new JRadioButton("向下");

		down.setSelected(true);
		ButtonGroup bg=new ButtonGroup();
		bg.add(up);
		bg.add(down);

		direction=new JPanel();
		Border etched=BorderFactory.createEtchedBorder();
		Border titled=BorderFactory.createTitledBorder(etched,"方向");
		direction.setBorder(titled);
		direction.setLayout(new GridLayout(1,2));
		direction.add(up);
		direction.add(down);

		JPanel southPanel=new JPanel();
		southPanel.setLayout(new GridLayout(1,2));
		southPanel.add(matchCase);
		southPanel.add(direction);


		findNextButton=new JButton("查找下一个");
		replaceButton=new JButton("替换");
		replaceAllButton=new JButton("全部替换");
		cancelButton=new JButton("取消");

		replaceButtonPanel=new JPanel();
		replaceButtonPanel.setLayout(new GridLayout(4,1));
		replaceButtonPanel.add(findNextButton);
		replaceButtonPanel.add(replaceButton);
		replaceButtonPanel.add(replaceAllButton);
		replaceButtonPanel.add(cancelButton);

		JPanel textPanel=new JPanel();
		textPanel.setLayout(new GridLayout(3,2));
		textPanel.add(new JLabel("查找内容为： "));
		textPanel.add(findWhat);
		textPanel.add(replaceLabel=new JLabel("替换为： "));
		textPanel.add(replaceWith);
		textPanel.add(new JLabel(" "));
		textPanel.add(new JLabel(" "));

		setLayout(new BorderLayout());

		add(new JLabel("       "),BorderLayout.NORTH);
		add(textPanel,BorderLayout.CENTER);
		add(replaceButtonPanel,BorderLayout.EAST);
		add(southPanel,BorderLayout.SOUTH);

		setSize(200,200);

		findNextButton.addActionListener(this);
		replaceButton.addActionListener(this);
		replaceAllButton.addActionListener(this);

		//取消按钮事件
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{dialog.setVisible(false);}
		});

		//查找输入框
		findWhat.addFocusListener(new FocusAdapter()
		{
			public void focusLost(FocusEvent te){
				enableDisableButtons();}
		});
		//查找输入框文本变化事件
		findWhat.addTextListener(new TextListener()
		{
			public void textValueChanged(TextEvent te){
				enableDisableButtons();}
		});

	}
	
	//根据不同情况，启用或禁用按钮
	void enableDisableButtons()
	{
		if(findWhat.getText().length()==0)
		{
			findNextButton.setEnabled(false);
			replaceButton.setEnabled(false);
			replaceAllButton.setEnabled(false);
		}
		else
		{
			findNextButton.setEnabled(true);
			replaceButton.setEnabled(true);
			replaceAllButton.setEnabled(true);
		}
	}
	//设置响应事件
	public void actionPerformed(ActionEvent ev)
	{
		if(ev.getSource()==findNextButton)
			findNextWithSelection();
		else if(ev.getSource()==replaceButton)
			replaceNext();
		else if(ev.getSource()==replaceAllButton)
			JOptionPane.showMessageDialog(null,"替换总数= "+replaceAllNext());
	}
	
	int findNext()
	{
		String s1=jta.getText();
		String s2=findWhat.getText();

		lastIndex=jta.getCaretPosition();

		int selStart=jta.getSelectionStart();
		int selEnd=jta.getSelectionEnd();

		if(up.isSelected())
		{
			if(selStart!=selEnd)
				lastIndex=selEnd-s2.length()-1;

			if(!matchCase.isSelected())
				lastIndex=s1.toUpperCase().lastIndexOf(s2.toUpperCase(),lastIndex);
			else
				lastIndex=s1.lastIndexOf(s2,lastIndex);	
		}
		else
		{
			if(selStart!=selEnd)
				lastIndex=selStart+1;
			if(!matchCase.isSelected())
				lastIndex=s1.toUpperCase().indexOf(s2.toUpperCase(),lastIndex);
			else
				lastIndex=s1.indexOf(s2,lastIndex);	
		}

		return lastIndex;
	}
	
	public void findNextWithSelection()
	{
		int idx=findNext();
		if(idx!=-1)
		{
			jta.setSelectionStart(idx);
			jta.setSelectionEnd(idx+findWhat.getText().length());
			}
		else
			JOptionPane.showMessageDialog(this,
					"找不到"+" \""+findWhat.getText()+"\"",
					"查找",JOptionPane.INFORMATION_MESSAGE);
	}
	
	void replaceNext()
	{
		// 没有选中内容
		if(jta.getSelectionStart()==jta.getSelectionEnd()) 
		{findNextWithSelection();return;}

		String searchText=findWhat.getText();
		String temp=jta.getSelectedText();	//获取选中内容

		//检查选中内容是否与查找内容匹配，如果匹配则替换

		if((matchCase.isSelected() && temp.equals(searchText))||
			(!matchCase.isSelected() && temp.equalsIgnoreCase(searchText)))
			jta.replaceSelection(replaceWith.getText());

		findNextWithSelection();
	}
	
	int replaceAllNext()
	{
		if(up.isSelected())
			jta.setCaretPosition(jta.getText().length()-1);
		else
			jta.setCaretPosition(0);
		int idx=0;
		int counter=0;//计数器
		do
		{
			idx=findNext();
			if(idx==-1) break;
			counter++;
			jta.replaceRange(replaceWith.getText(),idx,idx+findWhat.getText().length());
		}while(idx!=-1);
		return counter;
	}
	
	public boolean showDialog(Component parent, boolean isFind )
	{

		Frame owner=null;
		if(parent instanceof Frame) 
			owner=(Frame)parent;
		else
			owner=(Frame)SwingUtilities.getAncestorOfClass(Frame.class,parent);
		if(dialog==null || dialog.getOwner()!=owner)
		{
			dialog=new JDialog(owner,false);
			dialog.getContentPane().add(this);
			dialog.getRootPane().setDefaultButton(findNextButton);
		}

		if(findWhat.getText().length()==0)
			findNextButton.setEnabled(false);
		else
			findNextButton.setEnabled(true);

		replaceButton.setVisible(false);
		replaceAllButton.setVisible(false);
		replaceWith.setVisible(false);
		replaceLabel.setVisible(false);

		if(isFind)
		{
			dialog.setSize(460,180);
			dialog.setLocationRelativeTo(null);
			dialog.setTitle("查找");
		}
		else
		{
			replaceButton.setVisible(true);
			replaceAllButton.setVisible(true);
			replaceWith.setVisible(true);
			replaceLabel.setVisible(true);

			dialog.setSize(450,200);
			dialog.setLocationRelativeTo(null);
			dialog.setTitle("替换");
		}

		dialog.setVisible(true);
		return ok;
	}
}
