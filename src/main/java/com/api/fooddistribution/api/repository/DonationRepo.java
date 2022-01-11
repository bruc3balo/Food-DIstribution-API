package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.DONATION_COLLECTION;
import static com.api.fooddistribution.global.GlobalVariables.PURCHASE_COLLECTION;


@Repository
public class DonationRepo extends AbstractFirestoreRepository<Models.Donation> {

    protected DonationRepo(Firestore firestore) {
        super(firestore,DONATION_COLLECTION);
    }


}
