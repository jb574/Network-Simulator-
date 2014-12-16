/**
 * NetSim project
 */

package netsim.GUI;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.tools.classfile.StackMapTable_attribute;
import netsim.Model.Devices.Computer;
import netsim.Simulation.Agent;
import netsim.Simulation.Scheduler;
import netsim.Simulation.SignalTypes;
import netsim.tests.framingTests;
import java.util.List;
/**
 *this class rperesents the graphical user
 * interface that this gui rpresents
 * @author Jack Davey
 * @version 27th November 2013
 */
public class Window  extends JPanel
{
    // fields for this class

    // static constants
    final static  String waitRed = "/gfx/pc-red.gif";
    final static  String waitYellow = "/gfx/pc-yellow.gif";
    private final static  String packet = "/gfx/packet.gif";
    private final static  String ack = "/gfx/packet-ack.gif";

    private ConcurrentHashMap<Integer, Image> images;
    private ConcurrentHashMap<String, Shape> shapes;
    private List<String> protocolOptions;
    private int xsigLoc = -1;
    private int ysigloc = -1;
    private String text;
    private Point startPoint;
    private Point endPoint;
    Line2D.Double latestWire;

    private boolean inKillingMode;

    private ArrayList<Point> wirePositions;


     private JSlider slider;

    private JTextArea area;

    private Map<String,String> expandedOptions;;

    private JSlider listenSLider;



    /**
     * constructor for objects
     * of this class
     * all it does is init the gui
     */
    public Window()  throws UnsupportedFlavorException
    {




        // first we do all the farting aroundwith setting up
        // the gui.
        super();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception error)
        {

        }
        JFrame window = new JFrame("Network Simulator");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = window.getContentPane();
        contentPane.setLayout(new BorderLayout());
        this.setOpaque(true);
        window.setVisible(true);
        window.setSize(950,950);
        JPanel mediumChooser = createMediumChooser();
        JPanel toolbar = createOptionsPane();
        contentPane.add(toolbar,BorderLayout.WEST);
        contentPane.add(this, BorderLayout.CENTER);
        contentPane.add(mediumChooser,BorderLayout.NORTH);
        JPanel bottom = createStatusMessagePanel();
        JPanel right = InitDragAndDrop();
        contentPane.add(right,BorderLayout.EAST);
        contentPane.add(bottom,BorderLayout.SOUTH);
        images = new ConcurrentHashMap<>();
        shapes = new ConcurrentHashMap<>();
        text = "welcome to the Network Simulator";
        MouseResponder resp = new MouseResponder(this);
        this.addMouseListener(resp);
        this.addMouseMotionListener(resp);
        window.revalidate();

    }


    /**
     * method to set up the drag and drop
     * related stuff
     * @return the jpanel containging th esuff for the jbapenl
     */
    private JPanel InitDragAndDrop()  throws  UnsupportedFlavorException
    {
         dropEnabler enabler = new dropEnabler(this);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        BufferedImage pic = loadImage("/gfx/pc.gif");
        Icon currentIcon = new ImageIcon(pic);
        JLabel compIcon = new JLabel(currentIcon);
        DragListener dragger = new DragListener();
        DragSource loc = new DragSource();
        loc.createDefaultDragGestureRecognizer(compIcon, DnDConstants.ACTION_COPY,dragger);
        rightPanel.add(compIcon);
        return rightPanel;
    }



    /**
     * this sets up the options for the jcombobox in the expanded
     * itmes hashmap and sets up the appropriate JPanel.
     * @return as above
     */
    private JPanel createMediumChooser()
    {
          expandedOptions = new HashMap<>();
          expandedOptions.put("Web Page","page");
          expandedOptions.put("Audio File","tune");
          expandedOptions.put("Feature Length Film","film");
          JPanel listPane = new JPanel();
          listPane.setLayout(new BoxLayout(listPane,BoxLayout.X_AXIS));
          Vector<String> data = new Vector<>();
          for(String item : expandedOptions.keySet())
          {
              data.add(item);
          }
          JComboBox<String> chooser = new JComboBox<>(data);
          chooser.setSelectedIndex(0);
          chooser.setSelectedItem(data.get(2));
          chooser.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent event) {
                  JComboBox<String> box = (JComboBox<String>) event.getSource();
                  String selection = (String) box.getSelectedItem();
                  SignalTypes settingsOb = SignalTypes.obtain();
                  String trueChoice = expandedOptions.get(selection);
                  settingsOb.setCurrentMedium(trueChoice);
              }
          });
          protocolOptions = new Vector<>();
          protocolOptions.add(0,"Go Back N");
          protocolOptions.add(1,"CSDMA");
          JComboBox<String> protocolChooser = new JComboBox<>((Vector<String>)protocolOptions);
          protocolChooser.setSelectedIndex(1);
          chooser.setSelectedItem("CSDMA");
          protocolChooser.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent event) {
                 JComboBox<String> box = (JComboBox<String>) event.getSource();
                  String selection = (String) box.getSelectedItem();
                  SignalTypes.setCurrentProtocol(selection);
              }
          });
          listPane.add(chooser);
          listPane.add(protocolChooser);
          listPane.setBorder(BorderFactory.createLineBorder(Color.black));
        return listPane;
    }



    /**
     * method to  create the Jpanel neede
     * for the status messages
     * @return the jpanel to be used for this job
     */
    private JPanel createStatusMessagePanel()
    {
        area = new JTextArea(10,550);
        area.setEditable(false);
        JPanel bottom = new JPanel(new BorderLayout());
       bottom.add(new JScrollPane(area),BorderLayout.CENTER);
       bottom.setBorder(BorderFactory.createLineBorder(Color.BLACK));
       return  bottom;
    }


      private JButton modeChanger;

    public JTextArea getArea() {
        return area;
    }

    /**
     * method to create the iptions panel
     * on the side of the screen
     * @return  the completed options panel;
     */
    private JPanel createOptionsPane()
    {
        JPanel toolbar = new JPanel();
        toolbar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        toolbar.setBackground(Color.lightGray);
        toolbar.setLayout(new BoxLayout(toolbar,BoxLayout.Y_AXIS));
        JButton stopper = new JButton("stop!");
        JLabel label = new JLabel("File Size");

        createFramingSlider();
        stopper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                Scheduler sched = Scheduler.obtain();
                sched.reset();
            }
        }
        );
        toolbar.add(stopper);
        final JButton stopper2 = new JButton("Bulk Send");
        stopper2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                if (GUIManager.getSelectState() == 1) {
                    GUIManager.setSelectState(0);
                    GUIManager.clickSelection();
                    stopper2.setText("Bulk Send");
                } else {
                    // Let the user know what to do.
                    GUIManager.addText("Select which PCs you want to send a message from.");
                    GUIManager.setSelectState(1);
                    stopper2.setText("Send!");
                }
            }
        });
        modeChanger = new JButton("Remove Devices");
        modeChanger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!inKillingMode)
                {
                    modeChanger.setText("Resume");
                    inKillingMode = true;
                    Scheduler sched = Scheduler.obtain();
                    sched.reset();

                }
                else
                {
                    modeChanger.setText("Remove Devices");
                    inKillingMode = false;
                }
            }
        });
        toolbar.add(modeChanger);
        toolbar.add(stopper2);
        toolbar.add(label);
        JPanel sliderBar = new JPanel();
        sliderBar.setLayout(new BoxLayout(sliderBar,BoxLayout.X_AXIS));
        sliderBar.setBackground(Color.LIGHT_GRAY);
        sliderBar.add(listenSLider);
        sliderBar.add(slider);
        toolbar.add(sliderBar);
        return toolbar;
    }




    /**
     * method to set up the slider for choosing how
     * many frames to send
     */
    private void createFramingSlider()
    {
        slider = new JSlider(1,3,1);
        slider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent changeEvent)
            {
                System.out.println(slider.getValue());
                Scheduler.setNumberOfFrames(slider.getValue());
            }
        });
        slider.setOrientation(JSlider.VERTICAL);
        Hashtable<Integer,JLabel> labelList = new Hashtable<Integer, JLabel>();
        labelList.put(new Integer(1), new JLabel("small"));
        labelList.put(new Integer(2),new JLabel("medium"));
        labelList.put(new Integer(3), new JLabel("large"));
        slider.setLabelTable(labelList);
        slider.setPaintLabels(true);
        listenSLider = new JSlider(0,30,15);
        listenSLider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(" time to listen is" + listenSLider.getValue());
                Scheduler.setTimeToListenFor(listenSLider.getValue());
            }
        });
        listenSLider.setOrientation(JSlider.VERTICAL);
        Hashtable<Integer,JLabel> listenLabels = new Hashtable<>();
        listenLabels.put(new Integer(0),new JLabel("0"));
        listenLabels.put(new Integer(15),new JLabel("15"));
        listenLabels.put(new Integer(30),new JLabel("30"));
        listenSLider.setLabelTable(listenLabels);
        listenSLider.setPaintLabels(true);
    }

    /**
     * Resets us ahead of a redraw
     */
    void reset() {
        images = new ConcurrentHashMap<Integer, Image>();
        shapes = new ConcurrentHashMap<String, Shape>();
    }

    /**
     * Ping a redraw
     */
    void draw() {
        repaint();
    }


    /**
     * method to generate the coordinates
     * that the signal is drawn at during animation
     * of the wire
     */
    public void setUpCoordinates()
    {
        wirePositions = new ArrayList<Point>(21);
        int x = 100;
        int y = 180;
        for(int index = 0; index < 21; index++)
        {
            Point aPoint = new Point(x,y);
            wirePositions.add(index,aPoint);
            y = y + 5;
        }
    }





    /**
     * this is the main method to handle
     * mouse events, it works out if
     * a computer was clicked on
     * and if it was, it sends
     * a message through the main
     * network
     * @param x the x pos of the mouse event
     * @param y the y pos of the mouse event
     */
    public void handleUserInput(int x, int y) {
        for (Integer uid : images.keySet()) {
            Image im = images.get(uid);
            if (im.contains(x ,y)) {
                Scheduler s = Scheduler.obtain();
                if(inKillingMode)
                {
                    s.getAgent(uid).kill();
                }
                else
                {
                    s.getAgent(uid).onClick();
                }
                break;
            }
        }
    }

    /**
     * method to retreive a computer
     * at a given point
     * @param x the x p os
     * @param y the y pos
     * @return the  computer in question or null if none found
     */
    public Computer retreiveDeviceAtPoint(int x, int y)
    {
        for(Integer id : images.keySet())
        {
            Image im = images.get(id);
            if(im.contains(x,y))
            {
                Scheduler master = Scheduler.obtain();
                if(master.getAgent(id) instanceof  Computer)
                {
                    return (Computer) master.getAgent(id);
                }
            }
        }
        return null;
    }


    /**
     * this methdo checks that a point of a line is within a point
     * of a computer
     * @param x the x position
     * @param y the y position
     * @return a true or false answer to the question posed above
     */
    public boolean isPointInAComputer(int x , int y)
    {
        for(Integer id : images.keySet())
        {
            Image im = images.get(id);
            if(im.contains(x,y))
            {
                Scheduler master = Scheduler.obtain();
                return (master.getAgent(id) instanceof Computer);
            }
        }
        return  false;
    }







    /**
     * function to lead an image onto the screen
     *
     *@param a string containing the location of the image
     * @return the image to display
     */
    public BufferedImage loadImage(String location)
    {
        BufferedImage image = null;
        try
        {
            URL url = getClass().getResource(location);
            if (url != null) {
                image = ImageIO.read(url);
            } else {
                throw new IOException("Couldnt find resource");
            }
        }
        catch(IOException error)
        {
            System.out.println("Couldnt find file " + location);
        }
        return  image;
    }


    /**
     * this is the method that will eventaully
     * be called to update the screen
     * @param g the graphics object for this app
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D)g;
        for (Image im : images.values())
        {
            im.Draw(canvas);
        }
        for(Shape aShape : shapes.values())
        {
            canvas.draw(aShape);
        }
        if(latestWire != null)
        {
            canvas.draw(latestWire);
        }
        canvas.drawString(text,350,50);
    }


    /**
     * method to update the text on the screen
     * @param text new text
     */
    public void drawText(String text)
    {
        if(text != this.text)
        {
            this.text = text;
            repaint();
        }

    }



    /**
     * method to draw a wire on the screen
     * @param muid the muid of the wire
     * @param x the x pos of the wire
     * @param y the y pos of the wire
     * @param endx, the end x pos of the wire
     * @param endy the end y pos of the wire
     */
    public void drawWire(Integer muid, double x, double y,double endx,double endy)
    {
        Line2D line = new Line2D.Double(x,y,endx,endy);
        shapes.put(muid.toString(), line);
    }





    /**
     * this method retrieves the ack image or the standard
     * signal image depending on whats happening
     * on the network
     * @param isAck  oolean indicating what to retrieve
     */
    private BufferedImage createSignalImage(boolean isAck)
    {
        if (!isAck)
        {
            return loadImage(packet);
        }
        else
        {
            return  loadImage(ack);
        }
    }


    /**
     * this method is for working out whther the signal graphic should go
     * up or down
     * @param sigDirection signal boolean
     * @param currentY current y pos
     * @param amountToBeAdded the amount to be addred or subtracted
     */
    public int computeYPos(boolean sigDirection, int currentY, int amountToBeAdded)
    {
        if(sigDirection)
        {
            return currentY + amountToBeAdded;
        }
        else
        {
            return  currentY - amountToBeAdded;
        }
    }





    /**
     * method for adding a computer to the network
     * @param name name of the computer
     * @param x the x pos of the computer
     * @param y the y pos of the computer
     * @param width the width of the computer
     * @param height the height of the computer
     */
    public void  drawComputer(String image, Integer uid, int x, int y, int width, int height)
    {
        addImage(image, uid, x, y, width, height);
    }

    /**
     * method to draw an image on the screen
     * @param image the location of the image
     * @param uid  the uid to use
     * @param x  the x position
     * @param y the y position
     * @param width  the width of the image
     * @param height the height of the image
     */
    private void addImage(String image, Integer uid, int x, int y, int width, int height) {
        BufferedImage comp = loadImage(image);
        Image im = new Image(comp,x,y,width,height);
        images.put(uid, im);
        repaint(im.getX(),im.getY(),im.getWidth(),im.getHeight());
    }


    /**
     * method to draw a signal on screen
     * @param image the location of the image
     * @param uid  the uid to use
     * @param x  the x position
     * @param y the y position
     * @param width  the width of the image
     * @param height the height of the image
     */
    public void  drawSignal(String image, Integer uid, int x, int y, int width, int height)
    {
        addImage(image, uid, x, y, width, height);

    }

    public Line2D.Double getLatestWire()
    {
        return latestWire;
    }

    public void setLatestWire(Line2D.Double latestWire)
    {
        this.latestWire = latestWire;

    }
}
