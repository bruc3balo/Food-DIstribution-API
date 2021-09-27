package com.api.fooddistribution.api.specification;

import com.api.fooddistribution.api.domain.Models;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Set;

public class ProductPredicate implements Specification<Models.Product> {


    private Models.Product product;
    private String productName;
    private Long id;
    private Integer productCategoryId;
    private Set<String> sellerNames, buyerNames;

    public ProductPredicate(Models.Product product) {
        this.product = product;
    }

    public ProductPredicate(Long id) {
        this.id = id;
    }

    public ProductPredicate(String productName, Integer productCategoryId) {
        this.productName = productName;
        this.productCategoryId = productCategoryId;
    }

    public ProductPredicate(Set<String> sellerNames, Set<String> buyerNames) {
        this.sellerNames = sellerNames;
        this.buyerNames = buyerNames;
    }

    @Override
    public Predicate toPredicate(Root<Models.Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate p = cb.and();

        if (product != null) {

            if (product.getId() != null) {
                p.getExpressions().add(cb.equal(root.get("id"), product.getId()));
            }

            if (product.getName() != null) {
                p.getExpressions().add(cb.equal(cb.upper(root.get("name")), product.getName().toUpperCase()));
            }


            if (product.getDeleted() != null) {
                p.getExpressions().add(cb.equal(root.get("deleted"), product.getDeleted()));
            }

            if (product.getDisabled() != null) {
                p.getExpressions().add(cb.equal(root.get("disabled"), product.getDisabled()));
            }


        }

        if (id != null) {
            p.getExpressions().add(cb.equal(root.get("id"), id));
        }


        if (productName != null) {
            p.getExpressions().add(cb.equal(cb.upper(root.get("name")),productName.toUpperCase()));
        }

        if (productCategoryId != null) {
            p.getExpressions().add(cb.equal(root.get("id"), productCategoryId));
        }

        if (sellerNames != null) {
            if (!sellerNames.isEmpty()) {
                for (String name : sellerNames) {
                    p.getExpressions().add(cb.equal(cb.upper(root.get("username")), name.toUpperCase()));
                }
            }
        }

        if (buyerNames != null) {
            if (!buyerNames.isEmpty()) {
                for (String name : buyerNames) {
                    p.getExpressions().add(cb.equal(cb.upper(root.get("username")), name.toUpperCase()));
                }
            }
        }


        return p;
    }
}
