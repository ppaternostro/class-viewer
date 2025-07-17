# class-viewer
A graphical class viewer utility.

The JDK is freely downloadable from Oracle and contains all the necessary tools to compile, run and debug Java code. The one downside is that the free tools are command line driven. Some of the tools could benefit greatly from a graphical user interface. In particular the JDK's **javap** utility is one such tool. This utility mimics javap's ability to list a class's methods and attributes while presenting the results via a user friendly interface.

Upon application startup you will be presented with the following window:

![Main Window](https://user-images.githubusercontent.com/32653184/198867069-88489528-a1bb-4075-a5cb-1102e69d6dfd.png)

By default, the cursor is placed in the edit box. Enter a fully qualified Java class name that is located on the classpath and press the **Enter** key to view information about the class. For example, the windows below show the before and after views of the user entering **java.lang.String** as the class name.

![Before Image](https://user-images.githubusercontent.com/32653184/198866658-cae3211e-870f-4d69-a1ff-7bdba91b0206.png)
![After Image](https://user-images.githubusercontent.com/32653184/198866553-33bc9cf7-d788-48c1-b80c-9b118ec1ec70.png)

Note, the window on the bottom lists the entered Java class name in the drop down listbox located directly beneath the edit box. This facilitates selection of any previously entered class name without having to retype the same class name in the edit box. The output is rendered as a tree view with the class's constructors, methods and attributes rendered as leaf nodes under the class definition root node. Class names not found on the classpath will display an error message. Class files (files ending in a **.class** extension) not on the classpath may be selected via the **File->Open...** menu item with the output rendered as previously described.

## Update
The [Byte Buddy](https://bytebuddy.net/#/) project was added as a dependency to this project in order to create  _placeholder_  classes that are not found on the classpath or derivable from the custom file class loader when using the utility's **File->Open...** feature. This allows the utility to display the tree output without causing an error. When the output displays a non-fully qualified class name that is a signal that **Byte Buddy** was used to create and load a  _placeholder_  class. The project has also been  _Mavenized_  and as such can be built via the following executed from a terminal window

```bash
mvnw package
```

or

```bash
./mvnw package
```

on MacOS/Linux environments.

The command will create the following 2 JAR files in the project's root folder **target** directory.

- class-viewer-0.0.1-SNAPSHOT.jar
- class-viewer-0.0.1-SNAPSHOT-jar-with-dependencies.jar

The **class-viewer-0.0.1-SNAPSHOT-jar-with-dependencies.jar** file is an executable JAR that includes the **Byte Buddy** dependency in the JAR. Run the following from the terminal window to execute the application

```bash
java -jar class-viewer-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
