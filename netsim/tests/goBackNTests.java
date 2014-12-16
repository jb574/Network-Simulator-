package netsim.tests;

import netsim.GUI.GUIManager;
import netsim.Model.CommunicaionUnits.Communication;
import netsim.Model.Devices.Computer;
import netsim.Model.Devices.Signal;
import netsim.Model.Devices.Wire;
import netsim.Simulation.Scheduler;
import netsim.Simulation.SignalTypes;
import org.junit.*;

import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * Created by jackdavey on 01/03/2014.
 */
public class goBackNTests
{

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
    public static Scheduler init() throws UnsupportedFlavorException
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


    /**
     * method to check that on CSDMA
     * mode everything stll handles as it should
     */
     @Test
     public void checkCSDMAStillWorks() throws UnsupportedFlavorException
     {
         Computer comp = initWIreTests();
         SignalTypes sigOb = SignalTypes.obtain();
         SignalTypes.setCurrentProtocol("CSDMA");
         sig.setCorrupted(true);
         comp.recieveMessage(sig);
         comp.tick();
         assert(comp.getActionToTake().equals("send"));
     }

    /**
     * method to make sure go back n works as planned
     */
    @Test
    public void testGoBackN() throws UnsupportedFlavorException
    {
        SignalTypes sigOb = SignalTypes.obtain();
        SignalTypes.setCurrentProtocol("Go Back N");
        Computer comp = initWIreTests();
        comp.addDamagedSignals(new Communication(3),comp,0,2);
        System.out.println(comp.getNumberOfFramesInProcess());
        assert(comp.getNumberOfFramesInProcess() == 3);
    }




    /**
     * method to est that eh csdma procol is slected ok
     */
    @Test
    public void checkCSDMASelection()
    {
        SignalTypes sigOb = SignalTypes.obtain();
        SignalTypes.setCurrentProtocol("CSDMA");
        assert(SignalTypes.getCurrentProtocl() == 1);
    }


    /**
     * method to est that eh csdma procol is slected ok
     */
    @Test
    public void checkCGoBackNelection()
    {
        SignalTypes sigOb = SignalTypes.obtain();
        SignalTypes.setCurrentProtocol("Go Back N");
        assert(SignalTypes.getCurrentProtocl() == 0);
    }

    /**
     * method to check that when an
     * invalid procol is sent the
     * error is handled accoringly
     */
    @Test
    public void checkErrorHandlingMechanism()
    {
         boolean correctResposeGiven = false;
        SignalTypes sigOb = SignalTypes.obtain();
        try
        {
            SignalTypes.setCurrentProtocol("aaaaaaaa");
        }
        catch (IllegalArgumentException error)
        {
            correctResposeGiven = true;
        }
        assert(correctResposeGiven);
    }


}
