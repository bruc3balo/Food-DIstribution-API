package com.api.fooddistribution.api.specification;

import com.api.fooddistribution.api.domain.Models;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PermissionPredicate implements Specification<Models.Permissions> {

    Models.Permissions permissions;
    String name;

    public PermissionPredicate(Models.Permissions permissions) {
        this.permissions = permissions;
    }

    public PermissionPredicate(String name) {
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<Models.Permissions> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate p = cb.and();

        if (permissions != null) {
            if (permissions.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), permissions.getId()));
            }


            if (permissions.getName() != null) {
                p.getExpressions().add(cb.equal(cb.upper(root.get("name")), permissions.getName().toUpperCase()));
            }

        }

        if (name != null) {
            p.getExpressions().add(cb.equal(cb.upper(root.get("name")), name.toUpperCase()));
        }
        return p;
    }
}
