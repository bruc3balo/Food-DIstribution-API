package com.api.fooddistribution.api.repository;

import com.api.fooddistribution.api.domain.Models;

import com.api.fooddistribution.utils.AbstractFirestoreRepository;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import static com.api.fooddistribution.global.GlobalVariables.PERMISSION_COLLECTION;

@Repository
public class PermissionsRepo extends AbstractFirestoreRepository<Models.Permissions> {

    protected PermissionsRepo(Firestore firestore) {
        super(firestore, PERMISSION_COLLECTION);
    }

}
