package verilog.awt;

import java.awt.Point;

public class Block extends Polygon{
    
    private Point locaion;
    private Point size;

    /**
     * @return the size
     */
    public  Point getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public  void setSize(Point size) {
        this.size = size;
    }

    /**
     * @return the locaion
     */
    public Point getLocaion() {
        return locaion;
    }

    
    /**
     * @param locaion the locaion to set
     */
    public void setLocaion(Point locaion) {
        this.locaion = locaion;
    }

    @Override
    public void init() {
	// TODO Auto-generated method stub
	Point p1 = new Point(locaion.x,locaion.y);
	Point p2 = new Point(locaion.x+size.x,locaion.y);
	Point p3 = new Point(locaion.x+size.x,locaion.y+size.y);
	Point p4 = new Point(locaion.x,locaion.y+size.y);
	ConnectionLine cl1 = new ConnectionLine(p1, p4);
	listConnectionLine.add(cl1);
	ConnectionLine cl2 = new ConnectionLine(p3, p2);
	listConnectionLine.add(cl2);
	
	polygon.addPoint(p1.x, p1.y);
	polygon.addPoint(p2.x, p2.y);
	polygon.addPoint(p3.x, p3.y);
	polygon.addPoint(p4.x, p4.y);
	polygon.addPoint(p1.x, p1.y);
    }

    @Override
    void move(double deltaX, double deltaY) {
	// TODO Auto-generated method stub
	
    }


    
     
    
}
