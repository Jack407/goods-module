package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.CommentPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@ApiModel(description = "评论视图对象")
public class CommentRetVo {

    @ApiModelProperty(value = "评论id")
    private Long id;
    @ApiModelProperty(value = "评论者")
    private Long customerId;
    @ApiModelProperty(value = "评论的规格")
    private Long goodsSkuId;
    @ApiModelProperty(value = "评论的订单id")
    private Long orderItemId;
    @ApiModelProperty(value = "评论类型")
    private Byte type;
    @ApiModelProperty(value = "评论内容")
    private String content;
    @ApiModelProperty(value = "评论创建时间")
    private String gmtCreated;
    @ApiModelProperty(value = "评论修改时间")
    private String gmtModified;
    @ApiModelProperty(value = "评论状态")
    private Byte state;

    public CommentRetVo(Comment bo)
    {
        this.id=bo.getId();
        this.customerId=bo.getCustomerId();
        this.content=bo.getContent();
        this.goodsSkuId=bo.getGoodsSkuId();
        this.orderItemId=bo.getOrderItemId();
        this.type=bo.getType();
        this.state=bo.getState().getCode();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.gmtCreated=df.format(bo.getGmtCreated());
        this.gmtModified=df.format(bo.getGmtModified());
    }

}
