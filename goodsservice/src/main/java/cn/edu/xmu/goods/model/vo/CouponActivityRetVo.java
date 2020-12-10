package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.CouponActivity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@ApiModel(description = "优惠活动视图对象")
public class CouponActivityRetVo {

    @ApiModelProperty(value = "优惠活动id")
    private Long id;
    @ApiModelProperty(value = "优惠活动名称")
    private String name;
    @ApiModelProperty(value = "优惠活动开始时间")
    private String beginTime;
    @ApiModelProperty(value = "优惠活动结束时间")
    private String endTime;
    @ApiModelProperty(value = "优惠活动发放优惠券时间")
    private String couponTime;
    @ApiModelProperty(value = "优惠活动所属商店id")
    private Long shopId;
    @ApiModelProperty(value = "优惠活动发放优惠券数量")
    private Integer quantity;
    @ApiModelProperty(value = "优惠活动发放时间类型")
    private Byte validTerm;
    @ApiModelProperty(value = "优惠活动图片url")
    private String imageUrl;
    @ApiModelProperty(value = "优惠活动策略")
    private String strategy;
    @ApiModelProperty(value = "优惠活动创建者")
    private Long createdBy;
    @ApiModelProperty(value = "优惠活动上次修改者")
    private Long modifyBy;
    @ApiModelProperty(value = "优惠活动创建时间")
    private String gmtCreated;
    @ApiModelProperty(value = "优惠活动上次修改时间")
    private String gmtModified;
    @ApiModelProperty(value = "优惠活动数量类型")
    private Byte quantityType;
    @ApiModelProperty(value = "优惠活动状态")
    private Byte state;

    public CouponActivityRetVo(CouponActivity couponActivity) {
        this.id = couponActivity.getId();
        this.name = couponActivity.getName();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.beginTime = df.format(couponActivity.getBeginTime());
        this.endTime = df.format(couponActivity.getEndTime());
        this.couponTime = df.format(couponActivity.getCouponTime());
        this.shopId = couponActivity.getShopId();
        this.quantity = couponActivity.getQuantity();
        this.validTerm =couponActivity.getValidTerm();
        this.imageUrl = couponActivity.getImageUrl();
        this.strategy = couponActivity.getStrategy();
        this.createdBy = couponActivity.getCreatedBy();
        this.modifyBy = couponActivity.getModifyBy();
        this.gmtCreated = df.format(couponActivity.getGmtCreated());
        this.gmtModified =df.format(couponActivity.getGmtModified());
        this.quantityType = couponActivity.getQuantityType();
        this.state = couponActivity.getState().getCode();
    }




}
