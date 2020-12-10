package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.vo.CouponActivityVo;
import cn.edu.xmu.goods.model.vo.CouponStateVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityStateVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityVo;
import cn.edu.xmu.goods.service.CouponService;
import cn.edu.xmu.goods.service.PresaleService;
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
import cn.edu.xmu.ooad.model.VoObject;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;


/**
 * 预售模块控制器
 * @author 李狄翰
 * Modified at 2020/11/23 17:00
 **/
@Api(value = "预售服务", tags = "presale")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/presale", produces = "application/json;charset=UTF-8")
public class PresaleController {

    private  static  final Logger logger = LoggerFactory.getLogger(PresaleController.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PresaleService presaleService;


    @ApiOperation(value = "获得预售活动的所有状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("presales/states")
    public Object getPresaleAllStates(){
        logger.debug("getPresaleAllStates: ");
        PresaleActivity.State[] states=PresaleActivity.State.class.getEnumConstants();
        List<PresaleActivityStateVo> presaleActivityStateVos =new ArrayList<PresaleActivityStateVo>();
        for(int i=0;i<states.length;i++){
            presaleActivityStateVos.add(new PresaleActivityStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(presaleActivityStateVos).getData());
    }

    @ApiOperation(value = "查询所有有效的预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "shopId", value = "商店id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "timeline", value = "时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "skuId", value = "商品id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("presales")
    public Object getPresaleActivity(
            @RequestParam(required = false, defaultValue = "0") Long shopId,
            @RequestParam(required = false, defaultValue = "4") Long timeline,
            @RequestParam(required = false, defaultValue = "0") Long skuId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ){
        logger.debug("getPresaleActivity");
        ReturnObject<PageInfo<VoObject>> returnObject =  presaleService.getPresaleActivity(shopId,timeline,skuId, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "管理员查询Sku所有预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "商品id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "state", value = "状态", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("shops/{shopId}/skus/{Id}/presales")
    public Object getOnePresaleAc(
            @PathVariable Long Id,
            @PathVariable Long shopId,
            @RequestParam(required = false, defaultValue = "3") Byte state
    ){
        logger.debug("getOnePresaleAc");
        ReturnObject returnObject =  presaleService.getOnePresaleAc(Id, shopId,state);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            return ResponseUtil.ok(returnObject.getData());
        } else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员新建SKU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="PresaleActivityVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{shopId}/skus/{id}/presales")
    public Object createPresaleAc(@PathVariable Long shopId, @PathVariable Long id, @RequestBody PresaleActivityVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("createPresaleAc: id = "+ id );
        PresaleActivity bo=new PresaleActivity(vo);
        if (!bo.isBiggerBegin())
        {
            return new ReturnObject(ResponseCode.BEGIN_AFTER_END);
        }
        if (!bo.beginAfterNow())
        {
            return new ReturnObject(ResponseCode.BEGIN_BEFORE_NOW);
        }
        ReturnObject returnObject =  presaleService.createPresaleAc(id, shopId,userId,bo);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            return ResponseUtil.ok(returnObject.getData());
        } else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员修改SKU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="PresaleActivityVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/presales/{id}")
    public Object changePresaleAc(@PathVariable Long shopId, @PathVariable Long id, @RequestBody PresaleActivityVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("changePresaleAc: id = "+ id );
        PresaleActivity bo=new PresaleActivity(vo);
        if (!bo.isBiggerBegin())
        {
            return new ReturnObject(ResponseCode.BEGIN_AFTER_END);
        }
        if (!bo.beginAfterNow())
        {
            return new ReturnObject(ResponseCode.BEGIN_BEFORE_NOW);
        }
        ReturnObject returnObject =  presaleService.changePresaleAc(id, shopId,userId,bo);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员逻辑删除SKU预售活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/presales/{Id}")
    public Object cancelPresaleAc(
            @PathVariable Long Id,
            @PathVariable Long shopId
    ){
        logger.debug("cancelPresaleAc");
        ReturnObject returnObject =  presaleService.cancelPresaleAc(Id, shopId);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

}
