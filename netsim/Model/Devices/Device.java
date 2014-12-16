/**
 * NetSim project
 */

package netsim.Model.Devices;

import java.util.LinkedList;

import netsim.Model.CommunicaionUnits.Communication;
import netsim.Simulation.Agent;

/**
 * This is the main interface that all devices on the network
 * implement
 * 
 * @author: Jack Davey
 * @author: Sarah Rose
 * @version: 08/12/2013
 */
public abstract class Device extends Agent
{
    // Name that identifies this device in the system
    protected String deviceName;

    // The wires we are connected to
    protected LinkedList<Wire> wires;

    //better impementaiotn of the wires we are connected
    // to in odder to keep that process seperate from computer code
    protected portBank ports;


    // variable to represent how many messages a computer can revive before its queu  is full.
    protected int maxQueueSize;

    /**
     * default constructor for this class
     *  @param name the name to assign
     */
    public Device(String name) {
        super();
        deviceName = name;
        wires = new LinkedList<>();
        maxQueueSize = 3;
    }


    /**
     * mthod to take back a coorrupted signal
     * @param badSignal  the bad signal to  take back
     */
    public abstract void takeCorruptedMessage(Signal badSignal);

    /**
     * method to set up resending of a method after
     * a collision
     * @param backoffTIme the time to back off for
     * @param Sig the botched signal
     */
    public abstract void backoff(int backoffTIme, Signal Sig);


    /**
     * methdo to add a communication to
     * the computer
     * @param  com the commuication to add
     */
    public   abstract  void addCommunication(Communication com);

    public abstract void addDamagedSignals(Communication com, Device target, int startNumber, int endNumbewr);

    /**
     * methdo to  add frames to this devices queue
     * @param   the target device to ad.
     */
    public  abstract void  addFramesToQueueu(Device target);


    /**
     * Returns our attached wire
     */
    public LinkedList<Wire> getWires() {
        return this.wires;
    }

    public abstract boolean canProcessSigNow();


    /**
     * Set the wire we are attached to
     */
    public void connectWire(Wire wire,boolean direction) {
        wire.connect(this);
        this.wires.add(wire);
        Port aport = new Port(wire,direction);
        ports.addPort(aport);
    }

    /**
     *  Is that object equal to this?
     * @param obj   the device to compare
     * @return  a boolean that ays whether these things are the same object
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj instanceof Agent == false) {
            return false;
        }
        return this.getUID() == ((Agent)obj).getUID();
    }

    public abstract void removeOldCommunication(Communication com);


    /**
     * method to find out the name of this device
     * @return the device name
     */
    public String getDeviceName() {
        return deviceName;
    }


    /**
     * this method sends a message to any of the decies connected to it
     * @param singal  signal to send
     * @param manager, the gui manager, so progress updates can be sent to the end user
     */
     public abstract  void sendMessage(Signal sig);

    /**
     * method for checking to see if the network is clear
     */
     public abstract  void  listen();

    /**
     * method that recieves a signal
     * @param sig  the signal to recieve
     */
     public abstract void  recieveMessage(Signal sig);


    /**
     * method to complete the process of sending a message
     */
    public abstract void completeProc();

}
