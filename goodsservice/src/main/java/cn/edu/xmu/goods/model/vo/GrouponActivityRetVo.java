package cn.edu.xmu.goods.model.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GrouponActivityRetVo {

    @ApiModelProperty(value = "团购活动id")
    private Long id;
    @ApiModelProperty(value = "团购活动名称")
    private String name;
    @ApiModelProperty(value = "团购活动开始时间")
    private String beginTime;
    @ApiModelProperty(value = "团购活动结束时间")
    private String endTime;

    public GrouponActivityRetVo(GrouponActivity bo)
    {
        this.id=bo.getId();
        this.name=bo.getName();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.beginTime=df.format(bo.getBeginTime());
        this.endTime=df.format(bo.getEndTime());
    }

    public GrouponActivityRetVo(GrouponActivityPo po)
    {
        this.id=po.getId();
        this.name=po.getName();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.beginTime=df.format(po.getBeginTime());
        this.endTime=df.format(po.getEndTime());
    }
}
