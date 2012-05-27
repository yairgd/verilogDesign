package verilog.gui;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class t2 {
  private TabFolder tabFolder;

  /**
   * Creates an instance of a LayoutExample embedded inside the supplied
   * parent Composite.
   * 
   * @param parent
   *            the container of the example
   */
  public t2(Composite parent) {
    tabFolder = new TabFolder(parent, SWT.NULL);
    Tab[] tabs = new Tab[] { new FillLayoutTab(this),
        new RowLayoutTab(this), new GridLayoutTab(this),
        new FormLayoutTab(this), };
    for (int i = 0; i < tabs.length; i++) {
      TabItem item = new TabItem(tabFolder, SWT.NULL);
      item.setText(tabs[i].getTabText());
      item.setControl(tabs[i].createTabFolderPage(tabFolder));
    }
  }

  /**
   * Grabs input focus.
   */
  public void setFocus() {
    tabFolder.setFocus();
  }

  /**
   * Disposes of all resources associated with a particular instance of the
   * LayoutExample.
   */
  public void dispose() {
    tabFolder = null;
  }

  /**
   * Invokes as a standalone program.
   */
  public static void main(String[] args) {
    final Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    new t2(shell);
    shell.setText(getResourceString("window.title"));
    shell.addShellListener(new ShellAdapter() {
      public void shellClosed(ShellEvent e) {
        Shell[] shells = display.getShells();
        for (int i = 0; i < shells.length; i++) {
          if (shells[i] != shell)
            shells[i].close();
        }
      }
    });
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }

  /**
   * Gets a string from the resource bundle. We don't want to crash because of
   * a missing String. Returns the key if not found.
   */
  static String getResourceString(String key) {
      return key;
  }

  /**
   * Gets a string from the resource bundle and binds it with the given
   * arguments. If the key is not found, return the key.
   */
  static String getResourceString(String key, Object[] args) {
    try {
      return MessageFormat.format(getResourceString(key), args);
    } catch (MissingResourceException e) {
      return key;
    } catch (NullPointerException e) {
      return "!" + key + "!";
    }
  }
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

/**
 * <code>Tab</code> is the abstract superclass of every page in the example's
 * tab folder. Each page in the tab folder displays a layout, and allows the
 * user to manipulate the layout.
 * 
 * A typical page in a Tab contains a two column composite. The left column
 * contains the layout group, which contains the "layout composite" (the one
 * that has the example layout). The right column contains the "control" group.
 * The "control" group allows the user to interact with the example. Typical
 * operations are modifying layout parameters, adding children to the "layout
 * composite", and modifying child layout data. The "Code" button in the
 * "control" group opens a new window containing code that will regenerate the
 * layout. This code (or parts of it) can be selected and copied to the
 * clipboard.
 */
abstract class Tab {
  /* Common groups and composites */
  Composite tabFolderPage;

  SashForm sash;

  Group layoutGroup, controlGroup, childGroup;

  /* The composite that contains the example layout */
  Composite layoutComposite;

  /* Common controls for modifying the example layout */
  String[] names;

  Control[] children;

  Button size, add, delete, clear, code;

  /* Common values for working with TableEditors */
  Table table;

  int index;

  TableItem newItem, lastSelected;

  Vector data = new Vector();

  /* Controlling instance */
  final t2 instance;

  /* Listeners */
  SelectionListener selectionListener = new SelectionAdapter() {
    public void widgetSelected(SelectionEvent e) {
      resetEditors();
    }
  };

  TraverseListener traverseListener = new TraverseListener() {
    public void keyTraversed(TraverseEvent e) {
      if (e.detail == SWT.TRAVERSE_RETURN) {
        e.doit = false;
        resetEditors();
      }
    }
  };

  /**
   * Creates the Tab within a given instance of LayoutExample.
   */
  Tab(t2 instance) {
    this.instance = instance;
  }

  /**
   * Creates the "child" group. This is the group that allows you to add
   * children to the layout. It exists within the controlGroup.
   */
  void createChildGroup() {
    childGroup = new Group(controlGroup, SWT.NONE);
    childGroup.setText(t2.getResourceString("Children"));
    GridLayout layout = new GridLayout();
    layout.numColumns = 3;
    childGroup.setLayout(layout);
    GridData data = new GridData(GridData.FILL_BOTH);
    data.horizontalSpan = 2;
    childGroup.setLayoutData(data);
    createChildWidgets();
  }

  /**
   * Creates the controls for modifying the "children" table, and the table
   * itself. Subclasses override this method to augment the standard table.
   */
  void createChildWidgets() {
    /* Controls for adding and removing children */
    add = new Button(childGroup, SWT.PUSH);
    add.setText(t2.getResourceString("Add"));
    add.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    delete = new Button(childGroup, SWT.PUSH);
    delete.setText(t2.getResourceString("Delete"));
    delete.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    delete.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        resetEditors();
        int[] selected = table.getSelectionIndices();
        table.remove(selected);
        /* Refresh the control indices of the table */
        for (int i = 0; i < table.getItemCount(); i++) {
          table.getItem(i).setText(0, String.valueOf(i));
        }
        refreshLayoutComposite();
        layoutComposite.layout(true);
        layoutGroup.layout(true);
      }
    });
    clear = new Button(childGroup, SWT.PUSH);
    clear.setText(t2.getResourceString("Clear"));
    clear.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    clear.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        resetEditors();
        children = layoutComposite.getChildren();
        for (int i = 0; i < children.length; i++) {
          children[i].dispose();
        }
        table.removeAll();
        data.clear();
        children = new Control[0];
        layoutGroup.layout(true);
      }
    });
    /* Create the "children" table */
    table = new Table(childGroup, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL
        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
    table.setLinesVisible(true);
    table.setHeaderVisible(true);
    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 3;
    gridData.heightHint = 150;
    table.setLayoutData(gridData);
    table.addTraverseListener(traverseListener);

    /* Add columns to the table */
    String[] columnHeaders = getLayoutDataFieldNames();
    for (int i = 0; i < columnHeaders.length; i++) {
      TableColumn column = new TableColumn(table, SWT.NONE);
      column.setText(columnHeaders[i]);
      if (i == 0)
        column.setWidth(20);
      else if (i == 1)
        column.setWidth(80);
      else
        column.pack();
    }
  }

  /**
   * Creates the TableEditor with a CCombo in the first column of the table.
   * This CCombo lists all the controls that the user can select to place on
   * their layout.
   */
  void createComboEditor(CCombo combo, TableEditor comboEditor) {
    combo.setItems(new String[] { "Button", "Canvas", "Combo", "Composite",
        "CoolBar", "Group", "Label", "List", "ProgressBar", "Scale",
        "Slider", "StyledText", "Table", "Text", "ToolBar", "Tree" });
    combo.setText(newItem.getText(1));

    /* Set up editor */
    comboEditor.horizontalAlignment = SWT.LEFT;
    comboEditor.grabHorizontal = true;
    comboEditor.minimumWidth = 50;
    comboEditor.setEditor(combo, newItem, 1);

    /* Add listener */
    combo.addTraverseListener(new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_TAB_NEXT
            || e.detail == SWT.TRAVERSE_RETURN) {
          resetEditors();
        }
        if (e.detail == SWT.TRAVERSE_ESCAPE) {
          disposeEditors();
        }
      }
    });
  }

  /**
   * Creates the "control" group. This is the group on the right half of each
   * example tab. It contains controls for adding new children to the
   * layoutComposite, and for modifying the children's layout data.
   */
  void createControlGroup() {
    controlGroup = new Group(sash, SWT.NONE);
    controlGroup.setText(t2.getResourceString("Parameters"));
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    controlGroup.setLayout(layout);
    size = new Button(controlGroup, SWT.CHECK);
    size.setText(t2.getResourceString("Preferred_Size"));
    size.setSelection(false);
    size.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        resetEditors();
        if (size.getSelection()) {
          layoutComposite.setLayoutData(new GridData());
          layoutGroup.layout(true);
        } else {
          layoutComposite.setLayoutData(new GridData(
              GridData.FILL_BOTH));
          layoutGroup.layout(true);
        }
      }
    });
    GridData data = new GridData(GridData.FILL_HORIZONTAL);
    data.horizontalSpan = 2;
    size.setLayoutData(data);
    createControlWidgets();
  }

  /**
   * Creates the "control" widget children. Subclasses override this method to
   * augment the standard controls created.
   */
  void createControlWidgets() {
    createChildGroup();
    code = new Button(controlGroup, SWT.PUSH);
    code.setText(t2.getResourceString("Code"));
    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER
        | GridData.GRAB_HORIZONTAL);
    gridData.horizontalSpan = 2;
    code.setLayoutData(gridData);
    code.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        final Shell shell = new Shell();
        shell
            .setText(t2
                .getResourceString("Generated_Code"));
        shell.setLayout(new FillLayout());
        final StyledText text = new StyledText(shell, SWT.BORDER
            | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        String layoutCode = generateCode().toString();
        if (layoutCode.length() == 0)
          return;
        text.setText(layoutCode);

        Menu bar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(bar);
        MenuItem editItem = new MenuItem(bar, SWT.CASCADE);
        editItem.setText(t2.getResourceString("Edit"));
        Menu menu = new Menu(bar);
        MenuItem select = new MenuItem(menu, SWT.PUSH);
        select.setText(t2.getResourceString("Select_All"));
        select.setAccelerator(SWT.MOD1 + 'A');
        select.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
            text.selectAll();
          }
        });
        MenuItem copy = new MenuItem(menu, SWT.PUSH);
        copy.setText(t2.getResourceString("Copy"));
        copy.setAccelerator(SWT.MOD1 + 'C');
        copy.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
            text.copy();
          }
        });
        MenuItem exit = new MenuItem(menu, SWT.PUSH);
        exit.setText(t2.getResourceString("Exit"));
        exit.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
            shell.close();
          }
        });
        editItem.setMenu(menu);

        shell.pack();
        shell.setSize(400, 500);
        shell.open();
        Display display = shell.getDisplay();
        while (!shell.isDisposed())
          if (!display.readAndDispatch())
            display.sleep();
      }
    });
  }

  /**
   * Creates the example layout. Subclasses override this method.
   */
  void createLayout() {
  }

  /**
   * Creates the composite that contains the example layout.
   */
  void createLayoutComposite() {
    layoutComposite = new Composite(layoutGroup, SWT.BORDER);
    layoutComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
    createLayout();
  }

  /**
   * Creates the layout group. This is the group on the left half of each
   * example tab.
   */
  void createLayoutGroup() {
    layoutGroup = new Group(sash, SWT.NONE);
    layoutGroup.setText(t2.getResourceString("Layout"));
    layoutGroup.setLayout(new GridLayout());
    createLayoutComposite();
  }

  /**
   * Creates the tab folder page.
   * 
   * @param tabFolder
   *            org.eclipse.swt.widgets.TabFolder
   * @return the new page for the tab folder
   */
  Composite createTabFolderPage(TabFolder tabFolder) {
    /* Create a two column page with a SashForm */
    tabFolderPage = new Composite(tabFolder, SWT.NULL);
    tabFolderPage.setLayout(new FillLayout());
    sash = new SashForm(tabFolderPage, SWT.HORIZONTAL);

    /* Create the "layout" and "control" columns */
    createLayoutGroup();
    createControlGroup();

    return tabFolderPage;
  }

  /**
   * Creates the TableEditor with a Text in the given column of the table.
   */
  void createTextEditor(Text text, TableEditor textEditor, int column) {
    text.setFont(table.getFont());
    text.selectAll();
    textEditor.horizontalAlignment = SWT.LEFT;
    textEditor.grabHorizontal = true;
    textEditor.setEditor(text, newItem, column);

    text.addTraverseListener(new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
          resetEditors(true);
        }
        if (e.detail == SWT.TRAVERSE_ESCAPE) {
          disposeEditors();
        }
      }
    });
  }

  /**
   * Disposes the editors without placing their contents into the table.
   * Subclasses override this method.
   */
  void disposeEditors() {
  }

  /**
   * Generates the code needed to produce the example layout.
   */
  StringBuffer generateCode() {
    /* Make sure all information being entered is stored in the table */
    resetEditors();

    /* Get names for controls in the layout */
    names = new String[children.length];
    for (int i = 0; i < children.length; i++) {
      Control control = children[i];
      String controlClass = control.getClass().toString();
      String controlType = controlClass.substring(controlClass
          .lastIndexOf('.') + 1);
      names[i] = controlType.toLowerCase() + i;
    }

    /* Create StringBuffer containing the code */
    StringBuffer code = new StringBuffer();
    code.append("import org.eclipse.swt.*;\n");
    code.append("import org.eclipse.swt.custom.*;\n");
    code.append("import org.eclipse.swt.graphics.*;\n");
    code.append("import org.eclipse.swt.layout.*;\n");
    code.append("import org.eclipse.swt.widgets.*;\n\n");
    code.append("public class MyLayout {\n");
    code.append("\tpublic static void main (String [] args) {\n");
    code.append("\t\tDisplay display = new Display ();\n");
    code.append("\t\tShell shell = new Shell (display);\n");

    /* Get layout specific code */
    code.append(generateLayoutCode());

    code.append("\n\t\tshell.pack ();\n\t\tshell.open ();\n\n");
    code.append("\t\twhile (!shell.isDisposed ()) {\n");
    code.append("\t\t\tif (!display.readAndDispatch ())\n");
    code
        .append("\t\t\t\tdisplay.sleep ();\n\t\t}\n\t\tdisplay.dispose ();\n\t}\n}");

    return code;
  }

  /**
   * Generates layout specific code for the example layout. Subclasses
   * override this method.
   */
  StringBuffer generateLayoutCode() {
    return new StringBuffer();
  }

  /**
   * Returns the StringBuffer for the code which will create a child control.
   */
  StringBuffer getChildCode(Control control, int i) {
    StringBuffer code = new StringBuffer();
    /* Find the type of control */
    String controlClass = control.getClass().toString();
    String controlType = controlClass.substring(controlClass
        .lastIndexOf('.') + 1);
    /* Find the style of the control */
    String styleString;
    if (controlType.equals("Button")) {
      styleString = "SWT.PUSH";
    } else if (controlType.equals("Text")) {
      styleString = "SWT.BORDER";
    } else if (controlType.equals("StyledText")) {
      styleString = "SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL";
    } else if (controlType.equals("Canvas")
        || controlType.equals("Composite")
        || controlType.equals("Table")
        || controlType.equals("StyledText")
        || controlType.equals("ToolBar") || controlType.equals("Tree")
        || controlType.equals("List")) {
      styleString = "SWT.BORDER";
    } else
      styleString = "SWT.NONE";
    /* Write out the control being declared */
    code.append("\n\t\t" + controlType + " " + names[i] + " = new "
        + controlType + " (shell, " + styleString + ");\n");
    /* Add items to those controls that need items */
    if (controlType.equals("Combo") || controlType.equals("List")) {
      code
          .append("\t\t"
              + names[i]
              + ".setItems (new String [] {\"Item 1\", \"Item 2\", \"Item 2\"});\n");
    } else if (controlType.equals("Table")) {
      code.append("\t\t" + names[i] + ".setLinesVisible (true);\n");
      for (int j = 1; j < 3; j++) {
        code.append("\t\tTableItem tableItem" + j
            + " = new TableItem (" + names[i] + ", SWT.NONE);\n");
        code.append("\t\ttableItem" + j + ".setText (\"Item" + j
            + "\");\n");
      }
    } else if (controlType.equals("Tree")) {
      for (int j = 1; j < 3; j++) {
        code.append("\t\tTreeItem treeItem" + j + " = new TreeItem ("
            + names[i] + ", SWT.NONE);\n");
        code.append("\t\ttreeItem" + j + ".setText (\"Item" + j
            + "\");\n");
      }
    } else if (controlType.equals("ToolBar")) {
      for (int j = 1; j < 3; j++) {
        code.append("\t\tToolItem toolItem" + j + " = new ToolItem ("
            + names[i] + ", SWT.NONE);\n");
        code.append("\t\ttoolItem" + j + ".setText (\"Item" + j
            + "\");\n");
      }
    } else if (controlType.equals("CoolBar")) {
      code.append("\t\tToolBar coolToolBar = new ToolBar (" + names[i]
          + ", SWT.BORDER);\n");
      code
          .append("\t\tToolItem coolToolItem = new ToolItem (coolToolBar, SWT.NONE);\n");
      code.append("\t\tcoolToolItem.setText (\"Item 1\");\n");
      code
          .append("\t\tcoolToolItem = new ToolItem (coolToolBar, SWT.NONE);\n");
      code.append("\t\tcoolToolItem.setText (\"Item 2\");\n");
      code.append("\t\tCoolItem coolItem1 = new CoolItem (" + names[i]
          + ", SWT.NONE);\n");
      code.append("\t\tcoolItem1.setControl (coolToolBar);\n");
      code
          .append("\t\tPoint size = coolToolBar.computeSize (SWT.DEFAULT, SWT.DEFAULT);\n");
      code
          .append("\t\tcoolItem1.setSize (coolItem1.computeSize (size.x, size.y));\n");
      code.append("\t\tcoolToolBar = new ToolBar (" + names[i]
          + ", SWT.BORDER);\n");
      code
          .append("\t\tcoolToolItem = new ToolItem (coolToolBar, SWT.NONE);\n");
      code.append("\t\tcoolToolItem.setText (\"Item 3\");\n");
      code
          .append("\t\tcoolToolItem = new ToolItem (coolToolBar, SWT.NONE);\n");
      code.append("\t\tcoolToolItem.setText (\"Item 4\");\n");
      code.append("\t\tCoolItem coolItem2 = new CoolItem (" + names[i]
          + ", SWT.NONE);\n");
      code.append("\t\tcoolItem2.setControl (coolToolBar);\n");
      code
          .append("\t\tsize = coolToolBar.computeSize (SWT.DEFAULT, SWT.DEFAULT);\n");
      code
          .append("\t\tcoolItem2.setSize (coolItem2.computeSize (size.x, size.y));\n");
      code.append("\t\t" + names[i] + ".setSize (" + names[i]
          + ".computeSize (SWT.DEFAULT, SWT.DEFAULT));\n");
    } else if (controlType.equals("ProgressBar")) {
      code.append("\t\t" + names[i] + ".setSelection (50);\n");
    }
    /* Set text for those controls that support it */
    if (controlType.equals("Button") || controlType.equals("Combo")
        || controlType.equals("Group") || controlType.equals("Label")
        || controlType.equals("StyledText")
        || controlType.equals("Text")) {
      code.append("\t\t" + names[i] + ".setText (\"" + names[i]
          + "\");\n");
    }
    return code;
  }

  /**
   * Returns the layout data field names. Subclasses override this method.
   */
  String[] getLayoutDataFieldNames() {
    return new String[] {};
  }

  /**
   * Gets the text for the tab folder item. Subclasses override this method.
   */
  String getTabText() {
    return "";
  }

  /**
   * Refreshes the composite and draws all controls in the layout example.
   */
  void refreshLayoutComposite() {
    /* Remove children that are already laid out */
    children = layoutComposite.getChildren();
    for (int i = 0; i < children.length; i++) {
      children[i].dispose();
    }
    /* Add all children listed in the table */
    TableItem[] items = table.getItems();
    children = new Control[items.length];
    String[] itemValues = new String[] {
        t2.getResourceString("Item", new String[] { "1" }),
        t2.getResourceString("Item", new String[] { "2" }),
        t2.getResourceString("Item", new String[] { "3" }) };
    for (int i = 0; i < items.length; i++) {
      String control = items[i].getText(1);
      if (control.equals("Button")) {
        Button button = new Button(layoutComposite, SWT.PUSH);
        button.setText(t2.getResourceString("Button_Index",
            new String[] { new Integer(i).toString() }));
        children[i] = button;
      } else if (control.equals("Canvas")) {
        Canvas canvas = new Canvas(layoutComposite, SWT.BORDER);
        children[i] = canvas;
      } else if (control.equals("Combo")) {
        Combo combo = new Combo(layoutComposite, SWT.NONE);
        combo.setItems(itemValues);
        combo.setText(t2.getResourceString("Combo_Index",
            new String[] { new Integer(i).toString() }));
        children[i] = combo;
      } else if (control.equals("Composite")) {
        Composite composite = new Composite(layoutComposite, SWT.BORDER);
        children[i] = composite;
      } else if (control.equals("CoolBar")) {
        CoolBar coolBar = new CoolBar(layoutComposite, SWT.NONE);
        ToolBar toolBar = new ToolBar(coolBar, SWT.BORDER);
        ToolItem item = new ToolItem(toolBar, 0);
        item.setText(t2.getResourceString("Item",
            new String[] { "1" }));
        item = new ToolItem(toolBar, 0);
        item.setText(t2.getResourceString("Item",
            new String[] { "2" }));
        CoolItem coolItem1 = new CoolItem(coolBar, 0);
        coolItem1.setControl(toolBar);
        toolBar = new ToolBar(coolBar, SWT.BORDER);
        item = new ToolItem(toolBar, 0);
        item.setText(t2.getResourceString("Item",
            new String[] { "3" }));
        item = new ToolItem(toolBar, 0);
        item.setText(t2.getResourceString("Item",
            new String[] { "4" }));
        CoolItem coolItem2 = new CoolItem(coolBar, 0);
        coolItem2.setControl(toolBar);
        Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        coolItem1.setSize(coolItem1.computeSize(size.x, size.y));
        coolItem2.setSize(coolItem2.computeSize(size.x, size.y));
        coolBar.setSize(coolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        children[i] = coolBar;
      } else if (control.equals("Group")) {
        Group group = new Group(layoutComposite, SWT.NONE);
        group.setText(t2.getResourceString("Group_Index",
            new String[] { new Integer(i).toString() }));
        children[i] = group;
      } else if (control.equals("Label")) {
        Label label = new Label(layoutComposite, SWT.NONE);
        label.setText(t2.getResourceString("Label_Index",
            new String[] { new Integer(i).toString() }));
        children[i] = label;
      } else if (control.equals("List")) {
        List list = new List(layoutComposite, SWT.BORDER);
        list.setItems(itemValues);
        children[i] = list;
      } else if (control.equals("ProgressBar")) {
        ProgressBar progress = new ProgressBar(layoutComposite,
            SWT.NONE);
        progress.setSelection(50);
        children[i] = progress;
      } else if (control.equals("Scale")) {
        Scale scale = new Scale(layoutComposite, SWT.NONE);
        children[i] = scale;
      } else if (control.equals("Slider")) {
        Slider slider = new Slider(layoutComposite, SWT.NONE);
        children[i] = slider;
      } else if (control.equals("StyledText")) {
        StyledText styledText = new StyledText(layoutComposite,
            SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        styledText.setText(t2.getResourceString(
            "StyledText_Index", new String[] { new Integer(i)
                .toString() }));
        children[i] = styledText;
      } else if (control.equals("Table")) {
        Table table = new Table(layoutComposite, SWT.BORDER);
        table.setLinesVisible(true);
        TableItem item1 = new TableItem(table, 0);
        item1.setText(t2.getResourceString("Item",
            new String[] { "1" }));
        TableItem item2 = new TableItem(table, 0);
        item2.setText(t2.getResourceString("Item",
            new String[] { "2" }));
        children[i] = table;
      } else if (control.equals("Text")) {
        Text text = new Text(layoutComposite, SWT.BORDER);
        text.setText(t2.getResourceString("Text_Index",
            new String[] { new Integer(i).toString() }));
        children[i] = text;
      } else if (control.equals("ToolBar")) {
        ToolBar toolBar = new ToolBar(layoutComposite, SWT.BORDER);
        ToolItem item1 = new ToolItem(toolBar, 0);
        item1.setText(t2.getResourceString("Item",
            new String[] { "1" }));
        ToolItem item2 = new ToolItem(toolBar, 0);
        item2.setText(t2.getResourceString("Item",
            new String[] { "2" }));
        children[i] = toolBar;
      } else {
        Tree tree = new Tree(layoutComposite, SWT.BORDER);
        TreeItem item1 = new TreeItem(tree, 0);
        item1.setText(t2.getResourceString("Item",
            new String[] { "1" }));
        TreeItem item2 = new TreeItem(tree, 0);
        item2.setText(t2.getResourceString("Item",
            new String[] { "2" }));
        children[i] = tree;
      }
    }
  }

  /**
   * Takes information from TableEditors and stores it. Subclasses override
   * this method.
   */
  void resetEditors() {
    resetEditors(false);
  }

  void resetEditors(boolean tab) {
  }

  /**
   * Sets the layout data for the children of the layout. Subclasses override
   * this method.
   */
  void setLayoutData() {
  }

  /**
   * Sets the state of the layout. Subclasses override this method.
   */
  void setLayoutState() {
  }
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

class RowLayoutTab extends Tab {
  /* Controls for setting layout parameters */
  Button horizontal, vertical;

  Button wrap, pack, justify;

  Combo marginRight, marginLeft, marginTop, marginBottom, spacing;

  /* The example layout instance */
  RowLayout rowLayout;

  /* TableEditors and related controls */
  TableEditor comboEditor, widthEditor, heightEditor;

  CCombo combo;

  Text widthText, heightText;

  /* Constants */
  final int COMBO_COL = 1;

  final int WIDTH_COL = 2;

  final int HEIGHT_COL = 3;

  final int TOTAL_COLS = 4;

  /**
   * Creates the Tab within a given instance of LayoutExample.
   */
  RowLayoutTab(t2 instance) {
    super(instance);
  }

  /**
   * Creates the widgets in the "child" group.
   */
  void createChildWidgets() {
    /* Add common controls */
    super.createChildWidgets();

    /* Add TableEditors */
    comboEditor = new TableEditor(table);
    widthEditor = new TableEditor(table);
    heightEditor = new TableEditor(table);
    table.addMouseListener(new MouseAdapter() {
      public void mouseDown(MouseEvent e) {
        resetEditors();
        index = table.getSelectionIndex();
        Point pt = new Point(e.x, e.y);
        newItem = table.getItem(pt);
        if (newItem == null)
          return;
        TableItem oldItem = comboEditor.getItem();
        if (newItem == oldItem || newItem != lastSelected) {
          lastSelected = newItem;
          return;
        }
        table.showSelection();

        combo = new CCombo(table, SWT.READ_ONLY);
        createComboEditor(combo, comboEditor);

        widthText = new Text(table, SWT.SINGLE);
        widthText
            .setText(((String[]) data.elementAt(index))[WIDTH_COL]);
        createTextEditor(widthText, widthEditor, WIDTH_COL);

        heightText = new Text(table, SWT.SINGLE);
        heightText
            .setText(((String[]) data.elementAt(index))[HEIGHT_COL]);
        createTextEditor(heightText, heightEditor, HEIGHT_COL);

        for (int i = 0; i < table.getColumnCount(); i++) {
          Rectangle rect = newItem.getBounds(i);
          if (rect.contains(pt)) {
            switch (i) {
            case COMBO_COL:
              combo.setFocus();
              break;
            case WIDTH_COL:
              widthText.setFocus();
              break;
            case HEIGHT_COL:
              heightText.setFocus();
              break;
            default:
              resetEditors();
              break;
            }
          }
        }
      }
    });

    /* Add listener to add an element to the table */
    add.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        TableItem item = new TableItem(table, 0);
        String[] insert = new String[] {
            String.valueOf(table.indexOf(item)), "Button", "-1",
            "-1" };
        item.setText(insert);
        data.addElement(insert);
        resetEditors();
      }
    });
  }

  /**
   * Creates the control widgets.
   */
  void createControlWidgets() {
    /* Controls the type of RowLayout */
    Group typeGroup = new Group(controlGroup, SWT.NONE);
    typeGroup.setText(t2.getResourceString("Type"));
    typeGroup.setLayout(new GridLayout());
    GridData data = new GridData(GridData.FILL_HORIZONTAL);
    typeGroup.setLayoutData(data);
    horizontal = new Button(typeGroup, SWT.RADIO);
    horizontal.setText("SWT.HORIZONTAL");
    horizontal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    horizontal.setSelection(true);
    horizontal.addSelectionListener(selectionListener);
    vertical = new Button(typeGroup, SWT.RADIO);
    vertical.setText("SWT.VERTICAL");
    vertical.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    vertical.addSelectionListener(selectionListener);

    /* Controls the margins and spacing of the RowLayout */
    String[] marginValues = new String[] { "0", "3", "5", "10" };
    Group marginGroup = new Group(controlGroup, SWT.NONE);
    marginGroup.setText(t2.getResourceString("Margins_Spacing"));
    data = new GridData(GridData.FILL_HORIZONTAL
        | GridData.VERTICAL_ALIGN_BEGINNING);
    data.verticalSpan = 2;
    marginGroup.setLayoutData(data);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    marginGroup.setLayout(layout);
    new Label(marginGroup, SWT.NONE).setText("marginRight");
    marginRight = new Combo(marginGroup, SWT.NONE);
    marginRight.setItems(marginValues);
    marginRight.select(1);
    marginRight.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    marginRight.addSelectionListener(selectionListener);
    marginRight.addTraverseListener(traverseListener);
    new Label(marginGroup, SWT.NONE).setText("marginLeft");
    marginLeft = new Combo(marginGroup, SWT.NONE);
    marginLeft.setItems(marginValues);
    marginLeft.select(1);
    marginLeft.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    marginLeft.addSelectionListener(selectionListener);
    marginLeft.addTraverseListener(traverseListener);
    new Label(marginGroup, SWT.NONE).setText("marginTop");
    marginTop = new Combo(marginGroup, SWT.NONE);
    marginTop.setItems(marginValues);
    marginTop.select(1);
    marginTop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    marginTop.addSelectionListener(selectionListener);
    marginTop.addTraverseListener(traverseListener);
    new Label(marginGroup, SWT.NONE).setText("marginBottom");
    marginBottom = new Combo(marginGroup, SWT.NONE);
    marginBottom.setItems(marginValues);
    marginBottom.select(1);
    marginBottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    marginBottom.addSelectionListener(selectionListener);
    marginBottom.addTraverseListener(traverseListener);
    new Label(marginGroup, SWT.NONE).setText("spacing");
    spacing = new Combo(marginGroup, SWT.NONE);
    spacing.setItems(marginValues);
    spacing.select(1);
    spacing.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    spacing.addSelectionListener(selectionListener);
    spacing.addTraverseListener(traverseListener);

    /* Controls other parameters of the RowLayout */
    Group specGroup = new Group(controlGroup, SWT.NONE);
    specGroup.setText(t2.getResourceString("Properties"));
    specGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    specGroup.setLayout(new GridLayout());
    wrap = new Button(specGroup, SWT.CHECK);
    wrap.setText("wrap");
    wrap.setSelection(true);
    wrap.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    wrap.addSelectionListener(selectionListener);
    pack = new Button(specGroup, SWT.CHECK);
    pack.setText("pack");
    pack.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    pack.setSelection(true);
    pack.addSelectionListener(selectionListener);
    justify = new Button(specGroup, SWT.CHECK);
    justify.setText("justify");
    justify.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    justify.addSelectionListener(selectionListener);

    /* Add common controls */
    super.createControlWidgets();

    /* Position the sash */
    sash.setWeights(new int[] { 6, 5 });
  }

  /**
   * Creates the example layout.
   */
  void createLayout() {
    rowLayout = new RowLayout();
    layoutComposite.setLayout(rowLayout);
  }

  /**
   * Disposes the editors without placing their contents into the table.
   */
  void disposeEditors() {
    comboEditor.setEditor(null, null, -1);
    combo.dispose();
    widthText.dispose();
    heightText.dispose();
  }

  /**
   * Generates code for the example layout.
   */
  StringBuffer generateLayoutCode() {
    StringBuffer code = new StringBuffer();
    code.append("\t\tRowLayout rowLayout = new RowLayout ();\n");
    if (rowLayout.type == SWT.VERTICAL) {
      code.append("\t\trowLayout.type = SWT.VERTICAL;\n");
    }
    if (rowLayout.wrap == false) {
      code.append("\t\trowLayout.wrap = false;\n");
    }
    if (rowLayout.pack == false) {
      code.append("\t\trowLayout.pack = false;\n");
    }
    if (rowLayout.justify == true) {
      code.append("\t\trowLayout.justify = true;\n");
    }
    if (rowLayout.marginLeft != 3) {
      code.append("\t\trowLayout.marginLeft = " + rowLayout.marginLeft
          + ";\n");
    }
    if (rowLayout.marginRight != 3) {
      code.append("\t\trowLayout.marginRight = " + rowLayout.marginRight
          + ";\n");
    }
    if (rowLayout.marginTop != 3) {
      code.append("\t\trowLayout.marginTop = " + rowLayout.marginTop
          + ";\n");
    }
    if (rowLayout.marginBottom != 3) {
      code.append("\t\trowLayout.marginBottom = "
          + rowLayout.marginBottom + ";\n");
    }
    if (rowLayout.spacing != 3) {
      code.append("\t\trowLayout.spacing = " + rowLayout.spacing + ";\n");
    }
    code.append("\t\tshell.setLayout (rowLayout);\n");

    boolean first = true;
    for (int i = 0; i < children.length; i++) {
      Control control = children[i];
      code.append(getChildCode(control, i));
      RowData data = (RowData) control.getLayoutData();
      if (data != null) {
        if (data.width != -1 || data.height != -1) {
          code.append("\t\t");
          if (first) {
            code.append("RowData ");
            first = false;
          }
          if (data.width == -1) {
            code.append("data = new RowData (SWT.DEFAULT, "
                + data.height + ");\n");
          } else if (data.height == -1) {
            code.append("data = new RowData (" + data.width
                + ", SWT.DEFAULT);\n");
          } else {
            code.append("data = new RowData (" + data.width + ", "
                + data.height + ");\n");
          }
          code.append("\t\t" + names[i] + ".setLayoutData (data);\n");
        }
      }
    }
    return code;
  }

  /**
   * Returns the layout data field names.
   */
  String[] getLayoutDataFieldNames() {
    return new String[] { "", "Control", "width", "height" };
  }

  /**
   * Gets the text for the tab folder item.
   */
  String getTabText() {
    return "RowLayout";
  }

  /**
   * Takes information from TableEditors and stores it.
   */
  void resetEditors() {
    resetEditors(false);
  }

  void resetEditors(boolean tab) {
    TableItem oldItem = comboEditor.getItem();
    if (oldItem != null) {
      int row = table.indexOf(oldItem);
      /* Make sure user has entered valid data */
      try {
        new Integer(widthText.getText()).intValue();
      } catch (NumberFormatException e) {
        widthText.setText(oldItem.getText(WIDTH_COL));
      }
      try {
        new Integer(heightText.getText()).intValue();
      } catch (NumberFormatException e) {
        heightText.setText(oldItem.getText(HEIGHT_COL));
      }
      String[] insert = new String[] { String.valueOf(row),
          combo.getText(), widthText.getText(), heightText.getText() };
      data.setElementAt(insert, row);
      for (int i = 0; i < TOTAL_COLS; i++) {
        oldItem.setText(i, ((String[]) data.elementAt(row))[i]);
      }
      if (!tab)
        disposeEditors();
    }
    setLayoutState();
    refreshLayoutComposite();
    setLayoutData();
    layoutComposite.layout(true);
    layoutGroup.layout(true);
  }

  /**
   * Sets the layout data for the children of the layout.
   */
  void setLayoutData() {
    Control[] children = layoutComposite.getChildren();
    TableItem[] items = table.getItems();
    RowData data;
    int width, height;
    for (int i = 0; i < children.length; i++) {
      width = new Integer(items[i].getText(WIDTH_COL)).intValue();
      height = new Integer(items[i].getText(HEIGHT_COL)).intValue();
      data = new RowData(width, height);
      children[i].setLayoutData(data);
    }

  }

  /**
   * Sets the state of the layout.
   */
  void setLayoutState() {
    /* Set the type of layout */
    if (vertical.getSelection()) {
      rowLayout.type = SWT.VERTICAL;
    } else {
      rowLayout.type = SWT.HORIZONTAL;
    }

    /* Set the margins and spacing */
    try {
      rowLayout.marginRight = new Integer(marginRight.getText())
          .intValue();
    } catch (NumberFormatException e) {
      rowLayout.marginRight = 3;
      marginRight.select(1);
    }
    try {
      rowLayout.marginLeft = new Integer(marginLeft.getText()).intValue();
    } catch (NumberFormatException e) {
      rowLayout.marginLeft = 3;
      marginLeft.select(1);
    }
    try {
      rowLayout.marginTop = new Integer(marginTop.getText()).intValue();
    } catch (NumberFormatException e) {
      rowLayout.marginTop = 3;
      marginTop.select(1);
    }
    try {
      rowLayout.marginBottom = new Integer(marginBottom.getText())
          .intValue();
    } catch (NumberFormatException e) {
      rowLayout.marginBottom = 3;
      marginBottom.select(1);
    }
    try {
      rowLayout.spacing = new Integer(spacing.getText()).intValue();
    } catch (NumberFormatException e) {
      rowLayout.spacing = 3;
      spacing.select(1);
    }

    /* Set the other layout properties */
    rowLayout.wrap = wrap.getSelection();
    rowLayout.pack = pack.getSelection();
    rowLayout.justify = justify.getSelection();
  }
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

class GridLayoutTab extends Tab {
  /* Controls for setting layout parameters */
  Text numColumns;

  Button makeColumnsEqualWidth;

  Combo marginHeight, marginWidth, horizontalSpacing, verticalSpacing;

  /* The example layout instance */
  GridLayout gridLayout;

  /* TableEditors and related controls */
  TableEditor comboEditor, widthEditor, heightEditor;

  TableEditor vAlignEditor, hAlignEditor, hIndentEditor;

  TableEditor hSpanEditor, vSpanEditor, hGrabEditor, vGrabEditor;

  CCombo combo, vAlign, hAlign, hGrab, vGrab;

  Text widthText, heightText, hIndent, hSpan, vSpan;

  /* Constants */
  final int COMBO_COL = 1;

  final int WIDTH_COL = 2;

  final int HEIGHT_COL = 3;

  final int HALIGN_COL = 4;

  final int VALIGN_COL = 5;

  final int HINDENT_COL = 6;

  final int HSPAN_COL = 7;

  final int VSPAN_COL = 8;

  final int HGRAB_COL = 9;

  final int VGRAB_COL = 10;

  final int TOTAL_COLS = 11;

  /**
   * Creates the Tab within a given instance of LayoutExample.
   */
  GridLayoutTab(t2 instance) {
    super(instance);
  }

  /**
   * Creates the widgets in the "child" group.
   */
  void createChildWidgets() {
    /* Create the TraverseListener */
    final TraverseListener traverseListener = new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_RETURN
            || e.detail == SWT.TRAVERSE_TAB_NEXT)
          resetEditors();
        if (e.detail == SWT.TRAVERSE_ESCAPE)
          disposeEditors();
      }
    };

    /* Add common controls */
    super.createChildWidgets();

    /* Add TableEditors */
    comboEditor = new TableEditor(table);
    widthEditor = new TableEditor(table);
    heightEditor = new TableEditor(table);
    vAlignEditor = new TableEditor(table);
    hAlignEditor = new TableEditor(table);
    hIndentEditor = new TableEditor(table);
    hSpanEditor = new TableEditor(table);
    vSpanEditor = new TableEditor(table);
    hGrabEditor = new TableEditor(table);
    vGrabEditor = new TableEditor(table);
    table.addMouseListener(new MouseAdapter() {
      public void mouseDown(MouseEvent e) {
        resetEditors();
        index = table.getSelectionIndex();
        Point pt = new Point(e.x, e.y);
        newItem = table.getItem(pt);
        if (newItem == null)
          return;
        TableItem oldItem = comboEditor.getItem();
        if (newItem == oldItem || newItem != lastSelected) {
          lastSelected = newItem;
          return;
        }
        table.showSelection();

        combo = new CCombo(table, SWT.READ_ONLY);
        createComboEditor(combo, comboEditor);

        widthText = new Text(table, SWT.SINGLE);
        widthText
            .setText(((String[]) data.elementAt(index))[WIDTH_COL]);
        createTextEditor(widthText, widthEditor, WIDTH_COL);

        heightText = new Text(table, SWT.SINGLE);
        heightText
            .setText(((String[]) data.elementAt(index))[HEIGHT_COL]);
        createTextEditor(heightText, heightEditor, HEIGHT_COL);
        String[] alignValues = new String[] { "BEGINNING", "CENTER",
            "END", "FILL" };
        hAlign = new CCombo(table, SWT.NONE);
        hAlign.setItems(alignValues);
        hAlign.setText(newItem.getText(HALIGN_COL));
        hAlignEditor.horizontalAlignment = SWT.LEFT;
        hAlignEditor.grabHorizontal = true;
        hAlignEditor.minimumWidth = 50;
        hAlignEditor.setEditor(hAlign, newItem, HALIGN_COL);
        hAlign.addTraverseListener(traverseListener);

        vAlign = new CCombo(table, SWT.NONE);
        vAlign.setItems(alignValues);
        vAlign.setText(newItem.getText(VALIGN_COL));
        vAlignEditor.horizontalAlignment = SWT.LEFT;
        vAlignEditor.grabHorizontal = true;
        vAlignEditor.minimumWidth = 50;
        vAlignEditor.setEditor(vAlign, newItem, VALIGN_COL);
        vAlign.addTraverseListener(traverseListener);

        hIndent = new Text(table, SWT.SINGLE);
        hIndent
            .setText(((String[]) data.elementAt(index))[HINDENT_COL]);
        createTextEditor(hIndent, hIndentEditor, HINDENT_COL);

        hSpan = new Text(table, SWT.SINGLE);
        hSpan.setText(((String[]) data.elementAt(index))[HSPAN_COL]);
        createTextEditor(hSpan, hSpanEditor, HSPAN_COL);

        vSpan = new Text(table, SWT.SINGLE);
        vSpan.setText(((String[]) data.elementAt(index))[VSPAN_COL]);
        createTextEditor(vSpan, vSpanEditor, VSPAN_COL);

        String[] boolValues = new String[] { "false", "true" };
        hGrab = new CCombo(table, SWT.NONE);
        hGrab.setItems(boolValues);
        hGrab.setText(newItem.getText(HGRAB_COL));
        hGrabEditor.horizontalAlignment = SWT.LEFT;
        hGrabEditor.grabHorizontal = true;
        hGrabEditor.minimumWidth = 50;
        hGrabEditor.setEditor(hGrab, newItem, HGRAB_COL);
        hGrab.addTraverseListener(traverseListener);

        vGrab = new CCombo(table, SWT.NONE);
        vGrab.setItems(boolValues);
        vGrab.setText(newItem.getText(VGRAB_COL));
        vGrabEditor.horizontalAlignment = SWT.LEFT;
        vGrabEditor.grabHorizontal = true;
        vGrabEditor.minimumWidth = 50;
        vGrabEditor.setEditor(vGrab, newItem, VGRAB_COL);
        vGrab.addTraverseListener(traverseListener);

        for (int i = 0; i < table.getColumnCount(); i++) {
          Rectangle rect = newItem.getBounds(i);
          if (rect.contains(pt)) {
            switch (i) {
            case COMBO_COL:
              combo.setFocus();
              break;
            case WIDTH_COL:
              widthText.setFocus();
              break;
            case HEIGHT_COL:
              heightText.setFocus();
              break;
            case HALIGN_COL:
              hAlign.setFocus();
              break;
            case VALIGN_COL:
              vAlign.setFocus();
              break;
            case HINDENT_COL:
              hIndent.setFocus();
              break;
            case HSPAN_COL:
              hSpan.setFocus();
              break;
            case VSPAN_COL:
              vSpan.setFocus();
              break;
            case HGRAB_COL:
              hGrab.setFocus();
              break;
            case VGRAB_COL:
              vGrab.setFocus();
              break;
            default:
              resetEditors();
              break;
            }
          }
        }
      }
    });

    /* Add listener to add an element to the table */
    add.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        TableItem item = new TableItem(table, 0);
        String[] insert = new String[] {
            String.valueOf(table.indexOf(item)), "Button", "-1",
            "-1", "BEGINNING", "CENTER", "0", "1", "1", "false",
            "false" };
        item.setText(insert);
        data.addElement(insert);
        resetEditors();
      }
    });
  }

  /**
   * Creates the control widgets.
   */
  void createControlWidgets() {
    /* Rearrange the layout of the control group */
    size.setLayoutData(new GridData());

    /* Controls the margins and spacing of the GridLayout */
    String[] marginValues = new String[] { "0", "3", "5", "10" };
    Group marginGroup = new Group(controlGroup, SWT.NONE);
    marginGroup.setText(t2.getResourceString("Margins_Spacing"));
    GridData data = new GridData(GridData.FILL_HORIZONTAL);
    data.verticalSpan = 2;
    marginGroup.setLayoutData(data);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    marginGroup.setLayout(layout);
    new Label(marginGroup, SWT.NONE).setText("marginHeight");
    marginHeight = new Combo(marginGroup, SWT.NONE);
    marginHeight.setItems(marginValues);
    marginHeight.select(2);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.widthHint = 60;
    marginHeight.setLayoutData(data);
    marginHeight.addSelectionListener(selectionListener);
    marginHeight.addTraverseListener(traverseListener);
    new Label(marginGroup, SWT.NONE).setText("marginWidth");
    marginWidth = new Combo(marginGroup, SWT.NONE);
    marginWidth.setItems(marginValues);
    marginWidth.select(2);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.widthHint = 60;
    marginWidth.setLayoutData(data);
    marginWidth.addSelectionListener(selectionListener);
    marginWidth.addTraverseListener(traverseListener);
    new Label(marginGroup, SWT.NONE).setText("horizontalSpacing");
    horizontalSpacing = new Combo(marginGroup, SWT.NONE);
    horizontalSpacing.setItems(marginValues);
    horizontalSpacing.select(2);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.widthHint = 60;
    horizontalSpacing.setLayoutData(data);
    horizontalSpacing.addSelectionListener(selectionListener);
    horizontalSpacing.addTraverseListener(traverseListener);
    new Label(marginGroup, SWT.NONE).setText("verticalSpacing");
    verticalSpacing = new Combo(marginGroup, SWT.NONE);
    verticalSpacing.setItems(marginValues);
    verticalSpacing.select(2);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.widthHint = 60;
    verticalSpacing.setLayoutData(data);
    verticalSpacing.addSelectionListener(selectionListener);
    verticalSpacing.addTraverseListener(traverseListener);

    /* Controls the columns in the GridLayout */
    Group columnGroup = new Group(controlGroup, SWT.NONE);
    columnGroup.setText(t2.getResourceString("Columns"));
    layout = new GridLayout();
    layout.numColumns = 2;
    columnGroup.setLayout(layout);
    data = new GridData(GridData.VERTICAL_ALIGN_FILL);
    columnGroup.setLayoutData(data);
    numColumns = new Text(columnGroup, SWT.BORDER);
    numColumns.setText("1");
    numColumns.addSelectionListener(selectionListener);
    numColumns.addTraverseListener(traverseListener);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.widthHint = 15;
    numColumns.setLayoutData(data);
    new Label(columnGroup, SWT.NONE).setText("numColumns");
    makeColumnsEqualWidth = new Button(columnGroup, SWT.CHECK);
    makeColumnsEqualWidth.setText("makeColumnsEqualWidth");
    makeColumnsEqualWidth.addSelectionListener(selectionListener);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.horizontalSpan = 2;
    data.horizontalIndent = 14;
    makeColumnsEqualWidth.setLayoutData(data);

    /* Add common controls */
    super.createControlWidgets();
    controlGroup.pack();
  }

  /**
   * Creates the example layout.
   */
  void createLayout() {
    gridLayout = new GridLayout();
    layoutComposite.setLayout(gridLayout);
  }

  /**
   * Disposes the editors without placing their contents into the table.
   */
  void disposeEditors() {
    comboEditor.setEditor(null, null, -1);
    combo.dispose();
    widthText.dispose();
    heightText.dispose();
    hAlign.dispose();
    vAlign.dispose();
    hIndent.dispose();
    hSpan.dispose();
    vSpan.dispose();
    hGrab.dispose();
    vGrab.dispose();
  }

  /**
   * Generates code for the example layout.
   */
  StringBuffer generateLayoutCode() {
    StringBuffer code = new StringBuffer();
    code.append("\t\tGridLayout gridLayout = new GridLayout ();\n");
    if (gridLayout.numColumns != 1) {
      code.append("\t\tgridLayout.numColumns = " + gridLayout.numColumns
          + ";\n");
    }
    if (gridLayout.makeColumnsEqualWidth) {
      code.append("\t\tgridLayout.makeColumnsEqualWidth = true;\n");
    }
    if (gridLayout.marginHeight != 5) {
      code.append("\t\tgridLayout.marginHeight = "
          + gridLayout.marginHeight + ";\n");
    }
    if (gridLayout.marginWidth != 5) {
      code.append("\t\tgridLayout.marginWidth = "
          + gridLayout.marginWidth + ";\n");
    }
    if (gridLayout.horizontalSpacing != 5) {
      code.append("\t\tgridLayout.horizontalSpacing = "
          + gridLayout.horizontalSpacing + ";\n");
    }
    if (gridLayout.verticalSpacing != 5) {
      code.append("\t\tgridLayout.verticalSpacing = "
          + gridLayout.verticalSpacing + ";\n");
    }
    code.append("\t\tshell.setLayout (gridLayout);\n");

    boolean first = true;
    for (int i = 0; i < children.length; i++) {
      Control control = children[i];
      code.append(getChildCode(control, i));
      GridData data = (GridData) control.getLayoutData();
      if (data != null) {
        code.append("\t\t");
        if (first) {
          code.append("GridData ");
          first = false;
        }
        code.append("data = new GridData ();\n");
        if (data.widthHint != SWT.DEFAULT) {
          code.append("\t\tdata.widthHint = " + data.widthHint
              + ";\n");
        }
        if (data.heightHint != SWT.DEFAULT) {
          code.append("\t\tdata.heightHint = " + data.heightHint
              + ";\n");
        }
        if (data.horizontalAlignment != GridData.BEGINNING) {
          String alignment;
          int hAlignment = data.horizontalAlignment;
          if (hAlignment == GridData.CENTER)
            alignment = "GridData.CENTER";
          else if (hAlignment == GridData.END)
            alignment = "GridData.END";
          else
            alignment = "GridData.FILL";
          code.append("\t\tdata.horizontalAlignment = " + alignment
              + ";\n");
        }
        if (data.verticalAlignment != GridData.CENTER) {
          String alignment;
          int vAlignment = data.verticalAlignment;
          if (vAlignment == GridData.BEGINNING)
            alignment = "GridData.BEGINNING";
          else if (vAlignment == GridData.END)
            alignment = "GridData.END";
          else
            alignment = "GridData.FILL";
          code.append("\t\tdata.verticalAlignment = " + alignment
              + ";\n");
        }
        if (data.horizontalIndent != 0) {
          code.append("\t\tdata.horizontalIndent = "
              + data.horizontalIndent + ";\n");
        }
        if (data.horizontalSpan != 1) {
          code.append("\t\tdata.horizontalSpan = "
              + data.horizontalSpan + ";\n");
        }
        if (data.verticalSpan != 1) {
          code.append("\t\tdata.verticalSpan = " + data.verticalSpan
              + ";\n");
        }
        if (data.grabExcessHorizontalSpace) {
          code.append("\t\tdata.grabExcessHorizontalSpace = true;\n");
        }
        if (data.grabExcessVerticalSpace) {
          code.append("\t\tdata.grabExcessVerticalSpace = true;\n");
        }
        if (code.substring(code.length() - 33).equals(
            "GridData data = new GridData ();\n")) {
          code.delete(code.length() - 33, code.length());
          first = true;
        } else if (code.substring(code.length() - 24).equals(
            "data = new GridData ();\n")) {
          code.delete(code.length() - 24, code.length());
        } else {
          code.append("\t\t" + names[i] + ".setLayoutData (data);\n");
        }
      }
    }
    return code;
  }

  /**
   * Returns the layout data field names.
   */
  String[] getLayoutDataFieldNames() {
    return new String[] { "", "Control", "width", "height",
        "horizontalAlignment", "verticalAlignment", "horizontalIndent",
        "horizontalSpan", "verticalSpan", "grabExcessHorizontalSpace",
        "grabExcessVerticalSpace" };
  }

  /**
   * Gets the text for the tab folder item.
   */
  String getTabText() {
    return "GridLayout";
  }

  /**
   * Takes information from TableEditors and stores it.
   */
  void resetEditors() {
    resetEditors(false);
  }

  void resetEditors(boolean tab) {
    TableItem oldItem = comboEditor.getItem();
    if (oldItem != null) {
      int row = table.indexOf(oldItem);
      try {
        new Integer(widthText.getText()).intValue();
      } catch (NumberFormatException e) {
        widthText.setText(oldItem.getText(WIDTH_COL));
      }
      try {
        new Integer(heightText.getText()).intValue();
      } catch (NumberFormatException e) {
        heightText.setText(oldItem.getText(HEIGHT_COL));
      }
      try {
        new Integer(hIndent.getText()).intValue();
      } catch (NumberFormatException e) {
        hIndent.setText(oldItem.getText(HINDENT_COL));
      }
      try {
        new Integer(hSpan.getText()).intValue();
      } catch (NumberFormatException e) {
        hSpan.setText(oldItem.getText(HSPAN_COL));
      }
      try {
        new Integer(vSpan.getText()).intValue();
      } catch (NumberFormatException e) {
        vSpan.setText(oldItem.getText(VSPAN_COL));
      }
      String[] insert = new String[] { String.valueOf(row),
          combo.getText(), widthText.getText(), heightText.getText(),
          hAlign.getText(), vAlign.getText(), hIndent.getText(),
          hSpan.getText(), vSpan.getText(), hGrab.getText(),
          vGrab.getText() };
      data.setElementAt(insert, row);
      for (int i = 0; i < TOTAL_COLS; i++) {
        oldItem.setText(i, ((String[]) data.elementAt(row))[i]);
      }
      if (!tab)
        disposeEditors();
    }
    setLayoutState();
    refreshLayoutComposite();
    setLayoutData();
    layoutComposite.layout(true);
    layoutGroup.layout(true);
  }

  /**
   * Sets the layout data for the children of the layout.
   */
  void setLayoutData() {
    Control[] children = layoutComposite.getChildren();
    TableItem[] items = table.getItems();
    GridData data;
    int hIndent, hSpan, vSpan;
    String vAlign, hAlign, vGrab, hGrab;
    for (int i = 0; i < children.length; i++) {
      data = new GridData();
      /* Set widthHint and heightHint */
      data.widthHint = new Integer(items[i].getText(WIDTH_COL))
          .intValue();
      data.heightHint = new Integer(items[i].getText(HEIGHT_COL))
          .intValue();
      /* Set vertical alignment and horizontal alignment */
      hAlign = items[i].getText(HALIGN_COL);
      if (hAlign.equals("CENTER")) {
        data.horizontalAlignment = GridData.CENTER;
      } else if (hAlign.equals("END")) {
        data.horizontalAlignment = GridData.END;
      } else if (hAlign.equals("FILL")) {
        data.horizontalAlignment = GridData.FILL;
      } else {
        data.horizontalAlignment = GridData.BEGINNING;
      }
      vAlign = items[i].getText(VALIGN_COL);
      if (vAlign.equals("BEGINNING")) {
        data.verticalAlignment = GridData.BEGINNING;
      } else if (vAlign.equals("END")) {
        data.verticalAlignment = GridData.END;
      } else if (vAlign.equals("FILL")) {
        data.verticalAlignment = GridData.FILL;
      } else {
        data.verticalAlignment = GridData.CENTER;
      }
      /* Set indents and spans */
      hIndent = new Integer(items[i].getText(HINDENT_COL)).intValue();
      data.horizontalIndent = hIndent;
      hSpan = new Integer(items[i].getText(HSPAN_COL)).intValue();
      data.horizontalSpan = hSpan;
      vSpan = new Integer(items[i].getText(VSPAN_COL)).intValue();
      data.verticalSpan = vSpan;
      /* Set grabbers */
      hGrab = items[i].getText(HGRAB_COL);
      if (hGrab.equals("true")) {
        data.grabExcessHorizontalSpace = true;
      } else {
        data.grabExcessHorizontalSpace = false;
      }
      vGrab = items[i].getText(VGRAB_COL);
      if (vGrab.equals("true")) {
        data.grabExcessVerticalSpace = true;
      } else {
        data.grabExcessVerticalSpace = false;
      }
      children[i].setLayoutData(data);
    }
  }

  /**
   * Sets the state of the layout.
   */
  void setLayoutState() {
    /* Set the columns for the layout */
    try {
      gridLayout.numColumns = new Integer(numColumns.getText())
          .intValue();
    } catch (NumberFormatException e) {
      gridLayout.numColumns = 1;
    }
    gridLayout.makeColumnsEqualWidth = makeColumnsEqualWidth.getSelection();

    /* Set the margins and spacing */
    try {
      gridLayout.marginHeight = new Integer(marginHeight.getText())
          .intValue();
    } catch (NumberFormatException e) {
      gridLayout.marginHeight = 5;
      marginHeight.select(2);
    }
    try {
      gridLayout.marginWidth = new Integer(marginWidth.getText())
          .intValue();
    } catch (NumberFormatException e) {
      gridLayout.marginWidth = 5;
      marginWidth.select(2);
    }
    try {
      gridLayout.horizontalSpacing = new Integer(horizontalSpacing
          .getText()).intValue();
    } catch (NumberFormatException e) {
      gridLayout.horizontalSpacing = 5;
      horizontalSpacing.select(2);
    }
    try {
      gridLayout.verticalSpacing = new Integer(verticalSpacing.getText())
          .intValue();
    } catch (NumberFormatException e) {
      gridLayout.verticalSpacing = 5;
      verticalSpacing.select(2);
    }
  }
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

class FormLayoutTab extends Tab {
  /* Controls for setting layout parameters */
  Combo marginHeight, marginWidth;

  /* The example layout instance */
  FormLayout formLayout;

  /* TableEditors and related controls */
  TableEditor comboEditor, widthEditor, heightEditor;

  TableEditor leftEditor, rightEditor, topEditor, bottomEditor;

  CCombo combo;

  Text widthText, heightText;

  Button leftAttach, rightAttach, topAttach, bottomAttach;

  /* Constants */
  final int COMBO_COL = 1;

  final int WIDTH_COL = 2;

  final int HEIGHT_COL = 3;

  final int LEFT_COL = 4;

  final int RIGHT_COL = 5;

  final int TOP_COL = 6;

  final int BOTTOM_COL = 7;

  final int MODIFY_COLS = 4; // The number of columns with combo or text

  // editors

  final int TOTAL_COLS = 8;

  /**
   * Creates the Tab within a given instance of LayoutExample.
   */
  FormLayoutTab(t2 instance) {
    super(instance);
  }

  /**
   * Returns the constant for the alignment for an attachment given a string.
   */
  int alignmentConstant(String align) {
    if (align.equals("LEFT"))
      return SWT.LEFT;
    if (align.equals("RIGHT"))
      return SWT.RIGHT;
    if (align.equals("TOP"))
      return SWT.TOP;
    if (align.equals("BOTTOM"))
      return SWT.BOTTOM;
    if (align.equals("CENTER"))
      return SWT.CENTER;
    return SWT.DEFAULT;
  }

  /**
   * Returns a string representing the alignment for an attachment given a
   * constant.
   */
  String alignmentString(int align) {
    switch (align) {
    case SWT.LEFT:
      return "LEFT";
    case SWT.RIGHT:
      return "RIGHT";
    case SWT.TOP:
      return "TOP";
    case SWT.BOTTOM:
      return "BOTTOM";
    case SWT.CENTER:
      return "CENTER";
    }
    return "DEFAULT";
  }

  /**
   * Update the attachment field in case the type of control has changed.
   */
  String checkAttachment(String oldAttach, FormAttachment newAttach) {
    String controlClass = newAttach.control.getClass().toString();
    String controlType = controlClass.substring(controlClass
        .lastIndexOf('.') + 1);
    int i = 0;
    while (i < oldAttach.length()
        && !Character.isDigit(oldAttach.charAt(i))) {
      i++;
    }
    String index = oldAttach.substring(i, oldAttach.indexOf(','));
    return controlType + index + "," + newAttach.offset + ":"
        + alignmentString(newAttach.alignment);
  }

  /**
   * Creates the widgets in the "child" group.
   */
  void createChildWidgets() {
    /* Add common controls */
    super.createChildWidgets();

    /* Resize the columns */
    table.getColumn(LEFT_COL).setWidth(100);
    table.getColumn(RIGHT_COL).setWidth(100);
    table.getColumn(TOP_COL).setWidth(100);
    table.getColumn(BOTTOM_COL).setWidth(100);

    /* Add TableEditors */
    comboEditor = new TableEditor(table);
    widthEditor = new TableEditor(table);
    heightEditor = new TableEditor(table);
    leftEditor = new TableEditor(table);
    rightEditor = new TableEditor(table);
    topEditor = new TableEditor(table);
    bottomEditor = new TableEditor(table);
    table.addMouseListener(new MouseAdapter() {
      public void mouseDown(MouseEvent e) {
        resetEditors();
        index = table.getSelectionIndex();
        Point pt = new Point(e.x, e.y);
        newItem = table.getItem(pt);
        if (newItem == null)
          return;
        TableItem oldItem = comboEditor.getItem();
        if (newItem == oldItem || newItem != lastSelected) {
          lastSelected = newItem;
          return;
        }
        table.showSelection();

        combo = new CCombo(table, SWT.READ_ONLY);
        createComboEditor(combo, comboEditor);

        widthText = new Text(table, SWT.SINGLE);
        widthText
            .setText(((String[]) data.elementAt(index))[WIDTH_COL]);
        createTextEditor(widthText, widthEditor, WIDTH_COL);

        heightText = new Text(table, SWT.SINGLE);
        heightText
            .setText(((String[]) data.elementAt(index))[HEIGHT_COL]);
        createTextEditor(heightText, heightEditor, HEIGHT_COL);

        leftAttach = new Button(table, SWT.PUSH);
        leftAttach.setText(t2
            .getResourceString("Attach_Edit"));
        leftEditor.horizontalAlignment = SWT.LEFT;
        leftEditor.grabHorizontal = true;
        leftEditor.minimumWidth = leftAttach.computeSize(SWT.DEFAULT,
            SWT.DEFAULT).x;
        leftEditor.setEditor(leftAttach, newItem, LEFT_COL);
        leftAttach.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
            Shell shell = tabFolderPage.getShell();
            AttachDialog dialog = new AttachDialog(shell);
            dialog.setText(t2
                .getResourceString("Left_Attachment"));
            dialog.setColumn(LEFT_COL);
            String attach = dialog.open();
            newItem.setText(LEFT_COL, attach);
            resetEditors();
          }
        });

        rightAttach = new Button(table, SWT.PUSH);
        rightAttach.setText(t2
            .getResourceString("Attach_Edit"));
        rightEditor.horizontalAlignment = SWT.LEFT;
        rightEditor.grabHorizontal = true;
        rightEditor.minimumWidth = rightAttach.computeSize(SWT.DEFAULT,
            SWT.DEFAULT).x;
        rightEditor.setEditor(rightAttach, newItem, RIGHT_COL);
        rightAttach.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
            Shell shell = tabFolderPage.getShell();
            AttachDialog dialog = new AttachDialog(shell);
            dialog.setText(t2
                .getResourceString("Right_Attachment"));
            dialog.setColumn(RIGHT_COL);
            String attach = dialog.open();
            newItem.setText(RIGHT_COL, attach);
            if (newItem.getText(LEFT_COL).endsWith(")"))
              newItem.setText(LEFT_COL, "");
            resetEditors();
          }
        });

        topAttach = new Button(table, SWT.PUSH);
        topAttach.setText(t2
            .getResourceString("Attach_Edit"));
        topEditor.horizontalAlignment = SWT.LEFT;
        topEditor.grabHorizontal = true;
        topEditor.minimumWidth = topAttach.computeSize(SWT.DEFAULT,
            SWT.DEFAULT).x;
        topEditor.setEditor(topAttach, newItem, TOP_COL);
        topAttach.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
            Shell shell = tabFolderPage.getShell();
            AttachDialog dialog = new AttachDialog(shell);
            dialog.setText(t2
                .getResourceString("Top_Attachment"));
            dialog.setColumn(TOP_COL);
            String attach = dialog.open();
            newItem.setText(TOP_COL, attach);
            resetEditors();
          }
        });
        bottomAttach = new Button(table, SWT.PUSH);
        bottomAttach.setText(t2
            .getResourceString("Attach_Edit"));
        bottomEditor.horizontalAlignment = SWT.LEFT;
        bottomEditor.grabHorizontal = true;
        bottomEditor.minimumWidth = bottomAttach.computeSize(
            SWT.DEFAULT, SWT.DEFAULT).x;
        bottomEditor.setEditor(bottomAttach, newItem, BOTTOM_COL);
        bottomAttach.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent e) {
            Shell shell = tabFolderPage.getShell();
            AttachDialog dialog = new AttachDialog(shell);
            dialog.setText(t2
                .getResourceString("Bottom_Attachment"));
            dialog.setColumn(BOTTOM_COL);
            String attach = dialog.open();
            newItem.setText(BOTTOM_COL, attach);
            if (newItem.getText(TOP_COL).endsWith(")"))
              newItem.setText(TOP_COL, "");
            resetEditors();
          }
        });

        for (int i = 0; i < table.getColumnCount(); i++) {
          Rectangle rect = newItem.getBounds(i);
          if (rect.contains(pt)) {
            switch (i) {
            case 0:
              resetEditors();
              break;
            case COMBO_COL:
              combo.setFocus();
              break;
            case WIDTH_COL:
              widthText.setFocus();
              break;
            case HEIGHT_COL:
              heightText.setFocus();
              break;
            default:
              break;
            }
          }
        }
      }
    });

    /* Add listener to add an element to the table */
    add.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        TableItem item = new TableItem(table, 0);
        String[] insert = new String[] {
            String.valueOf(table.indexOf(item)),
            "Button",
            "-1",
            "-1",
            "0,0 (" + t2.getResourceString("Default")
                + ")",
            "",
            "0,0 (" + t2.getResourceString("Default")
                + ")", "" };
        item.setText(insert);
        data.addElement(insert);
        resetEditors();
      }
    });
  }

  /**
   * Creates the control widgets.
   */
  void createControlWidgets() {
    /* Controls the margins and spacing of the FormLayout */
    String[] marginValues = new String[] { "0", "3", "5", "10" };
    Group marginGroup = new Group(controlGroup, SWT.NONE);
    marginGroup.setText(t2.getResourceString("Margins"));
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    marginGroup.setLayout(layout);
    marginGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    new Label(marginGroup, SWT.NONE).setText("marginHeight");
    marginHeight = new Combo(marginGroup, SWT.NONE);
    marginHeight.setItems(marginValues);
    marginHeight.select(0);
    marginHeight.addSelectionListener(selectionListener);
    marginHeight.addTraverseListener(traverseListener);
    GridData data = new GridData(GridData.FILL_HORIZONTAL);
    data.widthHint = 60;
    marginHeight.setLayoutData(data);
    new Label(marginGroup, SWT.NONE).setText("marginWidth");
    marginWidth = new Combo(marginGroup, SWT.NONE);
    marginWidth.setItems(marginValues);
    marginWidth.select(0);
    marginWidth.addSelectionListener(selectionListener);
    marginWidth.addTraverseListener(traverseListener);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.widthHint = 60;
    marginWidth.setLayoutData(data);

    /* Add common controls */
    super.createControlWidgets();

    /* Position the sash */
    sash.setWeights(new int[] { 6, 4 });
  }

  /**
   * Creates the example layout.
   */
  void createLayout() {
    formLayout = new FormLayout();
    layoutComposite.setLayout(formLayout);
  }

  /**
   * Disposes the editors without placing their contents into the table.
   */
  void disposeEditors() {
    comboEditor.setEditor(null, null, -1);
    combo.dispose();
    widthText.dispose();
    heightText.dispose();
    leftAttach.dispose();
    rightAttach.dispose();
    topAttach.dispose();
    bottomAttach.dispose();
  }

  /**
   * Generates code for the example layout.
   */
  StringBuffer generateLayoutCode() {
    StringBuffer code = new StringBuffer();
    code.append("\t\tFormLayout formLayout = new FormLayout ();\n");
    if (formLayout.marginHeight != 0) {
      code.append("\t\tformLayout.marginHeight = "
          + formLayout.marginHeight + ";\n");
    }
    if (formLayout.marginWidth != 0) {
      code.append("\t\tformLayout.marginWidth = "
          + formLayout.marginWidth + ";\n");
    }
    code.append("\t\tshell.setLayout (formLayout);\n");

    boolean first = true;
    for (int i = 0; i < children.length; i++) {
      Control control = children[i];
      code.append(getChildCode(control, i));
      FormData data = (FormData) control.getLayoutData();
      if (data != null) {
        code.append("\t\t");
        if (first) {
          code.append("FormData ");
          first = false;
        }
        code.append("data = new FormData ();\n");
        if (data.width != SWT.DEFAULT) {
          code.append("\t\tdata.width = " + data.width + ";\n");
        }
        if (data.height != SWT.DEFAULT) {
          code.append("\t\tdata.height = " + data.height + ";\n");
        }
        if (data.left != null) {
          if (data.left.control != null) {
            TableItem item = table.getItem(i);
            String controlString = item.getText(LEFT_COL);
            int index = new Integer(controlString.substring(
                controlString.indexOf(',') - 1, controlString
                    .indexOf(','))).intValue();
            code
                .append("\t\tdata.left = new FormAttachment ("
                    + names[index] + ", "
                    + data.left.offset + ", SWT."
                    + alignmentString(data.left.alignment)
                    + ");\n");
          } else {
            if (data.right != null
                || (data.left.numerator != 0 || data.left.offset != 0)) {
              code.append("\t\tdata.left = new FormAttachment ("
                  + data.left.numerator + ", "
                  + data.left.offset + ");\n");
            }
          }
        }
        if (data.right != null) {
          if (data.right.control != null) {
            TableItem item = table.getItem(i);
            String controlString = item.getText(RIGHT_COL);
            int index = new Integer(controlString.substring(
                controlString.indexOf(',') - 1, controlString
                    .indexOf(','))).intValue();
            code.append("\t\tdata.right = new FormAttachment ("
                + names[index] + ", " + data.right.offset
                + ", SWT."
                + alignmentString(data.right.alignment)
                + ");\n");
          } else {
            code.append("\t\tdata.right = new FormAttachment ("
                + data.right.numerator + ", "
                + data.right.offset + ");\n");
          }
        }
        if (data.top != null) {
          if (data.top.control != null) {
            TableItem item = table.getItem(i);
            String controlString = item.getText(TOP_COL);
            int index = new Integer(controlString.substring(
                controlString.indexOf(',') - 1, controlString
                    .indexOf(','))).intValue();
            code.append("\t\tdata.top = new FormAttachment ("
                + names[index] + ", " + data.top.offset
                + ", SWT."
                + alignmentString(data.top.alignment) + ");\n");
          } else {
            if (data.bottom != null
                || (data.top.numerator != 0 || data.top.offset != 0)) {
              code.append("\t\tdata.top = new FormAttachment ("
                  + data.top.numerator + ", "
                  + data.top.offset + ");\n");
            }
          }
        }
        if (data.bottom != null) {
          if (data.bottom.control != null) {
            TableItem item = table.getItem(i);
            String controlString = item.getText(BOTTOM_COL);
            int index = new Integer(controlString.substring(
                controlString.indexOf(',') - 1, controlString
                    .indexOf(','))).intValue();
            code.append("\t\tdata.bottom = new FormAttachment ("
                + names[index] + ", " + data.bottom.offset
                + ", SWT."
                + alignmentString(data.bottom.alignment)
                + ");\n");
          } else {
            code.append("\t\tdata.bottom = new FormAttachment ("
                + data.bottom.numerator + ", "
                + data.bottom.offset + ");\n");
          }
        }
        code.append("\t\t" + names[i] + ".setLayoutData (data);\n");
      }
    }
    return code;
  }

  /**
   * Returns the layout data field names.
   */
  String[] getLayoutDataFieldNames() {
    return new String[] { "", "Control", "width", "height", "left",
        "right", "top", "bottom" };
  }

  /**
   * Gets the text for the tab folder item.
   */
  String getTabText() {
    return "FormLayout";
  }

  /**
   * Takes information from TableEditors and stores it.
   */
  void resetEditors() {
    resetEditors(false);
  }

  void resetEditors(boolean tab) {
    TableItem oldItem = comboEditor.getItem();
    if (oldItem != null) {
      int row = table.indexOf(oldItem);
      try {
        new Integer(widthText.getText()).intValue();
      } catch (NumberFormatException e) {
        widthText.setText(oldItem.getText(WIDTH_COL));
      }
      try {
        new Integer(heightText.getText()).intValue();
      } catch (NumberFormatException e) {
        heightText.setText(oldItem.getText(HEIGHT_COL));
      }
      String[] insert = new String[] { String.valueOf(row),
          combo.getText(), widthText.getText(), heightText.getText() };
      data.setElementAt(insert, row);
      for (int i = 0; i < MODIFY_COLS; i++) {
        oldItem.setText(i, ((String[]) data.elementAt(row))[i]);
      }
      if (!tab)
        disposeEditors();
    }
    setLayoutState();
    refreshLayoutComposite();
    setLayoutData();
    layoutComposite.layout(true);
    layoutGroup.layout(true);
  }

  /**
   * Sets an attachment to the edge of a widget using the information in the
   * table.
   */
  FormAttachment setAttachment(String attachment) {
    String control, align;
    int position, offset;
    int comma = attachment.indexOf(',');
    char first = attachment.charAt(0);
    if (Character.isLetter(first)) {
      /* Case where there is a control */
      control = attachment.substring(0, comma);
      int i = 0;
      while (i < control.length()
          && !Character.isDigit(control.charAt(i))) {
        i++;
      }
      String end = control.substring(i);
      int index = new Integer(end).intValue();
      Control attachControl = children[index];
      int colon = attachment.indexOf(':');
      try {
        offset = new Integer(attachment.substring(comma + 1, colon))
            .intValue();
      } catch (NumberFormatException e) {
        offset = 0;
      }
      align = attachment.substring(colon + 1);
      return new FormAttachment(attachControl, offset,
          alignmentConstant(align));
    } else {
      /* Case where there is a position */
      try {
        position = new Integer(attachment.substring(0, comma))
            .intValue();
      } catch (NumberFormatException e) {
        position = 0;
      }
      try {
        offset = new Integer(attachment.substring(comma + 1))
            .intValue();
      } catch (NumberFormatException e) {
        offset = 0;
      }
      return new FormAttachment(position, offset);
    }
  }

  /**
   * Sets the layout data for the children of the layout.
   */
  void setLayoutData() {
    Control[] children = layoutComposite.getChildren();
    TableItem[] items = table.getItems();
    FormData data;
    int width, height;
    String left, right, top, bottom;
    for (int i = 0; i < children.length; i++) {
      width = new Integer(items[i].getText(WIDTH_COL)).intValue();
      height = new Integer(items[i].getText(HEIGHT_COL)).intValue();
      data = new FormData();
      if (width > 0)
        data.width = width;
      if (height > 0)
        data.height = height;

      left = items[i].getText(LEFT_COL);
      if (left.length() > 0) {
        data.left = setAttachment(left);
        if (data.left.control != null) {
          String attachment = checkAttachment(left, data.left);
          items[i].setText(LEFT_COL, attachment);
        }
      }
      right = items[i].getText(RIGHT_COL);
      if (right.length() > 0) {
        data.right = setAttachment(right);
        if (data.right.control != null) {
          String attachment = checkAttachment(right, data.right);
          items[i].setText(RIGHT_COL, attachment);
        }
      }
      top = items[i].getText(TOP_COL);
      if (top.length() > 0) {
        data.top = setAttachment(top);
        if (data.top.control != null) {
          String attachment = checkAttachment(top, data.top);
          items[i].setText(TOP_COL, attachment);
        }
      }
      bottom = items[i].getText(BOTTOM_COL);
      if (bottom.length() > 0) {
        data.bottom = setAttachment(bottom);
        if (data.bottom.control != null) {
          String attachment = checkAttachment(bottom, data.bottom);
          items[i].setText(BOTTOM_COL, attachment);
        }
      }
      children[i].setLayoutData(data);
    }
  }

  /**
   * Sets the state of the layout.
   */
  void setLayoutState() {
    /* Set the margins and spacing */
    try {
      formLayout.marginHeight = new Integer(marginHeight.getText())
          .intValue();
    } catch (NumberFormatException e) {
      formLayout.marginHeight = 0;
      marginHeight.select(0);
    }
    try {
      formLayout.marginWidth = new Integer(marginWidth.getText())
          .intValue();
    } catch (NumberFormatException e) {
      formLayout.marginWidth = 0;
      marginWidth.select(0);
    }
  }

  /**
   * <code>AttachDialog</code> is the class that creates a dialog specific
   * for this example. It creates a dialog with controls to set the values in
   * a FormAttachment.
   */
  public class AttachDialog extends Dialog {
    String result = "";

    String controlInput, positionInput, alignmentInput, offsetInput;

    int col = 0;

    public AttachDialog(Shell parent, int style) {
      super(parent, style);
    }

    public AttachDialog(Shell parent) {
      this(parent, 0);
    }

    public void setColumn(int col) {
      this.col = col;
    }

    public String open() {
      Shell parent = getParent();
      final Shell shell = new Shell(parent, SWT.DIALOG_TRIM
          | SWT.APPLICATION_MODAL);
      shell.setText(getText());
      GridLayout layout = new GridLayout();
      layout.numColumns = 3;
      layout.makeColumnsEqualWidth = true;
      shell.setLayout(layout);

      /* Find out what was previously set as an attachment */
      TableItem newItem = leftEditor.getItem();
      result = newItem.getText(col);
      String oldAttach = result;
      String oldPos = "0", oldControl = "", oldAlign = "DEFAULT", oldOffset = "0";
      boolean isControl = false;
      if (oldAttach.length() != 0) {
        char first = oldAttach.charAt(0);
        if (Character.isLetter(first)) {
          /* We have a control */
          isControl = true;
          oldControl = oldAttach.substring(0, oldAttach.indexOf(','));
          oldAlign = oldAttach.substring(oldAttach.indexOf(':') + 1);
          oldOffset = oldAttach.substring(oldAttach.indexOf(',') + 1,
              oldAttach.indexOf(':'));
        } else {
          /* We have a position */
          oldPos = oldAttach.substring(0, oldAttach.indexOf(','));
          oldOffset = oldAttach.substring(oldAttach.indexOf(',') + 1);
          if (oldOffset.endsWith(")")) { // i.e. (Default)
            oldOffset = oldOffset.substring(0, oldOffset
                .indexOf(' '));
          }
        }
      }

      /* Add position field */
      final Button posButton = new Button(shell, SWT.RADIO);
      posButton.setText(t2.getResourceString("Position"));
      posButton.setSelection(!isControl);
      final Combo position = new Combo(shell, SWT.NONE);
      position.setItems(new String[] { "0", "25", "50", "75", "100" });
      position.setText(oldPos);
      position.setEnabled(!isControl);
      GridData data = new GridData(GridData.FILL_HORIZONTAL);
      data.horizontalSpan = 2;
      position.setLayoutData(data);

      /* Add control field */
      final Button contButton = new Button(shell, SWT.RADIO);
      contButton.setText(t2.getResourceString("Control"));
      contButton.setSelection(isControl);
      final Combo control = new Combo(shell, SWT.READ_ONLY);
      TableItem[] items = table.getItems();
      TableItem currentItem = leftEditor.getItem();
      for (int i = 0; i < table.getItemCount(); i++) {
        if (items[i].getText(0).length() > 0) {
          if (items[i] != currentItem) {
            control.add(items[i].getText(COMBO_COL) + i);
          }
        }
      }
      if (oldControl.length() != 0)
        control.setText(oldControl);
      else
        control.select(0);
      control.setEnabled(isControl);
      data = new GridData(GridData.FILL_HORIZONTAL);
      data.horizontalSpan = 2;
      control.setLayoutData(data);

      /* Add alignment field */
      new Label(shell, SWT.NONE).setText(t2
          .getResourceString("Alignment"));
      final Combo alignment = new Combo(shell, SWT.NONE);
      String[] alignmentValues;
      if (col == LEFT_COL || col == RIGHT_COL) {
        alignmentValues = new String[] { "SWT.LEFT", "SWT.RIGHT",
            "SWT.CENTER", "SWT.DEFAULT" };
      } else {
        // col == TOP_COL || col == BOTTOM_COL
        alignmentValues = new String[] { "SWT.TOP", "SWT.BOTTOM",
            "SWT.CENTER", "SWT.DEFAULT" };
      }
      alignment.setItems(alignmentValues);
      alignment.setText("SWT." + oldAlign);
      alignment.setEnabled(isControl);
      data = new GridData(GridData.FILL_HORIZONTAL);
      data.horizontalSpan = 2;
      alignment.setLayoutData(data);

      /* Add offset field */
      new Label(shell, SWT.NONE).setText(t2
          .getResourceString("Offset"));
      final Text offset = new Text(shell, SWT.SINGLE | SWT.BORDER);
      offset.setText(oldOffset);
      data = new GridData(GridData.FILL_HORIZONTAL);
      data.horizontalSpan = 2;
      offset.setLayoutData(data);

      /* Add listeners for choosing between position and control */
      posButton.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          position.setEnabled(true);
          control.setEnabled(false);
          alignment.setEnabled(false);
        }
      });
      contButton.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          position.setEnabled(false);
          control.setEnabled(true);
          alignment.setEnabled(true);
        }
      });

      Button clear = new Button(shell, SWT.PUSH);
      clear.setText(t2.getResourceString("Clear"));
      clear.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
      clear.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          result = "";
          shell.close();
        }
      });
      /* OK button sets data into table */
      Button ok = new Button(shell, SWT.PUSH);
      ok.setText(t2.getResourceString("OK"));
      ok.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
      ok.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          controlInput = control.getText();
          alignmentInput = alignment.getText().substring(4);
          positionInput = position.getText();
          if (positionInput.length() == 0)
            positionInput = "0";
          try {
            new Integer(positionInput).intValue();
          } catch (NumberFormatException except) {
            positionInput = "0";
          }
          offsetInput = offset.getText();
          if (offsetInput.length() == 0)
            offsetInput = "0";
          try {
            new Integer(offsetInput).intValue();
          } catch (NumberFormatException except) {
            offsetInput = "0";
          }
          if (posButton.getSelection() || controlInput.length() == 0) {
            result = positionInput + "," + offsetInput;
          } else {
            result = controlInput + "," + offsetInput + ":"
                + alignmentInput;
          }
          shell.close();
        }
      });
      Button cancel = new Button(shell, SWT.PUSH);
      cancel.setText(t2.getResourceString("Cancel"));
      cancel.setLayoutData(new GridData(
          GridData.HORIZONTAL_ALIGN_BEGINNING));
      cancel.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          shell.close();
        }
      });

      shell.setDefaultButton(ok);
      shell.pack();
      /* Center the dialog */
      Point center = parent.getLocation();
      center.x = center.x + (parent.getBounds().width / 2)
          - (shell.getBounds().width / 2);
      center.y = center.y + (parent.getBounds().height / 2)
          - (shell.getBounds().height / 2);
      shell.setLocation(center);
      shell.open();
      Display display = shell.getDisplay();
      while (!shell.isDisposed()) {
        if (display.readAndDispatch())
          display.sleep();
      }

      return result;
    }
  }
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

class FillLayoutTab extends Tab {
  /* Controls for setting layout parameters */
  Button horizontal, vertical;

  /* The example layout instance */
  FillLayout fillLayout;

  /* TableEditors and related controls */
  TableEditor comboEditor;

  CCombo combo;

  /**
   * Creates the Tab within a given instance of LayoutExample.
   */
  FillLayoutTab(t2 instance) {
    super(instance);
  }

  /**
   * Creates the widgets in the "child" group.
   */
  void createChildWidgets() {
    /* Add common controls */
    super.createChildWidgets();

    /* Add TableEditors */
    comboEditor = new TableEditor(table);
    table.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        resetEditors();
        index = table.getSelectionIndex();
        if (index == -1)
          return;
        TableItem oldItem = comboEditor.getItem();
        newItem = table.getItem(index);
        if (newItem == oldItem || newItem != lastSelected) {
          lastSelected = newItem;
          return;
        }
        table.showSelection();

        combo = new CCombo(table, SWT.READ_ONLY);
        createComboEditor(combo, comboEditor);
      }
    });

    /* Add listener to add an element to the table */
    add.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        TableItem item = new TableItem(table, 0);
        item.setText(0, String.valueOf(table.indexOf(item)));
        item.setText(1, "Button");
        data.addElement("Button");
        resetEditors();
      }
    });
  }

  /**
   * Creates the control widgets.
   */
  void createControlWidgets() {
    /* Controls the type of FillLayout */
    Group typeGroup = new Group(controlGroup, SWT.NONE);
    typeGroup.setText(t2.getResourceString("Type"));
    typeGroup.setLayout(new GridLayout());
    typeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    horizontal = new Button(typeGroup, SWT.RADIO);
    horizontal.setText("SWT.HORIZONTAL");
    horizontal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    horizontal.setSelection(true);
    horizontal.addSelectionListener(selectionListener);
    vertical = new Button(typeGroup, SWT.RADIO);
    vertical.setText("SWT.VERTICAL");
    vertical.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    vertical.addSelectionListener(selectionListener);

    /* Add common controls */
    super.createControlWidgets();

    /* Position the sash */
    sash.setWeights(new int[] { 4, 1 });
  }

  /**
   * Creates the example layout.
   */
  void createLayout() {
    fillLayout = new FillLayout();
    layoutComposite.setLayout(fillLayout);
  }

  /**
   * Disposes the editors without placing their contents into the table.
   */
  void disposeEditors() {
    comboEditor.setEditor(null, null, -1);
    combo.dispose();
  }

  /**
   * Generates code for the example layout.
   */
  StringBuffer generateLayoutCode() {
    StringBuffer code = new StringBuffer();
    code.append("\t\tFillLayout fillLayout = new FillLayout ();\n");
    if (fillLayout.type == SWT.VERTICAL) {
      code.append("\t\tfillLayout.type = SWT.VERTICAL;\n");
    }
    code.append("\t\tshell.setLayout (fillLayout);\n");
    for (int i = 0; i < children.length; i++) {
      Control control = children[i];
      code.append(getChildCode(control, i));
    }
    return code;
  }

  /**
   * Returns the layout data field names.
   */
  String[] getLayoutDataFieldNames() {
    return new String[] { "", "Control" };
  }

  /**
   * Gets the text for the tab folder item.
   */
  String getTabText() {
    return "FillLayout";
  }

  /**
   * Takes information from TableEditors and stores it.
   */
  void resetEditors() {
    TableItem oldItem = comboEditor.getItem();
    comboEditor.setEditor(null, null, -1);
    if (oldItem != null) {
      int row = table.indexOf(oldItem);
      data.insertElementAt(combo.getText(), row);
      oldItem.setText(1, data.elementAt(row).toString());
      combo.dispose();
    }
    setLayoutState();
    refreshLayoutComposite();
    layoutComposite.layout(true);
    layoutGroup.layout(true);
  }

  /**
   * Sets the state of the layout.
   */
  void setLayoutState() {
    if (vertical.getSelection()) {
      fillLayout.type = SWT.VERTICAL;
    } else {
      fillLayout.type = SWT.HORIZONTAL;
    }
  }
}