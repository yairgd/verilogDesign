package verilog.awt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.DefaultMenuLayout;

import verilog.manager.FileToolBar;
import verilog.manager.MainMenu;
import verilog.manager.ViewToolBar;


public class Model extends JPanel implements MouseListener, MouseMotionListener
{
	/**
     * 
     */
	Rectangle					rect				= null;
	private static final long	serialVersionUID	= 1L;
	private Dimension			area;						// indicates area
															// taken up by
															// graphics
	private DrawingPane1		drawingPanel;
	private Point				origin;
	private final JScrollPane	scroller;

	/**
	 * @return the drawingPane
	 */
	public DrawingPane1 getDrawingPanel()
	{
		return drawingPanel;
	}

	/**
	 * @param drawingPane
	 *            the drawingPane to set
	 */
	public void setDrawingPanel(DrawingPane1 drawingPane)
	{
		this.drawingPanel = drawingPane;
	}

	public Model()
	{
		super(new BorderLayout());
		area = new Dimension(0, 0);
		rect = new Rectangle(0, 0, getWidth(), getHeight());

		// Set up the drawing area.
		drawingPanel = new DrawingPane1();
		drawingPanel.setBackground(Color.white);
		drawingPanel.addMouseListener(this);
		drawingPanel.addMouseMotionListener(this);
		drawingPanel.setPreferredSize(area);
		drawingPanel.scrollRectToVisible(rect);

		scroller = new JScrollPane(drawingPanel);

		scroller.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener()
		{

			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				rect.x = scroller.getHorizontalScrollBar().getValue();
			}
		});

		scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
		{

			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				rect.y = scroller.getVerticalScrollBar().getValue();
			}
		});

		add(scroller, BorderLayout.CENTER);
		/*
		 * getLayout().addLayoutComponent("North",ViewToolBar.createViewToolBar(this
		 * ));
		 * getLayout().addLayoutComponent("North",FileToolBar.createFileBar(this
		 * ));
		 */
		add(ViewToolBar.createViewToolBar(this), BorderLayout.NORTH);
		add(FileToolBar.createFileBar(this), BorderLayout.SOUTH);
	}

	public void setHand(boolean b)
	{
		drawingPanel.setEnabled(b);
	}

	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e)
	{
		origin = e.getPoint();
		rect.x = scroller.getHorizontalScrollBar().getValue();
		rect.y = scroller.getVerticalScrollBar().getValue();
	}

	public void mouseReleased(MouseEvent e)
	{
		boolean change = false;
		for (Shape shape : drawingPanel.shapeList.shapes)
		{
			if (shape instanceof Block)
			{
				Polygon polygon = (Polygon) shape;
				if (area.width < polygon.getMinX())
				{
					area.width = polygon.getMinX() + ((Block) shape).getSize().x + 100;
					change = true;
				}
				if (area.height < polygon.getMinY())
				{
					area.height = polygon.getMinY() + ((Block) shape).getSize().y + 100;
					change = true;
				}
			}
		}

		origin = null;
		if (change)
		{
			scroller.scrollRectToVisible(rect);
			scroller.revalidate();
			drawingPanel.scrollRectToVisible(rect);
			drawingPanel.revalidate();
		}

		/*
		 * System.out.print("bbbbbbbbbbbbbbbb " + area.width + "  " +
		 * area.height + "\n");
		 */

	}

	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		// System.out.print("Dd");
	}

	public void mouseDragged(MouseEvent e)
	{
		if (drawingPanel.isEnabled())
			return;
		rect.x = scroller.getHorizontalScrollBar().getValue();
		rect.y = scroller.getVerticalScrollBar().getValue();

		/*
		 * System.out.print("aaaaaaaaaaa " + origin.x + "  " + origin.y + "\n");
		 */
		rect.x += (origin.x - e.getPoint().x);
		rect.y += (origin.y - e.getPoint().y);
		rect.width = getWidth();
		rect.height = getHeight();

		// drawingPanel.setPreferredSize(area);
		drawingPanel.scrollRectToVisible(rect);
		drawingPanel.revalidate();
		scroller.scrollRectToVisible(rect);
		scroller.revalidate();

		/*
		 * System.out.print("bbbbbbbbbbbbbbbb " + rect.x + "  " + rect.y +
		 * "\n");
		 */
	}

	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{

		WindowUtilities.setNativeLookAndFeel();

		// Create an instance of the test application
		Model model = new Model();
		model.setVisible(true);

		Block block1 = new Block(model.getDrawingPanel());
		block1.setLocaion(new Point(300, 300));
		block1.setSize(new Point(100, 100));
		block1.init();
		block1.setFillColor(Color.MAGENTA);
		model.getDrawingPanel().addShape(block1);

		Block block2 = new Block(model.getDrawingPanel());
		block2.setLocaion(new Point(100, 300));
		block2.setSize(new Point(100, 100));
		block2.init();
		model.getDrawingPanel().addShape(block2);

		JFrame aWindow = new JFrame();

		aWindow.setJMenuBar(MainMenu.createMainMenu(model));

		aWindow.setBounds(200, 200, 500, 500);
		aWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		//aWindow.getContentPane().add(model);
		aWindow.setContentPane(model);

		aWindow.setVisible(true);

	}
}

class DrawingPane1 extends JScrollPane implements MouseListener, MouseMotionListener, ActionListener, ItemListener
{

	// private Dimension area = new Dimension();
	Rectangle						rect				= null;
	private static final long		serialVersionUID	= 1L;

	ShapeList						shapeList			= new ShapeList();
	private Point					p					= new Point(0, 0);
	private Event					event				= new Event();
	private JPopupMenu				popup;

	private static ResourceBundle	verilogGui			= ResourceBundle.getBundle("verilog_gui");

	private ArrayList<Shape>		focusList			= new ArrayList<Shape>();

	/**
	 * @return the focusList
	 */
	public synchronized ArrayList<Shape> getFocusList()
	{
		return focusList;
	}

	/**
	 * @param focusList
	 *            the focusList to set
	 */
	public synchronized void setFocusList(ArrayList<Shape> focusList)
	{
		this.focusList = focusList;
	}

	public DrawingPane1()
	{
		// setSize(300, 200);
		setBackground(Color.red);
		setVisible(true);
		createPopupMenu();

		addMouseListener(this);
		addMouseMotionListener(this);

	}

	public void createPopupMenu()
	{
		JMenuItem mi;

		popup = new JPopupMenu("Edit");

		mi = new JMenuItem(verilogGui.getString("new_block"));
		mi.addActionListener(this);
		popup.add(mi);

		mi = new JMenuItem(verilogGui.getString("add_input"));
		mi.setHorizontalTextPosition(JMenuItem.RIGHT);
		mi.addActionListener(this);
		popup.add(mi);
		mi.setBackground(Color.cyan);

		popup.addSeparator();
		mi = new JMenuItem("Paste");
		mi.addActionListener(this);
		mi.setBackground(Color.RED);
		popup.add(mi);
		popup.setBorderPainted(true);
		popup.setBackground(Color.cyan);

		// popup.setLabel("Justification");
		popup.setBorder(new BevelBorder(BevelBorder.RAISED));
		popup.setBackground(Color.RED);
		// popup.addPopupMenuListener(new PopupPrintListener());

		// setComponentPopupMenu(popup);

	}

	public void addShape(Shape shape)
	{
		shapeList.add(shape);
		shapeList.setCurShape(shape);
	}

	@Override
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		shapeList.paint(g);
	}

	/*
	 * protected void paintComponent(Graphics g) { super.paintComponent(g);
	 * g.clearRect(0, 0, this.getWidth(), this.getHeight()); shapeList.paint(g);
	 * 
	 * }
	 */

	public void mouseDragged(java.awt.event.MouseEvent e)
	{

		p.x = e.getX();
		p.y = e.getY();
		shapeList.mouseDragged(p);
		this.repaint();

	}

	public void mouseMoved(java.awt.event.MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseClicked(java.awt.event.MouseEvent e)
	{

	}

	public void mouseEntered(java.awt.event.MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(java.awt.event.MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mousePressed(java.awt.event.MouseEvent e)
	{

		if (!isEnabled())
			return;
		boolean b = false;
		if (e.isPopupTrigger())
		{

			for (Shape shape : focusList)
			{
				shape.setFocus(false);
			}
			focusList.clear();

			for (Shape shape : shapeList.shapes)
			{
				if (shape instanceof Polygon)
				{
					JPopupMenu pm = ((Polygon) shape).getPopupMenu();
					if (pm != null && ((Polygon) shape).isInside(e.getPoint()))
					{
						pm.show(e.getComponent(), e.getX(), e.getY());
						b = true;
						break;
					}
				}
			}
			if (!b)
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}

		}
		if (e.getButton() == 1)
		{
			if (!e.isControlDown())
			{
				for (Shape shape : focusList)
				{
					shape.setFocus(false);
				}
				focusList.clear();
			}
			event.getPoint().x = e.getX();
			event.getPoint().y = e.getY();

			// find if more then one point was relased mouse (it means that
			// dragged
			// point related on another point)

			if (shapeList.mousePressed(event))
			{
				if (event.geteffectedShapes().size() > 0)
				{

					if (event.geteffectedShapes().get(0) instanceof ConectionPoint)
					{
						ConectionPoint conectionPoint = (ConectionPoint) event.geteffectedShapes().get(0);

						ConectionPoint p1 = new ConectionPoint(new Point(conectionPoint.getPoint().x, conectionPoint.getPoint().y), null);

						Line line = new Line(this);
						line.addPoint(p1);
						line.addPoint(conectionPoint);
						addShape(line);

						shapeList.mousePressed(event);
						event.clearAffectedShade();
					}
				}
				for (Shape shape : focusList)
				{
					shape.setFocus(true);

				}
			}
			else
			{
				Line line = new Line(this);
				line.addPoint(new Point(event.getPoint().x, event.getPoint().y));
				line.addPoint(new Point(event.getPoint().x, event.getPoint().y));
				addShape(line);
				shapeList.mousePressed(event);

				// make line focus
				for (Shape shape : focusList)
				{
					shape.setFocus(false);
				}
				focusList.clear();
				getFocusList().clear();
				getFocusList().add(line);
				line.setFocus(true);
			}
		}
		this.repaint();
	}

	public void mouseReleased(java.awt.event.MouseEvent e)
	{

		if (!isEnabled())
			return;
		event.mouseEvent = e;
		event.getPoint().x = e.getX();
		event.getPoint().y = e.getY();

		boolean b = false;
		if (e.isPopupTrigger())
		{

			for (Shape shape : shapeList.shapes)
			{
				if (shape instanceof Polygon)
				{
					JPopupMenu pm = ((Polygon) shape).getPopupMenu();
					if (pm != null && ((Polygon) shape).isInside(event.getPoint()))
					{
						pm.show(e.getComponent(), e.getX(), e.getY());
						b = true;
						// make just this object focus
						for (Shape shape1 : focusList)
						{
							shape1.setFocus(false);
						}
						focusList.clear();
						focusList.add(shape);
						shape.setFocus(true);
						break;
					}
				}
			}
			if (!b)
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}

		}

		shapeList.mouseReleased(event);

		// find if more then one point was relased mouse (it means that draged
		// point realed on another point)
		ConectionPoint p1 = null;
		for (Shape affctedShape : event.geteffectedShapes())
		{
			if (affctedShape instanceof ConectionPoint)
			{
				ConectionPoint conectionPoint = (ConectionPoint) affctedShape;
				if (!conectionPoint.isMoveAble())
				{
					p1 = conectionPoint;
					event.geteffectedShapes().remove(p1);
					break;
				}
			}
		}

		if (p1 != null)
		{
			for (Shape obj : event.geteffectedShapes())
			{

				if (obj instanceof ConectionPoint)
				{
					if (((ShapeList) obj.getFather()).getPoint(1) != p1
							&& (((Section) obj.getFather()).getP1().inSidePoint1((double) p1.getPoint().x, (double) p1.getPoint().y) || p1.inSidePoint1(((Section) obj.getFather())
									.getP1().getPoint().x, ((Section) obj.getFather()).getP1().getPoint().y)))
					{

						((ShapeList) obj.getFather()).setPoint(p1, 0);
						// ((ConectionPoint) obj).setPoint(p1.getPoint());

					}
					if (((ShapeList) obj.getFather()).getPoint(0) != p1
							&& (((Section) obj.getFather()).getP2().inSidePoint1((double) p1.getPoint().x, (double) p1.getPoint().y) || p1.inSidePoint1(((Section) obj.getFather())
									.getP2().getPoint().x, ((Section) obj.getFather()).getP2().getPoint().y)))
					{

						((ShapeList) obj.getFather()).setPoint(p1, 1);

						// ((ConectionPoint) obj).setPoint(p1.getPoint());
					}
				}
				if (obj instanceof Section)
				{
					@SuppressWarnings("unused")
					int gg=4;
					gg++;
				}
				if (obj instanceof ConnectionLine)
				{

				}
			}

		}
		this.repaint();
		event.clearAffectedShade();

	}

	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub

		String command = e.getActionCommand();

		if (command.equals(verilogGui.getString("new_block")))
		{
			Block block = new Block(this);
			block.setLocaion(new Point(this.getMousePosition().x, this.getMousePosition().y));
			block.setSize(new Point(100, 100));
			block.init();
			block.setFillColor(Color.MAGENTA);
			this.addShape(block);

			// make shape focus
			for (Shape shape : focusList)
			{
				shape.setFocus(false);
			}
			focusList.clear();
			getFocusList().clear();
			getFocusList().add(block);
			block.setFocus(true);

			// perform cut operation
		}
		else if (command.equals("Copy"))
		{
			// perform copy operation
		}
		else if (command.equals("Paste"))
		{
			// perform paste operation
		}
		repaint();

	}

	public void itemStateChanged(ItemEvent e)
	{
		JMenuItem source = (JMenuItem) (e.getSource());
		String s = "Item event detected." + "    Event source: " + source.getText()

		+ "    New state: " + ((e.getStateChange() == ItemEvent.SELECTED) ? "selected" : "unselected");

	}

}
