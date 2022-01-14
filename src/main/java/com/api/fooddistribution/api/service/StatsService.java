package com.api.fooddistribution.api.service;

import com.api.fooddistribution.api.model.SellerStats;

import java.util.List;

public interface StatsService {
    List<SellerStats> getSellerStats(Integer year,String sellername);
    List<SellerStats> getAllSellerStats(Integer year);

}
