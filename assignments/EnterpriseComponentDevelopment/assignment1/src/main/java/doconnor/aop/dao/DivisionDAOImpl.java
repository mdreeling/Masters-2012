package doconnor.aop.dao;

import java.util.Set;

import org.springframework.stereotype.Repository;

import doconnor.aop.domain.Club;
import doconnor.aop.domain.Division;
@Repository
public class DivisionDAOImpl extends HibetnateDAO  {

		public DivisionDAOImpl() {
			super();
		}

		public void remove(long divisionId) {
		}
		
		public Set<Club>  listDivision(long divisionId ) {
			Set<Club> result = null ;
	        return  result;
		}

		public void add(Division division) {		
		}		
		
}
