package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.LicensingDeal;

public interface LicensingDAO {

	void reattach(Company company);

	List<LicensingDeal> getAll();

	void save(Company c);

	void save(LicensingDeal s);

}
