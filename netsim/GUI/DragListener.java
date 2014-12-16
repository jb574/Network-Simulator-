package net;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.io.IOException;
import  javax.swing.JLabel;
import javax.swing.Icon;

/**
 * class to support the dragging and ropping of network compeonents
 * @author Jack Davey
 * @version 6th March 2014
 */
public class DragListener implements DragGestureListener
{
    /**
     * method to be called when we recogise
     * that a drag gesture has been recognised
     * @param event the recognised event
     */
    @Override
    public void dragGestureRecognized(DragGestureEvent event)
    {
        // the first job would be to get the selected
        // icon
        JLabel selectedLabel = (JLabel) event.getComponent();
        final Icon currentIcon  = selectedLabel.getIcon();
        Transferable dataDevice = new imageTransferable(currentIcon);
        event.startDrag(null,dataDevice);
    }


}
