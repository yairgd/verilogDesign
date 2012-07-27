package verilog.awt;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import verilog.awt.Shape.ShapeStatus;

public class ShapeList extends verilog.awt.Shape{
    protected final ArrayList<Shape> shapes = new ArrayList<Shape>();
    private Shape curShape = null;
    /**
     * @return the curShape
     */
    public synchronized Shape getCurShape() {
        return curShape;
    }

    /**
     * @param curShape the curShape to set
     */
    public synchronized void setCurShape(Shape curShape) {
        this.curShape = curShape;
        this.topShape = curShape;
    }

    private Shape topShape = null;
    
    public void mouseDragged(Point e) {

	if (curShape != null) {
	    curShape.mouseDragged(e);
	}
    }

    boolean mousePressed(Event event) {
	if (topShape != null) {

	   /* topShape.mousePressed(event);
	    if (topShape.getShapeStatus() == ShapeStatus.Move) {
		curShape = topShape;
		return true;
	    }*/

	    if (topShape.mousePressed(event)) {
		curShape = topShape;
		return true;
	    }
	     
	}
	for (final Shape r : shapes) {
	    
	    r.mousePressed(event) ;
	   /* if (   r.getShapeStatus() == ShapeStatus.Move) {
		curShape = r;
		topShape = r;
		return true;
	    }*/
  
	     if (r.mousePressed(event)) {
		curShape = r;
		topShape = r;
		return true;
	    }
	}
	return false;

    }

    void add(Shape shape) {
	shapes.add(shape);
    }

    public void paint(Graphics g) {
	for (final Shape r : shapes) {
	    r.paint(g);
	}
    }

    public boolean mouseReleased(Event event) {
	// TODO Auto-generated method stub
	/*
	 * if (curShape != null) { curShape.mouseReleased(event); curShape =
	 * null; }
	 */

	boolean b=false,b1;
	for (final Shape shape : shapes) {
	  //  if (b==false)
		b1=shape.mouseReleased(event);
	    //else 
		//shape.mouseReleased(event);
		b=b||b1;
	    if (b1) {
		shapeStatus = ShapeStatus.Idle;
		curShape = null;
		//event.addAffectedShade(this);
		//return true;
	    }

	}
	return b;

    }

    @Override
    void move(double deltaX, double deltaY) {
	// TODO Auto-generated method stub
	
    }

    Shape getPoint(int n) {
	// TODO Auto-generated method stub
	return shapes.get(n);
    }

   
    void setPoint(Shape shape,int n) {
	// TODO Auto-generated method stub
	shapes.set(n, shape);
    }
}
