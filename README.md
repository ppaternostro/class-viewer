# class-viewer
A graphical class viewer utility.

The JDK is freely downloadable from Oracle and contains all the necessary tools to compile, run and debug Java code. The one downside is that the free tools are command line driven. Some of the tools could benefit greatly from a graphical user interface. In particular the JDK's **javap** utility is one such tool. This utility mimics javap's ability to list a class's methods and attributes while presenting the results via a user friendly interface.

Upon application startup you will be presented with the following window:

![image](https://user-images.githubusercontent.com/32653184/198867069-88489528-a1bb-4075-a5cb-1102e69d6dfd.png)

By default, the cursor is placed in the edit box. Enter a fully qualified Java class name that is located on the classpath and press the **Enter** key to view information about the class. For example, the windows below show the before and after views of the user entering **java.lang.String** as the class name.

![image](https://user-images.githubusercontent.com/32653184/198866658-cae3211e-870f-4d69-a1ff-7bdba91b0206.png) ![image](https://user-images.githubusercontent.com/32653184/198866553-33bc9cf7-d788-48c1-b80c-9b118ec1ec70.png)

Note, the window on the right lists the entered Java class name in the drop down listbox located directly beneath the edit box. This facilitates selection of any previously entered class name without having to retype the same class name in the edit box. The output is rendered as a tree view with the class's constructors, methods and attributes rendered as leaf nodes under the class definition root node. Class names not found on the classpath will display an error message. Class files (files ending in a **.class** extension) not on the classpath may be selected via the **File->Open...** menu item with the output rendered as previously described.
