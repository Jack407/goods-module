package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.FlashSaleSku;
import cn.edu.xmu.goods.model.bo.GoodsSku;
import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "秒杀活动sku视图对象")
public class FlashSaleSkuRetVo {

    @ApiModelProperty(value = "秒杀项id")
    private Long id;
    @ApiModelProperty(value = "规格id")
    private Long skuId;
    @ApiModelProperty(value = "规格编号")
    private String skuSn;
    @ApiModelProperty(value = "规格名称")
    private String name;
    @ApiModelProperty(value = "规格原价")
    private Long originalPrice;
    @ApiModelProperty(value = "规格图片链接")
    private String imageUrl;
    @ApiModelProperty(value = "规格库存")
    private Integer inventory;
    @ApiModelProperty(value = "规格禁止访问")
    private Byte disabled;
    @ApiModelProperty(value = "秒杀项价格")
    private Long price;
    @ApiModelProperty(value = "秒杀项数量")
    private Integer quantity;

    public FlashSaleSkuRetVo(FlashSaleSku bo)
    {
        this.id=bo.getId();
        this.price=bo.getPrice();
        this.quantity=bo.getQuantity();
        this.skuId=bo.getId();
        this.name=bo.getSkuName();
        this.skuSn=bo.getSkuSn();
        this.imageUrl=bo.getSkuImageUrl();
        this.inventory=bo.getInventory();
        this.originalPrice=bo.getOriginalPrice();
        this.disabled=bo.getDisabled();
    }
}
