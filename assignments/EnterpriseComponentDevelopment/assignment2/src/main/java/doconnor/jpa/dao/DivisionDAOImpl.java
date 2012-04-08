package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Division;
@Repository
public class DivisionDAOImpl extends HibernateDAO implements DivisionDAO {

	@SuppressWarnings("unchecked")
	public List<Division> getDivisions() {
		return  getEntityManager().createQuery("from Division").
									getResultList();
	}
	
	public Division reattach(Division division) {
    	return getEntityManager().merge(division);
	}

	public void remove(Division division) {
		getEntityManager().remove(division) ;
	}

}
