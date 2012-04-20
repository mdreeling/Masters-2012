package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Product;
@Repository
public class ProductDAOImpl extends HibernateDAO implements ProductDAO {

	    public ProductDAOImpl( ) {
			super();
		}


	public void remove(Product product) {
		getEntityManager().remove(product);
	}


	public Product getProduct(long productId) {
		return getEntityManager().find(Product.class, productId);
	}


	public Product getProductAndCompany(long productId) {
		return (Product) getEntityManager()
				.createQuery(
						"from Product p LEFT JOIN FETCH p.company where p.id = :id")
				.setParameter("id", productId).getSingleResult();
	}


	public void save(Product product) {
		getEntityManager().persist(product);
	}


	@SuppressWarnings("unchecked")
	public List<Product> getProducts() {
		return getEntityManager().createQuery("from Product").getResultList();
	}


	public Product reattach(Product product) {
		return getEntityManager().merge(product);
	}

}
