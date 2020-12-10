package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.bo.FlashSale;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.CommentService;
import cn.edu.xmu.goods.service.CouponService;
import cn.edu.xmu.goods.service.FlashSaleService;
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
 * 秒杀模块控制器
 * @author 李狄翰
 * Modified at 2020/11/23 17:00
 **/
@Api(value = "秒杀服务", tags = "flashsale")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/flashsale", produces = "application/json;charset=UTF-8")
public class FlashSaleController {

    private  static  final Logger logger = LoggerFactory.getLogger(FlashSaleController.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FlashSaleService flashSaleService;

    @ApiOperation(value = "平台管理员在某个时段下新建秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="FlashSaleVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("timesegments/{id}/flashsales")
    public Object createFlashSale(@PathVariable Long id, @RequestBody FlashSaleVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("createFlashSale");
        FlashSale flashSale=new FlashSale(vo);
        ReturnObject returnObject=flashSaleService.createFlashSale(id,flashSale);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok(returnObject.getData());
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "平台管理员删除某个时段秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("flashsales/{id}")
    public Object deleteFlashSale(@PathVariable Long id, @LoginUser Long userId, @Depart Long departId){
        logger.debug("deleteFlashSale");
        ReturnObject returnObject=flashSaleService.deleteFlashSale(id);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "平台管理员修改秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="FlashSaleVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("flashsales/{id}")
    public Object changeFlashSale(@PathVariable Long id, @RequestBody FlashSaleVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("changeFlashSale");
        FlashSale flashSale=new FlashSale(vo);
        ReturnObject returnObject=flashSaleService.changeFlashSale(id,flashSale);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "平台管理员向秒杀活动添加商品SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="FlashSaleItemVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("flashsales/{id}/flashitems")
    public Object createFlashSaleItem(@PathVariable Long id, @RequestBody FlashSaleItemVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("createFlashSaleItem");
        ReturnObject returnObject=flashSaleService.createFlashSaleItem(id,vo);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok(returnObject.getData());
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "平台管理员在秒杀活动中删除某个SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="fid", required = true, dataType="int", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("flashsales/{fid}/flashitems/{id}")
    public Object deleteFlashSaleItem(@PathVariable Long fid,@PathVariable Long id, @LoginUser Long userId, @Depart Long departId){
        logger.debug("deleteFlashSaleItem");
        ReturnObject returnObject=flashSaleService.deleteFlashSaleItem(fid,id);
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
