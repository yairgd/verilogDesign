package verilog.awt;

import java.awt.Point;
import java.util.ArrayList;

import verilog.awt.Shape.ShapeStatus;

public class Event {
    private Point point = new Point();
 //   private ConnectionPointStatus prevConnectionPointStatus;
    private ShapeStatus prevShapeStatusStatus;
    /**
     * @return the prevShapeStatusStatus
     */
    public synchronized ShapeStatus getPrevShapeStatusStatus() {
        return prevShapeStatusStatus;
    }
    /**
     * @param prevShapeStatusStatus the prevShapeStatusStatus to set
     */
    public synchronized void setPrevShapeStatusStatus(
    	ShapeStatus prevShapeStatusStatus) {
        this.prevShapeStatusStatus = prevShapeStatusStatus;
    }
    private ArrayList<verilog.awt.Shape> effectedShapes = new ArrayList<verilog.awt.Shape>();
    
    /**
     * @return the conectionPointList
     */
    public ArrayList<Shape> geteffectedShapes() {
        return effectedShapes;
    }
    /**
     * @param conectionPointList the conectionPointList to set
     */
    public void seteffectedShapes(ArrayList<Shape> effectedShapes) {
        this.effectedShapes = effectedShapes;
    }

    /**
     * @return the point
     */
    public Point getPoint() {
        return point;
    }
    /**
     * @param point the point to set
     */
    public void setPoint(Point point) {
        this.point = point;
    }
    
  
    public void addAffectedShade(Shape p)
    {
	effectedShapes.add(p);
    }
    public void clearAffectedShade()
    {
	effectedShapes.clear();
    }
    
}
