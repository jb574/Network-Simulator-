/**
 * NetSim project
 */

package netsim.Model.Devices;

import com.sun.corba.se.spi.activation._ServerManagerImplBase;
import netsim.GUI.GUIManager;
import netsim.GUI.View;
import netsim.GUI.Window;
import netsim.Model.CommunicaionUnits.Communication;
import netsim.Simulation.Scheduler;
import netsim.Simulation.SignalTypes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import  java.util.concurrent.*;
import  java.util.Map;
/**
 * @author: Jack Davey
 * @version: 25/11/2013
 *  this class represents a computer on the network
 */
public class Computer  extends Device
{

    private boolean isForward;
    private boolean allclear;
    private int listenTime;
    private String actionToTake;
    private  Signal sig;
    private String nextAction;
    private boolean busy;
    private LinkedList<Signal> sigsToSend;
    private int timeToListenFor;
    // linked list to represent our recieved signals.
    private LinkedList<Signal> recievedSignals;
    private Map<Integer, Communication> openCommunications;

    // boolean to check wheter  work in progress
    // in other words either this pc is doing nothing and can send messages
    // or it is either sending a message or waiting for a response.
    private boolean canSendMessage;




    /**
     * method return the numbe rof frames
     * currenlty set to be processed
     * by this computer
     * @return  the information mentioned above
     */
    public int getNumberOfFramesInProcess()
    {
        return sigsToSend.size();
    }


    /**
     * method to tell a signal whether it should be
     *  processed or not
     * @return a boolean yes or no anwer
     */
    public boolean canProcessSigNow()
    {
        return (recievedSignals.size() <= maxQueueSize );
    }





    /**
     * main constructor for this class
     *  all this does is call the superclass constructor
     * @param id  the id of this object
     * @param name   the name of this object
     */
    public Computer (String name, boolean isForward)
    {
        super(name);
        this.isForward = isForward;
        this.setView(new netsim.View.Computer(this.getUID()));
        ports = new portBank();
        canSendMessage = true;
        reset();

    }


    @Override
    public void kill()
    {
        super.kill();
        reset();
        _view = null;
        ports.kill();
    }

    /**
     * method to restoe the computer back
     * to what it was
     */
    @Override
    public void reset() {
        actionToTake = "wait";
        busy =false;
        canSendMessage = true;
        sigsToSend = new LinkedList<Signal>();
        timeToListenFor = Scheduler.getTimeToListenFor();
        netsim.View.Computer compView = (netsim.View.Computer)this.getView();
        compView.setBlue();
        openCommunications = new HashMap<>();
        recievedSignals = new LinkedList<>();

    }

    /**
     * Called when we should do something
     */
    protected void onTick() {
        if(!busy)
        {
            busy = true;
            
            if(actionToTake.equals("listen"))
            {
                setActionToTake("");
                listen();
                ignoreTicks(1);
            }
            else if(actionToTake.equals("red"))
            {
                completeProcess(actionToTake);
                this.ignoreTicks(3);
            }
            else if(actionToTake.equals("green"))
            {
                completeProcess(actionToTake);
                this.ignoreTicks(3);
            }
            else if ( actionToTake.equals("blue") )
            {
                completeProcess(actionToTake);
            }

           else  if(actionToTake.equals("send"))
            {
                 if(sig.isNewProcess())
                 {
                     sendMessage(sig);
                 }
                 else
                 {
                      sendMessageAgain(sig);
                 }



                canSendMessage = true;
            }
            else if(actionToTake.equals("wait"))
            {
                if(GUIManager.checkGraphicsStatus())
                {
                    sigsToSend.clear();
                }
                if(canSendMessage)
                {

                    processNextEvent();

                }
                else
                {
                    if(sigsToSend.isEmpty())
                    {
                        netsim.View.Computer compView = (netsim.View.Computer)getView();
                         compView.setBlue();
                    }
                }
            }
            else if (actionToTake.equals("stop"))
            {
                setActionToTake("wait");
                canSendMessage = true;
                GUIManager.stopProcess();
                timeToListenFor = Scheduler.getTimeToListenFor();
            }
            else if(actionToTake.equals("recieve"))
            {
                setActionToTake("green");
                endProcess();
                sig.setAction("proc");
                sig.setAmountStillToProcess(0);

                recievedSignals.offer(sig);
                nextAction = "stop";
            }
            else if(actionToTake.equals("bad"))
            {
                setActionToTake("red");

                if(sig != null)
                {
                    endProcess();
                    sig.setAction("proc");
                    sig.setAmountStillToProcess(0);
                    recievedSignals.offer(sig);

                }



            }

            busy =false;
        }
        removeProcessedSignals();
    }


    /**
     * methdo to add a communication to
     * the computer
     * @param  com the commuication to add
     */
    public  void addCommunication(Communication com)
    {
        openCommunications.put(com.getUID(),com);
    }



    /**
     * methdo to remvoe all signals that have finished being
     * processe by this computer.
     */
    private void removeProcessedSignals()
    {
        Iterator<Signal> it = recievedSignals.iterator();
        while (it.hasNext())
        {
            Signal asig = it.next();
            asig.setAmountStillToProcess(asig.getAmountStillToProcess() + 1);
            if(asig.getAmountStillToProcess() > 300)
            {
                asig.kill();
                it.remove();
            }

        }
    }

    /**
     * method that is called upon reciept of a signal
     * it makks it off on the list
     * @param the signal to check off.
     */
    public void  CheckOffSignal(Signal asig)
    {
        int currentProtocol = SignalTypes.getCurrentProtocl();
        if(currentProtocol == 0)
        {
            System.out.println(asig.getComNumber() + " is the current communication number");
            if(openCommunications.containsKey(asig.getComNumber()))
            {
                openCommunications.get(asig.getComNumber()).confirmPacketArrival(asig);
            }
            else
            {
                GUIManager.addText("all packts were revied sucsessfully");
            }
        }

    }



    /**
     * method to find out the number of wires connected
     * to this machine
     * @return as above
     */
    public int getNumberOfWires()
    {
        return ports.getPortSet().size();
    }

    /**
     * method to process the next event
     * in the queue
     */
    public void processNextEvent()
    {
        if(!sigsToSend.isEmpty())
        {
            sig = sigsToSend.pop();
            if(sig.isNewProcess())
            {
                GUIManager.startProcess();
            }
            canSendMessage = false;

            setActionToTake(sig.getAction());
            if(actionToTake.equals("listen"))
            {
                GUIManager.addText("checking to make sure all channels are clear");
            }
            actionToTake = sig.getAction();

            System.out.println("action is" + actionToTake);
        }
    }

    private void endProcess() {
        GUIManager.updateText("welcome to the network simulator");
        sig.getOrigin().completeProc();
        nextAction = "stop";
        canSendMessage = true;
    }

    public void setActionToTake(String actionToTake) {
       // System.out.println("next action is"  + actionToTake);
        this.actionToTake = actionToTake;
    }

    /**
     * Called when we are clicked
     */
    public void onClick() {

        if (GUIManager.getSelectState() == 1) {
            GUIManager.addSelection(this);
            return;
        }
        
        Device target = Scheduler.getItemClickedOn();
        if(target == null)
        {
            Scheduler.setItemClickedOn(this);
            nextAction = "wait";
            setActionToTake("green");
            GUIManager.updateText("now choose the sender");
        }
        else
        {
            System.out.println(this.getDeviceName() + " wants to send a message to " + target.getDeviceName());
            GUIManager.addText(this.getDeviceName() + "wants to send a message to" + target.getDeviceName());
            addFramesToQueueu(target);
            timeToListenFor = Scheduler.getTimeToListenFor();
            Scheduler.setItemClickedOn(null);
            listenTime = 0;
        }
    }


    /**
     * method to chack whether a  signal is
     * queued up to send
     * @param the signal to send
     * @return a boolean indicating true or false
     */
    public boolean isSignalBeingProcessed(Signal asig)
    {
        return sigsToSend.contains(asig);
    }


    /**
     * method to add a signal to this computers
     * recieved queue
     *
      */
    public void addMessagesToReceiveQueue(Signal Sig)
    {
        recievedSignals.offer(Sig);
    }

    /**
     * method to send a message that has already been sent once
     * thus it does not need to go to every wire
     * @param sig the message to send
     */
    public  void sendMessageAgain(Signal asig)
    {
       Signal newSig = new Signal(asig.getWire(),asig.getOrigin(),asig.getDestination(),"move"
       ,asig.getTrueDestination());
        newSig.setComNumber(asig.getComNumber());
        newSig.setSequenceNumber(asig.getSequenceNumber());
        Port aport = ports.findCorrectPort(asig.getWire());
        aport.resendMessage(newSig);
        System.out.println("wayhay");
        actionToTake = "stop";

    }


    /**
     * method to remove old communications
     * @param com the communication to remove
     */
    public void removeOldCommunication(Communication com)
    {
        openCommunications.remove(com);
        com.kill();
    }

    /**
     * method to add  a set number of rames to the queue
     * @param target    the target computer to send to.
     */
    public void addFramesToQueueu(Device target) {
        int framesToSend = Scheduler.getNumberOfFramesToSend();
        int total = 0;
        int totalToSend = SignalTypes.getCurrentSize();
        int overallTotal =  framesToSend * totalToSend;
        GUIManager.addText(" in order to send this file, we need to send" +
                " " + overallTotal + " packets");
        Communication com = new Communication(overallTotal);
        target.addCommunication(com);
        while (framesToSend > 0)
        {
            for(int index = 0; index < SignalTypes.getCurrentSize(); index++)
            {
                total = AddSignal(target, total,com);
            }
            framesToSend = framesToSend -1;
        }
        GUIManager.addText("to transfer this file, we need to send " + total + " packets");
    }

    private int AddSignal(Device target, int total,Communication com)
    {
        Signal tempSig = new Signal(new Wire(), this, target,"listen");

        tempSig.setSequenceNumber(total);
        tempSig.setComNumber(com.getUID());
        System.out.println("intital communication number is" + tempSig.getComNumber());
        sigsToSend.offer(tempSig);
        total++;
        return total;
    }

    /**
     * this method sends a message down the network
     * @param sig  the signal to send
     */
    public  void sendMessage(Signal asig)
    {
        System.out.println("sending message now");
        ports.sendMessage(asig);
        setActionToTake("green");
        nextAction = "stop";
    }


    /**
     * message to complet eth process once
     * a message has been recieved
     */
    public void completeProc()
    {
        canSendMessage = true;
    }


    public void setListenTime(int listenTime)
    {
        this.listenTime = listenTime;
    }

    public int getListenTime()
    {
        return listenTime;
    }

    public String getNextAction()
    {
        return nextAction;
    }

    public void listen()
    {
      // if the listne timer is less then seventy
        // then we are still listening
        System.out.println(listenTime);
        System.out.println(timeToListenFor);
        if(listenTime < timeToListenFor)
        {
            this.pause(30);
            netsim.View.Computer compView = (netsim.View.Computer)this.getView();
            compView.flash();
            boolean allClear = ports.areAllChannelsClear();
            if(!allClear)
            {
                listenTime = 0;
                System.out.println("error - channel not clear!");
                setActionToTake("listen");
               return;
            }
            else
            {

                listenTime = listenTime + 1;
                actionToTake = "listen";
                return;
            }


        }
        else
        {

            System.out.println("error");
           listenTime = 0;
           nextAction = "send";
           setActionToTake("green");
        }

    }


    /**
     * method to schedule a resend after a collision
     * @param backoffTime the time to back off for
     * before attempting a resend
     * @param sig the signal that failed to send
     */
    public void backoff(int backoffTIme, Signal Sig)
    {
        System.out.println(backoffTIme);
        GUIManager.addText("computer" + this.getUID() + " is calculating a new backoff time now");
        timeToListenFor = backoffTIme;
        sigsToSend.offer(Sig);
    }


    /**
     * method to deal with the complete process
     *  of completing part of the operation
     * @param action the string to act upon
     */
    private void completeProcess(String action)
    {

        netsim.View.Computer compView = (netsim.View.Computer)this.getView();

        if (action.equals("green"))
        {

            compView.setGreen();
            setActionToTake("blue");
            this.pause(200);
        }
        else if(action.equals("red"))
        {
            compView.setRed();
            setActionToTake("blue");
            this.pause(200);
        }
        else
        {
            compView.setBlue();
            setActionToTake(nextAction);
            nextAction = "";
        }

    }

    public String getActionToTake()
    {
        return actionToTake;
    }

    /**
     * method for adding signals to the network during testing
     * @param asig the signal to add
     */
    public  void addSig(Signal asig)
    {
          sigsToSend.offer(asig);
          System.out.println(sigsToSend.size());
    }

    public void takeCorruptedMessage(Signal badSignal)
    {
        sigsToSend.offer(badSignal);
        badSignal.setAction("send");
        canSendMessage = true;
        System.out.println("sending now");
        setActionToTake("wait");
    }

    /**
     * method to add signals that need resending to the queue
     * @param the destiation computer
     */
    public  void addDamagedSignals(Communication com, Device  target, int startNumber, int endNumbewr)
    {
          System.out.println("initial start number is" + startNumber);
        do
        {
            System.out.println(" adding sig number" + startNumber);
            System.out.println("end number is" + endNumbewr);
            startNumber = AddSignal(target, startNumber,com);
            System.out.println("startnumber is now" + startNumber);
        }
        while (startNumber <= endNumbewr);

    }


    /**
     * method to proces a  corrupted
     * signal using th e go back n protocl
     * @param the signal to process
     */
    public void processCorruptedSignalUsingGoBackN(Signal asig)
    {
        GUIManager.addText("this message is corrupted so is being discarded");
         asig.setAction("bad");

         asig.kill();
        if(asig.getTrueDestination().equals(this))
        {
            this.CheckOffSignal(asig);
        }
        sigsToSend.offer(asig);
    }



    /**
     * method that recieves a signal
     * @param sig  the signal to recieve
     */
    public  void  recieveMessage(Signal asig)
    {
       if(asig.isCorrupted())
       {
           int currentProtocol = SignalTypes.getCurrentProtocl();
           System.out.println("protocol is" + currentProtocol);
           if(currentProtocol == 0)
           {
               processCorruptedSignalUsingGoBackN(asig);
           }
           else
           {
               System.out.println("using csdma");
               processCorruptedMethodCSMACD(asig);
           }
       }
       else if(this.equals(asig.getTrueDestination()))
       {

           GUIManager.addText("this signal has been proccessed" +
                   " as it has reached its true destination");
            asig.setAction("recieve");
           sigsToSend.offer(asig);
           CheckOffSignal(asig);
       }
       else
       {
           asig.kill();
           GUIManager.addText("this signal was discarded as it was not meant for pc "
                   + this.getUID());
           asig.setAction("bad");
           sigsToSend.offer(asig);
       }

    }

    private void processCorruptedMethodCSMACD(Signal asig)
    {
        GUIManager.addText("this message from " + asig.getOrigin().getUID() +
                " needs to be resent as it was corrupted during transmission");
        setActionToTake("bad");
        asig.setCorrupted(false);
        asig.setAction("send");

        asig.setNewProcess(false);
        asig.getOrigin().takeCorruptedMessage(asig);
    }

    public void setCanSendMessage(boolean canSendMessage) {
        this.canSendMessage = canSendMessage;
    }

}
