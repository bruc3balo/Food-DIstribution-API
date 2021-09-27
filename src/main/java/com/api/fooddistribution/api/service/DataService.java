package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models.Product;
import com.api.fooddistribution.api.domain.Models.ProductCategory;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.ProductUpdateForm;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;

public interface DataService {

    //Product
    Product saveNewProduct(ProductCreationFrom productCreationFrom) throws ParseException, NotFoundException;
    Product updateProduct(String productName,ProductUpdateForm form) throws ParseException, NotFoundException;
    Product disableProduct(Product product);
    Product enableProduct(Product product);
    Product deleteProduct(Product product);
    Product getProduct(String productName);
    Page<Product> getAllProducts(Specification<Product> specification, PageRequest pageRequest);

    //category
    ProductCategory saveNewProductCategory(String categoryName) throws ParseException;
    ProductCategory updateProductCategory(String categoryName, String productCategoryNewName) throws ParseException, NotFoundException;
    ProductCategory disableProductCategory(ProductCategory productCategory);
    ProductCategory enableProductCategory(ProductCategory productCategory);
    ProductCategory deleteProductCategory(ProductCategory productCategory);
    ProductCategory getProductCategory(String productCategoryName);
    Page<ProductCategory> getAllProductCategories(Specification<ProductCategory> specification, PageRequest pageRequest);

    //product category
    Product addProductToCategory(String productName, String productCategoryName) throws NotFoundException;



}
