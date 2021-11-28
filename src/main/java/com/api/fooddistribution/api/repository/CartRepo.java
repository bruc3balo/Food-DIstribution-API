package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.CART_COLLECTION;
import static com.api.fooddistribution.global.GlobalVariables.USER_COLLECTION;


@Repository
public class CartRepo extends AbstractFirestoreRepository<Models.Cart> {

    protected CartRepo(Firestore firestore) {
        super(firestore,CART_COLLECTION);
    }

}
