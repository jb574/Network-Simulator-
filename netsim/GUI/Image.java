/**
 * NetSim project
 */

package netsim.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *@author Jack Davey
 * @version 30th November 2013
 *
 */

public class Image
{
   private BufferedImage image;
   private  int x;
    private  int y;
    private  int width;
    private  int height;

    /**
     * constructor for this class
     * @param image  the image to set
     * @param x  the x posisiton to set
     * @param y the y position to set
     * @param width  the width positon to set
     * @param height   the height to set
     */
    public  Image(BufferedImage image, int x,  int y, int width, int height)
    {
       this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    public void Draw(Graphics2D canvas)
    {
        canvas.drawImage(image,x,y,width,height,null);
    }


    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    /**
     * this method cehcks to see if the ints
     * given are within the image
     * @param xToCheck the x to check
     * @param yToCheck the y to check
     * @return a boolean indicating whther thwe assumtion is correct or not
     */
    public boolean contains(int xToCheck, int yToCheck)
    {
        return ( (xToCheck >  x && xToCheck < x + width) && (yToCheck  > y && yToCheck < y + height)  );
    }


    /**
     * slightly better version of contains
     * we need to
     * @return
     */

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }
}
