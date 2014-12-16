package netsim.tests;
import netsim.GUI.GUIManager;
import netsim.Model.Devices.Computer;
import netsim.Model.Devices.Signal;
import netsim.Model.Devices.Wire;
import netsim.Simulation.Scheduler;
import org.junit.*;

import java.awt.datatransfer.UnsupportedFlavorException;


/**
 * class to test out the framing and signal corruption features
 * @author Jack Davey
 * @version 10th January 2014
 */
public class framingTests
{
    private Wire testWire;
    private Signal sig;
    private Signal sig2;

    /**
     * method to check that the appropriate nmber
     * of frames are being sent down the network
     *
     */
    @Test
    public void testFrames() throws  UnsupportedFlavorException
    {
        Computer comp = initWIreTests();
        Scheduler.setNumberOfFrames(3);
        assert(Scheduler.getNumberOfFramesToSend() == 3);
    }


    /**
     * method to test that when a computer recieves a corrupted
     * signal it is added to the sending queue
     *
     */
    @Test
    public void corruptTest() throws UnsupportedFlavorException
    {
        Computer comp = initWIreTests();
        sig.setCorrupted(true);
        assert(sig.isCorrupted() == true);
        comp.takeCorruptedMessage(sig);
        assert(comp.getNumberOfFramesInProcess() == 1);
    }






    public Computer initWIreTests()  throws UnsupportedFlavorException
    {
        Computer comp = initComputer();

        sig =  new Signal(testWire, comp,comp,"move");
        sig2 = new Signal(testWire, comp,comp,"move");
        return  comp;
    }

    /**
     * method to set up for tests that involve a single
     * computer
     * @return the computer in question
     */
    public Computer initComputer() throws UnsupportedFlavorException
    {
        init();

        Computer comp =  new Computer("test",true);
        Scheduler.obtain().addAgent(comp);
        return comp;
    }

    /**
     * method to init the application so the testing runs ok
     * @return the setup schedular
     */
    public static Scheduler init()  throws UnsupportedFlavorException
    {
        // Create a new simulator
        final Scheduler sim = Scheduler.obtainForTesting();
        sim.setTickRate(10);
        // Create GUI
        final GUIManager gui = GUIManager.obtainTesting();
        return sim;
    }


}
