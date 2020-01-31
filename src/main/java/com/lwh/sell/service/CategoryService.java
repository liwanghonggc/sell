package com.lwh.sell.service;

import com.lwh.sell.dataobject.ProductCategory;

import java.util.List;

/**
 * @author lwh
 * @date 2020-01-31
 * @desp
 */
public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
