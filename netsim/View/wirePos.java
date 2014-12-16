package netsim.View;

import netsim.GUI.Point;

import java.util.ArrayList;

/**
 * method to represent
 * the wire positions.
 * there should be one of these for each
 * wire in the network
 * @author Jack Davey
 * @version 24th december 2013
 */
public class wirePos
{
    private ArrayList<Point> wirePoints;

    /**
     * constructor to set things up
     * @param x the x position
     * @param yStart the y start position
     */
    public wirePos(int x, int yStart)
    {
        wirePoints = new ArrayList<Point>(21);

        for(int index = 0; index < 21; index++)
        {
            Point aPoint = new Point(x,yStart);
            wirePoints.add(index,aPoint);
            yStart = yStart + 5;
        }

    }


    /**
     * retrieves the x and y coodinates
     * for the signal
     * @param pos the position to get
     * @return the coodinates in question
     */
       public Point getPoint(int pos)
       {
           return  wirePoints.get(pos);
       }












}
