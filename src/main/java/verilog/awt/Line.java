package verilog.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;

class Section extends verilog.awt.ShapeList
{

	public int		width				= 2;
	private Point	prevMouseLocation	= new Point(0, 0);

	public Section(ConectionPoint P1, ConectionPoint P2)
	{
		add(P1);
		add(P2);

		shapeStatus = ShapeStatus.Idle;

	}

	public ConectionPoint getP1()
	{
		return (ConectionPoint) shapes.get(0);
	}

	public void setP1(ConectionPoint p)
	{
		shapes.set(0, p);
	}

	public ConectionPoint getP2()
	{
		return (ConectionPoint) shapes.get(1);
	}

	public void setP2(ConectionPoint p)
	{
		shapes.set(1, p);
	}

	public void paint(Graphics g)
	{
		Graphics2D ga = (Graphics2D) g;
		ConectionPoint P1 = (ConectionPoint) shapes.get(0);
		ConectionPoint P2 = (ConectionPoint) shapes.get(1);
		P1.paint(g);
		P2.paint(g);
		Line2D line2d = new Line2D.Float(P1.getPoint(), P2.getPoint());
		float[] dash2 = { 1f, 1f };
		BasicStroke bs2 = new BasicStroke(width);// (2, BasicStroke.JOIN_ROUND,
													// BasicStroke.JOIN_ROUND,
													// 1.0f, dash2,15f);
		ga.setColor(Color.RED);
		// BasicStroke ff=new BasicStroke(
		ga.setStroke(bs2);
		ga.draw(line2d);
		// ga.drawString ("intersects = " , P1.x, P1.y);
	}

	public boolean inLine(double x0, double y0)
	{

		ConectionPoint P1 = (ConectionPoint) shapes.get(0);
		ConectionPoint P2 = (ConectionPoint) shapes.get(1);
		double a = (P1.getPoint().y - P2.getPoint().y) / (P1.getPoint().x - P2.getPoint().x + 1e-10);
		double b = -1 / (a + 1e-10);
		double x = (P1.getPoint().y - y0 - a * P1.getPoint().x + b * x0) / (b - a + 1e-10);
		double y = P1.getPoint().y + a * (x - P1.getPoint().x);
		if (Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0)) < 3 && y0 > Math.min(P1.getPoint().y, P2.getPoint().y) && y0 < Math.max(P1.getPoint().y, P2.getPoint().y))
		{
			// line.getP2().getBlock().setLineConnection(null);

			// //// line.getP1().setIoblock(null);
			// //// line.getP2().setIoblock(null);
			return true;
		}

		return false;

	}

	boolean mousePressed(Event event)
	{
		ConectionPoint P1 = (ConectionPoint) shapes.get(0);
		ConectionPoint P2 = (ConectionPoint) shapes.get(1);

		boolean b1 = P1.mousePressed(event);
		boolean b2 = P2.mousePressed(event);
		if (!b1 && !b2)
		{
			if (inLine(event.getPoint().getX(), event.getPoint().getY()))
			{
				// sectionStatus = SectionStatus.Line_Move;
				shapeStatus = ShapeStatus.Move;
				prevMouseLocation.x = event.getPoint().x;
				prevMouseLocation.y = event.getPoint().y;
				event.addAffectedShade(this);
				return true;
			}

		}
		/*
		 * if (P1.getShapeStatus()==ShapeStatus.Move ||
		 * P2.getShapeStatus()==ShapeStatus.Move) return true;
		 */
		if (b1 || b2)
			shapeStatus = ShapeStatus.Move;

		return b1 || b2;

	}

	public void mouseDragged(Point e)
	{
		ConectionPoint P1 = (ConectionPoint) getPoint(0);
		ConectionPoint P2 = (ConectionPoint) shapes.get(1);
		if (P1.getShapeStatus().equals(ShapeStatus.Move))
		{
			P1.mouseDragged(e);
			return;
		}
		if (P2.getShapeStatus().equals(ShapeStatus.Move))
		{
			P2.mouseDragged(e);
			return;
		}
		if (!P1.isMoveAble())
		{
			// remove object from connection point fathers
			// P1.getDataList().remove(this);
			P1 = new ConectionPoint(new Point(P1.getPoint().x, P1.getPoint().y), this);

			// P1.setPoint(new Point(P1.getPoint().x,P1.getPoint().y));
		}
		if (!P2.isMoveAble())
		{
			// P2.getDataList().remove(this);
			P2 = new ConectionPoint(new Point(P2.getPoint().x, P2.getPoint().y), this);
			// P2.setPoint(new Point(P2.getPoint().x,P2.getPoint().y));
		}

		P1.getPoint().x -= (prevMouseLocation.x - e.getX());
		P1.getPoint().y -= (prevMouseLocation.y - e.getY());
		P2.getPoint().x -= (prevMouseLocation.x - e.getX());
		P2.getPoint().y -= (prevMouseLocation.y - e.getY());
		setPoint(P1, 0);
		setPoint(P2, 1);

		prevMouseLocation.x = e.x;
		prevMouseLocation.y = e.y;

	}

	public boolean mouseReleased(Event event)
	{
		ConectionPoint P1 = (ConectionPoint) shapes.get(0);
		ConectionPoint P2 = (ConectionPoint) shapes.get(1);
		boolean b1, b2, b3;
		b1 = P1.mouseReleased(event);
		b2 = P2.mouseReleased(event);
		b3 = inLine(event.getPoint().getX(), event.getPoint().getY());
		shapeStatus = ShapeStatus.Idle;
		if (b1 || b2 || b3)
		{
			event.addAffectedShade(this);
		}
		return b1 || b2 || b3;
	}

	@Override
	void move(double deltaX, double deltaY)
	{
		// TODO Auto-generated method stub

	}
}

public class Line extends verilog.awt.ShapeList
{
	private Section	curSection;

	Line()
	{
		// sectionList.add(new Section(null,null));
		shapes.add(new Section(null, null));
	}

	/*
	 * Line(ConectionPoint p1,ConectionPoint p2) { // sectionList.add(new
	 * Section(null,null)); shapes.add(new Section(null, null)); }
	 */

	public void addPoint(Point p)
	{
		Section section = (Section) (shapes.get(shapes.size() - 1));
		if (section.getP1() == null)
		{
			section.setP1(new ConectionPoint(p, section));

		}
		else if (section.getP2() == null)
			section.setP2(new ConectionPoint(p, section));
		else
		{
			ConectionPoint p1 = section.getP2();
			ConectionPoint p2 = new ConectionPoint(p, this);// ,sectionList.get(sectionList.size()-1));
			shapes.add(new Section(p1, p2));
			// p1.addData(section);
			// p2.addData(section);

		}

	}

	public void addPoint(ConectionPoint p)
	{

		Section section = (Section) (shapes.get(shapes.size() - 1));
		// if (p.isMoveAble())
		p.setFather(section);
		if (section.getP1() == null)
			section.setP1(p);
		else if (section.getP2() == null)
			section.setP2(p);
		else
		{
			ConectionPoint p1 = section.getP2();
			ConectionPoint p2 = p;
			shapes.add(new Section(p1, p2));
			// p2.setFather(section);
		}

	}

	public void paint(Graphics g)
	{
		for (final Shape section : shapes)
		{
			section.paint(g);
		}
	}

	public void mouseDragged(Point e)
	{
		if (curSection != null)
		{
			curSection.mouseDragged(e);
		}

	}

	public boolean mousePressed(Event event)
	{
		for (final Shape shape : shapes)
		{
			Section section = (Section) shape;
			section.mousePressed(event);

			if (section.getP1().getShapeStatus() == ShapeStatus.Move || section.getP2().getShapeStatus() == ShapeStatus.Move || section.getShapeStatus() == ShapeStatus.Move)
			{
				curSection = section;
				// lineStatus = LineStatus.Line_Move;
				shapeStatus = ShapeStatus.Move;
				event.addAffectedShade(this);
				return true;
			}
		}
		return false;
	}

	public boolean mouseReleased(Event event)
	{
		boolean b = false, b1;
		for (final Shape shape : shapes)
		{
			Section section = (Section) shape;
			b1 = section.mouseReleased(event);
			b = b || b1;
			if (b1)
			{
				event.addAffectedShade(this);
				shapeStatus = ShapeStatus.Idle;
				curSection = null;
			}

		}
		return b;

	}

	@Override
	void move(double deltaX, double deltaY)
	{
		// TODO Auto-generated method stub

	}

}