package com.api.fooddistribution.api.repository;


import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.ROLE_COLLECTION;


@Repository
public class AppRoleRepo extends AbstractFirestoreRepository<Models.AppRole> {

    protected AppRoleRepo(Firestore firestore) {
        super(firestore, ROLE_COLLECTION);
    }

}
