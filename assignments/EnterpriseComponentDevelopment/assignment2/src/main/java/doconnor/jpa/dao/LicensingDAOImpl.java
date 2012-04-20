package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.LicensingDeal;

@Repository
public class LicensingDAOImpl extends HibernateDAO implements LicensingDAO {


	public void reattach(Company company) {
		 getEntityManager().merge(company) ;
	}


	@SuppressWarnings("unchecked")
	public List<LicensingDeal> getAll() {
		return getEntityManager().createQuery("from LicensingDeal")
				.
					getResultList();
	}


	public void save(Company c) {
		getEntityManager().persist(c) ;
	}


	public void save(LicensingDeal s) {
		getEntityManager().persist(s) ;
		getEntityManager().flush(); // Had to add flush here! Junit did not
									// recognise the persist event had occurred
									// yet.
		System.out.println("Persisted licensing deal.");
	}

}
