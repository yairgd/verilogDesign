/**
 * 
 */
package verilog.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public final class BlockInput extends Canvas {

	private Text text;
 
	public BlockInput(Composite parent, int style) {
		super(parent, style);
		//setSize(new Point (50,50));

		// TODO Auto-generated constructor stub
	}
 
	public void init(String ioName) {
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
        
        BlockIORect blockIORect =  new BlockIORect(this,0);
        blockIORect.setLayoutData(new RowData(15, 15));
        initText(ioName);
      //  blockIORect.setLayout(layout)
        
        		 
        
        
       
       // this.pack();
        
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = getClientArea();
				e.gc.drawRectangle(0, 0, clientArea.width - 1,
						clientArea.height - 1);
			}
		});
	}
	 

	private void initText(String ioName) {
		text = new Text(this, 0);
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
				//text.getParent().getParent().getParent().getParent().pack();
				/*text.getParent().getParent().getParent().pack();
				text.getParent().getParent().pack();
				text.getParent().pack();
				text.pack();*/
				//pack(); // recalculate size
			//	text.
			}
		});

	}
}

 
