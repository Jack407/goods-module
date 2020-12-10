package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Brand;
import cn.edu.xmu.goods.model.bo.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "品牌简单视图对象")
public class BrandSimpleRetVo {

    @ApiModelProperty(value = "品牌id")
    private Long id;

    @ApiModelProperty(value = "品牌名称")
    private String name;


    public BrandSimpleRetVo(Brand brand) {
        this.id = brand.getId();
        this.name =brand.getName();
    }
}
