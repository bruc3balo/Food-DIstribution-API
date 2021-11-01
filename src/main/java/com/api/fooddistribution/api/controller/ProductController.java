package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.ProductCategoryUpdateForm;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.ProductUpdateForm;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalService.productService;
import static com.api.fooddistribution.global.GlobalVariables.*;
import static com.api.fooddistribution.utils.DataOps.filterRequestParams;
import static com.api.fooddistribution.utils.DataOps.getTransactionId;

@RestController
@RequestMapping(value = "/product")
@Slf4j
public class ProductController {

    @PostMapping(value = {"/new"})
    @PreAuthorize("hasAuthority('product:write')")
    public ResponseEntity<?> addProduct(@RequestBody ProductCreationFrom form) {
        try {

            Models.Product savedProduct = productService.saveNewProduct(form);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), savedProduct != null ? savedProduct.getName() + " saved" : "Product not saved", getTransactionId(PRODUCT_COLLECTION), savedProduct);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to save product with name " + form.getProductName(), getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"category/new"})
    @PreAuthorize("hasAuthority('product:write')")
    public ResponseEntity<?> addProductCategory(@RequestBody String name) {
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

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), !productList.isEmpty() ? productList.size() + "products found" : "products Not found", getTransactionId(PRODUCT_COLLECTION), productList);
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

            List<String> unknownParams = filterRequestParams(request, List.of(PRODUCT_CATEGORY_NAME));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<Models.Product> productList = productService.getAllProductsWithCategory(name);
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

    @PostMapping(value = {"/specific"})
    @PreAuthorize("hasAuthority('product:read')")
    public ResponseEntity<?> getSpecificProduct(HttpServletRequest request, @RequestParam(name = ID) String productId) {
        try {

            List<String> unknownParams = filterRequestParams(request, List.of(ID));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.Product product = productService.findProductById(productId).orElse(null);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), product != null ? product + "products found" : "products Not found", getTransactionId(PRODUCT_COLLECTION), product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to get product with " + productId, getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"category/specific"})
    @PreAuthorize("hasAuthority('product_category:read')")
    public ResponseEntity<?> getProductCategory(HttpServletRequest request, @RequestParam(name = ID, required = false) String categoryId, @RequestParam(name = PRODUCT_CATEGORY_NAME, required = false) String name) {
        Models.ProductCategory productCategory;
        try {

            List<String> unknownParams = filterRequestParams(request, Arrays.asList(PRODUCT_CATEGORY_NAME,ID));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

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

    @GetMapping(value = {"/update"})
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<?> updateProduct(HttpServletRequest request, @RequestParam(name = ID) String categoryId, @RequestBody ProductUpdateForm productUpdateForm) {
        try {

            List<String> unknownParams = filterRequestParams(request, List.of(ID));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.Product updatedProduct = productService.updateProduct(categoryId, productUpdateForm);

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), updatedProduct != null ? updatedProduct + "updated product "+updatedProduct.getName() : "update product not found", getTransactionId(PRODUCT_COLLECTION), updatedProduct);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to update product with id " + categoryId, getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/category/update"})
    @PreAuthorize("hasAuthority('product_category:update')")
    public ResponseEntity<?> updateProductCategory(HttpServletRequest request, @RequestParam(name = PRODUCT_CATEGORY_NAME) String name , @RequestBody ProductCategoryUpdateForm productCategoryUpdateForm) {
        try {

            List<String> unknownParams = filterRequestParams(request, List.of(PRODUCT_CATEGORY_NAME));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.ProductCategory updatedProductCategory = productService.updateProductCategory(name, productCategoryUpdateForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), updatedProductCategory != null ? updatedProductCategory + "updated product category "+updatedProductCategory.getName() : "update product category not found", getTransactionId(PRODUCT_COLLECTION), updatedProductCategory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), "Failed to update product category with name " + name, getTransactionId(PRODUCT_COLLECTION));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
