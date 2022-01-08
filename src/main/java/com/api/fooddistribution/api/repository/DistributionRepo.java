package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.DISTRIBUTION_COLLECTION;


@Repository
public class DistributionRepo extends AbstractFirestoreRepository<Models.Distribution> {
    protected DistributionRepo(Firestore firestore) {
        super(firestore, DISTRIBUTION_COLLECTION);
    }
}
