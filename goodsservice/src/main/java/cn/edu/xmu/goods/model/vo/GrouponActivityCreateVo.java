package cn.edu.xmu.goods.model.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.po.ShopPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GrouponActivityCreateVo {

    private Long id;
    private String name;
    private GrouponActivitySpuVo goodsSpu;
    private GrouponActivityShopVo shop;
    private String strategy;
    private String beginTime;
    private String endTime;

    public GrouponActivityCreateVo(GrouponActivityPo po1, GoodsSpuPo po2, ShopPo po3)
    {
        this.id=po1.getId();
        this.name=po1.getName();
        this.goodsSpu=new GrouponActivitySpuVo();
        this.goodsSpu.setId(po2.getId());
        this.goodsSpu.setName(po2.getName());
        this.goodsSpu.setGoodsSn(po2.getGoodsSn());
        this.goodsSpu.setImageUrl(po2.getImageUrl());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.goodsSpu.setGmtCreate(df.format(po2.getGmtCreate()));
        this.goodsSpu.setGmtModified(df.format(po2.getGmtModified()));
        this.goodsSpu.setDisable(po2.getDisabled());
        this.shop=new GrouponActivityShopVo();
        this.shop.setId(po3.getId());
        this.shop.setName(po3.getName());
        this.strategy=po1.getStrategy();
        this.beginTime=df.format(po1.getBeginTime());
        this.endTime=df.format(po1.getEndTime());
    }


}
