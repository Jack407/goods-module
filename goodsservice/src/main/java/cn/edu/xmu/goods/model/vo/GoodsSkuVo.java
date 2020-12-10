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
@ApiModel("商品规格传值对象")
public class GoodsSkuVo {

    @ApiModelProperty(name = "sku序号", value = "sn", required = true)
    private String sn;

    @ApiModelProperty(name = "商品型号名称", value = "name", required = true)
    private String name;

    @ApiModelProperty(name = "该型号原价", value = "originalPrice", required = true)
    private Long originalPrice;

    @ApiModelProperty(name = "规格配置", value = "configuration", required = true)
    private String configuration;

    @ApiModelProperty(name = "规格重量", value = "weight", required = true)
    private Long weight;

    @ApiModelProperty(name = "规格库存", value = "inventory", required = true)
    private Integer inventory;

    @ApiModelProperty(name = "规格图片链接", value = "imageUrl", required = true)
    private String imageUrl;

    @ApiModelProperty(name = "该规格描述", value = "detail", required = true)
    private String detail;
}
