package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.LicensingDeal;

@Repository
public class LicensingDAOImpl extends HibernateDAO implements LicensingDAO {


	public LicensingDeal reattach(LicensingDeal lic) {
		return getEntityManager().merge(lic);
	}


	@SuppressWarnings("unchecked")
	public List<LicensingDeal> getAll() {
		return getEntityManager().createQuery("from LicensingDeal")
				.
					getResultList();
	}

	public void save(LicensingDeal s) {
		getEntityManager().persist(s) ;
		getEntityManager().flush(); // Had to add flush here! Junit did not
									// recognise the persist event had occurred
									// yet.
		System.out.println("Persisted licensing deal.");
	}

	public void remove(LicensingDeal s) {
		getEntityManager().remove(s);
	}

}
