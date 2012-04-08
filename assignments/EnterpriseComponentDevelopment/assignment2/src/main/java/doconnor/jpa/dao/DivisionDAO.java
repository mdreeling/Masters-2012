package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.Division;

public interface DivisionDAO {

	public List<Division> getDivisions();

	public Division reattach(Division division);

	public void remove(Division division);
	
}
