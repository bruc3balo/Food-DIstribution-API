package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.PRODUCT_CATEGORY_COLLECTION;


@Repository
public class ProductCategoryRepo extends AbstractFirestoreRepository<Models.ProductCategory> {
    protected ProductCategoryRepo(Firestore firestore) {
        super(firestore,PRODUCT_CATEGORY_COLLECTION);
    }
}
