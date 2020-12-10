package cn.edu.xmu.goods.model.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.po.ShopPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BrandCreateVo {

    private Long id;
    private String name;
    private String detail;
    private String imageUrl;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public BrandCreateVo(BrandPo po)
    {
        this.id=po.getId();
        this.name=po.getName();
        this.detail=po.getDetail();
        this.imageUrl=po.getImageUrl();
        //this.gmtCreated=po.getGmtCreate();
        //this.gmtModified=po.getGmtModified();
    }
}
