package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;
import cn.edu.xmu.goods.model.vo.FlashSaleItemVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
public class FlashSaleItem {

    private Long id;
    private Long saleId;
    private Long goodsSkuId;
    private Long price;
    private Integer quantity;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public FlashSaleItem(FlashSaleItemVo vo)
    {
        this.goodsSkuId=vo.getSkuId();
        this.quantity=vo.getQuantity();
        this.price=vo.getPrice();
    }
}
