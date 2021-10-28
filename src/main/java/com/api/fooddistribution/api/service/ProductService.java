package com.api.fooddistribution.api.service;


import com.api.fooddistribution.api.domain.Models.Product;
import com.api.fooddistribution.api.domain.Models.ProductCategory;
import com.api.fooddistribution.api.model.ProductCategoryUpdateFor;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.ProductUpdateForm;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    //Name
    Product saveNewProduct(ProductCreationFrom productCreationFrom) throws NotFoundException, ParseException;
    Product updateProduct(ProductUpdateForm productUpdateForm) throws NotFoundException, ParseException;
    Optional<Product> findProductById(String id);


    //Category
    ProductCategory saveNewProductCategory(String name) throws DuplicateMemberException, ParseException;
    ProductCategory updateProductCategory(String name, ProductCategoryUpdateFor productCategoryUpdateForm) throws DuplicateMemberException, ParseException, NotFoundException;
    List<ProductCategory> getAllProductCategories();
    Optional<ProductCategory> findCategoryByName(String name);
    Optional<ProductCategory>  findCategoryById(String id);

}
