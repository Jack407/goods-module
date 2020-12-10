package cn.edu.xmu.goods.model.bo;



import cn.edu.xmu.goods.model.po.BrandPo;
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
public class Brand implements VoObject{

    private Long id;
    private String name;
    private String detail;
    private String imageUrl;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public Brand(BrandVo vo)
    {
        this.name=vo.getName();
        this.detail=vo.getDetail();
    }

    public Brand(BrandPo po)
    {
        this.id=po.getId();
        this.name=po.getName();
        this.detail=po.getDetail();
        this.imageUrl=po.getImageUrl();
        this.gmtCreated=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }

    @Override
    public Object createVo() {
        return new BrandRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new BrandSimpleRetVo(this);
    }


}
