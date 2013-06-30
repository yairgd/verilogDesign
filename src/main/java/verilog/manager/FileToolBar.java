package verilog.manager;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import verilog.awt.Model;
import verilog.awt.SaveModel;

public class FileToolBar {

    JButton save = new JButton("s");
    JButton open = new JButton("o");
     
    private Model model;
    JToolBar tbar = new JToolBar();

    FileToolBar() {
 
	save.addItemListener(new ItemListener() {

	    public void itemStateChanged(ItemEvent ev) {
		if (model!=null)
		{
		    SaveModel.saveModel(model);
		}

	    }
	});

	open.addItemListener(new ItemListener() {

	    public void itemStateChanged(ItemEvent ev) {
		

	    }
	});

	// tbar.add(new JButton(IconUtils.getGeneralIcon( "Open", 16 ) ));
	tbar.add(save);
	tbar.add(open);
	
	tbar.setFloatable(true);

    }

    public static JToolBar createFileBar(Model model) {
	FileToolBar fileToolBar = new FileToolBar();
	fileToolBar.setModel(model);
	return fileToolBar.getTbar();

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
	    save.setEnabled(false);
	    open.setEnabled(false);
	   
	} else {
	    save.setEnabled(true);
	    open.setEnabled(true);
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
