package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "优惠券简单视图对象")
public class CouponSimpleRetVo {
    @ApiModelProperty(value = "优惠券id")
    private Long id;

    @ApiModelProperty(value = "优惠券名称")
    private String name;


    public CouponSimpleRetVo(Coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
    }
}
