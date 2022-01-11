package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.ProductCategoryUpdateForm;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.ProductUpdateForm;
import com.api.fooddistribution.global.GlobalVariables;
import com.api.fooddistribution.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalService.productService;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.*;

@RestController
@RequestMapping(value = "/product")
@Slf4j
public class ProductController {

    @PostMapping(value = {"/new"})
    @PreAuthorize("hasAuthority('product:write')")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductCreationFrom form) {
        try {

            Models.Product savedProduct = productService.saveNewProduct(form);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), savedProduct != null ? savedProduct.getName() + " saved" : "Product not saved", getTransactionId(PRODUCT_COLLECTION), getProductModelFromProduct(savedProduct));
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to save product with name " + form.getProductName(), getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"category/new"})
    @PreAuthorize("hasAuthority('product:write')")
    public ResponseEntity<?> addProductCategory(@RequestParam(name = NAME) String name) {
        try {

            Models.ProductCategory savedProductCategory = productService.saveNewProductCategory(name);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), savedProductCategory != null ? savedProductCategory.getName() + " saved" : "Product category not saved", getTransactionId(PRODUCT_COLLECTION), savedProductCategory);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to save product category with name " + name, getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/all"})
    @PreAuthorize("hasAuthority('product:read')")
    public ResponseEntity<?> getProductList() {
        try {
            List<Models.Product> productList = productService.getAllProducts();
            List<Models.ProductModel> productModelList = new ArrayList<>();
            productList.forEach(i-> productModelList.add(getProductModelFromProduct(i)));

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), !productList.isEmpty() ? productList.size() + "products found" : "products Not found", getTransactionId(PRODUCT_COLLECTION), productModelList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get products", getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/allSeller"})
    @PreAuthorize("hasAuthority('product:read')")
    public ResponseEntity<?> getSellerProductList(HttpServletRequest request, @RequestParam(GlobalVariables.USERNAME) String username) {
        try {


            ResponseEntity<?> unknownResponse = checkUnknownParameters(request, USERNAME);
            if (unknownResponse != null) return unknownResponse;

            List<Models.ProductModel> productModelList = productService.getAllSellerProducts(username).stream().map(DataOps::getProductModelFromProduct).collect(Collectors.toList());


            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), !productModelList.isEmpty() ? productModelList.size() + "products found" : "products Not found", getTransactionId(PRODUCT_COLLECTION), productModelList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get products", getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/category"})
    @PreAuthorize("hasAuthority('product:read')")
    public ResponseEntity<?> getProductListWithCategory(HttpServletRequest request, @RequestParam(name = PRODUCT_CATEGORY_NAME) String name) {
        try {

            ResponseEntity<?> unknownResponse = checkUnknownParameters(request, PRODUCT_CATEGORY_NAME);
            if (unknownResponse != null) return unknownResponse;

            List<Models.ProductModel> productList = productService.getAllProductsWithCategory(name).stream().map(DataOps::getProductModelFromProduct).collect(Collectors.toList());
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), !productList.isEmpty() ? productList.size() + "products found" : "products Not found", getTransactionId(PRODUCT_COLLECTION), productList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get products", getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/category/all"})
    @PreAuthorize("hasAuthority('product_category:read')")
    public ResponseEntity<?> getAllProductCategoryList() {
        try {
            List<Models.ProductCategory> productCategoryList = productService.getAllProductCategories();

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), !productCategoryList.isEmpty() ? productCategoryList.size() + "products categories found" : "product categories not found", getTransactionId(PRODUCT_COLLECTION), productCategoryList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get product categories", getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/specific"})
    @PreAuthorize("hasAuthority('product:read')")
    public ResponseEntity<?> getSpecificProduct(HttpServletRequest request, @RequestParam(name = ID) String productId) {
        try {

            ResponseEntity<?> unknownResponse = checkUnknownParameters(request, ID);
            if (unknownResponse != null) return unknownResponse;

            Models.Product product = productService.findProductById(productId).orElse(null);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), product != null ? product + "products found" : "products Not found", getTransactionId(PRODUCT_COLLECTION), getProductModelFromProduct(product));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get product with " + productId, getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"category/specific"})
    @PreAuthorize("hasAuthority('product_category:read')")
    public ResponseEntity<?> getProductCategory(HttpServletRequest request, @RequestParam(name = ID, required = false) String categoryId, @RequestParam(name = PRODUCT_CATEGORY_NAME, required = false) String name) {
        Models.ProductCategory productCategory;
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList(PRODUCT_CATEGORY_NAME, ID));
            ResponseEntity<?> unknownResponse = unknownParameterList(unknownParams);
            if (unknownResponse != null) return unknownResponse;

            if (categoryId != null) {
                productCategory = productService.findCategoryById(categoryId).orElse(null);

                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "Product category found", getTransactionId(PRODUCT_CATEGORY_COLLECTION), productCategory);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (name != null) {
                productCategory = productService.findCategoryByName(name).orElse(null);

                JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), "Product category found", getTransactionId(PRODUCT_CATEGORY_COLLECTION), productCategory);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "No parameters provided", getTransactionId(PRODUCT_CATEGORY_COLLECTION));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get product category", getTransactionId(PRODUCT_CATEGORY_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/update"})
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductUpdateForm productUpdateForm) {
        try {

            Models.Product updatedProduct = productService.updateProduct(productUpdateForm);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), updatedProduct != null ? updatedProduct + "updated product " + updatedProduct.getName() : "update product not found", getTransactionId(PRODUCT_COLLECTION), updatedProduct);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to update product ", getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/category/update"})
    @PreAuthorize("hasAuthority('product_category:update')")
    public ResponseEntity<?> updateProductCategory(HttpServletRequest request, @RequestParam(name = PRODUCT_CATEGORY_NAME) String name, @RequestBody ProductCategoryUpdateForm productCategoryUpdateForm) {
        try {

            ResponseEntity<?> unknownResponse = checkUnknownParameters(request, PRODUCT_CATEGORY_NAME);
            if (unknownResponse != null) return unknownResponse;

            Models.ProductCategory updatedProductCategory = productService.updateProductCategory(name, productCategoryUpdateForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), updatedProductCategory != null ? updatedProductCategory + "updated product category " + updatedProductCategory.getName() : "update product category not found", getTransactionId(PRODUCT_COLLECTION), updatedProductCategory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to update product category with name " + name, getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
