package verilog.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

 
public class ConectionPoint extends verilog.awt.Shape {// implements Cloneable{
    
    public int r = 6;

 //   private ConnectionPointStatus connectionPointStatus;
    private ConectionPointStyle connectionPointStyle; 
    private boolean MoveAble;
   
    private Color fillColor  = Color.GREEN;
    private Color edgeColor = Color.BLACK;
    private int edgeWidh =1;


    /**
     * @return the moveAble
     */
    public boolean isMoveAble() {
        return MoveAble;
    }


    public ConectionPoint getClone()
    {
	try {
	    return (ConectionPoint) this.clone();
	} catch (CloneNotSupportedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }
    /**
     * @param moveAble the moveAble to set
     */
    public void setMoveAble(boolean moveAble) {
        MoveAble = moveAble;
    }

    /*enum ConnectionPointStatus {
	Idle, Move
    };
    */
    enum ConectionPointStyle {
	Circle,Visile
    };

    public Point point;
    /**
     * @return the point
     */
    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point =  point;
    }
    
    /**
     * @return the connectionPointStyle
     */
    public ConectionPointStyle getConnectionPointStyle() {
        return connectionPointStyle;
    }

    /**
     * @param connectionPointStyle the connectionPointStyle to set
     */
    public void setConnectionPointStyle(ConectionPointStyle connectionPointStyle) {
        this.connectionPointStyle = connectionPointStyle;
    }

 /*   public ConectionPoint(Point P) {
	point = P;
	//connectionPointStatus = ConnectionPointStatus.Idle;
	shapeStatus = ShapeStatus.Idle;
	connectionPointStyle = ConectionPointStyle.Circle;
	MoveAble = true;
    }*/

    public ConectionPoint(Point P,verilog.awt.Shape father) {
  	point = P;
  	//connectionPointStatus = ConnectionPointStatus.Idle;
  	shapeStatus = ShapeStatus.Idle;
  	connectionPointStyle = ConectionPointStyle.Circle;
  	//dataList.add(father);
  	MoveAble = true;
  	this.father = father;
  
  	/*if (father instanceof Model) {
	    Model new_name = (Model) father;
	    
	}*/
  	 
  	
      }
/*    
    public void addData(Object data)
    {
	//dataList.add(data);;
    }*/

    public void paint(Graphics g) {
	Graphics2D ga = (Graphics2D) g;
	BasicStroke bs2 = new BasicStroke(edgeWidh);//  (2, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dash2,15f);
	ga.setStroke(bs2);    

	
	if  (connectionPointStyle==ConectionPointStyle.Circle)
	{
	    
	    g.setColor(edgeColor);
	    Shape circ1 = new Ellipse2D.Float(point.x - r / 2, point.y - r / 2, r, r);
	    ga.draw(circ1);
	}
	//ga.drawString ("intersects = " , P1.x, P1.y);
    }
    
    public boolean inSidePoint1(double x, double y) {
	if (connectionPointStyle == ConectionPointStyle.Circle) {
	    if (Math.sqrt((point.x - x) * (point.x - x) + (point.y - y) * (point.y - y)) < r)
		return true;
	}
	return false;

    }
    
 
     void move(double deltaX,double deltaY)
    {
	point.x -= deltaX;
	   point.y -= deltaY;
    }
    
    public void mouseDragged(Point e)
    {
	move((prevMouseLocation.x - e.getX()),
		(prevMouseLocation.y - e.getY()));
	//if  ( connectionPointStatus==ConnectionPointStatus.Move)
	if (shapeStatus == ShapeStatus.Move)
	{
	point.x = e.x;
	point.y = e.y;
	}

	    prevMouseLocation.x = e.x;
	    prevMouseLocation.y = e.y;
    }
    
    public boolean mousePressed(Event event) {
	prevMouseLocation.x = event.getPoint().x;
	    prevMouseLocation.y = event.getPoint().y;
	if (inSidePoint1(event.getPoint().getX(), event.getPoint().getY()))
	{
	    if (MoveAble)
		//connectionPointStatus = ConnectionPointStatus.Move;
		shapeStatus = ShapeStatus.Move;
	    else
		event.addAffectedShade(this);
	    return true;
	}
	return false;
    }
   


    public boolean mouseReleased(Event event) {
	boolean b = false;
	// TODO Auto-generated method stub
	if (inSidePoint1(event.getPoint().x,event.getPoint().y) )// && !connectionPointStatus.equals(ConnectionPointStatus.Move))
	{
	    event.addAffectedShade(this);
	  //  event.setPrevConnectionPointStatus(connectionPointStatus);
	    event.setPrevShapeStatusStatus(shapeStatus);
	    
	    b=true;
	}
	//connectionPointStatus = ConnectionPointStatus.Idle;
	shapeStatus = ShapeStatus.Idle;
	return b;
    }




}
