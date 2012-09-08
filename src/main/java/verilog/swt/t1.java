package verilog.swt;


/*
 * Tracker example snippet: create a tracker (drag on mouse down)
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */

 
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class t1 {

	static int[] circle(int r, int offsetX, int offsetY) {
	    int[] polygon = new int[8 * r + 4];
	    // x^2 + y^2 = r^2
	    for (int i = 0; i < 2 * r + 1; i++) {
	      int x = i - r;
	      int y = (int) Math.sqrt(r * r - x * x);
	      polygon[2 * i] = offsetX + x;
	      polygon[2 * i + 1] = offsetY + y;
	      polygon[8 * r - 2 * i - 2] = offsetX + x;
	      polygon[8 * r - 2 * i - 1] = offsetY - y;
	    }
	    return polygon;
	  }
	
	private static org.eclipse.swt.widgets.Canvas label;// = new
														// org.eclipse.swt.widgets.Label(null,
														// 0);

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display);
		//final Shell shell = new Shell(shell1);
	//	shell1.open();
		shell.open();
		/*
		 * shell.addListener(SWT.MouseDown, new Listener() { public void
		 * handleEvent(Event e) { Tracker tracker = new Tracker(shell,
		 * SWT.NONE); tracker.setRectangles(new Rectangle[] { new Rectangle(e.x,
		 * e.y, 100, 100), }); tracker.open(); } });
		 */
		 Region region = new Region();
		    region.add(circle(67, 67, 67));
		    region.subtract(circle(20, 67, 50));
		    region.subtract(new int[] { 67, 50, 55, 105, 79, 105 });
		label = new org.eclipse.swt.widgets.Canvas(shell, 0);
		label.setToolTipText("help !!!");
	//	label.setText("dcededew");
		label.setSize(300, 230);
		label.setLocation(50, 50);
		//label.setBounds(new Rectangle(0,0,90,20));
		label.setBackground(display.getSystemColor(SWT.COLOR_RED));
		label.setRegion(region);
		 GridLayout layout = new GridLayout();
		    layout.numColumns = 2;
		    layout.makeColumnsEqualWidth = true;
		    label.setLayout(layout);
		    GridData data = new GridData(GridData.FILL_BOTH);
		    final Button one = new Button(label, SWT.PUSH);
		    one.setSize(100, 20);
		    one.setLocation(50,50);
		    one.setText("one");
		    one.setLayoutData(data);
		    
		Listener l = new Listener() {
			Point origin;

			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.MouseDown:
					origin = display.map(shell, null, e.x, e.y);// new
																// Point(e.x,
																// e.y);
					break;
				case SWT.MouseUp:
					origin = null;
					break;
				case SWT.MouseMove:
					if (origin != null) {
						Point p = display.map(label, null, e.x, e.y);
						label.setLocation(p.x - origin.x, p.y - origin.y);
					}
					break;
				}
			}
		};
		label.addListener(SWT.MouseDown, l);
		label.addListener(SWT.MouseUp, l);
		label.addListener(SWT.MouseMove, l);
		label.addPaintListener(new PaintListener() {
		     public void paintControl(PaintEvent e) {
		        Canvas canvas = (Canvas) e.widget;
		        int x = canvas.getBounds().width;
		        int y = canvas.getBounds().height;

		         e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_BLACK));

		        // Create the points for drawing a triangle in the upper left
		        int[] upper_left = { 0, 0, 200, 0, 0, 200};

		        // Create the points for drawing a triangle in the lower right
		        int[] lower_right = { 0, y, x, y - 10, x - 10, y};

		        // Draw the triangles
		        e.gc.drawPolyline(upper_left);
		        e.gc.drawPolygon(lower_right);      }
		    });
		
		Listener l_one = new Listener() {
			Point origin;

			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.MouseEnter:
				
					break;
				case SWT.MouseDown:
					origin = display.map(label, null, e.x, e.y);// new
																// Point(e.x,
													// e.y);
					break;
				case SWT.MouseUp:
					origin = null;
					break;
				case SWT.MouseMove:
					if (origin != null) {
						Point p = display.map(one, null, e.x, e.y);
						one.setLocation(p.x - origin.x, p.y - origin.y);
					}
					break;
				}
			}
		};
		
		one.addListener(SWT.MouseDown, l_one);
		one.addListener(SWT.MouseUp, l_one);
		one.addListener(SWT.MouseMove, l_one);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}