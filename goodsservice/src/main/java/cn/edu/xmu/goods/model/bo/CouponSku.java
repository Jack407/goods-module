package cn.edu.xmu.goods.model.bo;

import lombok.Data;
import cn.edu.xmu.goods.model.vo.CouponSkuVo;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponSku {

    private List<Long> skuIdList;

    public CouponSku(CouponSkuVo vo)
    {
        this.skuIdList=vo.getSkuIdList();
    }
}
