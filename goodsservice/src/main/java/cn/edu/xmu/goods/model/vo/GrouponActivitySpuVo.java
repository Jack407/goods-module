package cn.edu.xmu.goods.model.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GrouponActivitySpuVo {

    private Long id;
    private String name;
    private String goodsSn;
    private String imageUrl;
    private String gmtCreate;
    private String gmtModified;
    private Byte disable;

    public GrouponActivitySpuVo(){}
}
