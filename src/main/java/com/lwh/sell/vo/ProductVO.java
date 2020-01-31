package com.lwh.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lwh
 * @date 2020-01-31
 * @desp
 */
@Data
public class ProductVO {

    /**
     * 这边name是具体返回给前端的json字段的名称,可能有多个字段叫name,含义不是很清楚,可以使用@JsonProperty这个注解
     */
    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
