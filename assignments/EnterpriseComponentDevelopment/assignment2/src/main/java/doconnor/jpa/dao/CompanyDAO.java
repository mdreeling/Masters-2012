package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.Sponsorship;

public interface CompanyDAO {

	void reattach(Company company);

	List<Company> getAll();

	void save(Company c);
	
	void save(Sponsorship s) ;

}
