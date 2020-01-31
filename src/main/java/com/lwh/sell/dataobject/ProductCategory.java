package com.lwh.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 类目
 * Created by 廖师兄
 * 2017-05-07 14:30
 *
 * @Entity 注解将数据库表字段映射成对象
 * @Id 表明是数据库主键
 * @GeneratedValue 表明是自增字段
 * @DynamicUpdate 自动更新字段,见com.lwh.sell.repository.ProductCategoryRepositoryTest#updateTest()
 */
@Entity
@DynamicUpdate
@Data
public class ProductCategory {

    /**
     * 类目id.
     */
    @Id
    @GeneratedValue
    private Integer categoryId;

    /**
     * 类目名字.
     */
    private String categoryName;

    /**
     * 类目编号.
     */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
