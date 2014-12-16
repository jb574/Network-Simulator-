package netsim.View;

import netsim.GUI.View;
import netsim.GUI.Window;
import netsim.Simulation.SignalTypes;

/**
 * Created by jackdavey on 16/12/2013.
 */
public class SignalView extends View
{

    private int x, y;
    private Integer UID;
    private final int width = 50;
    private final int height = 50;
    private final static  String packet = "/gfx/packet.gif";
    private final static  String ack = "/gfx/packet-ack.gif";
    private String img;

    public SignalView(Integer UID)
    {
        setUp(UID);
    }

    /**
     * sets up the basics for this signal view
     * @param UID  the id to use
     */
    private void setUp(Integer UID) {
        this.UID = UID;
        img = SignalTypes.getCurrentImageFile();
    }
    
    public void update(float deltaX, float deltaY) {
        this.setPos(Math.round((float)x + deltaX), Math.round((float)y + deltaY));
    }

    /**
     * method for displaying
     * this signal on the screen
     * @param window the window to display on
     */
    @Override
    public void draw(Window window) {
        window.drawSignal(img,UID,x,y,width,height);
    }

    /**
     * called when we need to display a collison
     */
    public void displayCollision()
    {
        this.img = "/gfx/bang.jpg";
    }

    /**
     *  main constructor for this class
     * @param UID  the unique identifier
     * @param x  the x pos for this signal
     * @param y the y pos for this signal
     */
    public SignalView(Integer UID, int x, int y)
    {
        setUp(UID);
        setPos(x, y);

    }

    /**
     * Sets the position of this Signal
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the position of this Signal
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the position of this Signal
     */
    public int getY() {
        return this.y;
    }



}
