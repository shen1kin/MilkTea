package com.example.smartstudent.repository;

import com.example.smartstudent.model.ProductInfo;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private static List<ProductInfo> allProducts = new ArrayList<>();

    public static void setProducts(List<ProductInfo> products) {
        allProducts.clear();
        allProducts.addAll(products);
    }

    public static List<ProductInfo> getProducts() {
        return new ArrayList<>(allProducts);
    }

    public static ProductInfo getById(int id) {
        for (ProductInfo product : allProducts) {
            if (product.getId() == id) return product;
        }
        return null;
    }
}
