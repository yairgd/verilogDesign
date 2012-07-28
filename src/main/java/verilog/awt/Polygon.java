package verilog.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Polygon extends Shape {
    enum PolygonLineType {
	ConnectionLine, Line
    }

 
    //protected Point prevMouseLocation = new Point(0, 0);
    protected java.awt.Polygon polygon = new java.awt.Polygon();
    protected ArrayList<ConnectionLine> listConnectionLine = new ArrayList<ConnectionLine>();
    protected PolygonLineType polygonLineType;
    private ConnectionLine curConnectionLine = null;

   
    
    private Color fillColor  = Color.GREEN;
    private Color edgeColor = Color.BLACK;
    private int edgeWidh =2;
    /**
     * @return the fillColor
     */
    public  Color getFillColor() {
        return fillColor;
    }

    /**
     * @param fillColor the fillColor to set
     */
    public  void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * @return the polygonLineType
     */
    public PolygonLineType getPolygonLineType() {
	return polygonLineType;
    }

    /**
     * @param polygonLineType
     *            the polygonLineType to set
     */
    public void setPolygonLineType(PolygonLineType polygonLineType) {
	this.polygonLineType = polygonLineType;
    }

    public void paint(Graphics g) {
	Graphics2D ga = (Graphics2D) g;
	BasicStroke bs2 = new BasicStroke(edgeWidh);//  (2, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dash2,15f);
	ga.setStroke(bs2);    
	g.setColor(edgeColor);
	g.drawPolygon(polygon);
	g.setColor(fillColor);
	g.fillPolygon (polygon);

	for (final ConnectionLine connectionLine : listConnectionLine) {
	    connectionLine.paint(g);
	}
    }

    public void mouseDragged(Point e) {
	//if (polygonStatus == PolygonStatus.Move) {
	if (shapeStatus == ShapeStatus.Move) {
	    if (curConnectionLine != null) {
		curConnectionLine.mouseDragged(e);
	    }

	    int []x = polygon.xpoints;
	    int []y = polygon.ypoints;
	    int nn = polygon.npoints;
	    for (int i = 0; i < nn; i++) {
		x[i] -= (prevMouseLocation.x - e.getX());
		y[i] -= (prevMouseLocation.y - e.getY());
	    }
	    polygon.reset();
	    for (int i = 0; i < nn; i++) {
		polygon.addPoint(x[i], y[i]);
	    }
	
	    for (final ConnectionLine connectionLine : listConnectionLine) {
		connectionLine.move((prevMouseLocation.x - e.getX()),
			(prevMouseLocation.y - e.getY()));
		 
		connectionLine.mouseDragged(e);
		

	    }

	    prevMouseLocation.x = e.x;
	    prevMouseLocation.y = e.y;

	    // super.mouseDragged(e);

	}

    }

    public boolean mousePressed(Event event) {

	for (final ConnectionLine connectionLine : listConnectionLine) {
	    /*
	     * connectionLine.mousePressed(event); if
	     * (connectionLine.getShapeStatus().equals( ShapeStatus.Move) ==
	     * true) {
	     */

	    if (connectionLine.mousePressed(event)) {
		curConnectionLine = connectionLine;
		// polygonStatus = PolygonStatus.Move;
		//shapeStatus = ShapeStatus.Move;
		return true;
	    }
	    // connectionLine.
	}

	if (polygon.contains(event.getPoint())) {
	  //  polygonStatus = PolygonStatus.Move;
	    shapeStatus = ShapeStatus.Move;
	    prevMouseLocation.x = event.getPoint().x;
	    prevMouseLocation.y = event.getPoint().y;
	    return true;
	}
	return false;
    }

    public boolean mouseReleased(Event event) {
	boolean b=false,b1;
	for (final ConnectionLine connectionLine : listConnectionLine) {
	   
	    b1=connectionLine.mouseReleased(event);
		b=b||b1;
	    if (b1) {
		
		 curConnectionLine = null;
		 event.addAffectedShade(this);
		//return true;
	    }
	}
	
	if (polygon.contains(event.getPoint())) {
		  //  polygonStatus = PolygonStatus.Move;
	    event.addAffectedShade(this);
		    return true;
		}
	
	return b;
	    
	    
	/*//polygonStatus = PolygonStatus.Idle;
	shapeStatus = ShapeStatus.Idle;
	if (curConnectionLine != null) {
	    curConnectionLine.mouseReleased(event);
	    curConnectionLine = null;
	}*/

    }

    @Override
    void move(double deltaX, double deltaY) {
	// TODO Auto-generated method stub
	//listConnectionLine.move(3,3)
    }
    
 
    public abstract void init();
}
