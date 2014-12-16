/**
 * NetSim project
 */

package netsim.GUI;

/**
 * class to represent a point
 * @author  Jack Davey
 * @version  5th December 2013
 */
public class Point {

    private int x;
    private int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
