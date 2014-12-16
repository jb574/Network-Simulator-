package netsim.GUI;
import  java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.Icon;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
/**
 * class to repesent the transferable
 * image data
 * @author  Jack Davey
 * @version  6th March 2014
 */
public class imageTransferable implements Transferable
{
    private Icon currentIcon;

    imageTransferable(Icon currentIcon)
    {
        this.currentIcon = currentIcon;
    }

    /**
     * method to get the dataflavour for this image
     * @return the standard dataflavour for images
     */
    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    /**
     * this methdo takes in a dataflavour and
     * checks to see if it is supported
     * @param flavor the flavour to check
     * @return  a yes or no answer
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
       return (isDataFlavorSupported(flavor));
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        return currentIcon;
    }


}
