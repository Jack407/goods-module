package cn.edu.xmu.goods.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 权限传值对象
 *
 * @author Di Han Li
 * @date Created in 2020/11/24 9:08
 * Modified by 24320182203221 李狄翰 at 2020/11/24 8:00
 **/
@Data
@ApiModel("秒杀活动范围传值对象")
public class FlashSaleItemVo {

    @ApiModelProperty(name = "规格ID", value = "skuId", required = true)
    private Long skuId;

    @ApiModelProperty(name = "秒杀量", value = "quantity", required = true)
    private Integer quantity;

    @ApiModelProperty(name = "秒杀价格", value = "price", required = true)
    private Long price;
}
