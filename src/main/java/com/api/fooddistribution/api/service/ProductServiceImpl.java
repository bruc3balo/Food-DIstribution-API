package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models.*;
import com.api.fooddistribution.api.model.ProductCategoryUpdateForm;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.ProductUpdateForm;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalRepositories.productCategoryRepo;
import static com.api.fooddistribution.global.GlobalRepositories.productRepo;
import static com.api.fooddistribution.utils.DataOps.*;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Override
    public Product saveNewProduct(ProductCreationFrom productCreationFrom) throws NotFoundException, ParseException, NotActiveException {

        Optional<ProductCategory> productCategory = findCategoryByName(productCreationFrom.getProductCategoryName());
        if (productCategory.isEmpty()) {
            throw new NotFoundException("Product Category not found");
        }

        if (productCategory.get().getDeleted() || productCategory.get().getDisabled()) {
            throw new NotActiveException(productCategory.get().getName().concat(" is not active"));
        }

        Product newProduct = new Product(generateProductID(productCreationFrom.getProductName()), productCreationFrom.getProductName(), productCategory.get(), new BigDecimal(productCreationFrom.getProductPrice()), productCreationFrom.getImage(), getNowFormattedFullDate().toString(), getNowFormattedFullDate().toString(), false, false, productCreationFrom.getUnit(), productCreationFrom.getProductDescription(),0.0,productCreationFrom.getUsername());


        return productRepo.save(newProduct);
    }

    @Override
    public Product updateProduct(ProductUpdateForm productUpdateForm) throws NotFoundException, ParseException, NotActiveException {

        Optional<Product> oldProduct = findProductById(productUpdateForm.getId());

        if (oldProduct.isEmpty()) {
            throw new NotFoundException("Product not found");
        }

        Product newProduct = oldProduct.get();

        if (productUpdateForm.getProductName() != null) {
            newProduct.setName(productUpdateForm.getProductName());
        }

        if (productUpdateForm.getProductPrice() != null) {
            newProduct.setPrice(new BigDecimal(productUpdateForm.getProductPrice()));
        }

        if (productUpdateForm.getProductCategoryName() != null) {
            Optional<ProductCategory> productCategory = findCategoryByName(productUpdateForm.getProductCategoryName());
            if (productCategory.isEmpty()) {
                throw new NotFoundException("Product category not found");
            }

            if (productCategory.get().getDeleted() || productCategory.get().getDisabled()) {
                throw new NotActiveException(productCategory.get().getName().concat(" is not active"));
            }

            newProduct.setProduct_category(productCategory.get());
        }

        if (productUpdateForm.getImage() != null) {
            newProduct.setImage(productUpdateForm.getImage());
        }

        if (productUpdateForm.getUnit() != null) {
            newProduct.setUnit(productUpdateForm.getUnit());
        }

        if (productUpdateForm.getProductDescription() != null) {
            newProduct.setProduct_description(productUpdateForm.getProductDescription());
        }

        if (productUpdateForm.getDeleted() != null) {
            newProduct.setDeleted(productUpdateForm.getDeleted());
        }

        if (productUpdateForm.getDisabled() != null) {
            newProduct.setDisabled(productUpdateForm.getDisabled());
        }

        if (productUpdateForm.getUnitsLeft() != null) {
            newProduct.setUnitsLeft(productUpdateForm.getUnitsLeft());
        }

        newProduct.setUpdatedAt(getNowFormattedFullDate().toString());

        return productRepo.save(newProduct);
    }

    @Override
    public Optional<Product> findProductById(String id) {
        return productRepo.get(id);
    }

    @Override
    public ProductCategory saveNewProductCategory(String name) throws DuplicateMemberException, ParseException {

        Optional<ProductCategory> existing = findCategoryByName(name);

        if (existing.isPresent()) {
            throw new DuplicateMemberException("Product category exists");
        }

        return productCategoryRepo.save(new ProductCategory(generateProductCategoryID(name),name,false,false,getNowFormattedFullDate().toString(),getNowFormattedFullDate().toString()));
    }

    @Override
    public ProductCategory updateProductCategory(String name, ProductCategoryUpdateForm productCategoryUpdateForm) throws NotFoundException, ParseException {

        Optional<ProductCategory> existing = findCategoryByName(name);

        if (existing.isEmpty()) {
            throw new NotFoundException("Product category doesn't exists");
        }

        ProductCategory newCategory = existing.get();

        if (productCategoryUpdateForm.getDeleted() != null) {
            newCategory.setDeleted(productCategoryUpdateForm.getDeleted());
        }
        if (productCategoryUpdateForm.getDisabled() != null) {
            newCategory.setDisabled(productCategoryUpdateForm.getDisabled());
        }
        if (productCategoryUpdateForm.getName() != null) {
            newCategory.setName(productCategoryUpdateForm.getName());
        }

        newCategory.setUpdatedAt(getNowFormattedFullDate().toString());

        return productCategoryRepo.save(newCategory);
    }

    @Override
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepo.retrieveAll();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.retrieveAll();
    }

    @Override
    public List<Product> getAllSellerProducts(String username) {
        return productRepo.retrieveAll().stream().filter(u-> u.getSellerId().equals(username)).collect(Collectors.toList());
    }

    @Override
    public List<Product> getAllProductsWithCategory(String categoryName) {
        return getAllProducts().stream().filter(f-> f.getProduct_category().getName().equals(categoryName)).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductCategory> findCategoryByName(String name) {
        return productCategoryRepo.retrieveAll().stream().filter(c->c.getName().equals(name)).findFirst();
    }

    @Override
    public Optional<ProductCategory> findCategoryById(String id) {
        return productCategoryRepo.get(id);
    }

}
