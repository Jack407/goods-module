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
@ApiModel("预售活动传值对象")
public class PresaleActivityVo {

    @ApiModelProperty(name = "预售活动名称", value = "name", required = true)
    private String name;

    @ApiModelProperty(name = "定金", value = "advancePayPrice", required = true)
    private Long advancePayPrice;

    @ApiModelProperty(name = "尾款", value = "restPayPrice", required = true)
    private Long restPayPrice;

    @ApiModelProperty(name = "预售活动货量", value = "quantity", required = true)
    private Integer quantity;

    @ApiModelProperty(name = "支付尾款时间", value = "payTime", required = true)
    private String payTime;

    @ApiModelProperty(name = "优惠活动开始时间", value = "beginTime", required = true)
    private String beginTime;

    @ApiModelProperty(name = "优惠活动结束时间", value = "endTime", required = true)
    private String endTime;
}
