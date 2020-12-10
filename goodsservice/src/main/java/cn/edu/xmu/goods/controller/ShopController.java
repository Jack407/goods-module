package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.CouponService;
import cn.edu.xmu.goods.service.ShopService;
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
 * 商店模块控制器
 * @author 李狄翰
 * Modified at 2020/11/23 17:00
 **/
@Api(value = "商店服务", tags = "shop")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shop", produces = "application/json;charset=UTF-8")
public class ShopController {

    private  static  final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ShopService shopService;


    @ApiOperation(value = "获得商店的所有状态")
    @ApiImplicitParams({
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("shops/states")
    public Object getShopAllStates(){
        logger.debug("getShopAllStates:" );
        Shop.State[] states=Shop.State.class.getEnumConstants();
        List<ShopStateVo> shopStateVos =new ArrayList<ShopStateVo>();
        for(int i=0;i<states.length;i++){
            shopStateVos.add(new ShopStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(shopStateVos).getData());
    }

    @ApiOperation(value = "店家申请店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="vo", required = true, dataType="ShopVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops")
    public Object createShop(@RequestBody ShopVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("createShop");
        if(!Objects.equals(departId,-1L))
        {
            return ResponseUtil.fail(ResponseCode.USER_HASSHOP);
        }
        Shop shop = new Shop(vo);
        ReturnObject returnObject =  shopService.createShop(shop);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok(returnObject.getData());
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }

    }

    @ApiOperation(value = "卖家修改店铺信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="vo", required = true, dataType="ShopVo", paramType="body"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{id}")
    public Object changeShop(@RequestBody ShopVo vo, @PathVariable Long id){
        logger.debug("changeShop");
        Shop shop = new Shop(vo);
        ReturnObject returnObject =  shopService.changeShop(shop,id);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }

    }

    @ApiOperation(value = "关闭店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{id}")
    public Object closeShop(@PathVariable Long id){
        logger.debug("closeShop");
        ReturnObject returnObject =  shopService.closeShop(id);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "平台管理员审核店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopid", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="AuditVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/newshops/{id}/audit")
    public Object auditShop(@PathVariable Long id, @PathVariable Long shopId, @RequestBody AuditVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("auditShop");
        if(!Objects.equals(departId,0L))
        {
            return ResponseUtil.fail(ResponseCode.USER_CANT_AUDIT);
        }
        ReturnObject returnObject =  shopService.auditShop(id,vo);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "上线店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{id}/onshelves")
    public Object onlineShop(@PathVariable Long id){
        logger.debug("onlineShop");
        ReturnObject returnObject =  shopService.onlineShop(id);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "下线店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{id}/offshelves")
    public Object offlineShop(@PathVariable Long id){
        logger.debug("offlineShop");
        ReturnObject returnObject =  shopService.offlineShop(id);
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
