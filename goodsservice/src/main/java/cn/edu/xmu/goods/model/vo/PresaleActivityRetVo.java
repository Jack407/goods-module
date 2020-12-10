package cn.edu.xmu.goods.model.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PresaleActivityRetVo {

    @ApiModelProperty(value = "预售活动id")
    private Long id;
    @ApiModelProperty(value = "预售活动名称")
    private String name;
    @ApiModelProperty(value = "预售活动开始时间")
    private String beginTime;
    @ApiModelProperty(value = "预售活动支付尾款时间")
    private String payTime;
    @ApiModelProperty(value = "预售活动结束时间")
    private String endTime;
    @ApiModelProperty(value = "预售活动店铺id")
    private Long shopId;
    @ApiModelProperty(value = "预售活动商品id")
    private Long goodsSkuId;
    @ApiModelProperty(value = "预售活动货量")
    private Integer quantity;
    @ApiModelProperty(value = "预售活动订金")
    private Long advancePayPrice;
    @ApiModelProperty(value = "预售活动尾款")
    private Long restPayPrice;
    @ApiModelProperty(value = "预售活动创建时间")
    private String gmtCreated;
    @ApiModelProperty(value = "预售活动修改时间")
    private String gmtModified;
    @ApiModelProperty(value = "预售活动状态")
    private Byte state;

    public PresaleActivityRetVo(PresaleActivity bo)
    {
        this.id=bo.getId();
        this.shopId=bo.getShopId();
        this.goodsSkuId=bo.getGoodsSpuId();
        this.name=bo.getName();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.payTime=df.format(bo.getPayTime());
        this.quantity=bo.getQuantity();
        this.advancePayPrice=bo.getAdvancePayPrice();
        this.restPayPrice=bo.getRestPayPrice();
        this.beginTime=df.format(bo.getBeginTime());
        this.endTime=df.format(bo.getEndTime());
        this.gmtCreated=df.format(bo.getGmtCreated());
        this.gmtModified=df.format(bo.getGmtModified());
        this.state=bo.getState().getCode();
    }

    public PresaleActivityRetVo(PresaleActivityPo bo)
    {
        this.id=bo.getId();
        this.shopId=bo.getShopId();
        this.goodsSkuId=bo.getGoodsSkuId();
        this.name=bo.getName();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.payTime=df.format(bo.getPayTime());
        this.quantity=bo.getQuantity();
        this.advancePayPrice=bo.getAdvancePayPrice();
        this.restPayPrice=bo.getRestPayPrice();
        this.beginTime=df.format(bo.getBeginTime());
        this.endTime=df.format(bo.getEndTime());
        this.gmtCreated=df.format(bo.getGmtCreate());
        this.gmtModified=df.format(bo.getGmtModified());
        this.state=bo.getState();
    }
}
