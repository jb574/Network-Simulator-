/**
 * NetSim project
 */

package netsim.View;

import netsim.GUI.View;
import netsim.GUI.Window;

/**
 * Class to represent a wire's view
 * 
 * @author: Sarah Rose
 */
public class Wire extends View
{
    private Integer muid;
    private int x, y, x2, y2;

    /**
     * Constructor
     */
    public Wire(Integer muid) {
        this.muid = muid;
        this.x = 0;
        this.x2 = 0;
        this.y = 0;
        this.y2 = 0;
    }

    /**
     * Called when we draw
     */
    public void draw(Window window) {
        window.drawWire(this.muid, this.x, this.y, this.x2, this.y2);
    }

    /**
     * Sets the position of this wire
     */
    public void setPos(int x, int y, int x2, int y2) {
        this.x = x;
        this.x2 = x2;
        this.y = y;
        this.y2 = y2;
    }

    /**
     * Gets the position of this wire
     */
    public int getStartX() {
        return this.x;
    }

    /**
     * Gets the position of this wire
     */
    public int getStartY() {
        return this.y;
    }

    /**
     * Gets the position of this wire
     */
    public int getEndX() {
        return this.x2;
    }

    /**
     * Gets the position of this wire
     */
    public int getEndY() {
        return this.y2;
    }
}
