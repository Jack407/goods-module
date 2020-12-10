package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.ooad.annotation.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import cn.edu.xmu.goods.service.GoodsService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 商品模块控制器
 * @author 李狄翰
 * Modified at 2020/11/24 17:00
 **/
@Api(value = "商品服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class GoodsController {

    private  static  final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "管理员新增品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="String", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="Brandvo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{id}/brands")
    public Object createBrand(@PathVariable Long id, @RequestBody BrandVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("createBrand: id = "+ id );
        Brand bo=new Brand(vo);
        if(!Objects.equals(id,0L))
        {
            return ResponseUtil.fail(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject =  goodsService.createBrand(id,bo);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok(returnObject.getData());
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员修改品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="Brandvo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/brands/{id}")
    public Object changeBrand(@PathVariable Long shopId,@PathVariable Long id, @RequestBody BrandVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("createBrand: id = "+ id );
        if(!Objects.equals(shopId,0L))
        {
            return ResponseUtil.fail(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject =  goodsService.changeBrand(id,vo);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "管理员删除品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/brands/{id}")
    public Object deleteBrand(@PathVariable Long shopId,@PathVariable Long id, @LoginUser Long userId, @Depart Long departId){
        logger.debug("deleteBrand: id = "+ id );
        if(!Objects.equals(shopId,0L))
        {
            return new ReturnObject(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject =  goodsService.deleteBrand(id);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "获得商品的所有状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("skus/states")
    public Object getSkuAllStates(@LoginUser Long userId, @Depart Long departId){
        logger.debug("getSkuAllStates: 用户 = "+ userId );
        GoodsSku.State[] states=GoodsSku.State.class.getEnumConstants();
        List<GoodsSkuStateVo> goodsSkuStateVos =new ArrayList<GoodsSkuStateVo>();
        for(int i=0;i<states.length;i++){
            goodsSkuStateVos.add(new GoodsSkuStateVo(states[i]));
        }
        return ResponseUtil.ok(new ReturnObject<List>(goodsSkuStateVos).getData());
    }

    @ApiOperation(value = "查询SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "shopId", value = "商店id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "skuSn", value = "规格编号", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "spuId", value = "商品id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "spuSn", value = "商品编号", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("skus")
    public Object getSku(
            @RequestParam(required = false, defaultValue = "0") Long shopId,
            @RequestParam(required = false, defaultValue = "0") String skuSn,
            @RequestParam(required = false, defaultValue = "0") Long spuId,
            @RequestParam(required = false, defaultValue = "0") String spuSn,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ){
        logger.debug("getSku");
        ReturnObject<PageInfo<VoObject>> returnObject =  goodsService.getSku(shopId,skuSn,spuId,spuSn, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "获得SKU详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "SKUid", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("skus/{id}")
    public Object getOneSku(
            @PathVariable Long id
    ){
        logger.debug("getOneSku");
        ReturnObject<VoObject> returnObject =  goodsService.getOneSku(id);
        return returnObject;
    }

    @ApiOperation(value = "管理员添加新的SKU到SPU里")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="GoodsSkuVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{shopId}/spus/{id}/skus")
    public Object addSkuToSpu(@PathVariable Long shopId, @PathVariable Long id, @RequestBody GoodsSkuVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("addSkuToSpu: id = "+ id );
        GoodsSku bo=new GoodsSku(vo);
        ReturnObject returnObject =  goodsService.addSkuToSpu(id, shopId,userId,bo);
        return returnObject;
    }

    @ApiOperation(value = "管理员逻辑删除SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/skus/{id}")
    public Object deleteSku(@PathVariable Long shopId, @PathVariable Long id,  @LoginUser Long userId, @Depart Long departId){
        logger.debug("deleteSku: id = "+ id );
        ReturnObject returnObject =  goodsService.deleteSku(id, shopId);
        return returnObject;
    }

    @ApiOperation(value = "管理员修改SKU信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="vo", required = true, dataType="GoodsSkuVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/skus/{id}")
    public Object changeSku(@PathVariable Long shopId, @PathVariable Long id, @RequestBody GoodsSkuVo vo, @LoginUser Long userId, @Depart Long departId){
        logger.debug("changeSku: id = "+ id );
        GoodsSku bo=new GoodsSku(vo);
        ReturnObject returnObject =  goodsService.changeSku(id, shopId,bo);
        return returnObject;
    }

    @ApiOperation(value = "查询商品分类关系")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "种类id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("categories/{id}/subcatagories")
    public Object getCategory(
            @PathVariable Long Id
    ){
        logger.debug("getCategory");
        ReturnObject<List> returnObject =  goodsService.getCategory(Id);
        return returnObject;
    }

    @ApiOperation(value = "管理员新增商品类目")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "种类id", required = true),
            @ApiImplicitParam(name="vo", required = true, dataType="GoodsCategoryVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("categories/{id}/subcatagories")
    public Object createCategory(
            @PathVariable Long Id,
            @RequestBody GoodsCategoryVo vo
    ){
        logger.debug("createCategory");
        ReturnObject returnObject =  goodsService.createCategory(Id,vo);
        return returnObject;
    }

    @ApiOperation(value = "管理员修改商品类目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "种类id", required = true),
            @ApiImplicitParam(name="vo", required = true, dataType="GoodsCategoryVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("categories/{id}")
    public Object changeCategory(
            @PathVariable Long Id,
            @RequestBody GoodsCategoryVo vo
    ){
        logger.debug("changeCategory");
        ReturnObject returnObject =  goodsService.changeCategory(Id,vo);
        return returnObject;
    }

    @ApiOperation(value = "管理员删除商品类目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "种类id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("categories/{id}")
    public Object deleteCategory(
            @PathVariable Long Id
    ){
        logger.debug("deleteCategory");
        ReturnObject returnObject= goodsService.deleteCategory(Id);
        return returnObject;
    }

    @ApiOperation(value = "查看一条商品Spu的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "SPUid", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("spus/{id}")
    public Object getOneSpu(
            @PathVariable Long id
    ){
        logger.debug("getOneSpu");
        ReturnObject<VoObject> returnObject =  goodsService.getOneSpu(id);
        return returnObject;
    }

    @ApiOperation(value = "查看一条被分享的商品Spu的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "SPUid", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "sid", value = "分享id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("share/{sid}/spus/{id}")
    public Object getOneShareSpu(
            @PathVariable Long id,
            @PathVariable Long sid
    ){
        logger.debug("getOneShareSpu");
        ReturnObject<VoObject> returnObject =  goodsService.getOneShareSpu(id,sid);
        return returnObject;
    }


    @ApiOperation(value = "查看所有品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page_size", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("brands")
    public Object getBrands(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ){
        logger.debug("getBrands");
        ReturnObject<PageInfo<VoObject>> returnObject =  goodsService.getBrands( page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value = "店家新建商品Spu")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "店铺id", required = true),
            @ApiImplicitParam(name="vo", required = true, dataType="GoodsSpuVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{id}/spus")
    public Object createSpu(
            @PathVariable Long Id,
            @RequestBody GoodsSpuVo vo
    ){
        logger.debug("createSpu");
        ReturnObject returnObject =  goodsService.createSpu(Id,vo);
        return returnObject;
    }

    @ApiOperation(value = "店家修改商品Spu")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "SPUid", required = true),
            @ApiImplicitParam(name="vo", required = true, dataType="GoodsSpuVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/spus/{id}")
    public Object changeSpu(
            @PathVariable Long shopId,
            @PathVariable Long id,
            @RequestBody GoodsSpuVo vo
    ){
        logger.debug("changeSpu");
        ReturnObject returnObject =  goodsService.changeSpu(shopId,id,vo);
        return returnObject;
    }

    @ApiOperation(value = "店家逻辑删除商品Spu")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "SPUid", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/spus/{id}")
    public Object deleteSpu(
            @PathVariable Long shopId,
            @PathVariable Long id
    ){
        logger.debug("deleteSpu");
        ReturnObject returnObject =  goodsService.deleteSpu(shopId,id);
        return returnObject;
    }

    @ApiOperation(value = "店家上架商品Spu")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "SPUid", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/spus/{id}/onshelves")
    public Object onlineSpu(
            @PathVariable Long shopId,
            @PathVariable Long id
    ){
        logger.debug("onlineSpu");
        ReturnObject returnObject =  goodsService.onlineSpu(shopId,id);
        return returnObject;
    }

    @ApiOperation(value = "店家下架商品Spu")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "SPUid", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("shops/{shopId}/spus/{id}/offshelves")
    public Object offlineSpu(
            @PathVariable Long shopId,
            @PathVariable Long id
    ){
        logger.debug("offlineSpu");
        ReturnObject returnObject =  goodsService.offlineSpu(shopId,id);
        return returnObject;
    }

    @ApiOperation(value = "店家新建商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "SKUid", required = true),
            @ApiImplicitParam(name="vo", required = true, dataType="FloatPriceVo", paramType="body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{shopId}/skus/{id}/floatprices")
    public Object createFloatPrice(
            @PathVariable Long shopId,
            @PathVariable Long id,
            @RequestBody FloatPriceVo vo,
            @LoginUser Long userId
    ){
        logger.debug("createFloatPrice");
        FloatPrice bo=new FloatPrice(vo);
        if (!bo.isBiggerBegin())
        {
            return new ReturnObject(ResponseCode.BEGIN_AFTER_END);
        }
        if (!bo.beginAfterNow())
        {
            return new ReturnObject(ResponseCode.BEGIN_BEFORE_NOW);
        }
        ReturnObject returnObject =  goodsService.createFloatPrice(shopId,id,bo,userId);
        return returnObject;
    }

    @ApiOperation(value = "店家失效商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "Id", value = "SKUid", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/floatprices/{id}")
    public Object invalidFloatPrice(
            @PathVariable Long shopId,
            @PathVariable Long id,
            @LoginUser Long userId
    ){
        logger.debug("invalidFloatPrice");
        ReturnObject returnObject =  goodsService.invalidFloatPrice(shopId,id,userId);
        return returnObject;
    }

    @ApiOperation(value = "将SPU加入分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="spuid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{shopId}/spus/{spuId}/categories/{id}")
    public Object addSpuToCategory(@PathVariable Long shopId,@PathVariable Long spuId,@PathVariable Long id){
        logger.debug("addSpuToCategory: id = "+ id );
        if(!Objects.equals(shopId,0))
        {
            return new ReturnObject(ResponseCode.NO_PRI_TO_CATEGORY);
        }
        ReturnObject returnObject =  goodsService.addSpuToCategory(spuId,id);
        return returnObject;
    }

    @ApiOperation(value = "将SPU移出分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="spuid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/spus/{spuId}/categories/{id}")
    public Object removeSpuToCategory(@PathVariable Long shopId,@PathVariable Long spuId,@PathVariable Long id){
        logger.debug("removeSpuToCategory: id = "+ id );
        if(!Objects.equals(shopId,0))
        {
            return new ReturnObject(ResponseCode.NO_PRI_TO_CATEGORY);
        }
        ReturnObject returnObject =  goodsService.removeSpuToCategory(spuId,id);
        return returnObject;
    }

    @ApiOperation(value = "将SPU加入品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="spuid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("shops/{shopId}/spus/{spuId}/brands/{id}")
    public Object addSpuToBrand(@PathVariable Long shopId,@PathVariable Long spuId,@PathVariable Long id){
        logger.debug("addSpuToBrand: id = "+ id );
        if(!Objects.equals(shopId,0L))
        {
            return new ReturnObject(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject =  goodsService.addSpuToBrand(spuId,id);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "将SPU移出品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="spuid", required = true, dataType="int", paramType="path"),
            @ApiImplicitParam(name="id", required = true, dataType="int", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("shops/{shopId}/spus/{spuId}/brands/{id}")
    public Object removeSpuToBrand(@PathVariable Long shopId,@PathVariable Long spuId,@PathVariable Long id){
        logger.debug("removeSpuToBrand: id = "+ id );
        if(!Objects.equals(shopId,1L))
        {
            return new ReturnObject(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject =  goodsService.removeSpuToBrand(spuId,id);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return ResponseUtil.ok();
        }
        else
        {
            return ResponseUtil.fail(returnObject.getCode());
        }
    }

    @ApiOperation(value = "sku上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopid", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "img", required = true, dataType = "file", value = "文件", paramType = "formData")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("shops/{shopId}/skus/{id}/uploadImg")
    public Object uploadSkuImg(@PathVariable Long shopId, @PathVariable Long id,  MultipartFile img) {
        logger.debug("uploadImg: shopId = " + shopId + " id = " + id + " img:" + img.getOriginalFilename());
        if (!Objects.equals(shopId, 0)) {
            return new ReturnObject(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject = goodsService.uploadSkuImg(shopId, id,img);
        return returnObject;
    }

    @ApiOperation(value = "spu上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopid", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "img", required = true, dataType = "file", value = "文件", paramType = "formData")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("shops/{shopId}/spus/{id}/uploadImg")
    public Object uploadSpuImg(@PathVariable Long shopId, @PathVariable Long id,  MultipartFile img) {
        logger.debug("uploadImg: shopId = " + shopId + " id = " + id + " img:" + img.getOriginalFilename());
        if (!Objects.equals(shopId, 0)) {
            return new ReturnObject(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject = goodsService.uploadSpuImg(shopId, id,img);
        return returnObject;
    }

    @ApiOperation(value = "品牌上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopid", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "img", required = true, dataType = "file", value = "文件", paramType = "formData")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("shops/{shopId}/brands/{id}/uploadImg")
    public Object uploadBrandImg(@PathVariable Long shopId, @PathVariable Long id,  MultipartFile img) {
        logger.debug("uploadImg: shopId = " + shopId + " id = " + id + " img:" + img.getOriginalFilename());
        if (!Objects.equals(shopId, 0)) {
            return new ReturnObject(ResponseCode.NO_PRI_TO_BRAND);
        }
        ReturnObject returnObject = goodsService.uploadBrandImg(shopId, id,img);
        return returnObject;
    }



}
