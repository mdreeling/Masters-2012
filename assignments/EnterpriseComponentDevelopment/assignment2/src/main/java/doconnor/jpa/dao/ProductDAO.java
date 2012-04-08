package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.Product;

public interface ProductDAO {

	public abstract void remove(Product product);

	public abstract Product getProduct(long productId);

	public abstract List<Product> getProducts();

	public Product getProductAndCompany(long productId);

	public void save(Product product);

	public abstract Product reattach(Product product);

}