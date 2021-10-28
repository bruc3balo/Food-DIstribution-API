package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.*;


@Repository
public class ProductRepo extends AbstractFirestoreRepository<Models.Product> {

    protected ProductRepo(Firestore firestore) {
        super(firestore, PRODUCT);
    }

}
