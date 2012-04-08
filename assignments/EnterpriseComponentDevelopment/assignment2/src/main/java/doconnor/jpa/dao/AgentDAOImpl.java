package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Agent;
import doconnor.jpa.domain.Contract;

@Repository
public class AgentDAOImpl extends HibernateDAO  implements AgentDAO {

    public AgentDAOImpl() {
		super();
	}


	public Agent getAgent(long agentId) {
    	return getEntityManager().find(Agent.class, agentId);
	}

	public void save(Agent agent) {
		getEntityManager().persist(agent);
	}


	public void save(Contract c) {
		getEntityManager().persist(c);
	}


	@SuppressWarnings("unchecked")
	public List<Agent> getAll() {
		return getEntityManager().createQuery("from Agent").
					getResultList();
	}

}
