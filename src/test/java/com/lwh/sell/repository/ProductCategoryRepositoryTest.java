package com.lwh.sell.repository;

import com.lwh.sell.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


/**
 * @author lwh
 * @date 2020-01-31
 * @desp
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOneTest(){
        ProductCategory productCategory = productCategoryRepository.findOne(1);
        System.out.println(productCategory);
    }

    @Test
    public void saveTest(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(3);
        productCategoryRepository.save(productCategory);
    }

    @Test
    public void updateTest(){
        ProductCategory productCategory = productCategoryRepository.findOne(2);
        // 这里查出来数据进行修改时并没有修改时间,所以数据库中时间字段不会修改,需要在ProductCategory类中加上注解@DynamicUpdate
        productCategory.setCategoryType(5);
        productCategoryRepository.save(productCategory);
    }

    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list = Arrays.asList(2, 5);
        List<ProductCategory> result = productCategoryRepository.findByCategoryTypeIn(list);
        Assert.assertEquals(2, result.size());
    }

}