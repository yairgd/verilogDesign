/**
 * 
 */
package verilog.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import verilog.gui.LineConnection.ConectionPoint;
import verilog.gui.LineListener.LineStatus;

/**
 * @author yair
 * 
 */
class AddInputListener implements SelectionListener {
    private Block block;

    public AddInputListener(Block block) {
	this.block = block;

    }

    public void widgetSelected(SelectionEvent event) {
	block.addInput("in1");
	block.calcSize();
    }

    public void widgetDefaultSelected(SelectionEvent event) {
	int g = 5;
	g = g + 4;
    }
}

class AddOutputtListener implements SelectionListener {
    private Block block;

    public AddOutputtListener(Block block2) {
	this.block = block2;
    }

    public void widgetSelected(SelectionEvent event) {
	block.addOutput("out1");
	block.calcSize();
    }

    public void widgetDefaultSelected(SelectionEvent event) {
	int g = 5;
	g = g + 4;
    }
}

class BlockListener implements Listener {

    public Composite getBlock() {
	return block;
    }

    private Composite block;
    private Point origin;
    private Point p1;
    private Point p;

    public BlockListener(Composite block) {
	this.block = block;
	block.addListener(SWT.MouseEnter, this);
	block.addListener(SWT.MouseDown, this);
	block.addListener(SWT.MouseUp, this);
	block.addListener(SWT.MouseMove, this);
	// TODO Auto-generated constructor stub
    }

    public void handleEvent(Event e) {
	Composite block = this.block.getParent().getParent();

	switch (e.type) {
	case SWT.MouseEnter:
	    int gg = 4;
	    gg = gg + 1;

	    // block.pack();
	    break;
	case SWT.MouseDown:
	    if (e.button != 1)
		return;
	    block.setFocus();
	    block.moveAbove(null);
	    origin = block.getDisplay().map(block.getShell(), null, e.x, e.y);// new
									      // Point(e.x,
									      // e.y);
	    break;
	case SWT.MouseUp:
	    origin = null;
	    p1 = null;
	    break;
	case SWT.MouseMove:
	    if (origin != null) {
		Point p = block.getDisplay().map(block, null, e.x, e.y);
		block.setLocation(p.x - origin.x, p.y - origin.y);
		((Block) block).getLineList().paint();
	    }
	    break;
	}
    }

}

class BlockIORect extends Canvas {

    private LineConnection lineConnection;
    IOType iotype;
    private Text text;
    private Block block;
    private Point center = new Point(0, 0);
    private BlockIORectListener blockIORectListener;

    enum Status {
	mouseEnter, mouseExit, Idle
    };

    // private Status status = Status.Idle;

    /**
     * @return the blockIORectListener
     */
    public BlockIORectListener getBlockIORectListener() {
	return blockIORectListener;
    }

    /**
     * @param blockIORectListener
     *            the blockIORectListener to set
     */
    public void setBlockIORectListener(BlockIORectListener blockIORectListener) {
	this.blockIORectListener = blockIORectListener;
    }

    public Point getCenter() {
	return center;
    }

    public void setCenter(Point p) {
	center.x = p.x;
	center.y = p.y;

    }

    /**
     * @return the block
     */
    public Block getBlock() {
	return block;
    }

    /**
     * @param block
     *            the block to set
     */
    public void setBlock(Block block) {
	this.block = block;
    }

    public enum IOType {
	IN, OUT
    };

    public BlockIORect(Block block, int style) {
	super(block.getR2(), style);
	GridData gd = new GridData(15, 15);
	this.setLayoutData(gd);
	this.block = block;
	init();
	// TODO Auto-generated constructor stub
    }

    private void init() {
	blockIORectListener = new BlockIORectListener(this);
	addPaintListener(new PaintListener() {
	    public void paintControl(PaintEvent e) {
		Rectangle clientArea = getClientArea();
		e.gc.drawRectangle(0, 0, clientArea.width - 1,
			clientArea.height - 1);

	    }
	});
    }

    /**
     * @return the lineConnection
     */
    public LineConnection getLineConnection() {
	return lineConnection;
    }

    /**
     * @param lineConnection
     *            the lineConnection to set
     */
    public void setLineConnection(LineConnection lineConnection) {
	this.lineConnection = lineConnection;
    }

    public Text getText() {
	return text;
    }

    public void setText(Text text) {
	this.text = text;
    }

    public void setTypeAndName(String name, IOType iotype) {
	;
	text = initText(block.getR2(), name);
	this.iotype = iotype;
    }

    private Text initText(Composite parent, String ioName) {
	final Text text = new Text(parent, 0);
	text.setText(ioName);
	FontData[] fD = text.getFont().getFontData();
	fD[0].setHeight(8);
	fD[0].setStyle(SWT.NORMAL);
	// text.setFont( new Font(block.getDisplay(),fD[0]));
	text.setFont(new Font(getDisplay(), fD[0]));
	text.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		text.pack();
	    }
	});
	return text;
    }

    public boolean isMouseEnter(Point pp0) {
	Point pp1 = getDisplay().map(getParent(), getParent().getParent(),
		getLocation());
	Point pp2 = getDisplay().map(getParent().getParent(),
		getParent().getParent().getParent(), pp1);

	// Point pp1 = getDisplay().map(this, getBlock().getR2(),
	// getClientArea().x, getClientArea().y);
	// System.out.printf("%d %d %d   \n", pp0.y,pp1.y,pp2.y);

	// Math.sqrt( (p3.x-p.x)*(p3.x-p.x)+(p3.y-p.y)*(p3.y-p.y) )<10

	// System.out.printf("%d %d %d \n", pp1.x,p.x,this.getCenter().x);
	if (pp2.x >= pp0.x && pp2.y >= pp0.y
		&& pp2.x < pp0.x + getClientArea().width
		&& pp2.y < pp0.y + getClientArea().height)
	    return true;
	return false;
    }

    /*
     * public Status getStatus() { return status; }
     * 
     * 
     * public void setStatus(Status status) { this.status = status; }
     */

}

class BlockIORectListener implements Listener {

    public enum IOStatus {
	LineDrags, IDLE, MouseEnter, MouseExit
    }

    private IOStatus iostatus;

    /*
     * public BlockIORect getBlock() { return ioblock; }
     */

    /**
     * @return the iostatus
     */
    public IOStatus getIostatus() {
	return iostatus;
    }

    /**
     * @param iostatus
     *            the iostatus to set
     */
    public void setIostatus(IOStatus iostatus) {
	this.iostatus = iostatus;
    }

    private Line line1;
    // private BlockIORect ioblock;
    Point origin;

    /*
     * private Point p1 = new Point(0, 0); private Point p2 = new Point(0, 0);
     */

    public BlockIORectListener(BlockIORect ioblock) {
	// this.ioblock = ioblock;
	ioblock.addListener(SWT.MouseEnter, this);
	ioblock.addListener(SWT.MouseDown, this);
	ioblock.addListener(SWT.MouseExit, this);
	ioblock.addListener(SWT.MouseMove, this);
	ioblock.addListener(SWT.MouseUp, this);
	// TODO Auto-generated constructor stub
    }

    public void mouseEnter(BlockIORect ioblock) {
	Cursor cursor = new Cursor(ioblock.getDisplay(), SWT.CURSOR_CROSS);
	ioblock.setCursor(cursor);
	iostatus = IOStatus.MouseEnter;
    }

    public void mouseExit(BlockIORect ioblock) {
	Cursor cursor = new Cursor(ioblock.getDisplay(), SWT.CURSOR_ARROW);
	ioblock.setCursor(cursor);
	iostatus = IOStatus.MouseExit;
    }

    public void mouseMove(BlockIORect ioblock, Event e) {
	if (iostatus == IOStatus.LineDrags) {
	    ((Line) line1).getLineListener().p2Move( e.display.getCursorLocation());// (new Point(e.x, e.y));
		 
	    
	    
	    ioblock.getBlock().getLineList().paint();
	    //((Block)ioblock.getParent()).getLineList().paint();
	    
	    Point p = e.display.map(null,ioblock.getShell(), e.display.getCursorLocation());   

	   ioblock.getBlock().getLineList().scanIO(p);
		   
	    
	}
    }

    public void mouseDown(BlockIORect ioblock, Event e) {
	Cursor cursor = new Cursor(ioblock.getDisplay(), SWT.CURSOR_ARROW);
	ioblock.setCursor(cursor);
	origin = ioblock.getDisplay()
		.map(ioblock, ioblock.getShell(), e.x, e.y);// new
	// Point pp = ioblock.getDisplay().map(ioblock.getBlock().getR2(),
	// ioblock.getShell(), ioblock.getCenter().x, ioblock.getCenter().y);
	// Point uu2 = ioblock.getDisplay().map(ioblock.getParent(),
	// ioblock.getShell(), ioblock.getCenter().x, ioblock.getCenter().y);
	Point pp = e.display.getCursorLocation();
	pp = ioblock.getDisplay().map(null, ioblock.getShell(), pp.x, pp.y);
	line1 = new Line();
	// Point pp = ioblock.getDisplay().map(ioblock.getBlock().getR2(),
	// ioblock.getShell(), ioblock.getCenter().x, ioblock.getCenter().y);

	line1.setP1(new ConnectionPoint(ioblock.getCenter(), ioblock));
	line1.setP2(new ConnectionPoint(pp, null));
	line1.setStatus(LineStatus.P2_Move);
	
	//(curLine.getLineListener().getStatus()==LineStatus.P2_Move)
	
	
	ioblock.getBlock().getLineList().addLine(line1, "undefined");
	ioblock.setLineConnection(new LineConnection(line1, ConectionPoint.P1));
	ioblock.getBlock().getLineList().setCurLine(line1);
	//((Line) line1).getLineListener().prepareLineMove(pp);// new Point(e.x,
							     // e.y));
	
	
	iostatus = IOStatus.LineDrags;
	ioblock.getBlock().getLineList().paint();
    }

    public void handleEvent(Event e) {

	BlockIORect ioblock = (BlockIORect) e.widget;
	switch (e.type) {
	case SWT.MouseEnter:
	    mouseEnter(ioblock);
	    break;
	case SWT.MouseExit:
	    mouseExit(ioblock);
	    break;
	case SWT.MouseUp:
	    Point p = ioblock.getDisplay().map(null, ioblock.getShell(), e.display.getCursorLocation());
	    ioblock.getBlock().getLineList().mouseUp(p);
	    iostatus = IOStatus.IDLE;
	    break;
	case SWT.MouseDown:
	    mouseDown(ioblock, e);
	    break;
	case SWT.MouseMove:
	    
	    mouseMove(ioblock, e);
	    break;
	}

    }

}

public class Block extends Composite {

    // private Composite composite;
    // private Text text;
    private ArrayList<BlockIORect> inputs = new ArrayList<BlockIORect>();

    /**
     * @return the inputs
     */
    public ArrayList<BlockIORect> getInputs() {
	return inputs;
    }

    /**
     * @param inputs
     *            the inputs to set
     */
    public void setInputs(ArrayList<BlockIORect> inputs) {
	this.inputs = inputs;
    }

    /**
     * @return the outputs
     */
    public ArrayList<BlockIORect> getOutputs() {
	return outputs;
    }

    /**
     * @param outputs
     *            the outputs to set
     */
    public void setOutputs(ArrayList<BlockIORect> outputs) {
	this.outputs = outputs;
    }

    private ArrayList<BlockIORect> outputs = new ArrayList<BlockIORect>();

    private Composite r1;
    private Composite r2;

    /**
     * @return the r2
     */
    public Composite getR2() {
	return r2;
    }

    /**
     * @param r2
     *            the r2 to set
     */
    public void setR2(Composite r2) {
	this.r2 = r2;
    }

    private Text unitName;
    private Text instaneName;
    private LineList lineList;

    /**
     * @return the lineList
     */
    public LineList getLineList() {
	return lineList;
    }

    /**
     * @param lineList
     *            the lineList to set
     */
    public void setLineList(LineList lineList) {
	this.lineList = lineList;
    }

    int marginX = 15;
    int marginY = 5;
    int widthY = 15;
    int widthX = 15;
    int sizeY;
    int sizeX;
    // private Point location;
    // private Point size=null;

    private static ResourceBundle verilogGui = ResourceBundle
	    .getBundle("verilog_gui");// "verilog/gui/verilog_gui");

    /**
     * @param parent
     * @param style
     */
    public Block(Composite parent, int style) {
	super(parent, style);
	// TODO Auto-generated constructor stub
	// this.composite = parent;
	// this.getS

	GridLayout rowLayout = new GridLayout();
	// rowLayout.wrap = false;
	// rowLayout.pack = false;
	// rowLayout.justify = true;
	// rowLayout.type = SWT.HORIZONTAL;
	rowLayout.numColumns = 1;
	rowLayout.marginLeft = 0;
	rowLayout.marginTop = 0;
	rowLayout.marginRight = 0;
	rowLayout.marginBottom = 0;
	// rowLayout.spacing = 0;
	this.setLayout(rowLayout);

	 unitName = initText(this, "unit");
	r2 = new Composite(this, 0);
	  instaneName = initText(this, "instance");

	r1 = new Composite(r2, 0);
	new BlockListener(r1);
	initPopUpMenu(r1);
	r1.setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));
	r1.addPaintListener(new PaintListener() {
	    public void paintControl(PaintEvent e) {
		Rectangle clientArea = r1.getClientArea();
		e.gc.setLineWidth(2);
		e.gc.drawRectangle(0, 0, clientArea.width - 1,
			clientArea.height - 1);
	    }
	});

    }

    public void addInput(String str) {
	BlockIORect io = new BlockIORect(this, 0);
	io.setTypeAndName(str, BlockIORect.IOType.IN);
	lineList.addIo(io);
	inputs.add(io);
    }

    public void addOutput(String str) {
	BlockIORect io = new BlockIORect(this, 0);
	io.setTypeAndName(str, BlockIORect.IOType.OUT);
	lineList.addIo(io);
	outputs.add(io);
    }

    public void resize(Point location, Point size) {

	// this.size = size;
	// this.location = location;
	setLocation(location);
	r2.setSize(size);
	setSize(size);

    }

    public void calcSize() {
	Region region = new Region();
	sizeY = this.getSize().y;
	sizeX = this.getSize().x;
	int spaceY;

	/*
	 * if (this.size == null) { sizeY = this.getSize().y; sizeX =
	 * this.getSize().x; } else { sizeY = this.size.y; sizeX = this.size.x;
	 * }
	 */

	// calculate space y between io
	int m = Math.max(inputs.size(), outputs.size());
	if (m > 1)
	    spaceY = (sizeY - 2 * marginY - m * widthY) / (m - 1);
	else
	    spaceY = (sizeY - 2 * marginY - m * widthY);

	// resize main composite
	r1.setSize(sizeX - 2 * marginX, sizeY);
	r1.setLocation(marginX, 0);

	// region.add(new Rectangle(marginX+5,5,sizeX-2*marginX-1,sizeY+5));

	// Point p1 = this.getDisplay().map(r2, this, new Point(marginX,0));
	// region.add(new Rectangle(p1.x,p1.y,sizeX-2*marginX,widthY));

	// resize inputs
	int y = (sizeY - (spaceY) * (inputs.size() - 1) - inputs.size()
		* widthY) / 2;
	for (BlockIORect io : inputs) {
	    io.getText().moveAbove(r1);
	    io.getText().setLocation(marginX, y);
	    io.getText().pack();
	    io.setSize(widthX, widthY);
	    io.setLocation(0, y);
	    io.setCenter(new Point(6, y + 6));
	    region.add(new Rectangle(0 + 5, y + 5, widthX, widthY));
	    y = y + spaceY + widthY;
	}

	// resize outputs
	y = (sizeY - (spaceY) * (outputs.size() - 1) - outputs.size() * widthY) / 2;
	for (BlockIORect io : outputs) {
	    io.getText().moveAbove(r1);
	    io.getText().setLocation(sizeX - marginX - 30, y);
	    io.getText().pack();
	    io.setSize(widthX, widthY);
	    io.setCenter(new Point(sizeX - marginX + 6, y + 6));
	    io.setLocation(sizeX - marginX, y);
	    region.add(new Rectangle(sizeX - marginX + 5, y + 5, widthX, widthY));
	    // Point p = this.getDisplay().map(r2, this, new
	    // Point(sizeX-marginX-30,y));
	    // region.add(new Rectangle(p.x,y,widthX,widthY));

	    y = y + spaceY + widthY;
	}

	region.add(new Rectangle(5 + 15, 5, sizeX - 15 - 15, sizeY));
	//setRegion(region);

	// set general object location and size
	// setSize(sizeX,sizeY);

	// r2.pack();
	pack();
	/*
	 * if (this.size == null) { size=this.getSize(); } else { //size.y =
	 * getSize().y; } setSize(size.x,getSize().y);
	 */

    }

    public Composite getR1() {
	return r1;
    }

    public void setR1(Composite r1) {
	this.r1 = r1;
    }

    private void initPopUpMenu(Composite c1) {
	Menu menu = new Menu(getShell(), SWT.POP_UP);
	MenuItem item1 = new MenuItem(menu, SWT.PUSH);
	item1.addSelectionListener(new AddInputListener(this));
	item1.setText(verilogGui.getString("add_input"));
	MenuItem item2 = new MenuItem(menu, SWT.PUSH);
	item2.setText(verilogGui.getString("add_output"));
	item2.addSelectionListener(new AddOutputtListener(this));
	c1.setMenu(menu);
    }

    private Text initText(Composite parent, String ioName) {
	final Text text = new Text(parent, 0);
	text.setText(ioName);
	FontData[] fD = text.getFont().getFontData();
	fD[0].setHeight(8);
	fD[0].setStyle(SWT.NORMAL);
	text.setFont(new Font(this.getDisplay(), fD[0]));
	text.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));

	text.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		text.pack();
		// text.getParent().pack();
	    }
	});

	return text;
    }

    public static void main(String[] args) {
	// TODO Auto-generated method stub

	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.open();

	/*
	 * shell.setText("Canvas Example"); // shell.setLayout(new
	 * FillLayout()); Canvas canvas = new Canvas(shell, SWT.NONE);
	 * canvas.addPaintListener(new PaintListener() { public void
	 * paintControl(PaintEvent e) { e.gc.setLineWidth(4); int[] points = {
	 * 0, 0, 100, 0, 0, 100, 100, 100, 0, 200}; e.gc.drawPolyline(points); }
	 * }); canvas.pack(); // new BlockListener(canvas);
	 */

	final LineList lineList = new LineList(shell);
	Line line = new Line();
	line.setP1(new ConnectionPoint(new Point(300, 300), null));
	line.setP2(new ConnectionPoint(new Point(400, 400), null));
	lineList.addLine(line, "line1");

	Line line1 = new Line();
	line1.setP2(new ConnectionPoint(new Point(200, 300), null));
	line1.setP1(new ConnectionPoint(new Point(100, 300), null));
	lineList.addLine(line1, "line2");

	/*
	 * Line line = new Line(shell,0) ; line.setP1(new ConnectionPoint(new
	 * Point(300,300),null)); line.setP2(new ConnectionPoint(new
	 * Point(400,400),null)); // line.initText("line1"); line.calcRegion();
	 * 
	 * 
	 * Line line1 = new Line(shell,0) ; line1.setP1(new ConnectionPoint(new
	 * Point(200,200),null)); line1.setP2(new ConnectionPoint(new
	 * Point(200,200),null)); line1.initText("line2"); line1.calcRegion();
	 */

	final Block block1 = new Block(shell, 0);
	block1.setLineList(lineList);
	block1.addInput("in1");
	block1.addInput("in2");
	block1.addInput("in3");
	block1.addInput("in4");
	block1.addInput("in5");
	block1.addInput("in6");
	block1.addInput("in7");
	block1.addInput("in8");
	block1.addOutput("out1");
	block1.addOutput("out2");
	block1.addOutput("out3");
	block1.getR1().setToolTipText("Block1");
	block1.resize(new Point(50, 50), new Point(100, 250));
	block1.calcSize();
	// block1.pack();

	// block1.moveAbove(shell);
	final Block block2 = new Block(shell, 0);
	block2.setLineList(lineList);
	block2.addInput("in1");
	block2.addInput("in2");
	block2.addInput("in3");
	block2.addOutput("out1");
	block2.addOutput("out2");
	block2.addOutput("out3");
	block2.addOutput("out4");
	block2.getR1().setToolTipText("Block2");
	block2.resize(new Point(150, 150), new Point(150, 100));
	block2.calcSize();
	// block2.pack();

	while (!shell.isDisposed()) {
	    display.readAndDispatch();
	    /*
	     * if (!display.readAndDispatch()) display.sleep();
	     */
	}
	display.dispose();
    }

    public void GetEvent() {
	// TODO Auto-generated method stub

    }

    public void SendEvent() {
	// TODO Auto-generated method stub

    }

}
