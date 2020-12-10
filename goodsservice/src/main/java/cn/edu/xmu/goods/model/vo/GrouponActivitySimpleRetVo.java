package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GrouponActivity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "团购活动简单视图对象")
public class GrouponActivitySimpleRetVo {
    @ApiModelProperty(value = "团购活动id")
    private Long id;

    @ApiModelProperty(value = "团购活动名称")
    private String name;


    public GrouponActivitySimpleRetVo(GrouponActivity grouponActivity) {
        this.id = grouponActivity.getId();
        this.name = grouponActivity.getName();
    }
}