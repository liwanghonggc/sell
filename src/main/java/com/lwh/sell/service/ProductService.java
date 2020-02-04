package com.lwh.sell.service;

import com.lwh.sell.dataobject.ProductInfo;
import com.lwh.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author lwh
 * @date 2020-01-31
 * @desp
 */
public interface ProductService {

    ProductInfo findOne(String productId);

    /**
     * 查询所有在架的商品列表
     * @return
     */
    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    /**
     * 加减库存
     */
    void increaseStock(List<CartDTO> cartDTOList);

    void decreaseStock(List<CartDTO> cartDTOList);


    //上架
    ProductInfo onSale(String productId);

    //下架
    ProductInfo offSale(String productId);
}
