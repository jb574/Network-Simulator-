package netsim.Model.Devices;

import netsim.GUI.GUIManager;
import netsim.GUI.Window;
import netsim.Simulation.Agent;

import java.util.*;

/**
 * class to represent all ports
 * that belong to a single computer
 * @author  Jack Davey
 * @version  22nd January 2014
 */
public class portBank  extends Agent
{


    private Set<Port> portSet;

    /**
     * ontick method for this class
     * not even sure whether this really needs
     * to be an agent as it will only ever be called by its computer
     */
       public void onTick()
       {

       }


    public Set<Port> getPortSet() {
        return portSet;
    }

    public void setPortSet(Set<Port> portSet) {
        this.portSet = portSet;
    }

    /**
     * method to check wither the channell is clear on all ports
     * @return  self explanatory
     */
    public boolean areAllChannelsClear()
    {

       for(Port currentPort : portSet)
       {
          if(!currentPort.isChannelClear())
          {
              return  false;
          }
       }
        return  true;
    }


    /**
     * main constructor for this class
     */
      public portBank()
      {
          portSet = new HashSet<Port>();
      }


    /**
     * method to send a message down one of the wires
     * @param sig the signal to send
     */
    public void sendMessage(Signal sig)
    {
        for(Port currentPoort : portSet)
        {
            currentPoort.sendMessage(sig);
            GUIManager.addText("sending message from pc "
                    + sig.getOrigin().getUID() + " to pc " + sig.getDestination().getUID()
             );
        }

    }

    /**
     * method to retreive the port associated with a certain wire
     * @param wire wire to look for
     * @return the port that contains that wire
     * @throws NullPointerException  if the wire is not within this portbank
     */
    public Port findCorrectPort(Wire wire)
    {
        for(Port currentPort : portSet)
        {
            if(currentPort.isCorrectPort(wire))
            {
                return currentPort;
            }
        }
        throw  new NullPointerException("error cannot find the right port" +
                ", this must have been sent to the wrong pc");
    }





    /**
     * method to add a port
     * @param port the port that we are adding
     */
    public void addPort(Port port)
    {
       portSet.add(port);
    }


    /**
     * method to kll this ortbank
     *
     */
    @Override
    public void kill()
    {
        super.kill();
        for(Port currentPort : portSet)
        {
            currentPort.kill();
        }
    }










}
