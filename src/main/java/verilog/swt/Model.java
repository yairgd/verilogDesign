package verilog.swt;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
 


class AddModelListener implements SelectionListener {
	private Shell shell;

	public AddModelListener(Shell shell) {
		this.shell = shell;
		

	}
	public void widgetSelected(SelectionEvent event) {
		Point p = shell.getDisplay().map(shell , null, event.x, event.y);// new

		final Block block1 = new Block(shell, 0);
		block1.addInput("in1");
		block1.addOutput("out1");
		block1.getR1().setToolTipText("Block1");
		block1.resize(  p,new Point(50, 50));
		block1.calcSize();
	}

	public void widgetDefaultSelected(SelectionEvent event) {

	}
}

public class Model {

	private static ResourceBundle verilogGui = ResourceBundle
			.getBundle("verilog_gui");// "verilog/gui/verilog_gui");
	private Shell shell;

	public Model(Shell parent) {
		// TODO Auto-generated constructor stub

		this.shell = parent;

		initPopUpMenu();

	}

	private void initPopUpMenu() {
		Menu menu = new Menu(shell, SWT.POP_UP);
		MenuItem item1 = new MenuItem(menu, SWT.PUSH);
		item1.addSelectionListener(new AddModelListener(shell));
		item1.setText(verilogGui.getString("new_block"));
		shell.setMenu(menu);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.open();

		Model model = new Model(shell);
		 
		/*ToolSettings toolSettings = new ToolSettings();
		new PolyLineTool(toolSettings, shell);
		*/
		while (!shell.isDisposed()) {
			display.readAndDispatch();
			/*
			 * if (!display.readAndDispatch()) display.sleep();
			 */
		}
		display.dispose();
	}
}
