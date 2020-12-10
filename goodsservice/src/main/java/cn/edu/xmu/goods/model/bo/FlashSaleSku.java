package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.FlashSaleItemPo;
import cn.edu.xmu.goods.model.po.GoodsSkuPo;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
public class FlashSaleSku implements VoObject{

    private Long id;
    private Long skuId;
    private String skuName;
    private String skuSn;
    private String skuImageUrl;
    private Long originalPrice;
    private Integer inventory;
    private Byte disabled;
    private Long price;
    private Integer quantity;

    public FlashSaleSku(FlashSaleItemPo flashSaleItemPo, GoodsSkuPo goodsSkuPo)
    {
        this.id=flashSaleItemPo.getId();
        this.price=flashSaleItemPo.getPrice();
        this.quantity=flashSaleItemPo.getQuantity();
        this.skuId=goodsSkuPo.getId();
        this.skuName=goodsSkuPo.getName();
        this.skuSn=goodsSkuPo.getSkuSn();
        this.skuImageUrl=goodsSkuPo.getImageUrl();
        this.inventory=goodsSkuPo.getInventory();
        this.originalPrice=goodsSkuPo.getOriginalPrice();
        this.disabled=goodsSkuPo.getDisabled();
    }

    @Override
    public Object createVo() {
        return new FlashSaleSkuRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new FlashSaleSkuSimpleRetVo(this);
    }


}
