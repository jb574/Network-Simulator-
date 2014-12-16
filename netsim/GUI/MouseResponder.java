/**
 * NetSim project
 */

package netsim.GUI;

import netsim.Model.Devices.Computer;
import netsim.Model.Devices.Wire;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

/**
 * thsi calss controls mouse events
 * for the app
 * @author Jack Davey
 * @version  3rd December
 */
public class MouseResponder   implements MouseMotionListener , MouseListener
{

    private Window window;

    public  MouseResponder(Window window)
    {
        this.window = window;
    }

    /**
     * this is the main mouse event we need
     * it is acivated when a user clicks on any computer
     * and starts the interaction process
     * @param e the mouse event to respond to
     */
    public  void  mouseClicked(MouseEvent e)
    {
        window.handleUserInput(e.getX(),e.getY());
    }


    // the following  methods dont actaully do anything, we just
    // need them so that the mouse pressed code works effectively


    public void mouseExited(MouseEvent e)
    {

    }

    public void mouseMoved(MouseEvent e)
    {

    }

    /**
     * method to check whether a wire
     * can be placed
     *@return a yes or no answer to the queston
     */
    private boolean wireCanBeCreated()
    {
        return ((start != null && window.isPointInAComputer(start.getX(),
                start.getY())) && (end !=  null && window.isPointInAComputer(end.getX()
        ,end.getY())));
    }



    /**
     * methot that bangs into action after the
     * user has finished dragging the mouse
     * @param e the mouse event in question
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(wireCanBeCreated())
        {
            System.out.println("lets rock");
            Wire newWire = new Wire();
            netsim.View.Wire wireView  = (netsim.View.Wire) newWire.getView();
            Computer startComp = window.retreiveDeviceAtPoint(start.getX(),start.getY());
            startComp.connectWire(newWire,true);
            Computer endcomp = window.retreiveDeviceAtPoint(end.getX(),end.getY());
            endcomp.connectWire(newWire,false);
            wireView.setPos(start.getX(),start.getY(),end.getX(),end.getY());
        }
        window.setLatestWire(null);
        start = null;
        end = null;
    }



    /**
     * method to deal with what happens when
     * the mouse is draged
     * @param e the mouse event to respond to
     */
    public void mouseDragged(MouseEvent e)
    {
        end = new Point(e.getX(),e.getY());
        Line2D.Double nextWire = new Line2D.Double(start.getX(),start.getY(),end.getX(),end.getY());
        window.setLatestWire(nextWire);
    }



    public void mouseEntered(MouseEvent e)
    {

    }


    public  void  mousePressed(MouseEvent e)
    {
        start = new Point(e.getX(),e.getY());

    }

    Point start;
    Point end;

}
