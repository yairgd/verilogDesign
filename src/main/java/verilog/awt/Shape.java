package verilog.awt;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.BoldAction;

public abstract class Shape {

    enum ShapeStatus {
	Idle, Move
    };
    
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
    





  /*  *//**
     * @return the dataList
     *//*
    public ArrayList<Object> getDataList() {
        return dataList;
    }


    *//**
     * @param dataList the dataList to set
     *//*
    public void setDataList(ArrayList<Object> dataList) {
        this.dataList = dataList;
    }
    */
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
    
      abstract void move(double deltaX,double deltaY);
    //  abstract Shape getPoint(int n);
   //   abstract void setPoint(Shape shape,int n);

}
