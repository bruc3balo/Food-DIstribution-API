package com.api.fooddistribution.api.specification;

import com.api.fooddistribution.api.domain.Models;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProductCategoryPredicate implements Specification<Models.ProductCategory> {

    private Models.ProductCategory productCategory;
    private String name;
    private Boolean deleted,disabled;

    public ProductCategoryPredicate(Models.ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public ProductCategoryPredicate(Boolean deleted, Boolean disabled) {
        this.deleted = deleted;
        this.disabled = disabled;
    }

    public ProductCategoryPredicate(String name) {
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<Models.ProductCategory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate p = cb.and();

        if (productCategory != null) {

            if (productCategory.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), productCategory.getId()));
            }

            if (productCategory.getName() != null) {
                p.getExpressions().add(cb.equal(cb.upper(root.get("name")), productCategory.getName().toUpperCase()));
            }


        }

        if (name != null) {
            p.getExpressions().add(cb.equal(cb.upper(root.get("name")), name.toUpperCase()));
        }

        if (deleted != null) {
            p.getExpressions().add(cb.equal(cb.upper(root.get("deleted")), deleted));
        }

        if (disabled != null) {
            p.getExpressions().add(cb.equal(cb.upper(root.get("disabled")), disabled));
        }

        return p;
    }
}
