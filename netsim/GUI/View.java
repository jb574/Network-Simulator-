/**
 * NetSim project
 */

package netsim.GUI;

/**
 * Class to represent a wire's view
 * 
 * @author: Sarah Rose
 */
public abstract class View
{
    private GUIManager gui;
    /**
     * Constructor
     */
    public View() {
        super();
    }


    /**
     * method to reset defaults
     */
     public  void resetDefaults()
     {

     }


    /**
     * Called when we should do something
     */
    public void onTick() {
    }

    /**
     * Called when we draw
     */
    public abstract void draw(Window window);
}
