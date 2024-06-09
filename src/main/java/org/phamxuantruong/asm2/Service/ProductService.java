package org.phamxuantruong.asm2.Service;

import org.phamxuantruong.asm2.Entity.Product;
import org.phamxuantruong.asm2.Interface.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductDAO dao;
    public List<Product> findAll() {
        return dao.findAll();
    }
    public Optional<Product> findById(Long id) {
        return dao.findById(id);
    }
    public void save(Product product) {
        dao.save(product);
    }
    public void delete(Product product) {
        dao.deleteById(product.getProductID());
    }
}
