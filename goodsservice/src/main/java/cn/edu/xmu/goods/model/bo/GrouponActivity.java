package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.CouponPo;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
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
import java.util.Objects;

@Data
public class GrouponActivity implements VoObject{

    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Long shopId;
    private Long goodsSpuId;
    private String strategy;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public enum State {
        OFFLINE((byte)0, "已下线"),
        ONLINE((byte)1, "已上线"),
        DELETE((byte)2, "已删除");

        private Byte code;
        private String description;

        State(Byte code, String description) {
            this.code = code;
            this.description = description;
        }
        public void setCode(Byte code){
            this.code=code;
        }

        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
    private State state=State.OFFLINE;

    public GrouponActivity(GrouponActivityVo vo)
    {
        this.strategy=vo.getStrategy();
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

    public GrouponActivity(GrouponActivityPo po)
    {
        this.id=po.getId();
        this.shopId=po.getShopId();
        this.goodsSpuId=po.getGoodsSpuId();
        this.name=po.getName();
        this.strategy=po.getStrategy();
        this.beginTime=po.getBeginTime();
        this.endTime=po.getEndTime();
        this.gmtCreated=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.state.setCode(po.getState());
    }

    @Override
    public Object createVo() {
        return new GrouponActivityRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new GrouponActivitySimpleRetVo(this);
    }
}
