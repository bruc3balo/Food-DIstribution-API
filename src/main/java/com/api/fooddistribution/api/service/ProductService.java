package com.api.fooddistribution.api.service;


import com.api.fooddistribution.api.domain.Models.Product;
import com.api.fooddistribution.api.domain.Models.ProductCategory;
import com.api.fooddistribution.api.model.ProductCategoryUpdateForm;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.ProductUpdateForm;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;

import java.io.NotActiveException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    //Name
    //Create
    Product saveNewProduct(ProductCreationFrom productCreationFrom) throws NotFoundException, ParseException, NotActiveException;
    ProductCategory saveNewProductCategory(String name) throws DuplicateMemberException, ParseException;

    //read
    Optional<Product> findProductById(String id);
    Optional<ProductCategory> findCategoryByName(String name);
    Optional<ProductCategory>  findCategoryById(String id);
    List<ProductCategory> getAllProductCategories();
    List<Product> getAllProducts();
    List<Product> getAllProductsWithCategory(String categoryName);


    //update //delete
    Product updateProduct(String productId,ProductUpdateForm productUpdateForm) throws NotFoundException, ParseException, NotActiveException;
    ProductCategory updateProductCategory(String name, ProductCategoryUpdateForm productCategoryUpdateForm) throws DuplicateMemberException, ParseException, NotFoundException;

    //Delete


}
