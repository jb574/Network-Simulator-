package netsim.GUI;


import javax.swing.*;
import javax.swing.text.Document;
import java.io.OutputStream;
import javax.swing.text.Document;
import  java.io.*;

/**
 * this class is responsible for
 * taking all messages to standard output and
 * displaying them as part of the gui
 * @author Jack Davey
 * @version 3oth January 2014
 */
public class MessageDisplay  extends OutputStream
{

    private JTextArea display;

    private static MessageDisplay machine;

    /**
     * method to return the objec tof this class
     * for this program
     * @param this object stored, if null a
     *  nullpointer excetpion will be thrown
     */
    public static  MessageDisplay obtain()
    {
        if(machine == null)
        {
            throw new NullPointerException(" this objct is null");
        }
        return machine;
    }

    /**
     * method that we call to set everhytning up for the first tme
     * @param display the  text field to pass in
     * @return  the message display Object
     * @throws IllegalAccessException if we hit a problem with this work
     */
      public static MessageDisplay init(JTextArea display) throws IllegalAccessException
      {
          if(machine != null)
          {
              throw  new IllegalAccessException("this methdo shuodl not be accessed");
          }

          machine = new MessageDisplay(display);
          return  machine;
      }

    /**
     * methdo to add text to the console
     * @param text the text to be added
     */
    public void updateConsole(String Text)
    {
       display.append(Text);
    }


    /**
     * method to write a single int to the stream
     * £param number the int to write
     */
    @Override
    public void write(int i)
    {
      updateConsole(String.valueOf(i));
    }










    /**
     * constructor for this clas
     * it sets up the initial display to recieve output
     * mesages, which should already have been
     * set up and placed on the gui
     * @param display the jtextpane to display messages to
     * @Throws an error if the argument is nul
     */
    private MessageDisplay(JTextArea display)
    {
        if(display == null)
        {
            throw  new NullPointerException("the text pane to diplay to is null");
        }
        this.display = display;
    }


}















