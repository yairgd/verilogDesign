package verilog.manager;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import verilog.awt.Model;

public class ViewToolBar {

    JToggleButton hand = new JToggleButton("A");
    JToggleButton line = new JToggleButton("B");
    JButton zoomIn = new JButton("C");
    JButton zoomOut = new JButton("D");
    private Model model;
    JToolBar tbar = new JToolBar();

    ViewToolBar() {
 
	hand.addItemListener(new ItemListener() {

	    public void itemStateChanged(ItemEvent ev) {
		if (ev.getStateChange() == ItemEvent.SELECTED) {
		 //   System.out.println("hand is selected");
		    line.setSelected(false);
		    model.setHand(false);//.setEnabled(false);
		 
	 
		} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
		//    System.out.println("hand is not selected");
		   //  model.setEnabled(true);
		}

	    }
	});

	line.addItemListener(new ItemListener() {

	    public void itemStateChanged(ItemEvent ev) {
		if (ev.getStateChange() == ItemEvent.SELECTED) {
		  //  System.out.println("line is selected");
		     model.setHand(true);
		    hand.setSelected(false);
		} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
		 //   System.out.println("line is not selected");
		 //   model.setEnabled(true);
		}

	    }
	});

	// tbar.add(new JButton(IconUtils.getGeneralIcon( "Open", 16 ) ));
	tbar.add(hand);
	tbar.add(line);
	tbar.add(zoomIn);
	tbar.add(zoomOut);
	tbar.setFloatable(true);

    }

    public static JToolBar createViewToolBar(Model model) {
	ViewToolBar viewToolBar = new ViewToolBar();
	viewToolBar.setModel(model);
	return viewToolBar.getTbar();

	//
    }

    /**
     * @return the model
     */
    public Model getModel() {
	return model;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(Model model) {
	this.model = model;
	if (model == null) {
	    hand.setEnabled(false);
	    line.setEnabled(false);
	    zoomIn.setEnabled(false);
	    zoomOut.setEnabled(false);
	} else {
	    hand.setEnabled(true);
	    line.setEnabled(true);
	    zoomIn.setEnabled(true);
	    zoomOut.setEnabled(true);
	}
    }

    /**
     * @return the tbar
     */
    public JToolBar getTbar() {
	return tbar;
    }

    /**
     * @param tbar
     *            the tbar to set
     */
    public void setTbar(JToolBar tbar) {
	this.tbar = tbar;
    }

}
