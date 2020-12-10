package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.FlashSalePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.format.DateTimeFormatter;

/**
 * 权限传值对象
 *
 * @author Di Han Li
 * @date Created in 2020/11/24 9:08
 * Modified by 24320182203221 李狄翰 at 2020/11/24 8:00
 **/
@Data
@ApiModel("秒杀活动传值对象")
public class FlashSaleRetVo {

    private Long id;
    private String flashDate;

    public FlashSaleRetVo(FlashSalePo po)
    {
        this.id = po.getId();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.flashDate = df.format(po.getFlashDate());
    }

}
