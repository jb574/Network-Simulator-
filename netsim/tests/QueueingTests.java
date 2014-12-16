package netsim.tests;
import netsim.GUI.GUIManager;
import netsim.Model.Devices.Computer;
import netsim.Model.Devices.Signal;
import netsim.Model.Devices.Wire;
import netsim.Simulation.Scheduler;
import org.junit.*;

import java.awt.datatransfer.UnsupportedFlavorException;


/**
 * class to  test the queuing features of the application
 * @author Jack Davey
 * @version 15th febuary 2014
 */
public class QueueingTests
{
    private Wire testWire;
    private Signal sig;
    private Signal sig2;


    /**
     * method that checks that signals
     * can be revieved when everthing is nrmal
     * @throws IllegalAccessException
     */
    @Test
    public void checkqueueRecieving() throws UnsupportedFlavorException  {
          Computer comp = initWIreTests();
          assert(comp.canProcessSigNow());
    }

    /**
     * method to check that
     * signals are rejected whn the queue is full
     *
     */
     @Test
    public void  checkAppropriateFullQueueBehaviour() throws  UnsupportedFlavorException
     {
         Computer comp = initWIreTests();
         for(int index = 0; index < 5; index++)
         {
             comp.addMessagesToReceiveQueue(new Signal());
         }
         assert(comp.canProcessSigNow() == false);
     }


    /**
     * method to check that
     * signals are rejected whn the queue is full
     *
     */
    @Test
    public void  checkSignalsBeingRemoved() throws  UnsupportedFlavorException
    {
        Computer comp = initWIreTests();
        for(int index = 0; index < 5; index++)
        {
            Signal sig = new Signal();
            sig.setAmountStillToProcess(30000);
            comp.addMessagesToReceiveQueue(sig);
        }
        comp.tick();
        assert(comp.canProcessSigNow());
    }






    /**
     * method to set up for tests that involve a single
     * computer
     * @return the computer in question
     */
    public Computer initComputer()  throws UnsupportedFlavorException
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
