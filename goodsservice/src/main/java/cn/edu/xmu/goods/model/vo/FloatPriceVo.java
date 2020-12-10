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
@ApiModel("价格浮动传值对象")
public class FloatPriceVo {

    @ApiModelProperty(name = "浮动价格", value = "activityPrice", required = true)
    private Long activityPrice;

    @ApiModelProperty(name = "开始时间", value = "beginTime", required = true)
    private String beginTime;

    @ApiModelProperty(name = "结束时间", value = "endTime", required = true)
    private String endTime;

    @ApiModelProperty(name = "可销售的数量", value = "quantity", required = true)
    private Integer quantity;
}
