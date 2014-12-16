/**
 * NetSim project
 */

package netsim.GUI;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Collection;

import netsim.Simulation.Agent;
import netsim.View.SignalView;

import javax.swing.*;

/**
 * this class is called from the model
 * to signal updates to the gui
 * @author Jack davey
 * @version 20th november 2013
 */
public class GUIManager extends Agent
{
    private static GUIManager manager = null;

    private Window mainView;

    private boolean graphicsStopped;
    
    private int selectState;
    private ArrayList<Agent> selectList;

    public  static  void StopGraphics()
    {
        manager.graphicsStopped = true;
    }


    /**
     * the main constructor for this class
     */
    private GUIManager() throws UnsupportedFlavorException
    {
        super();
        this.mainView = new Window();
        graphicsStopped = false;
        selectState = 0;
        selectList = new ArrayList<>();
    }

    /**
     * method to check that the graphics has stopped
     */
    public static boolean checkGraphicsStatus()
    {
        return  manager.graphicsStopped;
    }


    /**
     * slr25 - Make this a singleton
     */
    public static GUIManager obtain()throws  UnsupportedFlavorException {
        if (manager == null) {
            manager = new GUIManager();
        }
        return manager;
    }

    /**
     * mehtod to obtain a gui manager
     * for testing that restes all old test data
     * @return the right test value
     */
    public static  GUIManager obtainTesting()  throws UnsupportedFlavorException
    {
           manager = new GUIManager();
           return  manager;
    }


    /**
     * method to update the
     * text on screen
     * @param text   the new text
     */
    public static void updateText(String text)
    {
        if(manager.graphicsStopped)
        {
            manager.mainView.drawText("Welcome to the network simulator");
        }
        else
        {
            manager.mainView.drawText(text);
        }

    }
    
    /**
     * Returns the current select state.
     */
    public static int getSelectState() {
        return manager.selectState;
    }
    
    /**
     * Returns the current select state.
     */
    public static void setSelectState(int state) {
        manager.selectState = state;
    }
    
    /**
     * Add an item to be clicked.
     */
    public static void addSelection(Agent obj) {
        manager.selectList.add(obj);
    }
    
    /**
     * Select all items due to be clicked.
     */
    public static void clickSelection() {
        for (Agent a : manager.selectList) {
            a.onClick();
        }
        manager.selectList.clear();
    }


    /**
     * mthod to get the numbe rof ops running
     * @return the number of ops  running
     */
    public static   int getOpsrunning()
    {
            return manager.opsrunning;
    }

   private int opsrunning;

    /**
     * method to singal to the schedular that another process is running
     */
    public  static void startProcess()
    {
        manager.opsrunning++;
    }


    /**
     * method to stop a proces form running
     */
    public static void stopGraphics()
    {
        manager.graphicsStopped = true;
    }


    /**
     * method to singal to the manager that a process has
     * stopped running
     */
    public  static  void  stopProcess()
    {
        manager.opsrunning--;
    }

    /**
     * method to add text to the status box
     * @param text the text to add
     */
    public static void addText(String text)
    {
        text = text + "\r\n";
        JTextArea area = manager.mainView.getArea();
        area.append(text);
        area.setCaretPosition(area.getText().length()-1);
    }


    /**
     * Called when we should do something
     */
    protected void onTick() {
        this.mainView.reset();
        if(opsrunning == 0)
        {
            graphicsStopped = false;
            updateText("welcome to the network simulator");
        }
        Collection<Agent> agents = this.getSimulation().getAgents();
        for (Agent agent : agents) {
            View v = agent.getView();
            if(v != null)
            {
                if(!graphicsStopped)
                {
                    v.draw(this.mainView);

                }
                else
                {
                    if(!( v instanceof SignalView))
                    {
                        v.resetDefaults();
                        v.draw(mainView);
                    }
                }
            }

        }

        this.mainView.draw();
        this.ignoreTicks(1);
    }
}
