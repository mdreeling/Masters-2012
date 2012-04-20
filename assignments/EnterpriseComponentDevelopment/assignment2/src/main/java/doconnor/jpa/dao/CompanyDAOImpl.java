package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.Sponsorship;

@Repository
public class CompanyDAOImpl extends HibernateDAO implements CompanyDAO {


	public void reattach(Company company) {
		 getEntityManager().merge(company) ;
	}


	@SuppressWarnings("unchecked")
	public List<Company> getAll() {
		return  getEntityManager().createQuery("from Company").
					getResultList();
	}


	public void save(Company c) {
		getEntityManager().persist(c) ;
	}


	public void save(Sponsorship s) {
		getEntityManager().persist(s) ;
	}

}
