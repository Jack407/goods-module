package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.po.CouponActivityPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "优惠活动简单视图对象")
public class CouponActivitySimpleRetVo {
    @ApiModelProperty(value = "优惠活动id")
    private Long id;

    @ApiModelProperty(value = "优惠活动名称")
    private String name;


    public CouponActivitySimpleRetVo(CouponActivity couponActivity) {
        this.id = couponActivity.getId();
        this.name = couponActivity.getName();
    }

    public CouponActivitySimpleRetVo(CouponActivityPo couponActivity) {
        this.id = couponActivity.getId();
        this.name = couponActivity.getName();
    }
}

