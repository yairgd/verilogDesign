package verilog.gui;

import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import verilog.gui.LineListener.LineStatus;

//import verilog.gui.LineListener.LineStatus;

class LineConnection {
	private Line line;
	private ConectionPoint p;
    enum ConectionPoint {
    	P1,P2
    };
	public LineConnection(Line line,ConectionPoint p) {
		super();
		this.line = line;
	 	this.p = p;
	}

	/**
	 * @return the p
	 */
	public ConectionPoint getP() {
		return p;
	}

	/**
	 * @param p the p to set
	 */
	public void setP(ConectionPoint p) {
		this.p = p;
	}

	/**
	 * @return the line
	 */
	public Line getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Line line) {
		this.line = line;
	}
	
}
class ConnectionPoint {
	private Point point;
	private BlockIORect ioblock;
	/**
	 * @return the ioblock
	 */
	public BlockIORect getIoblock() {
		return ioblock;
	}
	/**
	 * @param ioblock the ioblock to set
	 */
	public void setIoblock(BlockIORect ioblock) {
		if (ioblock==null && this.ioblock!=null )
			this.point = new Point (this.ioblock.getCenter().x,this.ioblock.getCenter().y);
		this.ioblock = ioblock;
	}
	
	public ConnectionPoint(Point point, BlockIORect ioblock) {
		super();
		this.point = point;
		this.ioblock = ioblock;
	}
	public BlockIORect getBlock() {
		return ioblock;
	}
/*	public void setBlockIORect(BlockIORect ioblock) {
		if (ioblock==null && this.ioblock!=null )
			this.point = new Point (this.ioblock.getCenter().x,this.ioblock.getCenter().y);
		this.ioblock = ioblock;
	}*/
	public Point getP() {
		if (ioblock != null)
		{
			Point pp  = ioblock.getDisplay().map(ioblock.getBlock().getR2(), ioblock.getShell(), ioblock.getCenter().x, ioblock.getCenter().y);
			return pp;
			//return ioblock.getCenter();
		}
		else
		{
			
			return point;
		}
	}
	public void setP(Point p) {
		if (ioblock != null)
			ioblock.setCenter(p);
		else {
			point.x = p.x;
			point.y = p.y;
		}
		
		
	}
};

public class Line {//extends Composite {

	public int width = 1;
	public int r = 6;
	private String name;
	private Text text = null;
	private Point textPos;
	ConnectionPoint p1;
	 ConnectionPoint p2;
	LineListener lineListener;
	 Shell shell;
	 
	 Point old_p1=new Point(0,0);
	 Point old_p2=new Point(0,0);
 
	 
	 public Rectangle getOldRect()
	 {
	 	 if (old_p1.x==p1.getP().x && old_p1.y==p1.getP().y && old_p2.x==p1.getP().x && old_p2.y==p1.getP().y)
	 	 		return null;
	   	 else
	 {
		 int x = Math.min(old_p1.x,old_p2.x);
		 int y = Math.min(old_p1.y,old_p2.y);
		 int w = Math.abs(old_p1.x-old_p2.x);
		 int h = Math.abs(old_p1.y-old_p2.y);
			 return new Rectangle(x,y,w,h);
	 }
	 }
	 public void saveCurPoints()
	 {
		 old_p1.x = p1.getP().x;
		 old_p1.y = p1.getP().y;
		 old_p2.x = p2.getP().x;
		 old_p2.y = p2.getP().y;
		 
	 }
	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}


	 
	/**
	 * @return the shell
	 */
	public Shell getShell() {
		return shell;
	}

	/**
	 * @param shell the shell to set
	 */
	public void setShell(Shell shell) {
		this.shell = shell;
	}

	public Line( ) {
 
		//this.shell = shell;
		lineListener = new LineListener(this);
	}

	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(ConnectionPoint p1) {
		this.p1 = p1;
	}

	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(ConnectionPoint p2) {
		this.p2 = p2;
	}

	public ConnectionPoint getP1() {
		return p1;
	}



	public ConnectionPoint getP2() {
		return p2;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the textPos
	 */
	public Point getTextPos() {
		return textPos;
	}

	/**
	 * @param textPos
	 *            the textPos to set
	 */
	public void setTextPos(Point textPos) {
		this.textPos = textPos;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}





	int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r ];
		int i;
		// x^2 + y^2 = r^2
		for (i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		
	//	polygon[2 * i] = polygon[2 * 0];
	//	polygon[2 * i + 1] = polygon[2 * 0 + 1];
	//	polygon[8 * r - 2 * i - 2] = polygon[8 * r - 2 * 0 - 2];
	//	polygon[8 * r - 2 * i - 1] = polygon[8 * r - 2 * 0 - 1];
		return polygon;
	}

	public Boolean movePoints() {
		ConnectionPoint c;
		if ((p1.getP().x > p2.getP().x) || (p1.getP().y > p2.getP().y)) {
			c = p1;
			p1 = p2;
			p2 = c;
			return true;
		}
		return false;

	}

	public void plotLine(GC gc)
	{
	/*	int x1;
		int y1;
		int x2;
		int y2;
		if (p1.getBlock()!=null)
		{
			// in this case p1 is connected to block
			BlockIORect ioblock = p1.getBlock();
			//Point pp  = p1.get.getDisplay().map(ioblock.getBlock().getR2(), ioblock.getShell(), ioblock.getCenter().x, ioblock.getCenter().y);
			Point pp  = ioblock.getDisplay().map(ioblock.getBlock().getR2(), ioblock.getShell(), ioblock.getCenter().x, ioblock.getCenter().y);
			x1=pp.x;
			y1=pp.y;
			
		}
		else
		{
			 x1 = p1.getP().x;
			 y1 = p1.getP().y;
		}
		
		 x1 = p1.getP().x;
		 y1 = p1.getP().y;*/
		gc.setLineWidth(3);
 		gc.drawLine(p1.getP().x, p1.getP().y, p2.getP().x, p2.getP().y);
		gc.setLineWidth(1);
		
		gc.drawPolygon(circle(r, p1.getP().x, p1.getP().y));
		gc.drawPolygon(circle(r, p2.getP().x, p2.getP().y));
	}
	/*public void calcRegion() {
		Region region = new Region();
		region.add(line_shape());
		region.add(circle(r, p1.getP().x, p1.getP().y));
		region.add(circle(r, p2.getP().x, p2.getP().y));
		if (text==null)
			initText("undefined");
	 
		text.setLocation(textPos.x, textPos.y);
		text.pack();
		region.add(textPos.x, textPos.y, text.getClientArea().width,text.getClientArea().height);
	
		if (getRegion() != null)
			getRegion().dispose();
		setRegion(region);
	}*/

	public boolean inLine(double x0, double y0) {
		
		double a=(p1.getP().y-p2.getP().y)/(p1.getP().x-p2.getP().x+1e-10);
		double b=-1/(a+1e-10);

		

		double x=(p1.getP().y-y0-a*p1.getP().x+b*x0)/(b-a+1e-10);
		double y=p1.getP().y+a*(x-p1.getP().x);

		if  (Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0))<3)
			return true;
	
		return false;

	}
	
	public boolean inSidePoint1(double x, double y) {
		if (Math.sqrt((p1.getP().x - x) * (p1.getP().x - x) + (p1.getP().y - y) * (p1.getP().y - y)) < r)
			return true;
		return false;

	}

	public boolean inSidePoint2(double x, double y) {
		if (Math.sqrt((p2.getP().x - x) * (p2.getP().x - x) + (p2.getP().y - y) * (p2.getP().y - y)) < r)
			return true;
		return false;

	}

	public int[] line_shape() {
		int[] line = new int[10];

		double px2 = p2.getP().x;
		double px1 = p1.getP().x;
		double py2 = p2.getP().y;
		double py1 = p1.getP().y;

		double c;

		double w = 2;
		double r = Math.sqrt((py1 - py2) * (py1 - py2) + (px1 - px2)
				* (px1 - px2));
		double xx1;
		xx1 = -w;
		double yy1;
		yy1 = -w;
		double xx2;
		xx2 = +w;
		double yy2;
		yy2 = -w;
		double xx3;
		xx3 = +w;
		double yy3;
		yy3 = +w + r;
		double xx4;
		xx4 = -w;
		double yy4;
		yy4 = +w + r;

		double alpha = Math.atan((py1 - py2) / (px1 - px2));
		double alphat = Math.PI / 2 - alpha;

		double R11 = Math.cos(alphat);
		double R12 = -Math.sin(alphat);
		double R21 = Math.sin(alphat);
		double R22 = Math.cos(alphat);
		double px;
		double py;

		if (px1 < px2) {
			px = px1;
			py = py1;
		} else {
			px = px2;
			py = py2;
		}

		double ppx1 = xx1 * R11 + yy1 * R21 + px;
		double ppy1 = xx1 * R12 + yy1 * R22 + py;

		double ppx2 = xx2 * R11 + yy2 * R21 + px;
		double ppy2 = xx2 * R12 + yy2 * R21 + py;

		double ppx3 = xx3 * R11 + yy3 * R21 + px;
		double ppy3 = xx3 * R12 + yy3 * R22 + py;

		double ppx4 = xx4 * R11 + yy4 * R21 + px;
		double ppy4 = xx4 * R12 + yy4 * R22 + py;

		line[0] = (int) Math.floor(ppx1);
		line[1] = (int) Math.floor(ppy1);
		line[2] = (int) Math.floor(ppx2);
		line[3] = (int) Math.floor(ppy2);
		line[4] = (int) Math.floor(ppx3);
		line[5] = (int) Math.floor(ppy3);
		line[6] = (int) Math.floor(ppx4);
		line[7] = (int) Math.floor(ppy4);
		line[8] = (int) Math.floor(ppx1);
		line[9] = (int) Math.floor(ppy1);

		return line;
	}

	/*public Line(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		lineListener = new LineListener(this);
		pack();
		setSize(5000, 5000);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_CYAN));
		//calcRegion();
	}
*/

	public LineListener getLineListener() {
		return lineListener;
	}

	
	public void setLineListener(LineListener lineListener) {
		this.lineListener = lineListener;
	}

	void setStatus(LineStatus status)
	{
		lineListener.setStatus(status);
	}
	public void initText(String name) {

	//	Point pp1 = shell.getDisplay().map(shell, null,p1.getP().x, p1.getP().y);// new origin
		//Point pp2 = shell.getDisplay().map(shell, null,p2.getP().x, p2.getP().y); 

		final Line line = this;
		setName(name);
		if (text!=null)
		{
				text.setText(name);
				return;
		}
		text = new Text(shell, 0);
		text.setText(name);
		FontData[] fD = text.getFont().getFontData();
		fD[0].setHeight(8);
		fD[0].setStyle(SWT.NORMAL);
		text.setFont(new Font(shell.getDisplay(), fD[0]));
		text.setOrientation(SWT.LEFT_TO_RIGHT);
		text.pack();
		text.setText(name);
		textPos = new Point(p1.getP().x,p1.getP().y);
		text.setLocation(textPos);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (text.getText().isEmpty())
					text.setText("undefined");
				//calcRegion();
			}
		});

		Listener l_text = new Listener() {
			Point origin;
			
			public void handleEvent(Event e) {
				//System.out.printf("%d %d\n",(e.stateMask & SWT.CTRL),e.button);
				if (e.button != 1 &  (e.stateMask & SWT.CTRL)==0)
					return;
				switch (e.type) {
				
				case SWT.MouseEnter:
					break;
				case SWT.MouseDown:
					origin = line.getShell().getDisplay().map(line.getShell(), null, e.x, e.y);// new origin
					break;
				case SWT.MouseUp:
					origin = null;
					break;
				case SWT.MouseMove:
					if (origin != null) {
						Point p = line.getShell().getDisplay().map(line.text, null, e.x, e.y);						
					 	((Line) line).getTextPos().x = (p.x -origin.x)  ;
						((Line) line).getTextPos().y = (p.y - origin.y) ;
						((Line) line).getText().setLocation(textPos.x, textPos.y);
						((Line) line).getText().pack();	
					}
					break;
				}
			}
		};

		text.addListener(SWT.MouseDown, l_text);
		text.addListener(SWT.MouseUp, l_text);
		text.addListener(SWT.MouseMove, l_text);

	}

}

class LineListener   {

	enum LineStatus {
		Idle, Line_Move, P1_Move, P2_Move
	};

	private LineStatus status;

	/**
	  @return the status
	 */
	public LineStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(LineStatus status) {
		this.status = status;
	}

	public Line getBlock() {
		return line;
	}

	private Line line;
	private Point origin;
	private Point originTextPlace = new Point (0,0);
	private Point p;
	private Point p1 = new Point(0, 0);
	private Point p2 = new Point(0, 0);

	public LineListener(Line line) {
		this.line = line;
		/*block.addListener(SWT.MouseEnter, this);
		block.addListener(SWT.MouseDown, this);
		block.addListener(SWT.MouseUp, this);
		block.addListener(SWT.MouseMove, this);*/
		// TODO Auto-generated constructor stub
	}

	public void prepareLineMove(Point e)
	{
		p1.x = ((Line) line).p1.getP().x;
		p2.x = ((Line) line).p2.getP().x;
		p1.y = ((Line) line).p1.getP().y;
		p2.y = ((Line) line).p2.getP().y;
	//	origin =  line.getShell().getDisplay().map(line.getShell(), null, e.x, e.y);   //old
		origin =  e;//new
	//	origin =  line.getShell().getDisplay().map(null, line.getShell(), e.x, e.y);   //new 1
		originTextPlace.x = ((Line) line).getTextPos().x;
		originTextPlace.y = ((Line) line).getTextPos().y;
	 
		status = LineStatus.Idle;
		
		// first check if mouse down in one of the edges
		if (((Line) line).inSidePoint1(e.x, e.y) == true) {
			status = LineStatus.P1_Move;
			return;
		  }

		if (((Line) line).inSidePoint2(e.x, e.y) == true) {
			status = LineStatus.P2_Move;
			return;
		}
		
		// check in the line
		 if (((Line) line).inLine(e.x, e.y) == true) {
			status = LineStatus.Line_Move;
		//	line.getP1().getBlock().setLineConnection(null);
		//	line.getP2().getBlock().setLineConnection(null);
	
			 
			line.getP1().setIoblock(null);
			line.getP2().setIoblock(null);
	 
			return;
		  }
		 
		

		//line.setFocus();
		//line.moveAbove(null);

		
	}
	public void lineMove(MouseEvent e)
	{
		//Point p = line.getShell().getDisplay().map(line.getShell(), null, e.x, e.y);
		//p.x=e.x;
		//p.y=e.y;
		((Line) line).p1.getP().x = (e.x - origin.x) + p1.x;  //old p changed to e
		((Line) line).p2.getP().x = (e.x - origin.x) + p2.x;
		((Line) line).p1.getP().y = (e.y - origin.y) + p1.y;
		((Line) line).p2.getP().y = (e.y - origin.y) + p2.y;
		((Line) line).getTextPos().x = (e.x - origin.x) +originTextPlace.x ;
		((Line) line).getTextPos().y = (e.y - origin.y) +originTextPlace.y;				
		((Line) line).getText().setLocation(((Line) line).getTextPos().x, ((Line) line).getTextPos().y);
 
	}
	
	public void p2Move(Point e)
	{
		/*Point p =e;// line.getShell().getDisplay().map(line.getShell(), null, e.x, e.y);
		((Line) line).p2.getP().x = (p.x - origin.x) + p2.x;  // old
		((Line) line).p2.getP().y = (p.y - origin.y) + p2.y;  // old
		*/
		
		e= line.getShell().getDisplay().map(null,line.getShell(), e.x, e.y);
		((Line) line).p2.getP().x = e.x;  // new
		((Line) line).p2.getP().y = e.y;  // new
		//GC gc = new GC(line.getShell());
	//	 line.plotLine(gc);
		// gc.dispose();
		 
	//	((Line) line).calcRegion();	
	}
	
	public void p1Move(Point e)
	{
		/*Point p = e;// line.getShell().getDisplay().map(line.getShell(), null, e.x, e.y);
		((Line) line).p1.getP().x = (p.x - origin.x) + p1.x;
		((Line) line).p1.getP().y = (p.y - origin.y) + p1.y;*/
		
		e= line.getShell().getDisplay().map(null,line.getShell(), e.x, e.y);
		((Line) line).p1.getP().x = e.x;  // new
		((Line) line).p1.getP().y = e.y;  // new
		
		//GC gc = new GC(line.getShell());
		// line.plotLine(gc);
		// gc.dispose();
	}
	
	public boolean mouseDown(MouseEvent e)
	{
		if (e.button != 1)
			return false;
		prepareLineMove(new Point(e.x,e.y));
		if (status != LineStatus.Idle)
			return true;
		return false;
		
	}
	
	public void mouseMove(MouseEvent e)
	{
		if (status == LineStatus.Line_Move) {
			lineMove( e);	
		}
		if (status == LineStatus.P2_Move) {
			p2Move(e.display.getCursorLocation());//  new Point(e.x,e.y));
		}

		if (status == LineStatus.P1_Move) {
			p1Move(e.display.getCursorLocation());//new Point(e.x,e.y));
		}
		
	}
	
	/*public void handleEvent(Event e) {
		//Composite line = this.line;
		switch (e.type) {
		case SWT.MouseEnter:
			int gg = 4;
			gg = gg + 1;

			break;
		case SWT.MouseDown:
			if (e.button != 1)
				return;
			status = LineStatus.Line_Move;
			prepareLineMove(e);
			break;
		case SWT.MouseUp:
			status = LineStatus.Idle;
			break;
		case SWT.MouseMove:
			if (status == LineStatus.Line_Move) {
				lineMove( e);
				
				// disconnect line and connected block
				((Line)line).p1.getBlock().setLineConnection(null);
				
			}
			if (status == LineStatus.P2_Move) {
				p2Move(e);
			}

			if (status == LineStatus.P1_Move) {
				p1Move(e);
			}
			break;
		}

	}*/

}
