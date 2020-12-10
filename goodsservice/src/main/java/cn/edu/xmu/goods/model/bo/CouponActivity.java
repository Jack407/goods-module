package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.CouponActivityPo;
import cn.edu.xmu.goods.model.vo.CouponActivityRetVo;
import cn.edu.xmu.goods.model.vo.CouponActivitySimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;
import cn.edu.xmu.goods.model.vo.CouponActivityVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class CouponActivity implements VoObject{

    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime couponTime;
    private Long shopId;
    private Integer quantity;
    private Byte validTerm;
    private String imageUrl;
    private String strategy;
    private Long createdBy;
    private Long modifyBy;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
    private Byte quantityType;

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


        public Byte getCode() {
            return code;
        }

        public void setCode(Byte code) {
            this.code=code;
        }

        public String getDescription() {
            return description;
        }
    }
    private State state=State.OFFLINE;

    public CouponActivity(CouponActivityVo vo)
    {
        this.name=vo.getName();
        this.quantity=vo.getQuantity();
        this.quantityType=vo.getQuantityType();
        this.validTerm=vo.getValidTerm();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.beginTime = LocalDateTime.parse(vo.getBeginTime(), df);
        this.endTime = LocalDateTime.parse(vo.getEndTime(), df);
        if(!Objects.equals(vo.getCouponTime(),null)) {
            this.couponTime = LocalDateTime.parse(vo.getCouponTime(), df);
        }
        this.strategy=vo.getStrategy();
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

    public CouponActivity(CouponActivityPo po)
    {
        this.id=po.getId();
        this.name=po.getName();
        this.beginTime=po.getBeginTime();
        this.endTime=po.getEndTime();
        this.couponTime=po.getCouponTime();
        this.shopId=po.getShopId();
        this.quantity=po.getQuantity();
        this.validTerm=po.getValidTerm();
        this.imageUrl=po.getImageUrl();
        this.strategy=po.getStrategy();
        this.createdBy=po.getCreatedBy();
        this.modifyBy=po.getModiBy();
        this.gmtCreated=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.quantityType=po.getQuantitiyType();
        this.state.setCode(po.getState());
    }

    @Override
    public Object createVo() {
        return new CouponActivityRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new CouponActivitySimpleRetVo(this);
    }




}
