package com.api.fooddistribution.utils;

import com.api.fooddistribution.api.domain.Models;
import com.api.fooddistribution.api.model.DonorItem;
import com.api.fooddistribution.api.model.ProductCountModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.fooddistribution.global.GlobalRepositories.*;
import static com.api.fooddistribution.global.GlobalService.productService;
import static com.api.fooddistribution.global.GlobalService.userService;
import static com.api.fooddistribution.global.GlobalVariables.HY;

public class DataOps {

    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss a";
    public static final DateTimeFormatter LDT_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter LD_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);


    public static List<String> filterRequestParams(HttpServletRequest request, List<String> knownParams) {
        Enumeration<String> query = request.getParameterNames();
        List<String> list = Collections.list(query);
        list.removeAll(knownParams);
        return list;
    }

    public static ResponseEntity<?> checkUnknownParameters(HttpServletRequest request, String accessToken) {
        List<String> unknownParams = filterRequestParams(request, List.of(accessToken));
        return unknownParameterList(unknownParams);
    }

    public static ResponseEntity<?> unknownParameterList(List<String> unknownParams) {
        if (!unknownParams.isEmpty()) {
            // get all errors
            String apiDesc = unknownParams.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.joining(", ")) + " : Not valid Parameters";
            JsonResponse response = JsonSetErrorResponse.setResponse(ApiCode.FAILED.getCode(), apiDesc, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public static Date getNowFormattedDate() throws ParseException {
        return ConvertDate.formatDate(formatLocalDate(LocalDate.now()), DATE_PATTERN);
    }

    public static Date getNowFormattedDate(LocalDate date) throws ParseException {
        return ConvertDate.formatDate(formatLocalDate(date), DATE_PATTERN);
    }

    public static Date getNowFormattedFullDate() throws ParseException {
        return ConvertDate.formatDate(formatLocalDateTime(LocalDateTime.now()), TIMESTAMP_PATTERN);
    }

    public static Date getSpecificFormattedFullDate(LocalDateTime dateTime) throws ParseException {
        return ConvertDate.formatDate(formatLocalDateTime(dateTime), TIMESTAMP_PATTERN);
    }

    public static String formatLocalDate(LocalDate ld) {
        return LD_FORMATTER.format(ld);
    }

    public static String formatLocalDateTime(LocalDateTime ldt) {
        return LDT_FORMATTER.format(ldt);
    }


    public static SimpleGrantedAuthority getGrantedAuthorityRole(String role) {
        return new SimpleGrantedAuthority(role);
    }

    public static boolean isNumeric(String text) {
        return StringUtils.isNumeric(text);
    }


    public static String exactStringValue(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    public static BigDecimal strToBigDecimal(String value) {
        try {
            return (value == null || value.trim().isEmpty() ? new BigDecimal("0") : ((new BigDecimal(value.trim()))));
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getTransactionId(String type) {
        return UUID.randomUUID().toString().concat(HY).concat(type);
    }


    public static String generateRoleID(String name) {
        return new KeyGenerator(4).nextString().concat(HY).concat(name).concat(HY).concat("RL");
    }

    public static String generatePermissionID(String name) {
        return new KeyGenerator(4).nextString().concat(HY).concat(name).concat(HY).concat("PM");
    }

    public static String generateProductID(String name) {
        return new KeyGenerator(6).nextString().concat(HY).concat(name).concat(HY).concat("P");
    }

    public static String generateProductCategoryID(String name) {
        return new KeyGenerator(6).nextString().concat(HY).concat(name).concat(HY).concat("PC");
    }

    public static String generateCartID(String name) {
        return new KeyGenerator(6).nextString().concat(HY).concat(name).concat(HY).concat("CT");
    }

    public static Models.DistributionModel getDistributionModelFromDistribution(Models.Distribution distribution) {

        if (distribution == null) {
            return null;
        }

        Optional<Models.AppUser> transporterOpt = userService.findByUsername(distribution.getTransporter());
        return new Models.DistributionModel(distribution.getDocumentId(), distribution.getId(), distribution.getTransporter() != null ? transporterOpt.orElse(new Models.AppUser(distribution.getTransporter())) : null, distribution.getBeneficiary() != null ? userService.findByUsername(distribution.getBeneficiary()).orElse(new Models.AppUser(distribution.getBeneficiary())) : null, distribution.getStatus(), distribution.getCreatedAt(), distribution.getUpdatedAt(), distribution.getCompletedAt(), getPurchaseModelFromPurchase(purchaseRepo.get(String.valueOf(distribution.getPurchasesId())).orElse(new Models.Purchase(distribution.getPurchasesId()))), distribution.getTransporter() != null && transporterOpt.isPresent() ? transporterOpt.get().getLastKnownLocation() : null, distribution.getDeleted(), distribution.getPaid(), distribution.getReported(), distribution.getRemarks() != null ? remarksRepo.get(String.valueOf(distribution.getRemarks())).orElse(new Models.Remarks(distribution.getRemarks())) : null, distribution.getProductStatus());
    }


    public static Models.DonationDistributionModel getDonorDistributionModelFromDistributionDonor(Models.DonationDistribution distribution) {

        if (distribution == null) {
            return null;
        }

        Optional<Models.AppUser> transporterOpt = userService.findByUsername(distribution.getTransporter());
        Optional<Models.AppUser> donorOpt = userService.findByUsername(distribution.getDonor());
        Optional<Models.AppUser> beneficiaryOpt = userService.findByUsername(distribution.getBeneficiary());

        return new Models.DonationDistributionModel(distribution.getId(), transporterOpt.orElse(new Models.AppUser(distribution.getTransporter())), beneficiaryOpt.orElse(new Models.AppUser(distribution.getBeneficiary())), donorOpt.orElse(new Models.AppUser(distribution.getDonor())),distribution.getStatus(), distribution.getCreatedAt(), distribution.getUpdatedAt(), distribution.getCompletedAt(), getDonationModelFromDonation(donationRepo.get(String.valueOf(distribution.getDonationId())).orElse(new Models.Donation(distribution.getDonationId()))), distribution.getTransporter() != null && transporterOpt.isPresent() ? transporterOpt.get().getLastKnownLocation() : null, distribution.getDeleted(), distribution.getReported(), distribution.getRemarks() != null ? remarksRepo.get(String.valueOf(distribution.getRemarks())).orElse(new Models.Remarks(distribution.getRemarks())) : null);
    }

    public static Models.DonationModel getDonationModelFromDonation (Models.Donation donation) {

        Optional<Models.AppUser> donorOpt = userService.findByUsername(donation.getDonorUsername());
        Optional<Models.AppUser> beneOpt = userService.findByUsername(donation.getBeneficiaryUsername());

        return new Models.DonationModel(donation.getId(),donorOpt.orElse(new Models.AppUser(donation.getDonorUsername())) ,beneOpt.orElse(new Models.AppUser(donation.getBeneficiaryUsername())),donation.getCreatedAt(),donation.getDeliveryLocation(),donation.getDeliveryAddress(),donation.getCollectionLocation(),donation.getCollectionAddress(),donation.isDeleted(),donation.isComplete(),donation.getAssigned(), donation.getProducts());
    }

    public static Models.ProductModel getProductModelFromProduct(Models.Product product){
        if (product == null) {
            return new Models.ProductModel();
        }
        return new Models.ProductModel(product.getId(), product.getName(), productService.findCategoryById(product.getProduct_category_id()).orElse(null), product.getPrice(), product.getImage(), product.getSellerId(),product.getUnitsLeft(), product.getCreatedAt(), product.getUpdatedAt(),product.getDeleted(), product.getDisabled(), product.getUnit(), product.getProduct_description(),product.getLocation());
    }

    public static Models.PurchaseModel getPurchaseModelFromPurchase (Models.Purchase purchase) {
        LinkedHashSet<ProductCountModel> products = new LinkedHashSet<>();
        purchase.getProducts().forEach((pid,items)-> {
            Optional<Models.Product> optionalProduct = productService.findProductById(pid);
            optionalProduct.ifPresent(p-> products.add(new ProductCountModel(p,items)));
        });
        return new Models.PurchaseModel(purchase.getId(),purchase.getBuyerId(),purchase.getLocation(),purchase.getAddress(),purchase.getCreatedAt(),products,purchase.isDeleted(),purchase.isComplete(),purchase.getAssigned());
    }

    public static Integer strToInteger(String value) {
        try {
            return (value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim()));
        } catch (Exception ex) {
            return null;
        }
    }


}
