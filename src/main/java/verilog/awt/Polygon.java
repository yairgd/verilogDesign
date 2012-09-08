package verilog.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JPopupMenu;

class BlockCopy implements ActionListener
{

	Polygon	polygon;

	BlockCopy(Polygon polygon)
	{
		this.polygon = polygon;
	}

	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		// polygon.model.getFocusList().add(polygon);

	}

}

class BlockCut implements ActionListener
{

	Polygon	shape;

	BlockCut(Polygon shape)
	{
		this.shape = shape;
	}

	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub

	}

}

class BlockDelete implements ActionListener
{

	Polygon	shape;

	BlockDelete(Polygon shape)
	{
		this.shape = shape;
	}

	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub

	}

}


public abstract class Polygon extends Shape
{
	enum PolygonLineType
	{
		ConnectionLine, Line
	}

	// protected Point prevMouseLocation = new Point(0, 0);
	protected java.awt.Polygon			polygon				= new java.awt.Polygon();
	protected ArrayList<ConnectionLine>	listConnectionLine	= new ArrayList<ConnectionLine>();
	protected PolygonLineType			polygonLineType;
	private ConnectionLine				curConnectionLine	= null;

	protected static ResourceBundle		verilogGui			= ResourceBundle.getBundle("verilog_gui");

	protected DrawingPane1						model;

	private Color						fillColor			= Color.GREEN;
	private Color						edgeColor			= Color.BLACK;
	private int							edgeWidh			= 2;

	Polygon(DrawingPane1 drawingPane1)
	{
		this.model = drawingPane1;
	}

	/**
	 * @return the fillColor
	 */
	public Color getFillColor()
	{
		return fillColor;
	}

	/**
	 * @param fillColor
	 *            the fillColor to set
	 */
	public void setFillColor(Color fillColor)
	{
		this.fillColor = fillColor;
	}

	/**
	 * @return the polygonLineType
	 */
	public PolygonLineType getPolygonLineType()
	{
		return polygonLineType;
	}

	/**
	 * @param polygonLineType
	 *            the polygonLineType to set
	 */
	public void setPolygonLineType(PolygonLineType polygonLineType)
	{
		this.polygonLineType = polygonLineType;
	}

	public int getMinX()
	{
	    int xx = 1000000000;
	    for (int i=0;i<polygon.npoints;i++)
	    {
		if (polygon.xpoints[i]<xx)
		    xx=polygon.xpoints[i]; 
	    }
	    return xx;
	    
	}
	public int getMinY()
	{
	    int xx = 1000000000;
	    for (int i=0;i<polygon.npoints;i++)
	    {
		if (polygon.ypoints[i]<xx)
		    xx=polygon.ypoints[i];
	    }
	    return xx;
	    
	}
	public void paint(Graphics g)
	{
		Graphics2D ga = (Graphics2D) g;
		int n;
		if (isFocus())
			n=5;
		else
			n=2;
		BasicStroke bs2 = new BasicStroke(n);// (2,
													// BasicStroke.JOIN_ROUND,
													// BasicStroke.JOIN_ROUND,
													// 1.0f, dash2,15f);
		ga.setStroke(bs2);
		g.setColor(edgeColor);
		g.drawPolygon(polygon);
		g.setColor(fillColor);
		g.fillPolygon(polygon);

		for (final ConnectionLine connectionLine : listConnectionLine)
		{
			connectionLine.paint(g);
		}
	}

	public boolean isInside(Point p)
	{
		return polygon.contains(p);
	}

	public void mouseDragged(Point e)
	{
		// if (polygonStatus == PolygonStatus.Move) {
		if (shapeStatus == ShapeStatus.Move)
		{
			if (curConnectionLine != null)
			{
				curConnectionLine.mouseDragged(e);
			}

			int[] x = polygon.xpoints;
			int[] y = polygon.ypoints;
			int nn = polygon.npoints;
			for (int i = 0; i < nn; i++)
			{
				x[i] -= (prevMouseLocation.x - e.getX());
				y[i] -= (prevMouseLocation.y - e.getY());
			}
			polygon.reset();
			for (int i = 0; i < nn; i++)
			{
				polygon.addPoint(x[i], y[i]);
			}

			for (final ConnectionLine connectionLine : listConnectionLine)
			{
				//connectionLine.move((prevMouseLocation.x - e.getX()), (prevMouseLocation.y - e.getY()));

				connectionLine.mouseDragged(e);

			}

			prevMouseLocation.x = e.x;
			prevMouseLocation.y = e.y;

			// super.mouseDragged(e);

		}

	}

	public boolean mousePressed(Event event)
	{

		for (final ConnectionLine connectionLine : listConnectionLine)
		{
			if (connectionLine.mousePressed(event))
			{
				curConnectionLine = connectionLine;
				 model.getFocusList().add(this);
				 model.shapeList.setCurShape(this);
				return true;
			}

		}

		if (polygon.contains(event.getPoint()))
		{
			/*
			 * if ( event.mouseEvent.isPopupTrigger()) { //
			 * popup.show(event.mouseEvent.getComponent(),
			 * event.mouseEvent.getX(), event.mouseEvent.getY()); }
			 */
			// polygonStatus = PolygonStatus.Move;
			shapeStatus = ShapeStatus.Move;
			prevMouseLocation.x = event.getPoint().x;
			prevMouseLocation.y = event.getPoint().y;
			model.getFocusList().add(this);
			model.shapeList.setCurShape(this);
			return true;
		}
		return false;
	}

	public boolean mouseReleased(Event event)
	{

		boolean b = false, b1;
		for (final ConnectionLine connectionLine : listConnectionLine)
		{

			b1 = connectionLine.mouseReleased(event);
			b = b || b1;
			if (b1)
			{

				curConnectionLine = null;
				event.addAffectedShade(this);
				// return true;
			}
		}

		if (polygon.contains(event.getPoint()))
		{
			// polygonStatus = PolygonStatus.Move;
			event.addAffectedShade(this);
			return true;
		}

		return b;
 

	}

 

	public JPopupMenu getPopupMenu()
	{
		return null;
	}

	public abstract void init();

}
