package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Brand;
import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.po.CommentPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "品牌视图对象")
public class BrandRetVo {

    @ApiModelProperty(value = "品牌id")
    private Long id;
    @ApiModelProperty(value = "品牌名称")
    private String name;
    @ApiModelProperty(value = "品牌描述")
    private String detail;
    @ApiModelProperty(value = "品牌图片链接")
    private String imageUrl;
    @ApiModelProperty(value = "品牌创建时间")
    private LocalDateTime gmtCreated;
    @ApiModelProperty(value = "品牌修改时间")
    private LocalDateTime gmtModified;

    public BrandRetVo(Brand bo)
    {
        this.id=bo.getId();
        this.name=bo.getName();
        this.detail=bo.getDetail();
        this.imageUrl=bo.getImageUrl();
        this.gmtCreated=bo.getGmtCreated();
        this.gmtModified=bo.getGmtModified();
    }

}
