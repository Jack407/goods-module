package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.po.CouponPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@ApiModel(description = "优惠券视图对象")
public class CouponRetVo {

    @ApiModelProperty(value = "优惠券id")
    private Long id;
    @ApiModelProperty(value = "优惠券编号")
    private String couponSn;
    @ApiModelProperty(value = "优惠券名称")
    private String name;
    @ApiModelProperty(value = "优惠券所有者")
    private Long customerId;
    @ApiModelProperty(value = "优惠券对应活动id")
    private Long activityId;
    @ApiModelProperty(value = "优惠券开始时间")
    private String beginTime;
    @ApiModelProperty(value = "优惠券结束时间")
    private String endTime;
    @ApiModelProperty(value = "优惠券创建时间")
    private String gmtCreated;
    @ApiModelProperty(value = "优惠修改时间")
    private String gmtModified;
    @ApiModelProperty(value = "优惠券状态")
    private Byte state;

    public CouponRetVo(Coupon coupon)
    {
        this.id=coupon.getId();
        this.couponSn=coupon.getCouponSn();
        this.activityId=coupon.getActivityId();
        this.name=coupon.getName();
        this.customerId=coupon.getCustomerId();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.beginTime=df.format(coupon.getBeginTime());
        this.endTime=df.format(coupon.getEndTime());
        this.gmtCreated=df.format(coupon.getGmtCreated());
        this.gmtModified=df.format(coupon.getGmtModified());
        this.state=coupon.getState().getCode();
    }

    public CouponRetVo(CouponPo coupon)
    {
        this.id=coupon.getId();
        this.couponSn=coupon.getCouponSn();
        this.activityId=coupon.getActivityId();
        this.name=coupon.getName();
        this.customerId=coupon.getCustomerId();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.beginTime=df.format(coupon.getBeginTime());
        this.endTime=df.format(coupon.getEndTime());
        this.gmtCreated=df.format(coupon.getGmtCreate());
        this.gmtModified=df.format(coupon.getGmtModified());
        this.state=coupon.getState();
    }
}
