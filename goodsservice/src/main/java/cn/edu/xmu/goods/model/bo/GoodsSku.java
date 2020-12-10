package cn.edu.xmu.goods.model.bo;

import java.time.LocalDateTime;

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
public class GoodsSku implements VoObject{

    private Long id;
    private Long goodsSpuId;
    private String skuSn;
    private String name;
    private Long originalPrice;
    private String configuration;
    private Long weight;
    private String imageUrl;
    private Integer inventory;
    private String detail;
    private Byte disabled;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public enum State {
        OFFSHELVES((byte)0, "未上架"),
        ONSHELVES((byte)4, "上架"),
        DELETE((byte)6, "已删除");



        private Byte code;
        private String description;

        State(Byte code, String description) {
            this.code = code;
            this.description = description;
        }


        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public void setCode(Byte code)
        {
            this.code=code;
        }
    }
    private State state;

    public GoodsSku(GoodsSkuVo vo)
    {
        this.skuSn=vo.getSn();
        this.name=vo.getName();
        this.configuration=vo.getConfiguration();
        this.originalPrice=vo.getOriginalPrice();
        this.weight=vo.getWeight();
        this.inventory=vo.getInventory();
        this.imageUrl=vo.getImageUrl();
        this.detail=vo.getDetail();
    }

    public GoodsSku(GoodsSkuPo po)
    {
        this.id=po.getId();
        this.goodsSpuId=po.getGoodsSpuId();
        this.skuSn=po.getSkuSn();
        this.name=po.getName();
        this.originalPrice=po.getOriginalPrice();
        this.configuration=po.getConfiguration();
        this.weight=po.getWeight();
        this.imageUrl=po.getImageUrl();
        this.inventory=po.getInventory();
        this.detail=po.getDetail();
        this.disabled=po.getDisabled();
        this.gmtCreated=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }

    @Override
    public Object createVo() {
        return new GoodsSkuRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new GoodsSkuSimpleRetVo(this);
    }
}
