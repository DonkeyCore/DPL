package me.donkeycore.dpl.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.Donkey.LogLevel;
import me.donkeycore.dpl.io.FileCreator;
import me.donkeycore.dpl.io.FileCreator.FileConfiguration;

/** GUI selection to select scripts and run them. Create a new GUI simply by using <code>new GUI();</code><br />
 * Automatically created by {@link Donkey} when given no file as an argument
 * @since 1.0
 * @see Donkey
 * @see JDialog
 * @see ActionListener
 * @see ScriptGUI#GUI() Constructor
 */
public class ScriptGUI extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
	private static final Point CENTER = new Point((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (WIDTH / 2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (HEIGHT / 2));
	private static final Point END = new Point((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
	
	private JButton exit, add, remove, run;
	private JList<String> list;
	private String value = null;
	private String[] data;
	private static GUIFrame frame = new GUIFrame();
	
	/** Create a new GUI menu 
	 * @since 1.0
	 * @see ScriptGUI
	 */
	public ScriptGUI(){
        super(frame, "Donkey Script Selector", true);
		setSize(WIDTH, HEIGHT);
		setLocation(CENTER);
        setResizable(false);
        setAlwaysOnTop(true);
        setAutoRequestFocus(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
        exit = new JButton("Exit");
        exit.addActionListener(this);
        
        run = new JButton("Run");
        run.setActionCommand("Run");
        run.addActionListener(this);
        getRootPane().setDefaultButton(run);
        
        add = new JButton("Add");
        add.setActionCommand("Add");
        add.addActionListener(this);
        
        remove = new JButton("Remove");
        remove.setActionCommand("Remove");
        remove.addActionListener(this);
        
		FileConfiguration fc = FileCreator.getFile("recentFiles", "log");
		if(fc.getFile().exists() && fc.getFile().length() > 0){
			String dat = "";
			String s = "";
			try{
				BufferedReader r = new BufferedReader(new FileReader(fc.getFile()));
				while((s = r.readLine()) != null){
					dat = (dat + ";" + s.replace("null", "")).trim();
				}
				
				r.close();
				dat = dat.replaceFirst(";", "");
				int c = dat.split(";").length;
				if(c < 5){
					for(int n = c; n < 5; n++)
						dat = dat + ";null";
				}
			}catch (IOException e) {
				Donkey.printError(e);
			}
			data = dat.split(";");
			for(int i = 0; i < data.length; i++){
				String d = data[i];
				if(d.equals("null"))
					data[i] = null;
			}
		}else
			data = new String[5];
        
        list = new JList<String>(data) {
			private static final long serialVersionUID = 1L;
			
            public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            	int row;
                if (orientation == SwingConstants.VERTICAL &&
                      direction < 0 && (row = getFirstVisibleIndex()) != -1) {
                    Rectangle r = getCellBounds(row, row);
                    if ((r.y == visibleRect.y) && (row != 0))  {
                        Point loc = r.getLocation();
                        loc.y--;
                        int prevIndex = locationToIndex(loc);
                        Rectangle prevR = getCellBounds(prevIndex, prevIndex);
 
                        if (prevR == null || prevR.y >= r.y) {
                            return 0;
                        }
                        return prevR.height;
                    }
                }
                return super.getScrollableUnitIncrement(
                                visibleRect, orientation, direction);
            }
        };
 
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	value = list.getSelectedValue();
                if (e.getClickCount() == 2) 
                    run.doClick();
            }
        });
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 100));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel("Recent scripts:");
        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
 
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(exit);
        buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPane.add(add);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(remove);
        buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPane.add(run);
        
        add(listPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
 
        setValue(null);
        pack();
		setVisible(true);
    }
	
	public void actionPerformed(ActionEvent event){
		Object source = event.getSource();
		if(source == exit){
			Donkey.log(LogLevel.DEBUG, "Shutting down...", "Donkey");
			System.exit(0);
		}else if(source == add){
			for(int i = 0; i < data.length; i++){
				String s = data[i];
				if(s == null || i == (data.length - 1)){
					data[i] = getNewValue();
					list.setListData(data);
					setValue(data[i]);
					break;
				}
			}
		}else if(source == remove){
			if(value == null || value.equals(""))
				return;
			
			boolean found = false;
			for(int i = 0; i < data.length; i++){
				String s = data[i];
				if(s != null && s.equals(value))
					found = true;
				else if(found)
					data[i - 1] = s;
			}
			data[data.length - 1] = null;
			list.setListData(data);
		}else if(source == run){
			if(value != null && !value.equals("")){
				setVisible(false);
				try {
					new Donkey(new java.io.File(value)).runCode();
				} catch (Throwable e) {
					display("An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}else
				display("You did not select a file!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
    private void setValue(String newValue) {
        value = newValue;
        list.setSelectedValue(value, true);
    }
	
	private String getNewValue(){
		setLocation(END);
		String s = JOptionPane.showInputDialog(frame, "File name:", "Add new file", JOptionPane.INFORMATION_MESSAGE);
		setLocation(CENTER);
		return s;
	}
	
	private void display(String message, String title, int type){
		setVisible(false);
		JOptionPane.showMessageDialog(frame, message, title, type);
		setVisible(true);
		setLocation(CENTER);
	}
	
	private static class GUIFrame extends JFrame{
        private static final long serialVersionUID = 1L;
		private GUIFrame(){
			setAlwaysOnTop(true);
        	setResizable(false);
        	setDefaultCloseOperation(EXIT_ON_CLOSE);
        	setVisible(false);
        }
	}
	
}