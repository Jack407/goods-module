package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.vo.GrouponActivityStateVo;
import cn.edu.xmu.goods.model.vo.GrouponActivityVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityVo;
import cn.edu.xmu.goods.service.GrouponService;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;


/**
 * 团购模块控制器
 *
 * @author 李狄翰
 * Modified at 2020/11/23 17:00
 **/
@Api(value = "团购服务", tags = "groupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/groupon", produces = "application/json;charset=UTF-8")
public class GrouponController {

    private static final Logger logger = LoggerFactory.getLogger(GrouponController.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private GrouponService grouponService;


    @ApiOperation(value = "获得团购活动的所有状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("groupons/states")
    public Object getGrouponAllStates(@LoginUser Long userId, @Depart Long departId) {
        logger.debug("getGrouponAllStates: 用户 = " + userId);
        GrouponActivity.State[] states = GrouponActivity.State.class.getEnumConstants();
        List<GrouponActivityStateVo> grouponActivityStateVos = new ArrayList<GrouponActivityStateVo>();
        for (int i = 0; i < states.length; i++) {
            grouponActivityStateVos.add(new GrouponActivityStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(grouponActivityStateVos).getData());
    }

    @ApiOperation(value = "查询所有有效的团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "shopId", value = "商店id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "timeline", value = "时间", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "spuId", value = "商品id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false),
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("groupons")
    public Object getGrouponActivity(
            @RequestParam(required = false, defaultValue = "0") Long shopId,
            @RequestParam(required = false, defaultValue = "4") Long timeline,
            @RequestParam(required = false, defaultValue = "0") Long spuId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        logger.debug("getGrouponActivity");
        ReturnObject<PageInfo<VoObject>> returnObject = grouponService.getGrouponActivity(shopId, timeline, spuId, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "管理员查询店铺内的团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "shopId", value = "商店id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "state", value = "状态", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "spuId", value = "商品id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false),
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("shops/{shopId}/groupons")
    public Object getShopGrouponActivity(
            @PathVariable Long shopId,
            @RequestParam(required = false, defaultValue = "5") Byte state,
            @RequestParam(required = false, defaultValue = "0") Long spuId,
            @RequestParam(required = false, defaultValue = "null") String startTime,
            @RequestParam(required = false, defaultValue = "null") String endTime,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        logger.debug("getShopGrouponActivity");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime1;
        LocalDateTime endTime1;
        if(!Objects.equals("null",startTime)){
            startTime1 = LocalDateTime.parse(startTime, df);
        }
        else{
            startTime1 = null;
        }
        if(!Objects.equals("null",endTime)) {
            endTime1 = LocalDateTime.parse(endTime, df);
        }
        else
        {
            endTime1 = null;
        }
        if(!Objects.equals(startTime1,null) && !Objects.equals(null,endTime1) && startTime1.isAfter(endTime1))
        {
            return ResponseUtil.fail(ResponseCode.BEGIN_AFTER_END);
        }
        ReturnObject<PageInfo<VoObject>> returnObject = grouponService.getShopGrouponActivity(shopId, state, spuId, startTime1,endTime1,page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "管理员查询Spu所有团购活动")
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
    @GetMapping("shops/{shopId}/spus/{Id}/groupons")
    public Object getOneGrouponAc(
            @PathVariable Long Id,
            @PathVariable Long shopId,
            @RequestParam(required = false, defaultValue = "2") Byte state
    ) {
        logger.debug("getOneGrouponAc");
        ReturnObject returnObject = grouponService.getOneGrouponAc(Id, shopId, state);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            return ResponseUtil.ok(returnObject.getData());
        } else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员新建SPU团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "vo", required = true, dataType = "GrouponActivityVo", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{shopId}/spus/{id}/groupons")
    public Object createGrouponAc(@PathVariable Long shopId, @PathVariable Long id, @RequestBody GrouponActivityVo vo, @LoginUser Long userId, @Depart Long departId) {
        logger.debug("createGrouponAc: id = " + id);
        GrouponActivity bo = new GrouponActivity(vo);
        if (!bo.isBiggerBegin()) {
            return ResponseUtil.fail(ResponseCode.BEGIN_AFTER_END);
        }
        if (!bo.beginAfterNow()) {
            return ResponseUtil.fail(ResponseCode.BEGIN_BEFORE_NOW);
        }
        ReturnObject returnObject = grouponService.createGrouponAc(id, shopId, userId, bo);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            return ResponseUtil.ok(returnObject.getData());
        } else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员修改SPU团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "vo", required = true, dataType = "GrouponActivityVo", paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/groupons/{id}")
    public Object changeGrouponAc(@PathVariable Long shopId, @PathVariable Long id, @RequestBody GrouponActivityVo vo, @LoginUser Long userId, @Depart Long departId) {
        logger.debug("changeGrouponAc: id = " + id);
        GrouponActivity bo = new GrouponActivity(vo);
        if (!bo.isBiggerBegin()) {
            return ResponseUtil.fail(ResponseCode.BEGIN_AFTER_END);
        }
        if (!bo.beginAfterNow()) {
            return ResponseUtil.fail(ResponseCode.BEGIN_BEFORE_NOW);
        }
        ReturnObject returnObject = grouponService.changeGrouponAc(id, shopId, userId, bo);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员逻辑删除SPU团购活动")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "authorization", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/groupons/{id}")
    public Object cancelGrouponAc(
            @PathVariable Long id,
            @PathVariable Long shopId
    ) {
        logger.debug("cancelGrouponAc");
        ReturnObject returnObject = grouponService.cancelGrouponAc(id, shopId);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

}
