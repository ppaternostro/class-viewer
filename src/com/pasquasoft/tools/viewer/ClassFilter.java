package com.pasquasoft.tools.viewer;

import java.io.File;

class ClassFilter extends javax.swing.filechooser.FileFilter
{
  public boolean accept(File file)
  {
    return file.isDirectory() || file.getName().endsWith(".class");
  }

  public String getDescription()
  {
    return "Class Files (*.class)";
  }
}
