package netsim.Model.Devices;

import netsim.View.wirePos;
import netsim.Simulation.Agent;
/**
 *this calss repreents a socket coming out of the computer for a wire
 * and allows us to have muliple wires from a single computer
 * @author Jack Davey
 * @version 22nd January 2014
 */
public class Port  extends Agent
{

    private boolean direction;

    private Wire connectedWire;

    /**
     * main constructor for this class
     * @param direction  the direction in which all outgoing signals should
     *                   travel down the wire
     * @param wire  the wire itself.
     */
    public Port(Wire wire, boolean direction)
    {
        this.direction = direction;
        connectedWire = wire;
    }

    public void onTick()
    {

    }

    /**
     * method to check whether this port is
     * the correct one for this wire
     * @param wire wire to check
     * @return  a boolean that is the outcome of this method
     */
    public boolean isCorrectPort(Wire wire)
    {
        return (connectedWire.equals(wire));
    }




    /**
     * method for checking whether the channel is clear
     * @return fairly self explanatory
     */
    public boolean isChannelClear()
    {
        System.out.println(connectedWire.getSignals().size());
        return  (connectedWire.isChannelClear());
    }

    /**
     * method to resend a message
     * down the wire
     * @param the signal to send
     */
    public void resendMessage(Signal sig)
    {
        sig.setDirection(direction);
        connectedWire.transmitMessage(sig);
    }


    /**
     * this sends a message down the wire
     * @param sig the signal to send
     */
   public  void sendMessage(Signal sig)
   {
       for(Device dev : connectedWire.getDevices())
       {
           if(!dev.equals(sig.getOrigin()))
           {
               Signal packet = new Signal(connectedWire,sig.getOrigin(),dev,"move",sig.getDestination());
               packet.setDirection(this.direction);
               System.out.println("original comm numbrr is " + sig.getComNumber());
               packet.setComNumber(sig.getComNumber());
               packet.setSequenceNumber(sig.getSequenceNumber());
               connectedWire.transmitMessage(packet);
               return;
           }
       }


   }

    /**
     * method to kill this port
     */
    @Override
    public void kill()
    {
        super.kill();
        connectedWire.kill();
    }














}
