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
@ApiModel("优惠活动传值对象")
public class CouponActivityVo {

    @ApiModelProperty(name = "优惠活动名称", value = "name", required = true)
    private String name;

    @ApiModelProperty(name = "优惠券发放数量", value = "quantity", required = true)
    private Integer quantity;

    @ApiModelProperty(name = "数量类型", value = "quantityType", required = true)
    private Byte quantityType;

    @ApiModelProperty(name = "优惠券有效期", value = "validTerm", required = true)
    private Byte validTerm;

    @ApiModelProperty(name = "优惠券发放时间", value = "couponTime", required = true)
    private String couponTime;

    @ApiModelProperty(name = "优惠活动开始时间", value = "beginTime", required = true)
    private String beginTime;

    @ApiModelProperty(name = "优惠活动结束时间", value = "endTime", required = true)
    private String endTime;

    @ApiModelProperty(name = "优惠活动策略", value = "strategy", required = true)
    private String strategy;
}
