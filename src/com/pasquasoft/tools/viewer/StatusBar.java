package com.pasquasoft.tools.viewer;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A class that constructs a status bar. Status
 * bars usually display status information in the 
 * bottom part of a window.
 *
 * @author    Pat Paternostro
 * @version   v1.0
 */
public class StatusBar extends JPanel
{
  /**
   * The serial version id.
   */
  private static final long serialVersionUID = 784637806667910831L;
  
  private JLabel[] sections;

  /**
   * Constructs a <code>StatusBar</code> object with
   * a single section.
   *
   * @param   value the value to display in the section
   */
  public StatusBar(String value)
  {
    this(new String[] {value});
  }

  /**
   * Constructs a <code>StatusBar</code> object with
   * one or more sections.
   *
   * @param   values the values to display in the sections
   */
  public StatusBar(String[] values)
  {
    setBorder(BorderFactory.createEtchedBorder());

    setLayout(new GridLayout(1,values.length));

    sections = new JLabel[values.length];

    for (int i = 0; i < values.length; i++)
    {
      sections[i] = new JLabel(values[i]);
      sections[i].setBorder(BorderFactory.createLoweredBevelBorder());
      add(sections[i]);
    }
  }

  /**
   * Retrieves the value associated with the
   * specified position (section).
   *
   * @param   pos a zero-based section position
   * @return  the value associated with the specified
   *          position
   */
  public String getStatusSection(int pos)
  {
    return
      pos >= 0 && pos < sections.length
        ? sections[pos].getText()
          : "";
  }

  /**
   * Sets the value associated with the
   * specified position (section).
   *
   * @param   pos a zero-based section position
   * @param   value the value associated with the
   *          specified position
   */
  public void setStatusSection(int pos, String value)
  {
    if (pos >= 0 && pos < sections.length)
    {
      sections[pos].setText(value);
    }
  }
}