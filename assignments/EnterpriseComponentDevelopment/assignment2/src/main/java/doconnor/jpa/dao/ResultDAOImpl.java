package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Result;

@Repository
public class ResultDAOImpl extends HibernateDAO implements ResultDAO {

    public ResultDAOImpl() {
		super();
	}


	public Result getResult(long resultId) {
		return getEntityManager().find(Result.class, resultId);
	}

	public void save(Result res) {
		getEntityManager().persist(res);
	}

	@SuppressWarnings("unchecked")
	public List<Result> getAll() {
		return getEntityManager().createQuery("from Result").
					getResultList();
	}

}
