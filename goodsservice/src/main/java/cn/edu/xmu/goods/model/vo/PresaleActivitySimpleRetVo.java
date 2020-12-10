package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.PresaleActivity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "预售活动简单视图对象")
public class PresaleActivitySimpleRetVo {
    @ApiModelProperty(value = "预售活动id")
    private Long id;

    @ApiModelProperty(value = "预售活动名称")
    private String name;


    public PresaleActivitySimpleRetVo(PresaleActivity presaleActivity) {
        this.id = presaleActivity.getId();
        this.name = presaleActivity.getName();
    }
}
