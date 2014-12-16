package netsim.tests;
import netsim.GUI.GUIManager;
import netsim.Main;
import netsim.Model.Devices.Computer;
import netsim.Simulation.Scheduler;
import netsim.Simulation.Agent;
import  org.junit.*;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Collection;

/**
 * class to do better unit testing the stopping funcionality
 * @author  Jack Davey
 * @version 7th January 2013
 */
public class stopTests
{

    /**
     * method to ensure that the
     * system stops when the stop button is pressed
     */
     @Test
    public  void stopTest() throws UnsupportedFlavorException
     {
         Main.main(null);
         Scheduler sched =  Scheduler.obtain();
          Collection<Agent> agents = sched.getAgents();
         for(Agent anAagent : agents )
         {
             if(anAagent instanceof Computer)
             {
                 Computer comp = (Computer)anAagent;
                 comp.onClick();
             }
         }
         GUIManager.StopGraphics();
         assert(GUIManager.getOpsrunning() == 0);
     }

}
