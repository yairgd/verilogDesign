/**
 * 
 */
package verilog.gui;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author yair
 * 
 */
public final class Block extends Canvas {

	// private Composite composite;
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
		initPopUpMenu();
		new BlockListener(this);

		this.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = getClientArea();
				e.gc.drawRectangle(0, 0, clientArea.width - 1,
						clientArea.height - 1);
			}
		});

	}

	private void initBlockOutputs() {
		RowLayout rowLayout = new RowLayout();
		// rowLayout.wrap = false;
		// rowLayout.pack = false;
		rowLayout.justify = true;
		rowLayout.type = SWT.VERTICAL;
		rowLayout.marginLeft = 0;
		rowLayout.marginTop = 5;
		rowLayout.marginRight = 5;
		rowLayout.marginBottom = 5;
		rowLayout.spacing = 10;
		
		// Optionally set layout fields.
		// rowLayout.wrap = true;
		// Set the layout into the composite.
		this.setLayout(rowLayout);
		// Create the children of the composite.

		new BlockOutput(this, 0);
		new BlockOutput(this, 0);
		 
		

		// blockIORect.setLayout(layout)

	}
	
	private void initBlockInputs( ) {
		RowLayout rowLayout = new RowLayout();
		// rowLayout.wrap = false;
		// rowLayout.pack = false;
		rowLayout.justify = true;
		rowLayout.type = SWT.VERTICAL;
		rowLayout.marginLeft = 0;
		rowLayout.marginTop = 5;
		rowLayout.marginRight = 5;
		rowLayout.marginBottom = 5;
		rowLayout.spacing = 10;
 
		// Optionally set layout fields.
		// rowLayout.wrap = true;
		// Set the layout into the composite.
		this.setLayout(rowLayout);
		// Create the children of the composite.

		new BlockInput(this, 0);
		new BlockInput(this, 0);
		new BlockInput(this, 0);
		new BlockInput(this, 0);
		new BlockInput(this, 0);
		 
		

		// blockIORect.setLayout(layout)

	}

	private void initPopUpMenu() {
		Menu menu = new Menu(getShell(), SWT.POP_UP);
		MenuItem item1 = new MenuItem(menu, SWT.PUSH);
		item1.setText(verilogGui.getString("input"));
		MenuItem item2 = new MenuItem(menu, SWT.PUSH);
		item2.setText(verilogGui.getString("output"));
		this.setMenu(menu);
	}

	public void init(String tooltip, Point location) {

		RowLayout rowLayout = new RowLayout();
		// rowLayout.wrap = false;
		// rowLayout.pack = false;
		rowLayout.justify = true;
		rowLayout.type = SWT.VERTICAL;
		rowLayout.marginLeft = 0;
		rowLayout.marginTop = 5;
		rowLayout.marginRight = 5;
		rowLayout.marginBottom = 5;
		rowLayout.spacing = 10;
		
		
		initBlockInputs();
		
	//	rowLayout.se
		//new Text("unit0").setLocation(location)
		pack();
		
		setToolTipText(tooltip);		
		setLocation(location.x, location.y);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.open();

		final Block block1 = new Block(shell, 0);
		block1.init("Block1", new Point(50, 50));

		final Block block2 = new Block(shell, 0);
		block2.init("Block2", new Point(150, 150));

		new BlockInput(shell, 0);

		while (!shell.isDisposed()) {
			display.readAndDispatch();
			/*if (!display.readAndDispatch())
				display.sleep();*/
		}
		display.dispose();
	}

}
