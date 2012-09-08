package verilog.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import verilog.awt.Model;
import verilog.awt.ModelPopupMenu;

public class MainMenu {
    Model model = null;
    JMenuBar menuBar;
    
    /**
     * @return the menuBar
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * @param menuBar the menuBar to set
     */
    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    /**
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }

    private static ResourceBundle verilogGui = ResourceBundle
	    .getBundle("verilog_gui");

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
	java.net.URL imgURL = ModelPopupMenu.class.getResource(path);
	if (imgURL != null) {
	    return new ImageIcon(imgURL);
	} else {
	    System.err.println("Couldn't find file: " + path);
	    return null;
	}
    }
    
    public MainMenu() {
	
	JMenu menu, submenu;
	JMenuItem menuItem;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;

	// Create the menu bar.
	menuBar = new JMenuBar();

	// Build the first menu.
	menu = new JMenu(verilogGui.getString("menu_main_file"));
	menu.setMnemonic(KeyEvent.VK_F);
	menu.getAccessibleContext().setAccessibleDescription(
		"The only menu in this program that has menu items");
	menuBar.add(menu);

	// a group of JMenuItems
	menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
	// menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.ALT_MASK));
	menuItem.getAccessibleContext().setAccessibleDescription(
		"This doesn't really do anything");
	menuItem.addActionListener(new ActionListener() {
	    
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	menu.add(menuItem);

	ImageIcon icon = createImageIcon("images/1.gif");
	menuItem = new JMenuItem("Both text and icon", icon);
	menuItem.setMnemonic(KeyEvent.VK_B);
	menuItem.addActionListener(new ActionListener() {
	    
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	menu.add(menuItem);

	menuItem = new JMenuItem(icon);
	menuItem.setMnemonic(KeyEvent.VK_D);
	menuItem.addActionListener(new ActionListener() {
	    
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	menu.add(menuItem);

	// a group of radio button menu items
	menu.addSeparator();
	ButtonGroup group = new ButtonGroup();

	rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
	rbMenuItem.setSelected(true);
	rbMenuItem.setMnemonic(KeyEvent.VK_R);
	group.add(rbMenuItem);
	rbMenuItem.addActionListener(new ActionListener() {
	    
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	menu.add(rbMenuItem);

	rbMenuItem = new JRadioButtonMenuItem("Another one");
	rbMenuItem.setMnemonic(KeyEvent.VK_O);
	group.add(rbMenuItem);
	rbMenuItem.addActionListener(new ActionListener() {
	    
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	menu.add(rbMenuItem);

	// a group of check box menu items
	menu.addSeparator();
	cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
	cbMenuItem.setMnemonic(KeyEvent.VK_C);
	cbMenuItem.addItemListener(new ItemListener() {
	    
	    public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	menu.add(cbMenuItem);

	cbMenuItem = new JCheckBoxMenuItem("Another one");
	cbMenuItem.setMnemonic(KeyEvent.VK_H);
	cbMenuItem.addItemListener(new ItemListener() {
	    
	    public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	menu.add(cbMenuItem);

	// a submenu
	menu.addSeparator();
	submenu = new JMenu("A submenu");
	submenu.setMnemonic(KeyEvent.VK_S);

	menuItem = new JMenuItem("An item in the submenu");
	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
		ActionEvent.ALT_MASK));
	menuItem.addActionListener(new ActionListener() {
	    
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	submenu.add(menuItem);

	menuItem = new JMenuItem("Another item");
	menuItem.addActionListener(new ActionListener() {
	    
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	submenu.add(menuItem);
	menu.add(submenu);

	// Build second menu in the menu bar.
	menu = new JMenu("Another Menu");
	menu.setMnemonic(KeyEvent.VK_N);
	menu.getAccessibleContext().setAccessibleDescription(
		"This menu does nothing");
	menuBar.add(menu);

 
    }

    public static JMenuBar createMainMenu(Model model) {
	MainMenu mainMenu = new MainMenu();
	mainMenu.setModel(model);
	return mainMenu.getMenuBar();

	//
    }
}
