package verilog.awt;

import java.awt.Graphics;
import java.awt.Point;

public class ConnectionLine extends verilog.awt.ShapeList {
   // private final ArrayList<ConectionPoint> points = new ArrayList<ConectionPoint>();
    private ConectionPoint curPoint ; 
    private int width=5;
    private int r=6;
    private Point p1,p2;

    /* void move(double deltaX,double deltaY)
    {
	p1.x -= deltaX;
	p1.y -= deltaY;
	p2.x -= deltaX;
	p2.y -= deltaY;
	for (final ConectionPoint r : points) {
	    r.getPoint().x -= deltaX;
	    r.getPoint().y -= deltaY;
	}
    }*/
    
    public ConnectionLine(Point p1,Point p2) {
	this.p1 = p1;
	this.p2 = p2;
	//connectionLineStatus=ConnectionLineStatus.Idle;
	shapeStatus = ShapeStatus.Idle;
	init();
	
	
    }

    
    public void init()
    {
	ConectionPoint cp;
	double s = Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y)  )	;
	int n = (int) Math.floor((s/(width*2+r  )));
	double a = (p1.y-p2.y)/(p1.x-p2.x+1e-10);
	if (Math.abs(a) < 1e5) {
	    for (int i = 0; i < n; i++) {
		int x = p1.x + (width * 2 + r / 2) * i + width;
		int y = (int) (-a * (p1.x - x) + p1.y);
		
		cp = new ConectionPoint(new Point(x, y), this);
		cp.setMoveAble(false);
		add(cp);
		//points.add( cp);
	    }
	} else {
	    a=0;
	    for (int i = 0; i < n; i++) {
		int y = Math.min(p1.y,p2.y) + (width * 2 + r / 2) * i + width;
		int x = (int) (-a * (Math.min(p1.y,p2.y) - y) + p1.x);
		cp = new ConectionPoint(new Point(x, y), this);
		cp.setMoveAble(false);
		//points.add(cp);
		add(cp);
		 
	    }
	}
    }
    
/*    boolean mousePressed(Event e) {
	prevMouseLocation.x = e.getPoint().x;
	    prevMouseLocation.y = e.getPoint().y;
	//for (final ConectionPoint r : points) {
	    for (final Shape r : shapes) {
		if (r.mousePressed(e))
		{
		curPoint=(ConectionPoint) r;
		//curPoint.getDataList().add(this);
		
		
		//shapeStatus = ShapeStatus.Move;
		return true;
		}
	  //  }
	}
	return false;

    }

    public boolean mouseReleased(Event event) {
	boolean b=false,b1;
	//for (final ConectionPoint r : points) {
	for (final Shape r : shapes) {
	    b1=r.mouseReleased(event);
		b=b||b1;
	  //  else 
	//	r.mouseReleased(event);
	    if (b1) {
		event.addAffectedShade(this);
		r.setShapeStatus(ShapeStatus.Idle);
		curPoint = null;
	//	return true;
	    }
	    // }
	}
	return b;

	
	 * //connectionLineStatus=ConnectionLineStatus.Idle; shapeStatus =
	 * ShapeStatus.Idle; if (curPoint!=null) { //
	 * curPoint.setConnectionPointStatus(ConnectionPointStatus.Idle);
	 * curPoint.setShapeStatus(ShapeStatus.Idle); curPoint=null; }
	 
    }
    */
    
    public void mouseDragged(Point e) {

	/*move((prevMouseLocation.x - e.getX()),
		(prevMouseLocation.y - e.getY()));*/
	//for (final ConectionPoint r : points) {
	for (final Shape r : shapes) {
	    r.mouseDragged(e);
	  /*  r.move((prevMouseLocation.x - e.getX()),
			(prevMouseLocation.y - e.getY()));*/
	    
	    
	   // if (r.getConnectionPointStatus() == ConnectionPointStatus.Move)
	  /*  if (r.getShapeStatus()==ShapeStatus.Move)
		//connectionLineStatus = ConnectionLineStatus.Move;
		shapeStatus = ShapeStatus.Move;*/

	}
	prevMouseLocation.x = e.x;
	    prevMouseLocation.y = e.y;
    }
     
    
 
    
   /* public void paint(Graphics g)
    {
	Graphics2D ga = (Graphics2D) g;
	Line2D line2d = new Line2D.Float(p1, p2);

	BasicStroke bs2 = new BasicStroke(1); 
	ga.setColor(Color.RED);
	ga.setStroke(bs2);
	ga.draw(line2d);
	
	//for (final ConectionPoint r : points) {
	for (final Shape r : shapes) {
            r.paint(g);
        }
    }*/
}
