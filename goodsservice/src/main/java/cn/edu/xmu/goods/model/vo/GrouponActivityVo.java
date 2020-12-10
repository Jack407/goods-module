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
@ApiModel("团购活动传值对象")
public class GrouponActivityVo {

    @ApiModelProperty(name = "团购策略", value = "strategy", required = true)
    private String strategy;

    @ApiModelProperty(name = "团购活动开始时间", value = "beginTime", required = true)
    private String beginTime;

    @ApiModelProperty(name = "团购活动结束时间", value = "endTime", required = true)
    private String endTime;
}
