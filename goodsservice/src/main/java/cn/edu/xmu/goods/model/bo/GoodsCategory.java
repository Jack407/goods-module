package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;
import cn.edu.xmu.goods.model.vo.GoodsCategoryVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
public class GoodsCategory {

    private Long id;
    private String name;
    private Long pid;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public GoodsCategory(GoodsCategoryVo vo)
    {
        this.name=vo.getName();
    }
}
