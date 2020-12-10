package cn.edu.xmu.goods.model.bo;


import cn.edu.xmu.goods.model.po.CouponPo;
import cn.edu.xmu.goods.model.vo.CouponActivityRetVo;
import cn.edu.xmu.goods.model.vo.CouponActivitySimpleRetVo;
import cn.edu.xmu.goods.model.vo.CouponRetVo;
import cn.edu.xmu.goods.model.vo.CouponSimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class Coupon implements VoObject{

    private Long id;
    private String couponSn;
    private String name;
    private Long customerId;
    private Long activityId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public enum State {
        UNAVAILABLE((byte)0, "未领取"),
        AVAILABLE((byte)1, "已领取"),
        USED((byte)2, "已使用"),
        DELETE((byte)3, "已失效");


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
    private State state=State.UNAVAILABLE;

    public Coupon(CouponPo po)
    {
        this.id=po.getId();
        this.couponSn=po.getCouponSn();
        this.activityId=po.getActivityId();
        this.name=po.getName();
        this.customerId=po.getCustomerId();
        this.beginTime=po.getBeginTime();
        this.endTime=po.getEndTime();
        this.gmtCreated=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
        this.state.setCode(po.getState());
    }

    @Override
    public Object createVo() {
        return new CouponRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new CouponSimpleRetVo(this);
    }


}
