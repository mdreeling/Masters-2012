package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.Result;

public interface ResultDAO {
	public abstract Result getResult(long resultId);

	public abstract List<Result> getAll();

	public abstract void save(Result agent);

	public abstract Result reattach(Result res);

	public abstract void remove(Result ress);

}
