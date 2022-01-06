package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.PURCHASE_COLLECTION;
import static com.api.fooddistribution.global.GlobalVariables.USER_COLLECTION;


@Repository
public class PurchaseRepo extends AbstractFirestoreRepository<Models.Purchase> {

    protected PurchaseRepo(Firestore firestore) {
        super(firestore,PURCHASE_COLLECTION);
    }


}
