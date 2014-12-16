package netsim.Utils;

/**
 * class that contain utilities to help with debugging
 * now that all print statements are going to the scren
 * @author Jack Davey
 * @version 29th January 2014
 */
public class DebugUtils
{

    /**
     * method to desplay a debug message
     * on scren
     * @param obj the message t display
     */
     public static void displayDebugMessage(Object obj)
     {
         if(inDebugMode)
         {
             if(obj instanceof  String)
             {
                 System.out.println(obj);
             }
             else
             {
                 System.out.println(obj.toString());
             }
         }
     }




    /**
     * this class is not meant ot be instantiated,
     * as such it has a private constructor
     */
    private DebugUtils() {}

    /**
     * the debug mode variable needs to be turned to
     * false whenever we release the app to the customer.
     */
    private static boolean inDebugMode = true;



}
