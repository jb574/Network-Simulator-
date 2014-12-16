package netsim.tests;

import netsim.Simulation.Agent;

/**
 * contains utility functions
 * useful for testing
 * @author Jack Davey
 * @version 7th January 2013
 */
public class TestUtils
{
    /**
     * method to tick an agent a set number of times
     * @param agent  the agent to tick
     * @param numberOfTicks the number of times to tick
     */
    public static void tickNTimes(Agent agent, int numberOfTicks)
    {
        for(int index = 0; index < numberOfTicks; index++)
        {
            agent.tick();
        }
    }

}
