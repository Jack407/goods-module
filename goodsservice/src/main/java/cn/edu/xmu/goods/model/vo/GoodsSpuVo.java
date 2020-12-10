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
@ApiModel("商品传值对象")
public class GoodsSpuVo {

    @ApiModelProperty(name = "商品名称", value = "name", required = true)
    private String name;

    @ApiModelProperty(name = "商品描述", value = "detail", required = true)
    private String detail;

    @ApiModelProperty(name = "商品可选规格", value = "spec", required = true)
    private String spec;
}
