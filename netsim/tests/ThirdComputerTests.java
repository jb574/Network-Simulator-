package netsim.tests;
import netsim.GUI.GUIManager;
import netsim.Model.Devices.Computer;
import netsim.Model.Devices.Signal;
import netsim.Model.Devices.Wire;
import netsim.Simulation.Scheduler;
import org.junit.*;

import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *  class for testing the third
 *  computer iteration
 *  @author Jack Davey
 *  @verion 6th febrary 2014
 */
public class ThirdComputerTests
{

    /**
     * method to check that adding multiple
     * computers to the network works ok
     */
    @Test
    public void addMultipleComputers()   throws  UnsupportedFlavorException
    {
        init();
        Computer comp1 = new Computer("test1",true);
        Computer comp2 = new Computer("test2",true);
        comp1.connectWire(new Wire(),true);
        comp1.connectWire(new Wire(),false);
        assert(comp1.getNumberOfWires() == 2);
    }


    /**
     * method to check that a
     * computer handles messages not
     * meant for it correctly
     */
    @Test
    public void checkIncorrectDestination()  throws UnsupportedFlavorException
    {
        init();
        Computer comp1 = new Computer("test1",true);
        Computer comp2 = new Computer("test2",true);
        Wire wire = new Wire();
        Signal asig = new Signal(wire,comp1,comp2,"move",comp2);
        wire.transmitMessage(asig);
        comp1.recieveMessage(asig);
        assert(asig.getAction().equals("bad"));
    }


    /**
     * method to check that when a signal
     * does get to its richt palce, it is accepted
     */
     @Test
     public void checkSignalAccepted()   throws UnsupportedFlavorException
     {
         init();
         Computer comp1 = new Computer("test1",true);
         Computer comp2 = new Computer("test2",true);
         Wire wire = new Wire();
         Signal asig = new Signal(wire,comp1,comp2,"move",comp1);
         wire.transmitMessage(asig);
         comp1.recieveMessage(asig);
         assert(asig.getAction().equals("recieve"));
     }


    private Wire testWire;
    private Signal sig;
    private Signal sig2;




    /**
     * method to set up for tests that involve a single
     * computer
     * @return the computer in question
     */
    public Computer initComputer()  throws  UnsupportedFlavorException
    {
        init();

        return new Computer("test",true);
    }

    /**
     * method to init the application so the testing runs ok
     * @return the setup schedular
     */
    public static Scheduler init() throws  UnsupportedFlavorException
    {
        // Create a new simulator
        final Scheduler sim = Scheduler.obtainForTesting();
        sim.setTickRate(10);
        // Create GUI
        final GUIManager gui = GUIManager.obtainTesting();
        return sim;
    }

    public Computer initWIreTests() throws  UnsupportedFlavorException
    {
        Computer comp = initComputer();
        testWire = new Wire();
        sig =  new Signal(testWire, comp,comp,"move");
        sig2 = new Signal(testWire, comp,comp,"move");
        return  comp;
    }



}
