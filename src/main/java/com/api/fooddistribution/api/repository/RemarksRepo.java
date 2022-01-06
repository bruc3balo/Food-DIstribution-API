package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.PURCHASE_COLLECTION;


@Repository
public class RemarksRepo extends AbstractFirestoreRepository<Models.Remarks> {

    protected RemarksRepo(Firestore firestore) {
        super(firestore,PURCHASE_COLLECTION);
    }


}
