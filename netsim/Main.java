/**
 * NetSim project
 *
 * @copyright Sarah Rose
 */

package netsim;

import netsim.Simulation.Scheduler;
import netsim.Model.Devices.Wire;
import netsim.Model.Devices.Computer;
import netsim.GUI.GUIManager;
import netsim.Simulation.SignalTypes;

import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * Our main class
 */
public class Main
{
	/**
	 * Initialises the application
	 */
	public static void main(String [] args) throws  UnsupportedFlavorException{
        final Scheduler sim = init();
        SignalTypes settings = SignalTypes.obtain();
        // Add a wire to our simulation
		Wire wire = new Wire();
		((netsim.View.Wire)wire.getView()).setPos(100, 150, 100, 300);
		Wire wire2 = new Wire();
		((netsim.View.Wire)wire2.getView()).setPos(100, 150, 360, 200);
		Wire wire3 = new Wire();
		((netsim.View.Wire)wire3.getView()).setPos(100, 300, 360, 200);

		// Add a computer to our simulation
                Computer pc1 = addComputer("PCLAB1",50,50,100,100,true);
                pc1.connectWire(wire,true);
                pc1.connectWire(wire2,true);
                Computer pc2 = addComputer("PCLAB2",50,300,100,100,false);
                pc2.connectWire(wire,false);
                pc2.connectWire(wire3,true);
                Computer pc3 = addComputer("PCLAB3",350,150,100,100,false);
                pc3.connectWire(wire2,false);
                pc3.connectWire(wire3,false);
        
		// Start the simulation thread
		sim.start();
        GUIManager.addText("starting Network Simulator");
		// Add a shutdown hook so we shut down okay
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.flush();
				sim.interrupt();
			}
		}));
	}

    /**
     * method to init the application so the testing runs ok
     * @return the setup schedular
     */
    public static Scheduler init() throws UnsupportedFlavorException
    {
        // Create a new simulator
        final Scheduler sim = Scheduler.obtain();
        sim.setTickRate(10);
        // Create GUI
        final GUIManager gui = GUIManager.obtain();

        return sim;
    }

    public static Computer addComputer(String name,int x, int y,
                                    int height, int width,boolean isForward)
    {
        Computer comp = new Computer(name, isForward);
        netsim.View.Computer view = (netsim.View.Computer)comp.getView();
        view.setPos(x, y);
        view.setWidth(width);
        view.setHeight(height);
        return comp;

    }
}