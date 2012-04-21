package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.LicensingDeal;

public interface LicensingDAO {

	LicensingDeal reattach(LicensingDeal company);

	List<LicensingDeal> getAll();

	void save(LicensingDeal s);

	void remove(LicensingDeal s);
}
