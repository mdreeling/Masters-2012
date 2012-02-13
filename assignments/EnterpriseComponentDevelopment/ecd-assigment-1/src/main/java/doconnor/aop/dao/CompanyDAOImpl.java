package doconnor.aop.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;

import doconnor.aop.domain.Company;
@Repository
public class CompanyDAOImpl extends HibetnateDAO{

		public CompanyDAOImpl() {
			super();
		}

		@SuppressWarnings("unchecked")
		public List<Company> listAll () {
			List<Company> result = new LinkedList<Company>() ;
			Session session = sessionFactory.openSession();
	        Transaction tx = null;
	        try {
	            tx = session.beginTransaction();
	            result  =  session.createCriteria(Company.class).
	            	setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).
	            	setFetchMode("support", FetchMode.JOIN).list() ;
	            tx.commit();
	        } catch (HibernateException e) {
	            if (tx != null) {
	                tx.rollback();
	            }
	            throw e;
	        } finally {
	            session.close();
		    }	
	        return result ;
		}
}
