/**
 *
 *  @author Mróz Marcin
 *
 */

package ex4;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

public class Main extends JFrame{
	
	private JTextArea ta = new JTextArea(30,40);
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "Untitled";
	private boolean changed = false;
	private static HashMap map = new HashMap();
	
	static{
		map.put("Home", "ul. Miodowa 12");
		map.put("School", "ul. Nowowiejska 24");
		map.put("The company", "ul. Potocka 13");
	}
	
	public Main() {
		
		int[] fontsizes = {8, 10, 12, 14, 16, 18, 20, 22, 24};
		Color[] colors = {Color.BLUE, Color.YELLOW, Color.ORANGE,Color.RED, Color.WHITE, Color.BLACK, Color.GREEN};
		String[] names = {"Blue", "Yellow", "Orange", "Red", "White", "Black", "Green"};
	    
	    JMenu file = new JMenu("File");
	    file.add(mi(Open, 'o', "control O" ));
	    file.addSeparator();
	    file.add(mi(Save, 's', "control S"));
	    file.addSeparator();
	    file.add(mi(SaveAs, 'a', "control A"));
	    file.addSeparator();
	    RedDash d = new RedDash(100, 3);
	    JLabel l = new JLabel();
	    l.setPreferredSize(new Dimension(2,10));
	    l.setIcon(d);
	    file.add(l);
	    file.addSeparator();
	    file.add(mi(Exit, 'x', "control X"));
	    	      
	    JMenu edit = new JMenu("Edit");
	    JMenu ads = new JMenu("Adresses");
	    
	    ads.add(mi(Home, 'h', "control shift H"));
	    ads.add(mi(School, 'c', "control shift C"));
	    ads.add(mi(TheCompany, 't', "control shift T"));
	    
	    edit.add(ads);
	    
	    JMenu ops = new JMenu("Options");
	    JMenu fg = new JMenu("Foreground");
	    JMenu bg = new JMenu("Background");
	    JMenu fonts = new JMenu("Fonts");
	    
	    for (int i = 0; i<colors.length; i++){
	    	JMenuItem m = new JMenuItem(changeFgColor(colors[i], names[i]));
	    	m.setIcon(new IconOval(15,15,colors[i]));
	    	fg.add(m);
	    }
	    
	    for (int i = 0; i<colors.length; i++){
	    	JMenuItem m = new JMenuItem(changeBgColor(colors[i], names[i]));
	    	m.setIcon(new IconOval(15,15,colors[i]));
	    	bg.add(m);
	    }
	    
	    for(int i = 0; i<fontsizes.length; i++){
	    	fonts.add(new JMenuItem(changeFontSize(fontsizes[i])));
	    }
	    
	    ops.add(fg);
	    ops.add(bg);
	    ops.add(fonts);
	    
	    JMenuBar mb = new JMenuBar();
	    mb.add(file);
	    mb.add(edit);
	    mb.add(ops);
	    setJMenuBar(mb);
	    
	    add(new JScrollPane(ta));

	    
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack();
	    setTitle(currentFile);
	    ta.addKeyListener(k1);
	    setLocationRelativeTo(null);
	    setVisible(true);
	  }
	
	
	JMenuItem mi(Action t, int mnemo, String accel) {
		  JMenuItem mi = new JMenuItem(t);
		  mi.setMnemonic(mnemo);               
		  mi.setAccelerator(KeyStroke.getKeyStroke(accel)); 
		  return mi;
		}
	
	Action changeFontSize(int size){
		Action a = new AbstractAction(size+" pts"){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Font f2 = new Font(ta.getFont().getFontName(), ta.getFont().getStyle(), size);
				ta.setFont(f2);
			}
			
		};
		return a;
	}
	
	Action changeBgColor(Color color, String name){
		Action a = new AbstractAction(name){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ta.setBackground(color);
			}
			
		};
		return a;
	}
	
	Action changeFgColor(Color color, String name){
		Action a = new AbstractAction(name){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ta.setForeground(color);
			}
			
		};
		return a;
	}
	
	Action Home = new AbstractAction("Home"){
		public void actionPerformed(ActionEvent e){
			ta.insert((String) map.get("Home"), ta.getCaretPosition());
		}
	};
	
	Action School = new AbstractAction("School"){
		public void actionPerformed(ActionEvent e){
			ta.insert((String) map.get("School"), ta.getCaretPosition());
		}
	};
	
	Action TheCompany = new AbstractAction("The Company"){
		public void actionPerformed(ActionEvent e){
			ta.insert((String) map.get("The company"), ta.getCaretPosition());
		}
	};
	
	Action Open = new AbstractAction("Open") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
		}
	};
	
	Action Save = new AbstractAction("Save") {
		public void actionPerformed(ActionEvent e) {
			if(!currentFile.equals("Untitled"))
				saveFile(currentFile);
			else
				saveFileAs();
		}
	};
	
	Action SaveAs = new AbstractAction("Save As...") {
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};
	
	Action Exit = new AbstractAction("Exit") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};
	
	private KeyListener k1 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true;
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
		}
	};
	
	private void saveFileAs() {
		if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}
	
	private void saveOld() {
		if(changed) {
			if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +" ?","Save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
				saveFile(currentFile);
		}
	}
	
	private void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			ta.write(w);
			w.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
			Save.setEnabled(false);
		}
		catch(IOException e) {
		}
	}
	
	private void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			ta.read(r,null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
		}
		catch(IOException e) {
			JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
		}
	}


public static void main(String[] args) {
	new Main();}
  }

