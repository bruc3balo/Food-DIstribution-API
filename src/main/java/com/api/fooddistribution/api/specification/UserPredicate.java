package com.api.fooddistribution.api.specification;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.domain.Models.AppUser;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class UserPredicate implements Specification<AppUser> {

    private AppUser appUser;
    private Long id;
    private String name;
    private String username;


    public UserPredicate(AppUser appUser) {
        this.appUser = appUser;
    }

    public UserPredicate(String username) {
        this.username = username;
    }

    public UserPredicate(Long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }


    @Override
    public Predicate toPredicate(Root<AppUser> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.and();

        if (appUser != null) {
            if (appUser.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), appUser.getId()));
            }

            if (appUser.getUsername() != null) {
                p.getExpressions().add(cb.equal(cb.upper(root.get("username")), appUser.getUsername().toUpperCase()));
            }

            if (appUser.getName() != null) {
                p.getExpressions().add(cb.equal(cb.upper(root.get("name")), appUser.getName().toUpperCase()));
            }

        }

        if (id != null) {
            p.getExpressions().add(cb.equal(root.get("id"),id));
        }

        if (username != null) {
            p.getExpressions().add(cb.equal(cb.upper(root.get("username")), username.toUpperCase()));
        }

        if (name != null) {
            p.getExpressions().add(cb.equal(cb.upper(root.get("name")), name.toUpperCase()));
        }

        return p;
    }
}
