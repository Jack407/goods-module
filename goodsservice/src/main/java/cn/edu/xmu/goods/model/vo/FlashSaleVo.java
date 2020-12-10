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
@ApiModel("秒杀活动传值对象")
public class FlashSaleVo {

    @ApiModelProperty(name = "秒杀活动日期", value = "flash_date", required = true)
    private String flash_date;
}
