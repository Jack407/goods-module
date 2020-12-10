package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.FlashSaleSku;
import cn.edu.xmu.goods.model.bo.GoodsSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "秒杀项简单视图对象")
public class FlashSaleSkuSimpleRetVo {

    @ApiModelProperty(value = "秒杀项id")
    private Long id;

    @ApiModelProperty(value = "规格名称")
    private String name;


    public FlashSaleSkuSimpleRetVo(FlashSaleSku bo) {
        this.id = bo.getId();
        this.name = bo.getSkuName();
    }
}
