package verilog.awt;

import java.awt.Point;

public class ConnectionLine extends verilog.awt.ShapeList
{
	private int				width	= 5;
	private int				r		= 6;

	public ConnectionLine(Point p1, Point p2,String name)
	{
		init(p1,p2,name);

	}

	public void init(Point p1,Point p2,String name)
	{
		ConectionPoint cp;
		double s = Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
		int n = (int) Math.floor((s / (width * 2 + r)));
		double a = (p1.y - p2.y) / (p1.x - p2.x + 1e-10);
		if (Math.abs(a) < 1e5)
		{
			for (int i = 0; i <= n; i++)
			{
				int x = p1.x + (width * 2 + r / 2) * i + width;
				int y = (int) (-a * (p1.x - x) + p1.y);

				cp = new ConectionPoint(new Point(x, y), this);
				cp.setMoveAble(false);
				cp.setProtName(new PortName(name+i, 0));
				add(cp);
			}
		}
		else // verical slope (slope ->Inf)
		{
			a = 0;
			for (int i = 0; i <= n; i++)
			{
				int y = Math.min(p1.y, p2.y) + (width * 2 + r / 2) * i + width;
				int x = (int) (-a * (Math.min(p1.y, p2.y) - y) + p1.x);
				cp = new ConectionPoint(new Point(x, y), this);
				cp.setMoveAble(false);
				cp.setProtName(new PortName(name+i, 0));
				add(cp);

			}
		}
	}



	public void mouseDragged(Point e)
	{

		for (final Shape r : shapes)
		{
			r.mouseDragged(e);

		}
		prevMouseLocation.x = e.x;
		prevMouseLocation.y = e.y;
	}

	
}
