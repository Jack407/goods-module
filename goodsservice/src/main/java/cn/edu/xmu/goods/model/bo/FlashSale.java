package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;
import cn.edu.xmu.goods.model.vo.FlashSaleVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
public class FlashSale {

    private Long id;
    private LocalDateTime flashDate;
    private Long timeSegId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public FlashSale(FlashSaleVo vo)
    {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.flashDate = LocalDateTime.parse(vo.getFlash_date(),df);
    }
}
