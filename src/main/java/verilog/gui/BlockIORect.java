package verilog.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public final class BlockIORect extends Canvas {
	 

	public BlockIORect(Composite parent, int style) {
		super(parent, style);
		
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		new BlockIORectListener(this);
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = getClientArea();
				e.gc.drawRectangle(0, 0, clientArea.width - 1,clientArea.height - 1);
						
			}
		});
	}
}
 
  class BlockIORectListener implements Listener {

		public BlockIORect getBlock() {
			return block;
		}


		private BlockIORect block;
		public BlockIORectListener(BlockIORect block) {
			this.block = block;
			block.addListener(SWT.MouseEnter, this);

			// TODO Auto-generated constructor stub
		}

		public void handleEvent(Event e) {
			switch (e.type) {
			case SWT.MouseEnter:
				int gg=4;
				gg=gg+1;
				break;
				
			}
		}

	 
	 
	}