package verilog.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class BlockListener implements Listener {

	public Block getBlock() {
		return block;
	}


	private Block block;
	private Point origin;
	public BlockListener(Block block) {
		this.block = block;
		block.addListener(SWT.MouseEnter, this);
		block.addListener(SWT.MouseDown, this);
		block.addListener(SWT.MouseUp, this);
		block.addListener(SWT.MouseMove, this);
		// TODO Auto-generated constructor stub
	}

	public void handleEvent(Event e) {
		switch (e.type) {
		case SWT.MouseEnter:
			int gg=4;
			gg=gg+1;
			
			block.pack();
			break;
		case SWT.MouseDown:
			block.setFocus();
			block.moveAbove(null);
			origin = block.getDisplay().map(block.getShell(), null, e.x, e.y);// new
														// Point(e.x,
														// e.y);
			break;
		case SWT.MouseUp:
			origin = null;
			break;
		case SWT.MouseMove:
			if (origin != null) {
				
				Point p = block.getDisplay().map(block, null, e.x, e.y);
				block.setLocation(p.x - origin.x, p.y - origin.y);
			}
			break;
		}
	}

 
 
}
