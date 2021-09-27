package com.api.fooddistribution.api.service;


import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.ProductUpdateForm;
import com.api.fooddistribution.api.specification.ProductPredicate;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import static com.api.fooddistribution.global.GlobalRepositories.productCategoryRepo;
import static com.api.fooddistribution.global.GlobalRepositories.productRepo;
import static com.api.fooddistribution.global.GlobalService.passwordEncoder;
import static com.api.fooddistribution.utils.DataOps.getNowFormattedDate;
import static com.api.fooddistribution.utils.DataOps.getNowFormattedFullDate;

@Service
@Transactional
@Slf4j
public class DataServiceImpl implements DataService {

    //products
    @Override
    public Models.Product saveNewProduct(ProductCreationFrom productCreationFrom) throws ParseException, NotFoundException {

        if (getProduct(productCreationFrom.getProductName()) != null) {
            throw new DuplicateRequestException("Product already exists with name " + productCreationFrom.getProductName());
        }

        Models.Product product = new Models.Product(productCreationFrom.getProductName(), new BigDecimal(productCreationFrom.getProductPrice()), productCreationFrom.getImage(), getNowFormattedFullDate(), getNowFormattedDate(), false, false);

        if (productCreationFrom.getProductCategoryName() != null) {
            Models.ProductCategory productCategory = getProductCategory(productCreationFrom.getProductCategoryName());
            if (productCategory == null) {
                throw new NotFoundException("Cannot find product category with name " + productCreationFrom.getProductCategoryName());
            } else {
                product.setProductCategory(productCategory);
            }
        }


        Models.Product newProduct = productRepo.save(product);
        log.info("Product {} saved", newProduct.getName());

        return newProduct;
    }

    @Override
    public Models.Product updateProduct(String productName, ProductUpdateForm updateForm) throws ParseException {

        Models.Product product = getProduct(productName);


        if (product == null) {
            throw new UsernameNotFoundException(productName + ", not found");
        }

        if (updateForm != null) {

            if (updateForm.getProductName() != null) {
                if (getProduct(updateForm.getProductName()) != null) {
                    throw new DuplicateRequestException("product exists with " + updateForm.getProductName());
                }
            }

            if (updateForm.getProductCategoryName() != null) {
                product.setName(updateForm.getProductName());
            }

            if (updateForm.getImage() != null) {
                product.setImage(updateForm.getImage());
            }

            if (updateForm.getProductPrice() != null) {
                product.setPrice(new BigDecimal(updateForm.getProductPrice()));
            }


            product.setUpdatedAt(getNowFormattedFullDate());

        }


        product.setUpdatedAt(getNowFormattedFullDate());
        return productRepo.save(product);
    }

    @Override
    public Models.Product disableProduct(Models.Product product) {
        product.setDisabled(true);
        return productRepo.save(product);
    }

    @Override
    public Models.Product enableProduct(Models.Product product) {
        product.setDisabled(false);
        return productRepo.save(product);
    }

    @Override
    public Models.Product deleteProduct(Models.Product product) {
        product.setDeleted(true);
        return productRepo.save(product);
    }

    @Override
    public Models.Product getProduct(String productName) {
        return productRepo.findByName(productName).orElse(null);
    }

    @Override
    public Page<Models.Product> getAllProducts(Specification<Models.Product> specification, PageRequest pageRequest) {
        return productRepo.findAll(specification, pageRequest);
    }

    //product category
    @Override
    public Models.ProductCategory saveNewProductCategory(String categoryName) throws ParseException {

        if (getProductCategory(categoryName) != null) {
            throw new DuplicateRequestException("Product category already exists with name " + categoryName);
        }

        Models.ProductCategory newProductCategory = productCategoryRepo.save(new Models.ProductCategory(categoryName, false, false, getNowFormattedFullDate(), getNowFormattedFullDate()));
        log.info("saving new product category {} ", newProductCategory.getName());
        return newProductCategory;
    }

    @Override
    public Models.ProductCategory updateProductCategory(String productCategoryName, String productCategoryNewName) throws ParseException, NotFoundException {
        Models.ProductCategory productCategory = getProductCategory(productCategoryName);

        if (productCategory == null) {
            throw new NotFoundException("Category not found");
        }

        if (productCategory.getName().equals(productCategoryNewName)) {
            throw new DuplicateRequestException("Category exists with that name");
        }

        productCategory.setName(productCategoryNewName);
        productCategory.setUpdatedAt(getNowFormattedFullDate());
        return productCategoryRepo.save(productCategory);
    }

    @Override
    public Models.ProductCategory disableProductCategory(Models.ProductCategory productCategory) {
        productCategory.setDisabled(true);
        return productCategoryRepo.save(productCategory);
    }

    @Override
    public Models.ProductCategory enableProductCategory(Models.ProductCategory productCategory) {
        productCategory.setDisabled(false);
        return productCategoryRepo.save(productCategory);
    }

    @Override
    public Models.ProductCategory deleteProductCategory(Models.ProductCategory productCategory) {
        productCategory.setDeleted(true);
        return productCategoryRepo.save(productCategory);
    }

    @Override
    public Models.ProductCategory getProductCategory(String productName) {
        return productCategoryRepo.findByName(productName).orElse(null);
    }

    @Override
    public Page<Models.ProductCategory> getAllProductCategories(Specification<Models.ProductCategory> specification, PageRequest pageRequest) {
        return productCategoryRepo.findAll(specification, pageRequest);
    }

    //product category

    @Override
    public Models.Product addProductToCategory(String productName, String productCategoryName) throws NotFoundException {

        Models.Product product = getProduct(productName);
        Models.ProductCategory productCategory = getProductCategory(productCategoryName);

        if (product == null) {
            throw new NotFoundException("Product not found " + productName);
        }

        if (productCategory == null) {
            throw new NotFoundException("Product category not found " + productCategoryName);
        }

        if (product.getProductCategory() != null) {

            if (product.getProductCategory().getName().equals(productCategory.getName())) {
                throw new DuplicateRequestException("Product " + product.getName() + "is already in category " + productCategory.getName());
            }

        } else { //role doesn't exists
            log.info("Adding product {} to category {}", product.getName(), productCategory.getName()); //will save because @Transactional
        }

        product.setProductCategory(productCategory);

        return product;
    }

}
