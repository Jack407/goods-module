package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "规格简单视图对象")
public class GoodsSkuSimpleRetVo {
    @ApiModelProperty(value = "规格id")
    private Long id;

    @ApiModelProperty(value = "规格名称")
    private String name;


    public GoodsSkuSimpleRetVo(GoodsSku bo) {
        this.id = bo.getId();
        this.name = bo.getName();
    }
}
