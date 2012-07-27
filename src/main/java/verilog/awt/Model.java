package verilog.awt;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UnsupportedLookAndFeelException;

 

 

class Model extends JScrollPane implements MouseListener,MouseMotionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JScrollPane scrollPane;
    //LineList lineList = new LineList();
    //PolygonList polyginList = new PolygonList();
    ShapeList shapeList = new ShapeList();
    private Point p = new Point(0,0);
    private Event event= new Event();
   //  ConnectionLine connectionLine = new ConnectionLine();
   
//   ConectionPoint aaa =  new ConectionPoint (new Point (100,100),this);
 //  ConectionPoint bbb =  new ConectionPoint (new Point (200,200),this);
   
   ConnectionLine connectionLine = new ConnectionLine(new Point (120,260), new Point (300,400)); 
	
 //  Block block1 = new Block( );
	
	
    public Model() {
	//setTitle("Scrolling Pane Application");
	setSize(300, 200);
	setBackground(Color.gray);
	setVisible( true );
	 
	//aaa.setMoveAble(false);
	//bbb.setMoveAble(false);
	
	//addShape(aaa);
	//addShape(bbb);
	//addShape(connectionLine);
	
	
	//connectionLine.init();
	
	/*block1.setLocaion(new Point(300,300));
	block1.setSize(new Point(100,100));
	block1.init();*/
	
	
	
	//JPanel topPanel = new JPanel();
	//topPanel.setLayout(new BorderLayout());
	//getContentPane().add(topPanel);

	//Icon image = new ImageIcon("main.gif");
	//JLabel label = new JLabel(image);

	// Create a tabbed pane
	//scrollPane = new JScrollPane();
	//scrollPane.getViewport().add(label);
	//topPanel.add(scrollPane, BorderLayout.CENTER);
	
	
	    
	addMouseListener(this);
	addMouseMotionListener(this);
	 
    }

    public void addShape(Shape shape) {
	      //  this.lines.add(new Line(x1, y1, x2, y2));
	        shapeList.add(shape);
	        shapeList.setCurShape(shape);
	    }

    
    @Override
    public void paint(Graphics g)
    {
	setBackground(Color.GREEN);
	WindowUtilities.setNativeLookAndFeel();
	super.paint(g);
	
	/*lineList.paint(g);
	polyginList.paint(g);*/
	shapeList.paint(g);
	//aaa.paint(g);
	//bbb.paint(g);
	
	//connectionLine.paint(g);
	
	//block1.paint(g);

    }
    public void mouseDragged(java.awt.event.MouseEvent e) {

	p.x = e.getX();
	p.y = e.getY();
	shapeList.mouseDragged(p);
	this.repaint();
	  
    }

    public void mouseMoved(java.awt.event.MouseEvent e) {
	// TODO Auto-generated method stub
	
    }

    public void mouseClicked(java.awt.event.MouseEvent e) {

	//lineList.mousePressed(p);

    }

    public void mouseEntered(java.awt.event.MouseEvent e) {
	// TODO Auto-generated method stub
	
    }

    public void mouseExited(java.awt.event.MouseEvent e) {
	// TODO Auto-generated method stub
	
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
	event.getPoint().x = e.getX();
	event.getPoint().y = e.getY();

	// find if more then one point was relased mouse (it means that dragged
	// point related on another point)

	if (shapeList.mousePressed(event)) {
	    // for (ConectionPoint conectionPoint :
	    // event.getConectionPointList()) {

	    if (event.geteffectedShapes().size() > 0) {

		if (event.geteffectedShapes().get(0) instanceof ConectionPoint) {
		    ConectionPoint conectionPoint = (ConectionPoint) event.geteffectedShapes() .get(0);
			   
		    ConectionPoint p1 = new ConectionPoint(new Point(
			    conectionPoint.getPoint().x,
			    conectionPoint.getPoint().y),null);

		    Line line = new Line();
		    line.addPoint(p1);
		    line.addPoint(conectionPoint);
		    addShape(line);

		    shapeList.mousePressed(event);
		    event.clearAffectedShade();
		}
	    }
	} else {
	    Line line = new Line();
	    line.addPoint(new Point(event.getPoint().x, event.getPoint().y));
	    line.addPoint(new Point(event.getPoint().x, event.getPoint().y));
	    addShape(line);
	    shapeList.mousePressed(event);
	}

    }

    public void mouseReleased(java.awt.event.MouseEvent e) {
	event.getPoint().x = e.getX();
	event.getPoint().y = e.getY();

	shapeList.mouseReleased(event);

	// find if more then one point was relased mouse (it means that draged
	// point realed on another point)
	ConectionPoint p1 = null;
	for (Shape affctedShape : event.geteffectedShapes()) {
	    if (affctedShape instanceof ConectionPoint) {
		ConectionPoint conectionPoint = (ConectionPoint) affctedShape;
		if (!conectionPoint.isMoveAble()) {
		    p1 = conectionPoint;
		    event.geteffectedShapes().remove(p1);
		    break;
		}
	    }
	}

	if (p1 != null) {
	    for (Shape obj : event.geteffectedShapes()) {

		if (obj instanceof ConectionPoint) {
		    if (((Section) obj.getFather()).getP1().inSidePoint1(
			    (double) p1.getPoint().x, (double) p1.getPoint().y)
			    || p1.inSidePoint1(((Section) obj.getFather())
				    .getP1().getPoint().x, ((Section) obj
				    .getFather()).getP1().getPoint().y)) {
			((ShapeList) obj.getFather()).setPoint(p1, 0);

		    }
		    if (((Section) obj.getFather()).getP2().inSidePoint1(
			    (double) p1.getPoint().x, (double) p1.getPoint().y)
			    || p1.inSidePoint1(((Section) obj.getFather())
				    .getP2().getPoint().x, ((Section) obj
				    .getFather()).getP2().getPoint().y)) {

			((ShapeList) obj.getFather()).setPoint(p1, 1);
		    }
		}
		if (obj instanceof ConnectionLine) {

		}
	    }

	}
	this.repaint();
	event.clearAffectedShade();

    }


    
    public static void main( String args[] ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
	
	WindowUtilities.setNativeLookAndFeel();
	
	// Create an instance of the test application
	Model mainFrame	= new Model();
	mainFrame.setVisible( true );
	
	Line line1 = new Line();
	line1.addPoint(new Point (10,100));
	line1.addPoint(new Point (500,100));
	line1.addPoint(new Point (300,200));
	line1.addPoint(new Point (100,230));
	mainFrame.addShape(line1);
	
	Line line2 = new Line();
	line2.addPoint(new Point (50,100));
	line2.addPoint(new Point (470,300));
	mainFrame.addShape(line2);
	
	
	Block block1 = new Block( );
	block1.setLocaion(new Point(300,300));
	block1.setSize(new Point(100,100));
	block1.init();
	mainFrame.addShape(block1);
	
	Block block2 = new Block( );
	block2.setLocaion(new Point(100,300));
	block2.setSize(new Point(100,100));
	block2.init();
	mainFrame.addShape(block2);
	
	JFrame aWindow = new JFrame();
	aWindow.setBounds(200, 200, 500, 500);
	aWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	Container content = aWindow.getContentPane();
	    content.add(mainFrame);
	    aWindow.setVisible(true);
	    
		
	}
}
