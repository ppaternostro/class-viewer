# class-viewer
A graphical class viewer utility.

The JDK is freely downloadable from Oracle and contains all the necessary tools to compile, run and debug Java code. The one downside is that the free tools are command line driven. Some of the tools could benefit greatly from a graphical user interface. In particular the JDK's **javap** utility is one such tool. This utility mimics javap's ability to list a class's methods and attributes while presenting the results via a user friendly interface.

Upon application startup you will be presented with the following window:

![Main window](https://user-images.githubusercontent.com/32653184/31637310-244168a4-b29c-11e7-9eb3-334ccf381432.png)

By default, the cursor is placed in the edit box. Enter a fully qualified Java class name that is located on the classpath and press the **Enter** key to view information about the class. For example, the windows below show the before and after views of the user entering **java.lang.String** as the class name.

![Left view](https://user-images.githubusercontent.com/32653184/31637489-f71ec398-b29c-11e7-9ca5-d8bc112aae82.png) ![Right view](https://user-images.githubusercontent.com/32653184/31637598-697a784c-b29d-11e7-8c9f-43b66d06953f.png)

Note, the window on the right lists the entered Java class name in the drop down listbox located directly beneath the edit box. This facilitates selection of any previously entered class names without having to retype the class name in the edit box. The output is rendered as a tree view with the class's constructors, methods and attributes rendered as leaf nodes under the class definition root node. Class names not found in the classpath will display an error message. Class files not on the classpath may be selected via the **File->Open...** menu item with the output rendered as previously described.
