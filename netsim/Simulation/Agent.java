/**
 * NetSim project
 *
 * @copyright Sarah Rose
 */

package netsim.Simulation;

import netsim.GUI.View;

/**
 * Our Agent class.
 *
 * You should inherit this for your agents (things like PCs and Packets).
 * Also override tick()
 */
public abstract class Agent
{
	/**
	 * A static counter for the number of agents we have
	 */
	private static Integer UID_COUNTER = 1000;

	/**
	 * Our unique identifier
	 */
	private Integer _uid;

	/**
	 * Ignore the next x ticks
	 */
	private int _ignoreTicks;

	/**
	 * The Simulation we live in
	 */
	private Scheduler _simulation;

	/**
	 * Our View (optional)
	 */
   protected View _view;

	/**
	 * Create a new Agent
	 */
	public Agent() {
		this.setUID();
		this._ignoreTicks = 0;
                this.addToSimulation();
	}
        
        /**
         * Adds this agent to the simulation
         */
        protected void addToSimulation() {
            Scheduler s = Scheduler.obtain();
            s.addAgent(this);
        }

    /**
     * method that is called
     * when the simulator is asked to reset
     * should be overriden for each individual
     * agent
     * @author Jack Davey
     */
     public void reset()
     {

     }


    /**
     * standard equals method for all agents
     * @param o the other object to check
     * @return  a boolean result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        if (!_uid.equals(agent._uid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return _uid.hashCode();
    }

    /**
	 * Override me!
	 * Called when we start up
	 */
	protected void onStart() {
		// :)
	}

	/**
	 * Override me!
	 * Called when we should do something
	 */
	protected abstract void onTick();

	/**
	 * Override me!
	 * Called when we are clicked
	 */
	public  void  onClick() {
		// :|
	}

	/**
	 * Override me!
	 * Called when we die
	 */
	protected void onStop() {
		// :(
	}

	/**
	 * Sets our view
	 */
	protected void setView(View view) {
		this._view = view;
	}

	/**
	 * Returns our view if we have one
	 */
	public View getView() {
		return this._view;
	}

	/**
	 * Returns the UID
	 * 
	 * @return int The unique ID of this object
	 */
	public Integer getUID() {
		return this._uid;
	}

	/**
	 * Ignore the next x ticks
	 */
	public void ignoreTicks(int number) {
		this._ignoreTicks = number;
	}

	/**
	 * Kills off this object
	 */
	public void kill() {
        if(getSimulation() != null)
        {
            this.getSimulation().removeAgent(this);
            this._ignoreTicks = 1;
        }
	}

	/**
	 * Get our world
	 *
	 * @return Simulation The simulation we are a part of
	 */
	protected Scheduler getSimulation() {
		return this._simulation;
	}

	/**
	 * Set our UID
	 */
	private void setUID() {
		this._uid = Agent.UID_COUNTER++;
	}

	/**
	 * Alias for ignoreTicks
	 */
	 public  void pause(int ms) {
            if(_simulation != null)
            {
                int schedulerTickRate = this._simulation.getTickRate();
                this.ignoreTicks(ms / schedulerTickRate);
            }

         }

	/**
	 * Tick, ignore this one
	 */
	 public  void tick() {
		// Are we ignoring ticks?
		if (this._ignoreTicks == 0) {
			this.onTick();
			return;
		}

		this._ignoreTicks--;
	}

	/**
	 * Get our world
	 *
	 * @return Simulation The simulation we are a part of
	 */
	void setSimulation(Scheduler sim) {
		this._simulation = sim;
		this.onStart();
	}
}