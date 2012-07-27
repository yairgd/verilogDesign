package verilog.gui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import verilog.gui.BlockIORect.Status;
import verilog.gui.BlockIORectListener.IOStatus;
import verilog.gui.LineConnection.ConectionPoint;
import verilog.gui.LineListener.LineStatus;

public class LineList {

	private Shell shell;
	private ArrayList<Line> lineList = new ArrayList<Line>();
	private ArrayList<Block1> blockList = new ArrayList<Block1>();
	private Line curLine;
	/**
	 * @return the curLine
	 */
	public Line getCurLine() {
	    return curLine;
	}

	/**
	 * @param curLine the curLine to set
	 */
	public void setCurLine(Line curLine) {
	    this.curLine = curLine;
	}
	private BlockIORect curIO;
	
	private ArrayList<BlockIORect> ioList = new ArrayList<BlockIORect>();
	

	/**
	 * @return the ioList
	 */
	public ArrayList<BlockIORect> getIoList() {
		return ioList;
	}

	/**
	 * @param ioList the ioList to set
	 */
	public void setIoList(ArrayList<BlockIORect> ioList) {
		this.ioList = ioList;
	}

	LineList(Shell shell) {
		this.shell = shell;
		final LineList lineList = this;
		shell.addMouseMoveListener(new MouseMoveListener() {

			public void mouseMove(MouseEvent e) {
				// TODO Auto-generated method stub
				lineList.mouseMove(e);
			}
		});

		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				lineList.paint();
			}
		});

		shell.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent e) {
			    lineList.mouseUp(e.display.getCursorLocation());
				
				
			}

			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				lineList.mouseDown(e);
			}

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void mouseUp(Point pp) //MouseEvent e)
	{
	    	// TODO Auto-generated method stub
	    	if (curLine != null) {
	    	    if (curIO!=null)
	    	    {
	    		//Point pp = e.display.getCursorLocation();
		    	pp = shell.getDisplay().map(null, shell, pp.x, pp.y);
	    		if (curLine.getLineListener().getStatus()==LineStatus.P2_Move)
	    		{
	    		    curLine.setP2(new ConnectionPoint(pp, curIO));
	    		    curLine.setStatus(LineStatus.Idle);
	    		}
	    		if (curLine.getLineListener().getStatus()==LineStatus.P1_Move)
	    		{
	    		    curLine.setP1(new ConnectionPoint(pp, curIO));
	    		    curLine.setStatus(LineStatus.Idle);
	    		}
	    	    }
	    	  
	    	    curLine.getLineListener().setStatus(LineStatus.Idle);			
	    	    curLine = null;
		}
	    	 Cursor cursor = new Cursor(shell.getDisplay(), SWT.CURSOR_ARROW);
		 shell.setCursor(cursor);
	}
	public void addLine(Line line, String text) {
		lineList.add(line);
		line.setShell(shell);
		line.initText(text);
	}
	
	public void addIo (BlockIORect io) {
		ioList.add(io);
	}


	public void paint() {

		GC gc = new GC(shell);
		// clear the shell
		Rectangle clientRect = shell.getClientArea();
		gc.setClipping(clientRect);
	 	gc.fillRectangle(clientRect);
		for (Line line : lineList) {
			/*if (line.getOldRect()!=null)
			{
				// clear the shell
				Rectangle clientRect1 = line.getOldRect();
				//System.out.printf("%d %d\n", clientRect1.x,clientRect1.y);
				gc.setClipping(clientRect1);
				gc.fillRectangle(clientRect1);
			}*/	
				
			// plot line in new place
			line.plotLine(gc);
			line.saveCurPoints();
			
		}
		gc.dispose();
	}
	
	
	

	public void mouseDown(MouseEvent e) {
		for (Line line : lineList) {
			if (line.getLineListener().mouseDown(e) == true) {
				curLine = line;
				break;
			}
		}
		
	}

	public void scanIO(Point p)
	{
	 //   Point p = e.display.map(null, curLine.getShell(), e.display.getCursorLocation());
		
		for (BlockIORect io : ioList) {
			/*Point p1 = e.display.map(io,io.getParent() ,io.getLocation());
			Point p2 = e.display.map(io.getParent(),io.getParent().getParent() ,p1);
			Point p3 = e.display.map(io.getParent().getParent(),io.getParent().getParent().getParent() ,p2);
			Point p4 = e.display.map(io.getParent().getParent().getParent(),io.getParent().getParent().getParent().getParent() ,p3);
			Point p5 = e.display.map(null,curLine.getShell(),p4);
*/
			//System.out.printf("%d %d %d  %d %f\n", p.y,p1.y,p2.y,p3.y,Math.sqrt( (p2.x-p.x)*(p2.x-p.x)+(p2.y-p.y)*(p2.y-p.y)  ));
			//break;
		  	if (io.isMouseEnter(p) && io.getBlockIORectListener().getIostatus()!=IOStatus.MouseEnter) {
		//	if (Math.sqrt( (p3.x-p.x)*(p3.x-p.x)+(p3.y-p.y)*(p3.y-p.y)  )<10){
		 	 	//System.out.printf("%d %d %d %f %s \n", p.y,p1.y,p2.y,Math.sqrt( (p2.x-p.x)*(p2.x-p.x)+(p2.y-p.y)*(p2.y-p.y)  ),io.getText().getText());
		 	 	//io.getBlockIORectListener().setIostatus(IOStatus.MouseEnter); 
		 	 	 io.getBlockIORectListener().mouseEnter(io);
		 	 	 
		 		Cursor cursor = new Cursor(shell.getDisplay(), SWT.CURSOR_CROSS);
		 		shell.setCursor(cursor);
		 		curIO = io;
			//	int f = 4;
		//		f++;
		//		return;
		 	//	 System.out.printf("%d %d %d %f %s \n", p.y,p1.y,p2.y,Math.sqrt( (p2.x-p.x)*(p2.x-p.x)+(p2.y-p.y)*(p2.y-p.y)  ),io.getText().getText());
		 		
		 		System.out.printf(" %s \n", io.getText().getText());
		 		
		 	}
		  	 
		  	if (!io.isMouseEnter(p) && io.getBlockIORectListener().getIostatus()==IOStatus.MouseEnter) {
				//	if (Math.sqrt( (p3.x-p.x)*(p3.x-p.x)+(p3.y-p.y)*(p3.y-p.y)  )<10){
				// System.out.printf("%d %d %d %f  \n", p.y,p1.y,p2.y,Math.sqrt( (p2.x-p.x)*(p2.x-p.x)+(p2.y-p.y)*(p2.y-p.y)  ));
				// io.getBlockIORectListener().setIostatus(IOStatus.MouseExit); 
				 io.getBlockIORectListener().mouseExit(io);
					 
				 Cursor cursor = new Cursor(shell.getDisplay(), SWT.CURSOR_ARROW);
			 	shell.setCursor(cursor);
			 	curIO=null;
			 	//LineConnection lineConnection = io.getLineConnection();
			 	
			 	//curIO 
					
					//	int f = 4;
				//		f++;
				//		return;
			}

		}
	}
	public void mouseMove(MouseEvent e) {
		if (curLine != null) {
			curLine.getLineListener().mouseMove(e);
			Point p = e.display.map(null, curLine.getShell(), e.display.getCursorLocation());
			scanIO( p);
			paint();
		}

		

	}
}
 

 
