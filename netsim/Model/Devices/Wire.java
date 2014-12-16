/**
 * NetSim project
 *//**
 * NetSim project
 *//**
 * NetSim project
 *//**
 * NetSim project
 */

package netsim.Model.Devices;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import netsim.GUI.Point;
import netsim.Simulation.Agent;
import netsim.View.wirePos;

/**
 * Class to represent a wire on the network
 * 
 * @author: Jack Davey
 * @version: 25/11/2013
 */
public class Wire extends Agent
{
    private ArrayList<Device> devices;
    private Vector<Signal> signals;
    private final int sectionCount;



    /**
     * constructor for this class
     * all it does is create the list that we
     * are using
     */
    public Wire() {
        super();
        
        sectionCount = 20;
        
        devices = new ArrayList<Device>();

        this.setView(new netsim.View.Wire(this.getUID()));
        signals = new Vector<Signal>();

    }
    
    public Vector<Signal> getSignals() {
        return signals;
    }
    
    public int getSectionCount() {
        return sectionCount;
    }


    /**
     * method to set the wire back to its
     * original state
     */
    @Override
    public void reset()
    {
        signals.clear();
    }

    /**
     * Called when we should do something
     */
    protected void onTick()
    {
    }

    /**
     * method to check whether a specific index is ocupied
     * @param index the index to check
     */
     private boolean isOccupied(int index)
     {
        for(Signal signal :signals)
        {
            int sindex = signal.getIndex();
            if (sindex == index) return true;
        }
        return false;
     }

    /**
     * method to add a signal to  the
     * central list for this wire for testing
     * @param sig the signal to add
     */
     public void addSignalForTesting(Signal sig)
     {
         signals.add(sig);
     }


    /**
     * method to add the signal to the main wire
     * for testing
     *
     * @param index the place to put it
     */
    public  void addSignal(int index)
    {
        // Slr25 - this should change.. why is it making a new PC?
         Signal aSig = new Signal(this, new Computer("test",true),null,"move");
        aSig.setIndex(index);
        signals.add(aSig);
    }


    /**
     * method to get the sixe of the signals
     * within wire vector for testing
     * @return the size mentioned above
     */
    public int getSizeOfSignalsWithinWire()
    {
        return signals.size();
    }

    /**
     * Connect a device to this wire
     */
    public void connect(Device d) {
        devices.add(d);
    }

    /**
     * method to remove a device
     * @param dev the device to remove
     */
    public void disconnect(Device dev)
    {
        devices.remove(dev);
    }

     @Override
     public void kill()
     {
         super.kill();
         _view = null;

     }


    /**
     * Returns all devices attached to this wire
     */
    public ArrayList<Device> getDevices() {
        return this.devices;
    }

    /**
     * method to check hether this wiere is clear or not
     * @return a true or false value
     */
    public boolean isChannelClear()
    {
      return  signals.isEmpty();
    }

    public void transmitMessage(Signal sig)
    {

        System.out.println("adding signal" + sig.getUID());
       signals.add(sig);
    }

    /**
     * cleanup method to be called when it is clear
     * that this signal sis no longer in the wire
     * @param sig signal to clean up from
     */
    public void removeOldSignal(Signal sig)
    {
      signals.remove(sig);

    } 
    
    /**
     * method to check whether a collision has occoured
     * @param pos the position of the signal
     * @param sig the  callee of this method
     * @return  boolean explaingn the outcome
     */
    public boolean checkForCollision(int pos, Signal sig)
    {
        for(Signal otherSig : signals)
        {

            if(!otherSig.equals(sig))
            {
                System.out.println("our sig id is" + sig.getIndex());
                System.out.println("the other id is" + otherSig.getIndex());
                if(otherSig.getIndex() == sig.getIndex())
                {
                    System.out.println("hit");
                    return true;
                }
            }
        }
        return  false;
    }



    /**
     * method to check whether
     * the supplied int index
     * is within the wire
     * @param section the section to look at
     * @return  a truth value
     */
    private boolean isWithinBounds(int section)
    {
        return  (section > -1 || section < sectionCount);
    }


    /**
     * method to compute the next index in the wire
     * if all indexes have been rues up then we return -1
     * @param isForward the direction of the message
     * @param currentIndex the current ppsition in the array
     * @return the new index to use
     */
    private int computeNextSection(boolean isForward, int currentIndex)
    {
        if(isForward)
        {
            currentIndex = currentIndex + 1;
        }
        else
        {
            currentIndex = currentIndex -1;
        }
        return  currentIndex;
    }
}






















