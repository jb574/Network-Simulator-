/**
 * NetSim project
 *
 * @copyright Sarah Rose
 */

package netsim.Simulation;

import java.util.HashMap;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import netsim.Model.Devices.Device;
import netsim.Simulation.Agent;

/**
 * Our Simulator class.
 * 
 * This is here to register new agents and simulates concurrency
 * using a round-robin approach (DES).
 *
 * You should create one of these and then add agents to it.
 * You should call run() when ready.
 */
public class Scheduler extends Thread
{

    // the last clicked on device
    private static Device itemClickedOn;


    public static Device getItemClickedOn() {
        return itemClickedOn;
    }

    public static void setItemClickedOn(Device itemClickedOn) {
        Scheduler.itemClickedOn = itemClickedOn;
    }

    /**
	 * Singleton
	 */
   private static Scheduler scheduler = null;

	/**
	 * Our tick rate (how many times we tick per second)
	 */
	private int tickRate;

     public static int getNumberOfFramesToSend()
     {
         return scheduler.framesToSend;
     }

    public  static void setNumberOfFrames(int newFrameToSend)
    {
        scheduler.framesToSend =  newFrameToSend;
    }

	/**
	 * A list of agents
	 */
	private HashMap<Integer, Agent> _agents;

	/**
	 * A list of agents to be added post-tick
	 */
	private ConcurrentLinkedQueue<Agent> _newAgents;

    private int framesToSend;


    private static int timeToListenFor;


    public static int getTimeToListenFor()
    {
        return Scheduler.timeToListenFor;
    }

    public static void setTimeToListenFor(int timeToListenFor)
    {
        Scheduler.timeToListenFor = timeToListenFor;
    }

    /**
	 * A list of agents to be removed post-tick
	 */
	private ConcurrentLinkedQueue<Agent> _oldAgents;

	/**
	 * Create a new simulator
	 *
	 * @param int Our tick rate (how many times we tick per second)
	 */
	private Scheduler() {
		super("NetSimulator");
        framesToSend = 1;
		this.tickRate = 10;
        itemClickedOn = null;
		this._agents = new HashMap<Integer, Agent>();
		this._newAgents = new ConcurrentLinkedQueue<Agent>();
		this._oldAgents = new ConcurrentLinkedQueue<Agent>();
        timeToListenFor = 15;
	}

	/**
	* Make this a singleton
	*/
	public static Scheduler obtain() {
		if (scheduler == null) {
			scheduler = new Scheduler();
		}
		return scheduler;
	}

    /**
     * called when we want to stop the graphisc
     */

      public void reset()
      {
           for(Agent anAgent : this.getAgents())
           {
               anAgent.reset();
           }
      }


    /**
     * this is sim8iar to the obtain method
     * above, exect it resets old test results
     * @return the appropriate object
     */
     public static Scheduler obtainForTesting()
     {
       scheduler = new Scheduler();
       return  scheduler;

     }


	/**
	 * Set our tick rate
	 */
	public void setTickRate(int tickrate) {
		this.tickRate = tickrate;
	}
	/**
	 * Get our tick rate
	 */
	public int getTickRate() {
		return this.tickRate;
	}

	/**
	 * Add a new agent to this simulation.
	 * 
	 * @param agent The agent to add
	 */
	public void addAgent(Agent agent) {
		this._newAgents.add(agent);
	}





	/**
	 * Remove a new agent to this simulation.
	 * 
	 * @param agent The agent to remove
	 */
	public void removeAgent(Agent agent) {
		this._oldAgents.add(agent);
	}

	/**
	 * Returns a registered agent with the given UID
	 * 
	 * @param Integer The agent UID
	 * @return Agent The agent that relates to the given UID
	 */
	public Agent getAgent(Integer uid) {
		return this._agents.get(uid);
	}

	/**
	 * Returns all registered agents
	 */
	public Collection<Agent> getAgents() {
		return this._agents.values();
	}

	/**
	 * Tick once, calls tick on all agents
	 */
	private void tick() {
		// Loop through all agents and tick them
		Collection<Agent> agents = this._agents.values();
		for (Agent agent : agents) {
			// Tick the agent
			agent.tick();

		}

		// Add new agents
		for (Agent agent : this._newAgents) {
			agent.setSimulation(this);
                        if (!this._agents.containsKey(agent.getUID())) {
                            this._agents.put(agent.getUID(), agent);
                        }
			this._newAgents.remove(agent);
		}

		// Kill off old agents
		for (Agent agent : this._oldAgents) {
			agent.onStop();
                        if (this._agents.containsKey(agent.getUID())) {
                            this._agents.remove(agent.getUID());
                        }
			this._oldAgents.remove(agent);
		}
	}



	/**
	 * This is a never ending cycle that cannot be stopped... (unless it is interrupted)
	 */
	@Override
	public void run() {
		System.out.println("Starting Network Scheduler...");

		while (this.isAlive()) {
			// Do stuff
			this.tick();
			
			try {
				this.sleep(1000 / this.tickRate);
			} catch (InterruptedException ex) {
				break;
			}
		}

		System.out.println("Shutting down...");
	}
}