package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.PURCHASE_COLLECTION;
import static com.api.fooddistribution.global.GlobalVariables.REMARKS;


@Repository
public class RemarksRepo extends AbstractFirestoreRepository<Models.Remarks> {

    protected RemarksRepo(Firestore firestore) {
        super(firestore,REMARKS);
    }

}
