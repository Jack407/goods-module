package cn.edu.xmu.goods.model.vo;



import cn.edu.xmu.goods.model.bo.GoodsSpu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "商品简单视图对象")
public class GoodsSpuSimpleRetVo {
    @ApiModelProperty(value = "商品id")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String name;


    public GoodsSpuSimpleRetVo(GoodsSpu bo) {
        this.id = bo.getId();
        this.name = bo.getName();
    }
}
