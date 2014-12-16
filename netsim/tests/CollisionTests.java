package netsim.tests;
import netsim.GUI.GUIManager;
import netsim.Model.Devices.Computer;
import netsim.Model.Devices.Signal;
import netsim.Model.Devices.Wire;
import netsim.Simulation.Scheduler;
import org.junit.*;

import java.awt.datatransfer.UnsupportedFlavorException;


/**
 * tests to ensure the collision detection part of the simulator
 * works as planned
 * @version 8th Janaury 2014
 * @author Jack Davey
 */
public class CollisionTests
{

    private Wire testWire;
    private Signal sig;
    private Signal sig2;


    /**
     * method to ensure that this test detects
     * all collisions
     */
    @Test
    public void detectPositiveCollision()  throws  UnsupportedFlavorException
    {
        initWIreTests();
        sig.setIndex(16);
        sig2.setIndex(16);
        testWire.transmitMessage(sig);
        testWire.transmitMessage(sig2);
        assert(sig.checkCollision() != null);
    }





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

    public Computer initWIreTests() throws UnsupportedFlavorException
    {
        Computer comp = initComputer();
        testWire = new Wire();
        sig =  new Signal(testWire, comp,comp,"move");
        sig2 = new Signal(testWire, comp,comp,"move");
        return  comp;
    }







}
