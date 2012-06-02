/**
 * 
 */
package verilog.gui;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * @author yair
 * 
 */

class IOBlock {
	private BlockIORect block ;//= new BlockIORect(this,0);
	private Text text;//= initText(this,str);
	public BlockIORect getBlock() {
		return block;
	}
	public void setBlock(BlockIORect block) {
		this.block = block;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	IOBlock(Block block,String name)
	{
		this.block = new BlockIORect(block,0);
		text=initText(block,name);
		
	}
	private  Text initText(Composite parent,String ioName) {
		final Text text = new Text(parent, 0);
		text.setText(ioName);
		FontData[] fD = text.getFont().getFontData();
		fD[0].setHeight(8);
		fD[0].setStyle(SWT.NORMAL);
		text.setFont( new Font(block.getDisplay(),fD[0]));

		
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				text.pack();
			}
		});
		return text;
	}
}

public final class Block extends Composite {

	// private Composite composite;
	private Text text;
	private ArrayList<IOBlock> inputs = new ArrayList<IOBlock>();
	private ArrayList<IOBlock> outputs = new ArrayList<IOBlock>();

	private Composite r1;
	int marginX=15;
 	int marginY=5;
	int widthY=15;
	int widthX=15;
	
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
		 r1 = new Composite(this, 0);
		
		new BlockListener(r1);
		initPopUpMenu(r1);
		
		setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));
		
		this.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = getClientArea();
				e.gc.drawRectangle(0, 0, clientArea.width - 1,
						clientArea.height - 1);
			}
		});
		
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = getClientArea();
				e.gc.drawRectangle(0, 0, clientArea.width - 1,
						clientArea.height - 1);
			}
		});
		

	}
	public void addInput(String str)
	{
		
		inputs.add(new IOBlock(this,str));
	}
	
	public void addOutput(String str)
	{
		outputs.add(new IOBlock(this,str));
	}
	
	
	public void resize(String tooltip, Point location,Point size)
	{
		
		
		int sizeY = size.y; 
		int sizeX=size.x;	
		
		// calculate space y between io
		int m = Math.max(inputs.size(), outputs.size());
		int spaceY=(sizeY-2*marginY -m* widthY)/(m-1);
		
		// resize main composite
		r1.setSize(sizeX-2*marginX,sizeY);
		r1.setLocation(marginX,0);
		r1.setToolTipText(tooltip);
		
 
		// resize inputs
		int y= (sizeY-(spaceY)*(inputs.size()-1)-inputs.size()* widthY)/2;
		for (IOBlock io: inputs)
		{
			io.getText().moveAbove(r1);
			io.getText().setLocation(marginX,y);
			io.getText().pack();
			io.getBlock().setSize(widthX, widthY);
			io.getBlock().setLocation(0,y);
			y=y+spaceY+widthY;
		}
		
		// resize outputs
		 y = (sizeY - (spaceY) * (outputs.size() - 1) - outputs.size()	* widthY) / 2;	
		for (IOBlock io : outputs) {
			io.getText().moveAbove(r1);
			io.getText().setLocation(sizeX-marginX-30, y);
			io.getText().pack();
			io.getBlock().setSize(widthX, widthY);
			io.getBlock().setLocation(sizeX-marginX, y);
			y = y + spaceY + widthY;
		}
		
		// set general object location and size
		setSize(sizeX,sizeY);			
		setLocation(location.x, location.y);
	}

	
	
	
	private void initBlockOutputs( Composite parent) {
	 		final Composite  composite =  new Composite(parent,0);
	
		  GridLayout gridLayout = new GridLayout();
		    gridLayout.numColumns = 1;
		    gridLayout.makeColumnsEqualWidth = false;
		    gridLayout.verticalSpacing=0;
		    gridLayout.marginLeft=0;
		    gridLayout.horizontalSpacing=0;
		    gridLayout.marginTop=0;
		   // gridLayout.marginWidth=0;
		    composite.setLayout(gridLayout);
		    
		 
		new BlockOutput(composite, 0).init("in1");
		 new BlockOutput(composite, 0).init("in2");
		 
	
	 	composite.pack();
		

	 	
	}
	private void initBlockInputs( Composite parent) {
	//	org.eclipse.swt.widgets.C
		final Composite  composite =  new Composite(parent,0);
	//	composite.moveBelow(this);
	/*	RowLayout rowLayout = new RowLayout();
		// rowLayout.wrap = false;
		// rowLayout.pack = false;
		rowLayout.justify = true;
		rowLayout.type = SWT.VERTICAL;
		rowLayout.marginLeft = 0;
		rowLayout.marginTop = 5;
		rowLayout.marginRight = 5;
		rowLayout.marginBottom = 5;
		rowLayout.spacing = 10;*/
	//	composite.setLayout(rowLayout);
		
		// Optionally set layout fields.
		// rowLayout.wrap = true;
		// Set the layout into the composite.
		
		// Create the children of the composite.

		  GridLayout gridLayout = new GridLayout();
		    gridLayout.numColumns = 1;
		    gridLayout.makeColumnsEqualWidth = false;
		    gridLayout.verticalSpacing=0;
		    gridLayout.marginLeft=0;
		    gridLayout.horizontalSpacing=0;
		    gridLayout.marginTop=0;
		   // gridLayout.marginWidth=0;
		    composite.setLayout(gridLayout);
		    
		   
		    
		    
		//composite
		
		
		new BlockInput(composite, 0).init("in1");
		 new BlockInput(composite, 0).init("in2");
		 
		/* createBlockInput(composite,"in1");
		 createBlockInput(composite,"in2");*/
		 
		// composite.pack();
		/*new BlockInput(composite, 0);
		new BlockInput(composite, 0);
		new BlockInput(composite, 0);*/
		
	 	composite.pack();
		//pararent.pack();
 
		// blockIORect.setLayout(layout)
 //Block ParentClass=this;
	 /*	this.addListener(SWT.MouseExit, new Listener() {
	        @Override
	        public void handleEvent(Event event) {
	            for (Control control : composite.this.getChildren()) {
	                if (control == event.item)
	                    return;
	            }
	            // handler logic goes here
	        }           
	    });
*/ 

	 	
	}

	/*private void createBlockInput(Composite parent,String ioName)
	{
		 
       
	    
 
     //   BlockIORect blockIORect =  new BlockIORect(parent,0);
      //   blockIORect.setLayoutData(new GridData(15, 15));
        initText(parent,ioName);
	}
	*/
	private  Text initText(Composite parent,String ioName) {
		final Text text = new Text(parent, 0);
		text.setText(ioName);
		FontData[] fD = text.getFont().getFontData();
		fD[0].setHeight(8);
		fD[0].setStyle(SWT.NORMAL);
		text.setFont( new Font(this.getDisplay(),fD[0]));

		
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Control p=text;
				while (true)
				{
					p=p.getParent();
					if (p.getClass()==Shell.class)
						break;
					p.pack();
				}

			}
		});
		return text;
	}
	
	private void initPopUpMenu(Composite c1) {
		Menu menu = new Menu(getShell(), SWT.POP_UP);
		MenuItem item1 = new MenuItem(menu, SWT.PUSH);
		item1.setText(verilogGui.getString("input"));
		MenuItem item2 = new MenuItem(menu, SWT.PUSH);
		item2.setText(verilogGui.getString("output"));
		c1.setMenu(menu);
	}

	private void initBlockName(String str,Composite parent) {
		text = new Text(parent, 0);
		text.setText(str);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Control p=text;
				while (true)
				{
					p=p.getParent();
					if (p.getClass()==Shell.class)
						break;
					p.pack();
				}
			}
		});
		text.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));

	}
	
	public void init(String tooltip, Point location) {

		/*RowLayout rowLayout = new RowLayout();
		// rowLayout.wrap = false;
		 // rowLayout.pack = false;
		
		rowLayout.justify = true;
		rowLayout.type = SWT.HORIZONTAL;
		rowLayout.marginLeft = 0;
		rowLayout.marginTop = 5;
		rowLayout.marginRight = 5;
		rowLayout.marginBottom = 5;
		rowLayout.spacing = 10;*/
		
		/*  GridLayout gridLayout = new GridLayout();
		    gridLayout.numColumns = 3;
		    gridLayout.makeColumnsEqualWidth = true;
		    this.setLayout(gridLayout);*/
		    
	///	Composite parent = new Composite(this, 0);
		//parent.setLayout(rowLayout);
	///	parent.setLayout(gridLayout);
		
		initBlockName("unit",this);
		
		 GridLayout gridLayout = new GridLayout();
		    gridLayout.numColumns = 2;
		    gridLayout.makeColumnsEqualWidth = true;
		    gridLayout.marginLeft = -5;
		    gridLayout.marginRight = -5;
		    gridLayout.verticalSpacing=0;
		    gridLayout.horizontalSpacing=0;
		    gridLayout.marginTop=-5;
		    gridLayout.marginBottom=-5;
		    
	 	//this.setLayout(rowLayout);
		    Composite parent = new Composite(this,0);
		      initBlockInputs(parent);
	//	initBlockName("unit",this);
		    initBlockOutputs(parent);
		    parent.setLayout(gridLayout);
		    parent.pack();
		    
		    parent.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLUE));
			
		    
		    GridLayout gridLayout1 = new GridLayout();
		    gridLayout1.numColumns = 1;
		 //   gridLayout1.makeColumnsEqualWidth = true;
		    gridLayout1.marginLeft = -5;
		    gridLayout1.marginRight = -5;
		    gridLayout1.verticalSpacing=0;
		    gridLayout1.horizontalSpacing=0;
		    gridLayout1.marginTop=-5;
		    gridLayout1.marginBottom=-5;
		    this.setLayout(gridLayout1);
		    
		    
		    this.pack();
		 //   packAll(this);
		    
		  /*
		    for (Control control : this.getChildren()) {
            	control.pack();
            }*/
	            
	            // handler logic goes here
	      
		    
		    
		//
		   // parent.pack();
		   // this.pack();
	//	this.setSize(150,150);
		;
	//	rowLayout.se
		//new Text("unit0").settLocation(location)
		//parent.pack();
	//	pack();
	
/* Control []crt =  this.getChildren();
 for (int i=0;i<crt.length;i++)
 {
	 crt[i].pack();
	 Control []crt1 = ((Composite) crt[i]).getChildren();
	 for (int j=0;j<crt1.length;j++)
	 {
		 crt1[j].pack();
	 }
 }
 pack();
 getParent().pack();*/
		// inputs.pack();
		setToolTipText(tooltip);		
		setLocation(location.x, location.y);
		this.setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));

	}

	/**
	 * @param args
	 */
	public static void packAll(Composite parent) {
		
		if (parent.getClass() != Composite.class || parent.getClass() !=Block.class ) 
			return;
		//if (parent.getClass()==Text.class)
		//	return;
			for (Control control :  ((Composite) parent).getChildren()) {
				//control.pack();
				{
				//if (control != null) {	
					packAll((Composite) control);
				//}
				}

			}
		//}
		parent.pack();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.open();

		final Block block1 = new Block(shell, 0);
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
		block1.resize("Block1", new Point(50, 50),new Point(100, 250));

		final Block block2 = new Block(shell, 0);
		block2.addInput("in1");
		block2.addInput("in2");
		block2.addInput("in3");
		block2.addOutput("out1");
		block2.addOutput("out2");
		block2.addOutput("out3");
		block2.addOutput("out4");
		block2.resize("Block2", new Point(150, 150),new Point(150, 100));

		new BlockInput(shell, 0);

		
		
		while (!shell.isDisposed()) {
			display.readAndDispatch();
			/*if (!display.readAndDispatch())
				display.sleep();*/
		}
		display.dispose();
	}

}
