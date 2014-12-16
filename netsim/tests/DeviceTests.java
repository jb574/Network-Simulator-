package netsim.tests;

import netsim.GUI.GUIManager;
import netsim.Main;
import netsim.Model.Devices.Computer;
import netsim.Model.Devices.Signal;
import netsim.Model.Devices.Wire;
import netsim.Simulation.Scheduler;
import netsim.View.wirePos;
import org.junit.*;

import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *testing class to make sure that all the decieces have been tested thoruroyly
 * @author Jack Davey
 * @version 7th Jnauary 2013
 */
public class DeviceTests
{


    private Wire testWire;
    private Signal sig;
    private Signal sig2;
    private Scheduler sched;


    /**
     * test to check that the app behaves correctly when
     * a user clicks onto it for the first time.
     */
    @Test
    public  void  testAddingNewSignal() throws UnsupportedFlavorException
    {
        System.out.println("starting the test for adding a new signal");
        Computer comp = initWIreTests();
       Signal asig = new Signal(testWire, comp,comp,"listen");
        comp.addSig(asig);
        comp.processNextEvent();

        System.out.println(comp.getActionToTake());
        assert (comp.getActionToTake().equals("listen"));
    }


    /**
     * method to check that the excution order behaves
     * as planned, ie the most recent signal is operated on first
     *
     */
       @Test
      public  void testExecutionOrder() throws  UnsupportedFlavorException
      {
          System.out.println("starting the execution order test");
          Computer comp = initWIreTests();
          comp.addSig(new Signal(testWire, comp,null,"recieve"));
          comp.addSig(new Signal(testWire, comp,null,"move"));
          comp.processNextEvent();
          assert (comp.getActionToTake().equals("recieve"));
      }





    /**
     * method for testing the wire code
     */
    @Test
      public void wireMoveTests() throws  UnsupportedFlavorException
      {
          System.out.println("testing wire move tests");
          Computer comp = initWIreTests();
          sig.initView(comp,comp);
          sig2.initView(comp,comp);
          sig.setDirection(true);
          sig.setIndex(3);
          sig2.setDirection(false);
          sig2.setIndex(3);
          sig.tick();
          System.out.println(sig.getIndex());
          assert(sig.getIndex() == 4);
          sig2.tick();
          assert(sig2.getIndex() == 2);

      }

    /**
     * code for testing wire stopping
     */
    @Test
    public void testWireStopping()  throws  UnsupportedFlavorException
    {
        Computer comp = initWIreTests();
        System.out.println("testing wire stopping");

        sig.initView(comp, comp);
        sig2.initView(comp,comp);
        sig.setIndex(22);
        sig2.setIndex(0);
        sig.setAction("move");
        sig2.setAction("move");
        testWire.addSignalForTesting(sig);
        testWire.addSignalForTesting(sig2);
        sig.tick();
        sig2.tick();

        System.out.println(sig2.getView());
        assert(sig.getView() == null && sig2.getView() == null);
    }






    /**
     * method to check standard listening behaviour
     * that it iterates correctly
     */
    @Test
    public void checkListenLoop() throws  UnsupportedFlavorException
    {
        System.out.println("running the check listen loop test");
        Computer comp = initWIreTests();
        comp.addSig(new Signal(testWire, comp,null,"listen"));
        comp.processNextEvent();
        assert (comp.getActionToTake().equals("listen"));
        comp.setListenTime(71);
        TestUtils.tickNTimes(comp, 2);
        assert(comp.getNextAction().equals("send"));
        Signal Sig = new Signal(testWire, comp,comp,"listen");
        comp.setListenTime(0);
        comp.addSig(new Signal(testWire, comp, null,"listen"));
        testWire.transmitMessage(Sig);
        TestUtils.tickNTimes(testWire, 2);
        TestUtils.tickNTimes(comp, 3);
        assert(comp.getListenTime() == 0);
    }


    /**
     * method to init the application so the testing runs ok
     * @return the setup schedular
     */
    public  Scheduler init()  throws UnsupportedFlavorException
    {
         sched = Main.init();
        GUIManager man = GUIManager.obtain();
        return sched;
    }

    public Computer initWIreTests()  throws  UnsupportedFlavorException
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

        testWire = new Wire();
        return new Computer("test",true);
    }


}
