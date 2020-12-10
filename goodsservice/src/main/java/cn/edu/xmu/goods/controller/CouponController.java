package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.bo.CouponSku;
import cn.edu.xmu.goods.model.vo.CouponSkuVo;
import cn.edu.xmu.ooad.annotation.*;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cn.edu.xmu.goods.service.CouponService;
import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.vo.CouponStateVo;
import cn.edu.xmu.goods.model.vo.CouponActivityVo;
import cn.edu.xmu.ooad.model.VoObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;


/**
 * 优惠模块控制器
 * @author 李狄翰
 * Modified at 2020/11/23 17:00
 **/
@Api(value = "优惠服务", tags = "coupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/coupon", produces = "application/json;charset=UTF-8")
public class CouponController {
    private  static  final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;


    @ApiOperation(value = "买家领取活动的优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="String", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 910, message = "优惠券已发放"),
            @ApiResponse(code = 911, message = "优惠活动终止"),
    })
    @Audit
    @PostMapping("couponactivities/{id}/usercoupons")
    public Object getCoupons(@PathVariable Long id,  @LoginUser Long userId, @Depart Long departId){
        logger.debug("getCoupons: id = "+ id );
        ReturnObject returnObject =  couponService.getCoupons(id,userId);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok(returnObject.getData());
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "获得优惠券的所有状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("coupons/states")
    public Object getCouponAllStates(@LoginUser Long userId, @Depart Long departId){
        logger.debug("getCoupons: 用户 = "+ userId );
        Coupon.State[] states=Coupon.State.class.getEnumConstants();
        List<CouponStateVo> couponStateVos =new ArrayList<CouponStateVo>();
        for(int i=0;i<states.length;i++){
            couponStateVos.add(new CouponStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(couponStateVos).getData());
    }

    @ApiOperation(value = "管理员新建己方的优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="CouponActivityVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{shopId}/couponactivities")
    public Object createCouponAc(@PathVariable Long shopId, @RequestBody CouponActivityVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("createCouponAc");
        CouponActivity bo=new CouponActivity(vo);
        if (!bo.isBiggerBegin())
        {
            return ResponseUtil.fail(ResponseCode.BEGIN_AFTER_END);
        }
        if (!bo.beginAfterNow())
        {
            return ResponseUtil.fail(ResponseCode.BEGIN_BEFORE_NOW);
        }
        ReturnObject returnObject =  couponService.createCouponAc(shopId,userId,bo);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok(returnObject.getData());
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "查看上线的优惠活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "shopId", value = "商店id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "timeline", value = "时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("couponactivities")
    public Object getCouponAc(
            @RequestParam(required = false) Long shopId,
            @RequestParam(required = false, defaultValue = "4") Integer time,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ){
        logger.debug("getCouponAc");
        ReturnObject<PageInfo<VoObject>> returnObject =  couponService.selectAllAc(shopId,time, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "查看本店下线的优惠活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "商店id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("shops/{shopId}/couponactivities/invalid")
    public Object getInvalidCouponAc(
            @PathVariable Long shopId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ){
        logger.debug("getInvalidCouponAc");
        ReturnObject<PageInfo<VoObject>> returnObject =  couponService.selectAllInvalidAc(shopId, page, pageSize);
        return Common.getPageRetObject(returnObject);

    }

    @ApiOperation(value = "查看优惠活动的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("couponactivities/{Id}/skus")
    public Object getSkuInCouponAc(
            @PathVariable Long Id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ){
        logger.debug("getSkuInCouponAc");
        ReturnObject<PageInfo<VoObject>> returnObject =  couponService.getSkuInCouponAc(Id, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "查看优惠活动详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("shops/{shopId}/couponactivities/{Id}")
    public Object getOneCouponAc(
            @PathVariable Long Id,
            @PathVariable Long shopId
    ){
        logger.debug("getOneCouponAc");
        ReturnObject<VoObject> returnObject =  couponService.getOneCouponAc(Id, shopId);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok(returnObject.getData());
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员修改己方某优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(name="vo", required = true, dataType="CouponActivityVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/couponactivities/{Id}")
    public Object changeCouponAc(
            @PathVariable Long Id,
            @PathVariable Long shopId,
            @Validated @RequestBody CouponActivityVo vo,
            @LoginUser Long userId
    ){
        logger.debug("changeCouponAc");
        CouponActivity bo=new CouponActivity(vo);
        if (!bo.isBiggerBegin())
        {
            return new ReturnObject(ResponseCode.BEGIN_AFTER_END);
        }
        if (!bo.beginAfterNow())
        {
            return new ReturnObject(ResponseCode.BEGIN_BEFORE_NOW);
        }
        ReturnObject returnObject =  couponService.changeCouponAc(Id, shopId,bo,userId);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员下线己方某优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/couponactivities/{id}")
    public Object offlineCouponAc(
            @PathVariable Long id,
            @PathVariable Long shopId
    ){
        logger.debug("offlineCouponAc");
        ReturnObject returnObject =  couponService.offlineCouponAc(id, shopId);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员为己方某优惠券活动新增范围")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(name="vo", required = true, dataType="CouponSkuVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{shopId}/couponactivities/{id}/skus")
    public Object addSkuInCouponAc(
            @PathVariable Long id,
            @PathVariable Long shopId,
            @Validated @RequestBody CouponSkuVo vo,
            @LoginUser Long userId
    ){
        logger.debug("addSkuInCouponAc");
        CouponSku bo = new CouponSku(vo);
        ReturnObject returnObject =  couponService.addSkuInCouponAc(id, shopId,bo);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员为己方某优惠券活动删除范围")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/couponskus/{id}")
    public Object removeSkuInCouponAc(
            @PathVariable Long id,
            @PathVariable Long shopId,
            @LoginUser Long userId
    ){
        logger.debug("removeSkuInCouponAc");
        ReturnObject returnObject =  couponService.removeSkuInCouponAc(id, shopId);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "查看本店下线的优惠活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "state", value = "优惠券", required = false),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("coupons")
    public Object getUserCoupon(
            @RequestParam(required = false, defaultValue = "4") Byte state,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @LoginUser Long userId
    ){
        logger.debug("getUserCoupon");
        ReturnObject<PageInfo<VoObject>> returnObject =  couponService.getUserCoupon(userId,state, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "用户使用优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "优惠券id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("coupons/{id}")
    public Object useCoupon(
            @PathVariable Long id,
            @LoginUser Long userId
    ){
        logger.debug("useCoupon");
        ReturnObject returnObject =  couponService.useCoupon(userId,id);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }


    @ApiOperation(value = "上线优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "优惠活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/couponactivities/{id}/onshelves")
    public Object onlineCoupon(
            @PathVariable Long id,
            @PathVariable Long shopId,
            @LoginUser Long userId
    ){
        logger.debug("onlineCoupon");
        ReturnObject returnObject =  couponService.onlineCoupon(userId,id,shopId);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "下线优惠活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "优惠活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/couponactivities/{id}/offshelves")
    public Object offlineCoupon(
            @PathVariable Long id,
            @PathVariable Long shopId,
            @LoginUser Long userId
    ){
        logger.debug("offlineCoupon");
        ReturnObject returnObject =  couponService.offlineCoupon(userId,id,shopId);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "优惠活动上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopid", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "img", required = true, dataType = "file", value = "文件", paramType = "formData")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("shops/{shopId}/couponactivities/{id}/uploadImg")
    public Object uploadAcImg(@PathVariable Long shopId, @PathVariable Long id,  MultipartFile img) {
        logger.debug("uploadImg: shopId = " + shopId + " id = " + id + " img:" + img.getOriginalFilename());
        if (!Objects.equals(shopId, 0)) {
            return new ReturnObject(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject = couponService.uploadAcImg(shopId, id,img);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

}
