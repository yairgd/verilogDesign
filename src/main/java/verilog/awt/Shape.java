package verilog.awt;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;

public abstract class Shape {

    enum ShapeStatus {
	Idle, Move
    };
    
    private boolean focus;

    /**
	 * @return the focus
	 */
	public synchronized boolean isFocus()
	{
		return focus;
	}

	/**
	 * @param focus the focus to set
	 */
	public synchronized void setFocus(boolean focus)
	{
		this.focus = focus;
	}
	
    protected Object father;

    /**
     * @return the father
     */
    public  Object getFather() {
        return father;
    }


    /**
     * @param father the father to set
     */
    public  void setFather(Object father) {
        this.father = father;
    }

    protected Point prevMouseLocation = new Point(0, 0);
    
    public ShapeStatus shapeStatus = ShapeStatus.Idle;

 //protected ArrayList<Object>  dataList = new  ArrayList<Object> ();
    




    /**
     * @return the shapeStatus
     */
    public synchronized ShapeStatus getShapeStatus() {
	return shapeStatus;
    }

    /**
     * @param shapeStatus
     *            the shapeStatus to set
     */
    public synchronized void setShapeStatus(ShapeStatus shapeStatus) {
	this.shapeStatus = shapeStatus;
    }

    public abstract void paint(Graphics g);

    public abstract void mouseDragged(Point e);

    abstract boolean mousePressed(Event event);

    abstract boolean mouseReleased(Event event);
 

}
