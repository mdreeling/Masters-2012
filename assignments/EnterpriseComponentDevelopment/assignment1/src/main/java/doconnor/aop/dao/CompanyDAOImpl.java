package doconnor.aop.dao;

import java.util.LinkedList;
import java.util.List;

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
	        return result ;
		}
}
