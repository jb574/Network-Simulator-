package netsim.GUI;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * the transfer handler that we use for the ntwork siulator
 * @author Jack Davey
 * @version 7th March 2014
 */
public class NetTransferHandler  extends TransferHandler
{
    /**
     * mehtod to check to see if we can import this particualr data handler
     * @param support the  support handler to chekc
     */
    @Override
    public  boolean canImport(TransferSupport support)
    {
        if(support.isDrop())
        {
            return  false;
        }
        return (support.isDataFlavorSupported(DataFlavor.imageFlavor));
    }


    /**
     * method for importing the data
     */
    @Override
    public boolean importData(TransferSupport support)
    {
        if(!canImport(support))
        {
            return  false;
        }
        Transferable trans = support.getTransferable();
        Icon currentIcon;
        try
        {
            currentIcon = (Icon) trans.getTransferData(DataFlavor.imageFlavor);

             return  true;
        }
        catch (UnsupportedFlavorException  | IOException error)
        {
            System.out.println("something fucked up");
            error.printStackTrace();
            return  false;
        }

    }



}
