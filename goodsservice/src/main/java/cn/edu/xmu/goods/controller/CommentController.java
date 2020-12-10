package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.CommentService;
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
 * 评论模块控制器
 * @author 李狄翰
 * Modified at 2020/11/23 17:00
 **/
@Api(value = "评论服务", tags = "comment")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/comment", produces = "application/json;charset=UTF-8")
public class CommentController {

    private  static  final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "获得评论的所有状态")
    @ApiImplicitParams({

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("comments/states")
    public Object getCommentAllStates(){
        logger.debug("getCommentAllStates:" );
        Comment.State[] states=Comment.State.class.getEnumConstants();
        List<CommentStateVo> commentStateVos =new ArrayList<CommentStateVo>();
        for(int i=0;i<states.length;i++){
            commentStateVos.add(new CommentStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(commentStateVos).getData());
    }

    @ApiOperation(value = "查看SKU的评价列表(已经通过审核的)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "规格id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("skus/{id}/comments")
    public Object getSkuComment(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ){
        logger.debug("getSkuComment");
        ReturnObject<PageInfo<VoObject>> returnObject =  commentService.getSkuComment(id, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "管理员审核评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="did", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="AuditVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{did}/comments/{id}/confirm")
    public Object auditComment(@PathVariable Long id,@PathVariable Long did, @RequestBody AuditVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("auditComment");
        if(!Objects.equals( departId ,0L))
        {
            return ResponseUtil.fail(ResponseCode.USER_CANT_AUDIT);
        }
        ReturnObject returnObject =  commentService.auditComment(id,vo);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "买家新增sku评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="CommentVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("orderitems/{id}/comments")
    public Object createComment(@PathVariable Long id, @RequestBody CommentVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("createComment");
        ReturnObject returnObject =  commentService.createComment(id,userId,vo);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok(returnObject.getData());
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "买家查看自己的评论记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("comments")
    public Object getUserComment(
            @LoginUser Long userId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ){
        logger.debug("getUserComment");
        ReturnObject<PageInfo<VoObject>> returnObject =  commentService.getUserComment(userId, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "管理员查看审核/未审核评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(paramType = "query", dataType = "byte", name = "state", value = "状态", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("shops/{shopId}/comments/all")
    public Object getAdComment(
            @PathVariable Long shopId,
            @LoginUser Long userId,
            @Depart Long departId,
            @RequestParam(required = false, defaultValue = "3") Byte state,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        logger.debug("getAdComment");
        if(!Objects.equals( departId ,0L))
        {
            return ResponseUtil.fail(ResponseCode.USER_CANT_AUDIT);
        }
        ReturnObject returnObject =  commentService.getAdComment(state,page,pageSize);
        return Common.getPageRetObject(returnObject);

    }

}
