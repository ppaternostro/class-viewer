package com.pasquasoft.tools.viewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

class ClassViewerFrame extends JFrame implements ActionListener, ItemListener
{
  /**
   * The serial version id.
   */
  private static final long serialVersionUID = 4173076731993434776L;

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
  private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

  private JToolBar jtb = new JToolBar();

  private JMenuBar mb = new JMenuBar();

  private JMenu file = new JMenu("File");
  private JMenu view = new JMenu("View");
  private JMenu look = new JMenu("Look and Feel");
  private JMenu help = new JMenu("Help");

  private JMenuItem open = new JMenuItem();
  private JMenuItem exit = new JMenuItem();
  private JMenuItem about = new JMenuItem();

  private JButton tbOpen = new JButton();
  private JButton tbExit = new JButton();
  private JButton tbAbout = new JButton();

  private JRadioButtonMenuItem looks[];

  private JCheckBoxMenuItem tool = new JCheckBoxMenuItem("Toolbar");
  private JCheckBoxMenuItem status = new JCheckBoxMenuItem("Status Bar");

  private JScrollPane jsp = new JScrollPane();

  private JComboBox<String> jcb = new JComboBox<String>();

  private JFileChooser jfc;

  private JTextField jtf = new JTextField(10);

  private JPanel panel = new JPanel();

  private ButtonGroup looksGroup = new ButtonGroup();

  private StatusBar statusBar;

  private Timer timer;

  private Date current = new Date();

  @SuppressWarnings("serial")
  public ClassViewerFrame(String title)
  {
    super(title);

    UIManager.LookAndFeelInfo[] installedLooks = UIManager.getInstalledLookAndFeels();

    looks = new JRadioButtonMenuItem[installedLooks.length];

    /* Create actions for common menu and toolbar items */
    Action openAction = new AbstractAction(null, new ImageIcon(ClassLoader.getSystemResource("images/open.gif"))) {
      public void actionPerformed(ActionEvent evt)
      {
        jfc = new JFileChooser();
        jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
        jfc.addChoosableFileFilter(new ClassFilter());

        int approve = jfc.showOpenDialog(ClassViewerFrame.this);

        if (approve == JFileChooser.APPROVE_OPTION)
        {
          File file = jfc.getSelectedFile();

          if (file != null && file.getName().endsWith(".class"))
            jsp.setViewportView(getTree(file));
          else
            JOptionPane.showMessageDialog(ClassViewerFrame.this, "Invalid file selected!", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
      }
    };

    Action exitAction = new AbstractAction(null, new ImageIcon(ClassLoader.getSystemResource("images/exit.gif"))) {
      public void actionPerformed(ActionEvent evt)
      {
        dispatchEvent(new WindowEvent(ClassViewerFrame.this, WindowEvent.WINDOW_CLOSING));
      }
    };

    Action aboutAction = new AbstractAction(null, new ImageIcon(ClassLoader.getSystemResource("images/help.gif"))) {
      public void actionPerformed(ActionEvent evt)
      {
        JOptionPane.showMessageDialog(ClassViewerFrame.this,
            "<html><center>ClassViewer Application<br>Pat Paternostro<br>Copyright &copy; 2004-2021</center></html>",
            "About ClassViewer", JOptionPane.INFORMATION_MESSAGE);
      }
    };

    statusBar = new StatusBar(new String[] { "Date: ", "Time: " });

    /* Components should be added to the container's content pane */
    Container cp = getContentPane();

    /* Add toolbar and scroll pane to frame */
    cp.add(BorderLayout.NORTH, jtb);
    cp.add(BorderLayout.CENTER, jsp);

    /* Add components to panel */
    panel.setLayout(new GridLayout(2, 2, 5, 5));
    panel.add(jtf);
    panel.add(jcb);

    /* Add items to the toolbar */
    tbOpen.setAction(openAction);
    tbOpen.setToolTipText("Open");
    jtb.add(tbOpen);
    tbExit.setAction(exitAction);
    tbExit.setToolTipText("Exit");
    jtb.add(tbExit);
    jtb.addSeparator();
    tbAbout.setAction(aboutAction);
    tbAbout.setToolTipText("About");
    jtb.add(tbAbout);
    jtb.addSeparator();
    jtb.add(panel);

    /* Set the toolbar fixed */
    jtb.setFloatable(false);

    String defaultLaf = UIManager.getLookAndFeel().getName();

    /*
     * Create menu items, add action listener, set the action command to the
     * Look and Feel class name, set selected menu item, and add to Look and
     * Feel menu and group.
     */
    for (int i = 0; i < looks.length; i++)
    {
      String installedLaf = installedLooks[i].getName();

      looks[i] = new JRadioButtonMenuItem(installedLaf);
      looks[i].addActionListener(this);
      looks[i].setActionCommand(installedLooks[i].getClassName());

      if (installedLaf.equals(defaultLaf))
        looks[i].setSelected(true);

      looksGroup.add(looks[i]);
      look.add(looks[i]);
    }

    /* Add menu items to menus */
    open.setAction(openAction);
    file.add(open);
    file.addSeparator();
    exit.setAction(exitAction);
    file.add(exit);
    view.add(tool);
    view.add(status);
    view.addSeparator();
    view.add(look);
    about = help.add(aboutAction);

    /* Set labels */
    open.setText("Open...");
    exit.setText("Exit");
    about.setText("About...");

    /* Set mnemonics */
    file.setMnemonic(KeyEvent.VK_F);
    open.setMnemonic(KeyEvent.VK_O);
    exit.setMnemonic(KeyEvent.VK_E);
    view.setMnemonic(KeyEvent.VK_V);
    tool.setMnemonic(KeyEvent.VK_T);
    status.setMnemonic(KeyEvent.VK_S);
    help.setMnemonic(KeyEvent.VK_H);
    about.setMnemonic(KeyEvent.VK_A);

    /* Set accelerators */
    open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
    about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));

    /* Add menus to menubar */
    mb.add(file);
    mb.add(view);
    mb.add(help);

    /* Set menubar */
    setJMenuBar(mb);

    /* Add the listeners */
    tool.addActionListener(this);
    status.addActionListener(this);
    jtf.addActionListener(this);
    jcb.addItemListener(this);

    /* Set the state of the Toolbar menu item */
    tool.setState(true);

    /* Add the window listener */
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt)
      {
        dispose();
        System.exit(0);
      }
    });

    /* Size the frame */
    setSize(400, 300);

    /* Center the frame */
    setLocationRelativeTo(null);

    /* Show the frame */
    setVisible(true);
  }

  public void actionPerformed(final ActionEvent evt)
  {
    final Object obj = evt.getSource();

    if (obj == tool)
    {
      Container cp = getContentPane();

      if (tool.getState())
      {
        /* Look and feel may have changed */
        jtb.updateUI();

        cp.add(BorderLayout.NORTH, jtb);
      }
      else
      {
        cp.remove(jtb);
      }

      /* Layout components after container change */
      SwingUtilities.updateComponentTreeUI(ClassViewerFrame.this);
    }
    else if (obj == status)
    {
      Container cp = getContentPane();

      if (status.getState())
      {
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new StatusTask(), 0, 1000);

        /* Look and feel may have changed */
        statusBar.updateUI();

        cp.add(BorderLayout.SOUTH, statusBar);
      }
      else
      {
        timer.cancel();

        cp.remove(statusBar);
      }

      /* Layout components after container change */
      SwingUtilities.updateComponentTreeUI(ClassViewerFrame.this);
    }
    else if (obj == jtf)
    {
      String name = jtf.getText();

      if (!name.equals(""))
      {
        jtf.setText("");
        jcb.insertItemAt(name, 0);
        jcb.setSelectedIndex(0);
      }
    }
    else if (obj instanceof JRadioButtonMenuItem)
    {
      try
      {
        /*
         * The radio button menu item's action command is set to the associated
         * Look and Feel class name.
         */
        UIManager.setLookAndFeel(((AbstractButton) obj).getActionCommand());
        SwingUtilities.updateComponentTreeUI(ClassViewerFrame.this);
      }
      catch (final Throwable th)
      {
        JOptionPane.showMessageDialog(ClassViewerFrame.this, th.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void itemStateChanged(final ItemEvent evt)
  {
    Object obj = evt.getSource();

    if (obj == jcb)
    {
      if (evt.getStateChange() == ItemEvent.SELECTED)
      {
        SwingUtilities.invokeLater(new Runnable() {
          public void run()
          {
            jsp.setViewportView(getTree((String) evt.getItem()));
          }
        });
      }
    }
  }

  public JTree getTree(File f)
  {
    Class<?> c = null;

    try
    {
      c = new FileClassLoader().loadClassBytes(f);
    }
    catch (final ClassNotFoundException cnfe)
    {
      SwingUtilities.invokeLater(new Runnable() {
        public void run()
        {
          JOptionPane.showMessageDialog(ClassViewerFrame.this, cnfe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      });
    }

    return c != null ? getTreeFromClass(c) : null;
  }

  public JTree getTree(String str)
  {
    Class<?> c = null;

    try
    {
      c = new FileClassLoader().loadClass(str);
    }
    catch (final ClassNotFoundException cnfe)
    {
      SwingUtilities.invokeLater(new Runnable() {
        public void run()
        {
          int index = jcb.getSelectedIndex();

          jcb.setSelectedIndex(-1);
          jcb.removeItemAt(index);

          JOptionPane.showMessageDialog(ClassViewerFrame.this, cnfe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      });
    }

    return c != null ? getTreeFromClass(c) : null;
  }

  private JTree getTreeFromClass(Class<?> c)
  {
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
    DefaultMutableTreeNode constructorNode = new DefaultMutableTreeNode("Constructors");
    DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode("Methods");
    DefaultMutableTreeNode attributeNode = new DefaultMutableTreeNode("Attributes");
    JTree classTree = new JTree(rootNode);
    Class<?> interfaces[] = null;

    /* Set tree's line style */
    classTree.putClientProperty("JTee.lineStyle", "Angled");

    Constructor<?> constructors[] = c.getDeclaredConstructors();
    Method methods[] = c.getDeclaredMethods();
    Field fields[] = c.getDeclaredFields();

    /* Build the class signature */
    StringBuffer sb = new StringBuffer(150).append(Modifier.toString(c.getModifiers()))
        .append(c.isInterface() ? " " : " class ").append(c.getName());

    if (c.getSuperclass() != null)
      sb.append(" extends ").append(c.getSuperclass().getName());

    interfaces = c.getInterfaces();

    for (int i = 0; i < interfaces.length; i++)
    {
      sb.append(i != 0 ? ", " : " implements ");
      sb.append(interfaces[i].getName());
    }

    rootNode.setUserObject(sb.toString());

    /* Build the constructor, method, and attribute signatures */
    buildSignatures(constructors, constructorNode);
    buildSignatures(methods, methodNode);
    buildSignatures(fields, attributeNode);

    /* Add the nodes to the root node */
    if (constructors.length != 0)
    {
      constructorNode.setUserObject(constructorNode + String.format(" (%d)", constructors.length));
      rootNode.add(constructorNode);
    }

    if (methods.length != 0)
    {
      methodNode.setUserObject(methodNode + String.format(" (%d)", methods.length));
      rootNode.add(methodNode);
    }

    if (fields.length != 0)
    {
      attributeNode.setUserObject(attributeNode + String.format(" (%d)", fields.length));
      rootNode.add(attributeNode);
    }

    /* Ensure root node is expanded */
    classTree.expandRow(0);

    return classTree;
  }

  private void buildSignatures(Member[] member, DefaultMutableTreeNode node)
  {
    StringBuffer sb = new StringBuffer(100);
    Class<?> param[] = null;
    Class<?> except[] = null;

    for (int i = 0; i < member.length; i++)
    {
      sb.append(Modifier.toString(member[i].getModifiers())).append(" ");

      if (member instanceof Method[])
        sb.append(getTypeName(((Method) member[i]).getReturnType())).append(" ");
      else if (member instanceof Field[])
        sb.append(getTypeName(((Field) member[i]).getType())).append(" ");

      sb.append(member[i].getName());

      if (!(member instanceof Field[]))
      {
        param = member instanceof Constructor<?>[] ? ((Constructor<?>) member[i]).getParameterTypes()
            : ((Method) member[i]).getParameterTypes();

        except = member instanceof Constructor<?>[] ? ((Constructor<?>) member[i]).getExceptionTypes()
            : ((Method) member[i]).getExceptionTypes();

        sb.append("(");

        /* Cycle through the parameters */
        for (int j = 0; j < param.length; j++)
        {
          if (j != 0)
            sb.append(", ");

          sb.append(getTypeName(param[j]));
        }

        sb.append(")");

        /* Cycle through the exceptions */
        for (int j = 0; j < except.length; j++)
        {
          sb.append(j != 0 ? ", " : " throws ");
          sb.append(except[j].getName());
        }
      }

      node.add(new DefaultMutableTreeNode(sb.toString().trim()));

      sb.setLength(0);
    }
  }

  /**
   * Retrieves the type name of the specified <code>Class</code> object.
   * 
   * @param type a <code>Class</code> object
   * @return the <code>Class</code> object's type name
   */
  private String getTypeName(Class<?> type)
  {
    String typeName = null;
    int dimensions = 0;

    /* If an array type, cycle through the dimensions */
    while (type.isArray())
    {
      dimensions++;
      type = type.getComponentType();
    }

    /* Get the type name */
    typeName = type.getName();

    /* If an array type add the dimensions to the name */
    for (int i = 0; i < dimensions; i++)
      typeName += "[]";

    return typeName;
  }

  private class StatusTask extends TimerTask
  {
    public void run()
    {
      current.setTime(System.currentTimeMillis());

      /* Update the status bar */
      statusBar.setStatusSection(0, "Date: " + dateFormat.format(current));
      statusBar.setStatusSection(1, "Time: " + timeFormat.format(current));
    }
  }
}
