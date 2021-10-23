package com.pasquasoft.tools.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class FileClassLoader extends ClassLoader
{
  private String path;

  private String derivePath(String name)
  {
    String start = null;
    String root = null;
    int index = -1;

    /* Determine if we have a fully-qualified class name */
    index = name.indexOf(".");

    if (index != -1)
    {
      /* Parse the start of the package name */
      start = name.substring(0, index);

      /* Locate the parsed string in the path and derive the root name */
      if (path != null)
        root = path.substring(0, path.indexOf(start));
    }

    /* Build the dependent class's fully-qualified path name */
    return root != null ? root + name.replace('.', File.separator.charAt(0)) + ".class"
        : (path != null ? (path + File.separator + name + ".class") : name);
  }

  public Class<?> findClass(String name) throws ClassNotFoundException
  {
    return loadClassBytes(new File(derivePath(name)));
  }

  public Class<?> loadClassBytes(File f) throws ClassNotFoundException
  {
    FileInputStream fis = null;
    byte b[] = null;

    try
    {
      fis = new FileInputStream(f);
      b = new byte[(int) f.length()];

      fis.read(b, 0, b.length);
    }
    catch (IOException ioe)
    {
      throw new ClassNotFoundException(ioe.getMessage());
    }
    finally
    {
      try
      {
        if (fis != null)
          fis.close();
      }
      catch (IOException ioe)
      {
        // no op
      }
    }

    /* Save the file's path name */
    path = f.getParent();

    return defineClass(null, b, 0, b.length);
  }
}
