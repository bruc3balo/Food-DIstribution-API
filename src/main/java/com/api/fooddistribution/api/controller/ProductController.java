package com.api.fooddistribution.api.controller;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.ProductCreationFrom;
import com.api.fooddistribution.api.model.ProductUpdateForm;
import com.api.fooddistribution.api.specification.ProductCategoryPredicate;
import com.api.fooddistribution.api.specification.ProductPredicate;
import com.api.fooddistribution.utils.ApiCode;
import com.api.fooddistribution.utils.JsonResponse;
import com.api.fooddistribution.utils.JsonSetErrorResponse;
import com.api.fooddistribution.utils.JsonSetSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalService.dataService;
import static com.api.fooddistribution.utils.DataOps.filterRequestParams;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {


    @GetMapping("/product/all")
    @PreAuthorize("hasAuthority('product:read')")
    public ResponseEntity<?> getAllProducts(HttpServletRequest request,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                            @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                            @RequestParam(value = "id", required = false) Long id,
                                            @RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "is_deleted", required = false) Boolean deleted,
                                            @RequestParam(value = "is_disabled", required = false) Boolean disabled) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id", "is_deleted", "is_disabled","pageSize","pageNo"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pageSze = pageSize != null ? pageSize : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? pageNo : 0;

            Page<Models.Product> productsList = dataService.getAllProducts(new ProductPredicate(new Models.Product(id, name, deleted, disabled)), PageRequest.of(pageNumber, pageSze)); //todo add sort

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, productsList.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('product:write')")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody ProductCreationFrom productCreationFrom) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/product/save").toUriString());
        log.info("uri saveproduct ::: {}", uri);

        try {
            Models.Product product = dataService.saveNewProduct(productCreationFrom);
            JsonResponse response = JsonSetSuccessResponse.setResponse(product != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), product != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "update/product/{id}")
    @PreAuthorize("hasAuthority('product:update')")
    public ResponseEntity<?> updateProduct(HttpServletRequest request,
                                           @RequestParam(value = "product_name") String productName,
                                           @RequestBody ProductUpdateForm updateForm) {

        try {

            List<String> unknownParams = filterRequestParams(request, List.of("product_name"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.Product updateProduct = dataService.updateProduct(productName, updateForm);
            JsonResponse response = JsonSetSuccessResponse.setResponse(updateProduct != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updateProduct != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updateProduct);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping(value = "delete/product")
    @PreAuthorize("hasAuthority('product:delete')")
    public ResponseEntity<?> deleteProduct(HttpServletRequest request,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<Models.Product> productToBeDeleted = dataService.getAllProducts(new ProductPredicate(new Models.Product(id, name)), PageRequest.of(0, 1)).getContent();

            if (productToBeDeleted.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Models.Product deleteProduct = dataService.deleteProduct(productToBeDeleted.get(0));


            JsonResponse response = JsonSetSuccessResponse.setResponse(deleteProduct != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), deleteProduct != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, deleteProduct);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "disable/product")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> disableProduct(HttpServletRequest request,
                                            @RequestParam(value = "disabled") Boolean disabled,
                                            @RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id", "disabled"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<Models.Product> productToBeDeleted = dataService.getAllProducts(new ProductPredicate(new Models.Product(id, name)), PageRequest.of(0, 1)).getContent();

            if (productToBeDeleted.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Models.Product disableProduct;

            if (disabled) {
                disableProduct = dataService.disableProduct(productToBeDeleted.get(0));
            } else {
                disableProduct = dataService.enableProduct(productToBeDeleted.get(0));
            }


            JsonResponse response = JsonSetSuccessResponse.setResponse(disableProduct != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), disableProduct != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, disableProduct);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/productCategory/all")
    @PreAuthorize("hasAuthority('product_category:read')")
    public ResponseEntity<?> getAllProductCategories(HttpServletRequest request,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                     @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                     @RequestParam(value = "id", required = false) Long id,
                                                     @RequestParam(value = "name", required = false) String name,
                                                     @RequestParam(value = "is_deleted", required = false) Boolean deleted,
                                                     @RequestParam(value = "is_disabled", required = false) Boolean disabled) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id", "is_deleted", "is_disabled","pageNo","pageSize"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pageSze = pageSize != null ? pageSize : Integer.MAX_VALUE;
            int pageNumber = pageNo != null ? pageNo : 0;

            Page<Models.ProductCategory> productCategoryPage = dataService.getAllProductCategories(new ProductCategoryPredicate(new Models.ProductCategory(id, name, deleted, disabled)), PageRequest.of(pageNumber, pageSze)); //todo add sort

            JsonResponse response = JsonSetSuccessResponse.setResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getDescription(), null, productCategoryPage.getContent());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/save/productCategory")
    @PreAuthorize("hasAuthority('product_category:write')")
    public ResponseEntity<?> saveProductCategory(HttpServletRequest request, @RequestParam(name = "name") String productName) {


        try {

            List<String> unknownParams = filterRequestParams(request, List.of("name"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.ProductCategory productCategory = dataService.saveNewProductCategory(productName);
            JsonResponse response = JsonSetSuccessResponse.setResponse(productCategory != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), productCategory != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, productCategory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "update/productCategory/{id}")
    @PreAuthorize("hasAuthority('product_category:update')")
    public ResponseEntity<?> updateProductCategory(HttpServletRequest request,
                                                   @RequestParam(value = "product_category_name_old") String productCategoryName,
                                                   @RequestParam(value = "product_category_name_new") String productCategoryNewName) {

        try {

            List<String> unknownParams = filterRequestParams(request, List.of("product_category_name_old","product_category_name_new"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Models.ProductCategory updateProductCategory = dataService.updateProductCategory(productCategoryName,productCategoryNewName);
            JsonResponse response = JsonSetSuccessResponse.setResponse(updateProductCategory != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), updateProductCategory != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, updateProductCategory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "delete/productCategory")
    @PreAuthorize("hasAuthority('product_category:delete')")
    public ResponseEntity<?> deleteProductCategory(HttpServletRequest request,
                                                   @RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<Models.ProductCategory> productCategoryToBeDeleted = dataService.getAllProductCategories(new ProductCategoryPredicate(new Models.ProductCategory(id, name)), PageRequest.of(0, 1)).getContent();

            if (productCategoryToBeDeleted.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Models.ProductCategory deleteProduct = dataService.deleteProductCategory(productCategoryToBeDeleted.get(0));


            JsonResponse response = JsonSetSuccessResponse.setResponse(deleteProduct != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), deleteProduct != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, deleteProduct);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "disable/productCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> disableProductCategory(HttpServletRequest request,
                                                    @RequestParam(value = "disabled") Boolean disabled,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "id", required = false) Long id) {

        try {
            List<String> unknownParams = filterRequestParams(request, Arrays.asList("name", "id","disabled"));
            if (!unknownParams.isEmpty()) {
                // get all errors
                String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<Models.ProductCategory> productCategoryToBeDeleted = dataService.getAllProductCategories(new ProductCategoryPredicate(new Models.ProductCategory(id, name)), PageRequest.of(0, 1)).getContent();

            if (productCategoryToBeDeleted.isEmpty()) {
                JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Models.ProductCategory disableProductCategory ;

            if (disabled) {
                disableProductCategory = dataService.disableProductCategory(productCategoryToBeDeleted.get(0));
            } else {
                disableProductCategory = dataService.enableProductCategory(productCategoryToBeDeleted.get(0));
            }


            JsonResponse response = JsonSetSuccessResponse.setResponse(disableProductCategory != null ? ApiCode.SUCCESS.getCode() : ApiCode.FAILED.getCode(), disableProductCategory != null ? ApiCode.SUCCESS.getDescription() : ApiCode.FAILED.getDescription(), null, disableProductCategory);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), ApiCode.FAILED.getDescription(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
