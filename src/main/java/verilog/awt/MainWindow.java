package verilog.awt;

 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.javadocking.DockingManager;
import com.javadocking.dock.FloatDock;
import com.javadocking.dock.Position;
import com.javadocking.dock.SplitDock;
import com.javadocking.dock.TabDock;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockingMode;
import com.javadocking.dockable.DraggableContent;
import com.javadocking.drag.DragListener;
import com.javadocking.model.FloatDockModel;

/**
 * This example shows 5 windows. A dockable is created for every window. 4 dockables are docked in  
 * tab docks. The tab docks are docked in split docks. 1 dockable is floating.
 * The dockables can be moved around by dragging the tabs and by dragging their content.
 * 
 * @author Heidi Rakels
 */
public class MainWindow extends JPanel
{

	// Static fields.

	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 450;

	// Constructor.

	public MainWindow(JFrame frame)
	{
		super(new BorderLayout());
		WindowUtilities.setNativeLookAndFeel();
		// Create the dock model for the docks.
		FloatDockModel dockModel = new FloatDockModel();
		dockModel.addOwner("frame0", frame);

		// Give the dock model to the docking manager.
		DockingManager.setDockModel(dockModel);

		// Create the content components.
		TextPanel textPanel1 = new TextPanel("I am window 1.");
		TextPanel textPanel2 = new TextPanel("I am window 2.");
		TextPanel textPanel3 = new TextPanel("I am window 3.");
		TextPanel textPanel4 = new TextPanel("I am window 4.");
		TextPanel textPanel5 = new TextPanel("I am window 5.");

		
		Model mainFrame1 = new Model();
		mainFrame1.setVisible(true);
		
		Model mainFrame2 = new Model();
		mainFrame2.setVisible(true);
		
		
		// Create the dockables around the content components.
		Dockable dockable1 = new DefaultDockable("Window1", mainFrame1, "Window 1", null, DockingMode.ALL);
		Dockable dockable2 = new DefaultDockable("Window2", mainFrame2, "Window 2", null, DockingMode.ALL);
		Dockable dockable3 = new DefaultDockable("Window3", textPanel3, "Window 3", null, DockingMode.ALL);
		Dockable dockable4 = new DefaultDockable("Window4", textPanel4, "Window 4", null, DockingMode.ALL);
		Dockable dockable5 = new DefaultDockable("Window5", textPanel5, "Window 5", null, DockingMode.ALL);

		
		
		// Create the tab docks.
		TabDock topTabDock = new TabDock();
		TabDock bottomTabDock = new TabDock();
		TabDock rightTabDock = new TabDock();

		// Add the dockables to these tab docks.
		topTabDock.addDockable(dockable1, new Position(0));
		topTabDock.addDockable(dockable2, new Position(1));
		bottomTabDock.addDockable(dockable3, new Position(0));
		rightTabDock.addDockable(dockable4, new Position(0));

		// The windows of the tab docks should be able to split.
		// Put the tab docks in split docks.
		SplitDock topSplitDock = new SplitDock();
		topSplitDock.addChildDock(topTabDock, new Position(Position.CENTER));
		//topSplitDock.addChildDock(bottomTabDock, new Position(Position.LEFT));
		SplitDock bottomSplitDock = new SplitDock();
		bottomSplitDock.addChildDock(bottomTabDock, new Position(Position.CENTER));
		SplitDock rightSplitDock = new SplitDock();
		rightSplitDock.addChildDock(rightTabDock, new Position(Position.CENTER));
		
		// Add the 3 root docks to the dock model.
		dockModel.addRootDock("topdock", topSplitDock, frame);
		dockModel.addRootDock("bottomdock", bottomSplitDock, frame);
		dockModel.addRootDock("rightdock", rightSplitDock, frame);
			
		// Dockable 5 should float. Add dockable 5 to the float dock of the dock model (
		// The float dock is a default root dock of the FloatDockModel.
		FloatDock floatDock = dockModel.getFloatDock(frame);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		floatDock.addDockable(dockable5, new Point(screenSize.width / 2, screenSize.height / 2), new Point());

		// Create the split panes.
		JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftSplitPane.setDividerLocation(250);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(400);

		// Add the root docks to the split panes.
		leftSplitPane.setLeftComponent(topSplitDock);
		leftSplitPane.setRightComponent(bottomSplitDock);
		splitPane.setLeftComponent(leftSplitPane);
		splitPane.setRightComponent(rightSplitDock);
		
		// Add the split pane to the panel.
		add(splitPane, BorderLayout.CENTER);
		
	}
	
	/**
	 * This is the class for the content.
	 */
	private class TextPanel extends JPanel implements DraggableContent
	{
		
		private JLabel label; 
		
		public TextPanel(String text)
		{
			super(new FlowLayout());
			
			// The panel.
			setMinimumSize(new Dimension(80,80));
			setPreferredSize(new Dimension(150,150));
			setBackground(Color.white);
			setBorder(BorderFactory.createLineBorder(Color.lightGray));
			
			// The label.
			label = new JLabel(text);
			label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			add(label);
		}
		
		// Implementations of DraggableContent.

		public void addDragListener(DragListener dragListener)
		{
			addMouseListener(dragListener);
			addMouseMotionListener(dragListener);
			label.addMouseListener(dragListener);
			label.addMouseMotionListener(dragListener);
		}
	}

	// Main method.
	
	public static void createAndShowGUI()
	{
		
		// Create the frame.
		JFrame frame = new JFrame("First Example");

		// Create the panel and add it to the frame.
		MainWindow panel = new MainWindow(frame);
		frame.getContentPane().add(panel);
		
		// Set the frame properties and show it.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width - FRAME_WIDTH) / 2, (screenSize.height - FRAME_HEIGHT) / 2);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setVisible(true);
		
	}

	public static void main(String args[]) 
	{
        Runnable doCreateAndShowGUI = new Runnable() 
        {
            public void run() 
            {
                createAndShowGUI();
            }
        };
        SwingUtilities.invokeLater(doCreateAndShowGUI);
    }
	
}

/*


 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import com.javadocking.DockingManager;
import com.javadocking.dock.FloatDock;
import com.javadocking.dock.Position;
import com.javadocking.dock.SplitDock;
import com.javadocking.dock.TabDock;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockingMode;
import com.javadocking.dockable.DraggableContent;
import com.javadocking.drag.DragListener;
import com.javadocking.model.FloatDockModel;

class PIC extends JScrollPane implements MouseListener,MouseMotionListener
{

	*//**
	 * 
	 *//*
	private static final long	serialVersionUID	= 1L;

	public PIC()
	{
		super();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		// TODO Auto-generated constructor stub
			this.addMouseListener(new MouseListener() {
		
        public void mouseClicked(MouseEvent e) {
            System.out.println(e.getPoint());
        }


        public void mousePressed(MouseEvent e) {
        }

   
        public void mouseReleased(MouseEvent e) {
        }

 
        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    });
	}

	public PIC(Component view, int vsbPolicy, int hsbPolicy)
	{
		super(view, vsbPolicy, hsbPolicy);
		// TODO Auto-generated constructor stub
	}

	public PIC(Component view)
	{
		super(view);
		// TODO Auto-generated constructor stub
	}

	public PIC(int vsbPolicy, int hsbPolicy)
	{
		super(vsbPolicy, hsbPolicy);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics g)
	{
		// Get the current size of this component
		Dimension d = this.getSize();

		// draw in black
		g.setColor(Color.BLACK);
		// draw a centered horizontal line
		g.drawLine(0, d.height / 2, d.width, d.height / 2);

	}

	public void mousePressed(MouseEvent ev)
	{
		System.out.println("Event mouse position: " + ev.getPoint());
		System.out.println("Panel mouse position: " + this.getMousePosition());
	}
	public void mouseClicked(MouseEvent ev)
	{
		// TODO Auto-generated method stub
		System.out.println("Event mouse position: " + ev.getPoint());
		System.out.println("Panel mouse position: " + this.getMousePosition());
	}

	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent ev)
	{
		// TODO Auto-generated method stub
		System.out.println("Event mouse position: " + ev.getPoint());
		System.out.println("Panel mouse position: " + this.getMousePosition());
	}

	public void mouseDragged(MouseEvent ev)
	{
		// TODO Auto-generated method stub
		System.out.println("dragg !!!!!!!!!!!!!!! " + ev.getPoint());
		//System.out.println("Panel mouse position: " + this.getMousePosition());
	}

	public void mouseMoved(MouseEvent ev)
	{
		// TODO Auto-generated method stub
		System.out.println("movveee !!!!!!!!!!!!!!! " + ev.getPoint());
	}

}

// SplitPaneDemo itself is not a visible component.
public class MainWindow extends JPanel implements ListSelectionListener
{
	private JLabel		picture;
	private JList		list;
	private JSplitPane	splitPane;
	private String[]	imageNames	= { "Bird", "Cat", "Dog", "Rabbit", "Pig", "dukeWaveRed", "kathyCosmo", "lainesTongue", "left", "middle", "right", "stickerface" };

	public MainWindow()
	{

		Model mainFrame = new Model();
		mainFrame.setVisible(true);
		
		// Create the list of images and put it in a scroll pane.
		WindowUtilities.setNativeLookAndFeel();
		list = new JList(imageNames);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);

		JScrollPane listScrollPane = new JScrollPane(list);
		picture = new JLabel();
		picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
		picture.setHorizontalAlignment(JLabel.CENTER);

		PIC pictureScrollPane = new PIC();

		JSplitPane  splitListandTree =  new JSplitPane(JSplitPane.VERTICAL_SPLIT, listScrollPane, picture);
		// Create a split pane with the two scroll panes in it.
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitListandTree, mainFrame);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		// Provide minimum sizes for the two components in the split pane.
		Dimension minimumSize = new Dimension(100, 50);
		listScrollPane.setMinimumSize(minimumSize);
		pictureScrollPane.setMinimumSize(minimumSize);

		// Provide a preferred size for the split pane.
		splitPane.setPreferredSize(new Dimension(400, 200));
		updateLabel(imageNames[list.getSelectedIndex()]);
	}

	private DefaultDockingPort buildDockingPort(Color color, String desc) {
	    // create the DockingPort
	    DefaultDockingPort port = createDockingPort();
	    
	    // create and register the Dockable panel
	    JPanel p = new JPanel();
	    p.setBackground(color);
	    DockingManager.registerDockable(p, desc, true);
	     
	    // dock the panel and return the DockingPort
	    port.dock(p, desc, DockingPort.CENTER_REGION, false);
	    return port;
	  }
	
	// Listens to the list
	public void valueChanged(ListSelectionEvent e)
	{
		JList list = (JList) e.getSource();
		updateLabel(imageNames[list.getSelectedIndex()]);
	}

	// Renders the selected image
	protected void updateLabel(String name)
	{
		ImageIcon icon = createImageIcon("images/" + name + ".gif");
		picture.setIcon(icon);
		if (icon != null)
		{
			picture.setText(null);
		}
		else
		{
			picture.setText("Image not found");
		}
	}

	// Used by SplitPaneDemo2
	public JList getImageList()
	{
		return list;
	}

	public JSplitPane getSplitPane()
	{
		return splitPane;
	}

	*//** Returns an ImageIcon, or null if the path was invalid. *//*
	protected static ImageIcon createImageIcon(String path)
	{
		java.net.URL imgURL = MainWindow.class.getResource(path);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL);
		}
		else
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	*//**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 *//*
	private static void createAndShowGUI()
	{

		
		
		// Create and set up the window.
		JFrame desktop = new JFrame("SplitPaneDemo");
		desktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainWindow splitPaneDemo = new MainWindow();
		 desktop.getContentPane().add(splitPaneDemo.getSplitPane());
		 

		// add toolbar
		JToolBar tbar = new JToolBar();
		tbar.add(new JButton(IconUtils.getGeneralIcon("Open", 16)));
		tbar.add(new JButton("B"));
		tbar.add(new JButton("C"));
		desktop.getContentPane().setLayout(new BorderLayout());
		desktop.getContentPane().add(tbar, BorderLayout.NORTH);
		tbar.setFloatable(true);
		    
		
		// WindowUtilities.setMotifLookAndFeel();
		// Display the window.
		desktop.pack();
		desktop.setVisible(true);

		
		 * for(int i=0; i<5; i++) { JInternalFrame frame = new
		 * JInternalFrame(("Internal Frame " + i), true, true, true, true);
		 * frame.setLocation(i*50+10, i*50+10); frame.setSize(200, 150);
		 * frame.setBackground(Color.white); desktop.add(frame);
		 * frame.moveToFront(); // Graphics g = new Graphics()
		 * 
		 * frame.setVisible(true); }
		 
	}

	public static void main(String[] args)
	{
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		});
	}

}*/
