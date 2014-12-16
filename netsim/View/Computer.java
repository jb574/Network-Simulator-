/**
 * NetSim project
 */

package netsim.View;

import netsim.GUI.View;
import netsim.GUI.Window;

/**
 * Class to represent a Computer's view
 * 
 * @author: Sarah Rose
 */
public class Computer extends View
{
    private Integer uid;
    private int x, y, width, height;

    private String current;

    private final static String computer = "/gfx/pc.gif";
    private final static  String Done = "/gfx/pc-green.gif";
    final static  String waitRed = "/gfx/pc-red.gif";
    final static  String waitYellow = "/gfx/pc-yellow.gif";
    private boolean flasher = true;





    /**
     * Constructor
     */
    public Computer(Integer uid) {
        this.uid = uid;
        this.setPos(0,0);
        this.width = 0;
        this.height = 0;
        current = computer;

    }


      // methods to change the colour of the computer
      public void setBlue()
      {
          current = computer;

      }


    public void setRed()
    {
        current = waitRed;
    }


    public void setGreen()
    {
        current = Done;
    }


    @Override
    public  void resetDefaults()
    {
           setBlue();
    }


    public void flash()
    {
        if(flasher)
        {
            current = waitRed;
            flasher = false;
        }
        else
        {
            current =waitYellow;
            flasher =true;
        }
    }



    /**
     * Called when we draw
     */
    public void draw(Window window) {
        window.drawComputer(current,this.uid, this.x, this.y, this.width, this.height);
    }

    /**
     * Sets the position of this Computer
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get x
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets the width of this Computer
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the height of this Computer
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
