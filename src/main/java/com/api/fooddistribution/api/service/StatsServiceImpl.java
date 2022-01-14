package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.SellerStats;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalRepositories.productRepo;
import static com.api.fooddistribution.global.GlobalRepositories.purchaseRepo;

@Service
public class StatsServiceImpl implements StatsService {

    @Override
    public List<SellerStats> getSellerStats(Integer year, String sellerName) {
        List<SellerStats> sellerStatsList = new ArrayList<>();
        List<Models.Purchase> allPurchases = purchaseRepo.retrieveAll();
        List<Models.Product> allProducts = productRepo.retrieveAll().stream().filter(product -> product.getSellerId().equals(sellerName)).collect(Collectors.toList());

        //add all months
        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyProducts = new LinkedHashMap<>();
            allProducts.forEach(product -> emptyProducts.put(product.getId(), 0));
            sellerStatsList.add(new SellerStats(i, emptyProducts));
        }

        allPurchases.stream().filter(Models.Purchase::isComplete).forEach(purchase -> {
            Date purchaseDate = purchase.getCreatedAt();
            Calendar purchaseCalendar = Calendar.getInstance();
            purchaseCalendar.setTime(purchaseDate);


            sellerStatsList.stream().filter(i -> (i.getMonth().equals(purchaseCalendar.get(Calendar.MONTH))) && (purchaseCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> {
                purchase.getProducts().forEach(sellerStats::addProductToMonth);
            });
        });


        return sellerStatsList;
    }

    @Override
    public List<SellerStats> getAllSellerStats(Integer year) {
        List<SellerStats> sellerStatsList = new ArrayList<>();
        List<Models.Purchase> allPurchases = purchaseRepo.retrieveAll();
        List<Models.Product> allProducts = productRepo.retrieveAll();

        //add all months
        for (int i = 0; i < 12; i++) {
            final LinkedHashMap<String, Integer> emptyProducts = new LinkedHashMap<>();
            allProducts.forEach(product -> emptyProducts.put(product.getId(), 0));
            sellerStatsList.add(new SellerStats(i, emptyProducts));
        }

        allPurchases.stream().filter(Models.Purchase::isComplete).forEach(purchase -> {
            Date purchaseDate = purchase.getCreatedAt();
            Calendar purchaseCalendar = Calendar.getInstance();
            purchaseCalendar.setTime(purchaseDate);


            sellerStatsList.stream().filter(i -> (i.getMonth().equals(purchaseCalendar.get(Calendar.MONTH))) && (purchaseCalendar.get(Calendar.YEAR) == year)).findFirst().ifPresent(sellerStats -> {
                purchase.getProducts().forEach(sellerStats::addProductToMonth);
            });
        });

        return sellerStatsList;
    }
}
