package doconnor.aop.dao;

import java.util.Set;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import doconnor.aop.domain.Club;
import doconnor.aop.domain.Division;
@Repository
public class DivisionDAOImpl extends HibetnateDAO  {

		public DivisionDAOImpl() {
			super();
		}

		public void remove(long divisionId) {
			Session session = sessionFactory.openSession();
	        Transaction tx = null;
	        try {
	            tx = session.beginTransaction();
	            Division division = (Division) session.get(Division.class, divisionId) ;
	            session.delete(division) ;
	            tx.commit();
	        } catch (HibernateException e) {
	            if (tx != null) {
	                tx.rollback();
	            }
	            throw e;
	        } finally {
	            session.close();
		    }
		}

		public Set<Club>  listDivision(long divisionId ) {
			Set<Club> result = null ;
			Session session = sessionFactory.openSession();
	        Transaction tx = null;
	        try {
	            tx = session.beginTransaction();
	            Division division = (Division) session.createCriteria(Division.class).
	            	add(Restrictions.eq("id", divisionId) ).
	            	setFetchMode("members", FetchMode.JOIN).uniqueResult() ;
	           for (Club club : division.getMembers()){
	        	   // Force fetching of players in club
	        	   club.getPlayers().size() ;
	           }
	            result = division.getMembers() ;
	            tx.commit();
	        } catch (HibernateException e) {
	            if (tx != null) {
	                tx.rollback();
	            }
	            throw e;
	        } finally {
	            session.close();
		    }
	        return  result;
	}

	public void add(Division division) {
		}

}
