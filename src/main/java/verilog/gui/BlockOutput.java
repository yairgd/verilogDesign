package verilog.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class BlockOutput extends Canvas {

	private Text text;
	public BlockOutput(Composite parent, int style) {
		super(parent, style);
		//setSize(new Point (50,50));
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		RowLayout rowLayout = new RowLayout();
		//rowLayout.wrap = false;
	//	rowLayout.pack = false;
		rowLayout.justify = true;
		rowLayout.type = SWT.HORIZONTAL;
		rowLayout.marginLeft = 0;
		rowLayout.marginTop = 5;
		rowLayout.marginRight = 5;
		rowLayout.marginBottom = 5;
		rowLayout.spacing = 5;
        // Optionally set layout fields.
		//rowLayout.wrap = true;
        // Set the layout into the composite.
        this.setLayout(rowLayout);
        // Create the children of the composite.
        
        
        initText();
        BlockIORect blockIORect =  new BlockIORect(this,0);
        
      //  blockIORect.setLayout(layout)
        
        		 
        blockIORect.setLayoutData(new RowData(15, 15));
        
       
       // this.pack();
        
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = getClientArea();
				e.gc.drawRectangle(0, 0, clientArea.width - 1,
						clientArea.height - 1);
			}
		});
	}
	
	private void initText() {
		text = new Text(this, 0);
		text.setText("test");
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				pack(); // recalculate size
			//	text.
			}
		});

	}
}

