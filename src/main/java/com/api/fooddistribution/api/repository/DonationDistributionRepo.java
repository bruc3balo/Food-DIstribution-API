package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.DISTRIBUTION_COLLECTION;
import static com.api.fooddistribution.global.GlobalVariables.DONATION_DISTRIBUTION_COLLECTION;


@Repository
public class DonationDistributionRepo extends AbstractFirestoreRepository<Models.DonationDistribution> {
    protected DonationDistributionRepo(Firestore firestore) {
        super(firestore, DONATION_DISTRIBUTION_COLLECTION);
    }
}
