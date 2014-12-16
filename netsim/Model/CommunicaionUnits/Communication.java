package netsim.Model.CommunicaionUnits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import netsim.GUI.GUIManager;
import  netsim.Model.Devices.Signal;
import netsim.Simulation.Agent;

/**
 * class to track a communication between two computers
 * in the netwrok
 * @author  Jack Davey
 * @version 21st February 2014
 */
public class Communication extends Agent
{
    private List<Boolean> packetStatusList;

    private int TotalSignals;


    public  void onTick()
    {

    }

    /**
     * constructor for this class
     * @param totalNumberOfPackets  the total number fo packets that
     *                              need to be sent
     */
     public Communication(int totalNumberOfPackets)
     {
         System.out.println(totalNumberOfPackets+1 + "is the total number we are due to recieve");
          List<Boolean> tempList = Collections.nCopies(totalNumberOfPackets,false);
          packetStatusList = new ArrayList<>();
         packetStatusList.addAll(tempList);
         TotalSignals = totalNumberOfPackets;
         System.out.println("total list size is" + packetStatusList.size());
     }


    /**
     * method to choose the number of signals that we need to resend
     * @return the number of signals that need to be resent in int form, and 0 otherwise
     */
    public  int  computeAmountTOGoBack()
    {
       for(int index = 0; index < packetStatusList.size(); index++)
       {
           if(!packetStatusList.get(index))
           {
               GUIManager.addText("error at packet number" + index
               + " resending all signals beyond that point");
               if(index == 0)
               {
                   return  1;
               }
               return  index;
           }
       }
        return  0;
    }





    /**
     * method to confirm that a packet has arrived sucessfully
     * @param  int the recieved packet
     */
    public void confirmPacketArrival(Signal sig)
    {
        if (!sig.isCorrupted())
        {
            System.out.println("current index is" + sig.getSequenceNumber());
            System.out.println("total is"  + packetStatusList.size());
            if(sig.getSequenceNumber() >= 0)
            {
                packetStatusList.set(sig.getSequenceNumber(),true);
            }

        }
        else
        {
            packetStatusList.set(sig.getSequenceNumber(), false);
            System.out.println("error corrupted packet");
        }

        if(sig.getSequenceNumber() >=packetStatusList.size()-1)
        {
             int amounttoGoBack = computeAmountTOGoBack();
            if(amounttoGoBack == 0)
            {
                GUIManager.addText("all packets were recieved sucessfully");
                sig.getTrueDestination().removeOldCommunication(this);
            }
            else
            {
                GUIManager.addText(" we need to resend " + amounttoGoBack + " packets again" +
                        " as these got corrupted during transit");
                sig.getOrigin().addDamagedSignals(this,sig.getTrueDestination(),amounttoGoBack,packetStatusList.size()-1);
            }

        }
    }





}
