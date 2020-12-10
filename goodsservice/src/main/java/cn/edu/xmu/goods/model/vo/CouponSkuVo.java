package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限传值对象
 *
 * @author Di Han Li
 * @date Created in 2020/11/24 9:08
 * Modified by 24320182203221 李狄翰 at 2020/11/24 8:00
 **/
@Data
@ApiModel("优惠活动限定范围传值对象")
public class CouponSkuVo {

    private List<Long> skuIdList;

    public CouponSkuVo()
    {
        this.skuIdList=new ArrayList<>();
    }

}
