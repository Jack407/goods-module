package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GoodsSpu;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "商品视图对象")
public class GoodsSpuRetVo {

    @ApiModelProperty(value = "商品id")
    private Long id;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "商品品牌")
    private Long brandId;
    @ApiModelProperty(value = "商品分类")
    private Long categoryId;
    @ApiModelProperty(value = "商品运费模板")
    private Long freightId;
    @ApiModelProperty(value = "商品商店")
    private Long shopId;
    @ApiModelProperty(value = "商品编号")
    private String goodsSn;
    @ApiModelProperty(value = "商品详述")
    private String detail;
    @ApiModelProperty(value = "商品图片")
    private String imageUrl;
    @ApiModelProperty(value = "商品规格")
    private String spec;
    @ApiModelProperty(value = "商品可见")
    private Byte disabled;
    @ApiModelProperty(value = "商品创建时间")
    private LocalDateTime gmtCreated;
    @ApiModelProperty(value = "商品修改时间")
    private LocalDateTime gmtModified;

    public GoodsSpuRetVo(GoodsSpu bo)
    {
        this.id=bo.getId();
        this.name=bo.getName();
        this.brandId=bo.getBrandId();
        this.categoryId=bo.getCategoryId();
        this.freightId=bo.getFreightId();
        this.shopId=bo.getShopId();
        this.goodsSn=bo.getGoodsSn();
        this.detail=bo.getDetail();
        this.imageUrl=bo.getImageUrl();
        this.spec=bo.getSpec();
        this.disabled=bo.getDisabled();
        this.gmtCreated=bo.getGmtCreated();
        this.gmtModified=bo.getGmtModified();
    }
}
