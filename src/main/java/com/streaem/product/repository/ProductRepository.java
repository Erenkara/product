package com.streaem.product.repository;

import com.streaem.product.model.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductRepository {

    @Select("SELECT * FROM products")
    List<Product> getAllProducts();

    @Select("SELECT * FROM products WHERE id = #{id}")
    Product getProductById(Long id);

    @Select("SELECT * FROM products WHERE category = #{category} AND stock > 0")
    List<Product> findInStockProductsByCategory(
            @Param("category") String category,
            @Param("offset") int offset,
            @Param("size") int size
    );

    @Select("SELECT * FROM products WHERE category = #{category}")
    List<Product> findProductsByCategory(
            @Param("category") String category,
            @Param("offset") int offset,
            @Param("size") int size
    );

    @Select("SELECT COUNT(*) FROM products WHERE category = #{category} AND (#{inStockOnly} = false OR stock > 0)")
    long countProductsByCategory(
            @Param("category") String category,
            @Param("inStockOnly") boolean inStockOnly
    );

    @Update("UPDATE products SET name = #{name}, category = #{category}, price = #{price}, description = #{description}, stock = #{stock}, version = version + 1 WHERE id = #{id} AND version = #{version}")
    int updateProduct(Product product);

    @Update("UPDATE products SET stock = #{stock}, version = version + 1 WHERE id = #{id} AND version = #{version}")
    int updateStockLevel(Product product);

    @Insert({
            "<script>",
            "INSERT INTO products (name, category, price, description, stock, version) VALUES ",
            "<foreach collection='products' item='product' separator=','>",
            "(#{product.name}, #{product.category}, #{product.price}, #{product.description}, #{product.stock}, #{product.version})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void saveAll(List<Product> products);
}

