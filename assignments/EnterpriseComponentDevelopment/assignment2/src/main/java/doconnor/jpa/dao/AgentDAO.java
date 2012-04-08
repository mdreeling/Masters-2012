package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.Agent;
import doconnor.jpa.domain.Contract;


public interface AgentDAO {
	public abstract Agent getAgent(long agentId);

	public abstract void save(Contract c);

	public abstract List<Agent> getAll();

	public abstract void save(Agent agent);

}
