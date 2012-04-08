package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.LicensingDeal;

@Repository
public class LicensingDAOImpl extends HibernateDAO implements LicensingDAO {

	@Override
	public void reattach(Company company) {
		 getEntityManager().merge(company) ;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LicensingDeal> getAll() {
		return getEntityManager().createQuery("from LicensingDeal")
				.
					getResultList();
	}

	@Override
	public void save(Company c) {
		getEntityManager().persist(c) ;
	}

	@Override
	public void save(LicensingDeal s) {
		getEntityManager().persist(s) ;
		getEntityManager().flush();
		System.out.println("Persisted licensing deal.");
	}

}
