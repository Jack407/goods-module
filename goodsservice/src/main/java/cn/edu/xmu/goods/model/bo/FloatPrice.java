package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;
import cn.edu.xmu.goods.model.vo.FloatPriceVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
public class FloatPrice {

    private Long id;
    private Long goodsSkuId;
    private Long activityPrice;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer quantity;
    private Long createdBy;
    private Long invalidBy;
    private Byte valid;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public FloatPrice(FloatPriceVo vo)
    {
        this.activityPrice=vo.getActivityPrice();
        this.quantity=vo.getQuantity();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.beginTime = LocalDateTime.parse(vo.getBeginTime(), df);
        this.endTime = LocalDateTime.parse(vo.getEndTime(), df);
    }

    public boolean isBiggerBegin(){
        LocalDateTime nowBeginDate = this.beginTime;
        LocalDateTime nowEndDate = this.endTime;
        return nowEndDate.isAfter(nowBeginDate);
    }


    public boolean beginAfterNow()
    {
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime BeginDate = this.beginTime;
        return BeginDate.isAfter(now);
    }
}
