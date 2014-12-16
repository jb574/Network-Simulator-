package netsim.Simulation;

import java.util.HashMap;
import java.util.Map;

/**
 * static class that deals with the
 * back end functionlaity for supporting diffent
 * types of signals
 * @author Jack Davey
 * @version 5th february 2014
 */
public class SignalTypes
{

    private static SignalTypes setttingsStorer;

    /**
     * method to obtain the instance of this class
     * @return the signaltypes object for this object
     */
    public static SignalTypes obtain()
    {
        if(setttingsStorer == null)
        {
            setttingsStorer = new SignalTypes();
            return setttingsStorer;
        }
        else
        {
            return setttingsStorer;
        }
    }


    private Map<String,String> files;

    private Map<String,Integer> sizes;

    private String currentType;

    private int currentSize;
    private int  protocolType;
     private Map<String, Integer> netProtocols;

    /**
     * method to retreive the current
     * size of the media going through the network
     * @return as mentioned above
     */
    public static int getCurrentSize()
    {
        return setttingsStorer.currentSize;
    }

    public static int getCurrentProtocl()
    {
        return setttingsStorer.protocolType;
    }


    /**
     * method to set the current protocl in use
     * by the application
     * @param protocol protocol the protocol chosen
     * @throws java.lang.IllegalArgumentException if the protocol does not exist
     */
    public static void setCurrentProtocol(String protocol)
    {
       if(setttingsStorer.netProtocols.containsKey(protocol))
       {
           setttingsStorer.protocolType = setttingsStorer.netProtocols.get(protocol);
       }
        else
       {
           throw new IllegalArgumentException("protocol" + protocol + "does not exist");
       }
    }




    /**
     * method to retirev ethe current image
     * assoicated with the cureent file type
     * @return as above
     */
    public  static String getCurrentImageFile()
    {
        return setttingsStorer.currentType;
    }


    /**
     * this code is called from the GUI
     * to set the  type fo files currently being
     * sent around the network
     * @param filetype, the type of medium that the
     *  user has selected.
     * @throws IllegalArgumentException if the filetype does not exist
     */
    public void setCurrentMedium(String choice)
    {
        if(!files.containsKey(choice) || !sizes.containsKey(choice))
        {
            throw new IllegalArgumentException("there is no such filetype");
        }
        else
        {
            currentSize = sizes.get(choice);
            currentType = files.get(choice);
        }
    }












    public SignalTypes()
    {
        files = new HashMap<>();
        files.put("page","/gfx/packet.gif");
        files.put("tune","/gfx/tune.jpeg");
        files.put("film","/gfx/film.jpeg");
        sizes = new HashMap<>();
        sizes.put("page",1);
        sizes.put("tune",2);
        sizes.put("film",3);
        currentSize = sizes.get("page");
        currentType = files.get("page");
        netProtocols = new HashMap<>();
        netProtocols.put("Go Back N",0);
        netProtocols.put("CSDMA",1);
        protocolType = 1;
    }


















}
