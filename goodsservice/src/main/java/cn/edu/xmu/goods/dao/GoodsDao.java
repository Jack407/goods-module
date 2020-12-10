package cn.edu.xmu.goods.dao;


import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.ImgHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class GoodsDao {

    private static final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GoodsSpuPoMapper goodsSpuPoMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GoodsSkuPoMapper goodsSkuPoMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GoodsCategoryPoMapper goodsCategoryPoMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    BrandPoMapper brandPoMapper;
    public ReturnObject createBrand(Brand bo) {
        try{
            BrandPo po = new BrandPo();
            po.setName(bo.getName());
            po.setDetail(bo.getDetail());
            po.setImageUrl(bo.getImageUrl());
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            brandPoMapper.insert(po);
            BrandCreateVo vo=new BrandCreateVo(po);
            return new ReturnObject<>(vo);
        }
        catch (DataAccessException e){
            logger.error("createShop: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    FloatPricePoMapper floatPricePoMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ShopPoMapper shopPoMapper;

    @Value("${privilegeservice.dav.username}")
    private String davUsername;

    @Value("${privilegeservice.dav.password}")
    private String davPassword;

    @Value("${privilegeservice.dav.baseUrl}")
    private String baseUrl;

    public ReturnObject<PageInfo<VoObject>> getSku(Long shopId, String skuSn,Long spuId,String spuSn, Integer pageNum, Integer pageSize) {
        GoodsSkuPoExample example = new GoodsSkuPoExample();
        GoodsSkuPoExample.Criteria criteria=example.createCriteria();
        if(!Objects.equals(skuSn,"0"))
        {
            criteria.andSkuSnEqualTo(skuSn);
        }
        if(!Objects.equals(spuId,0))
        {
            criteria.andGoodsSpuIdEqualTo(spuId);
        }
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<GoodsSkuPo> goodsSkuPos = null;
        try {
            goodsSkuPos = goodsSkuPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(goodsSkuPos.size());
            for (GoodsSkuPo po : goodsSkuPos) {
                GoodsSpuPo goodsSpuPo=goodsSpuPoMapper.selectByPrimaryKey(po.getGoodsSpuId());
                if(Objects.equals(shopId,goodsSpuPo.getShopId()) && Objects.equals(spuSn,goodsSpuPo.getGoodsSn())) {
                    GoodsSku goodsSku = new GoodsSku(po);
                    ret.add(goodsSku);
                }
            }
            PageInfo<VoObject> goodsSkuPage = PageInfo.of(ret);
            return new ReturnObject<>(goodsSkuPage);

        }
        catch (DataAccessException e){
            logger.error("getSku: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject  getOneSku(Long Id)
    {
        try {
            GoodsSkuPo goodsSkupo=goodsSkuPoMapper.selectByPrimaryKey(Id);
            GoodsSku bo=new GoodsSku(goodsSkupo);
            return new ReturnObject<>(bo);
        }
        catch (DataAccessException e){
            logger.error("getOneSku: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject addSkuToSpu(Long Id, Long shopId,Long userId,GoodsSku bo) {
        GoodsSpuPo po=goodsSpuPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        try{
            GoodsSkuPo goodsSkuPo=new GoodsSkuPo();
            goodsSkuPo.setName(bo.getName());
            goodsSkuPo.setConfiguration(bo.getConfiguration());
            goodsSkuPo.setGoodsSpuId(Id);
            goodsSkuPo.setDetail(bo.getDetail());
            goodsSkuPo.setDisabled((byte) 0);
            goodsSkuPo.setImageUrl(bo.getImageUrl());
            goodsSkuPo.setOriginalPrice(bo.getOriginalPrice());
            goodsSkuPo.setInventory(bo.getInventory());
            goodsSkuPo.setWeight(bo.getWeight());
            goodsSkuPo.setSkuSn(bo.getSkuSn());
            goodsSkuPo.setGmtCreate(LocalDateTime.now());
            goodsSkuPo.setGmtModified(LocalDateTime.now());
            goodsSkuPoMapper.insert(goodsSkuPo);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("addSkuToSpu: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject deleteSku(Long Id, Long shopId) {
        GoodsSkuPo po=goodsSkuPoMapper.selectByPrimaryKey(Id);
        GoodsSpuPo po1=goodsSpuPoMapper.selectByPrimaryKey(po.getGoodsSpuId());
        if(!Objects.equals(po1.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        try{
            po.setDisabled((byte) 1);
            goodsSkuPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("deleteSku: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject changeSku(Long Id, Long shopId,GoodsSku bo) {
        GoodsSkuPo po=goodsSkuPoMapper.selectByPrimaryKey(Id);
        GoodsSpuPo po1=goodsSpuPoMapper.selectByPrimaryKey(po.getGoodsSpuId());
        if(!Objects.equals(po1.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        try{
            po.setOriginalPrice(bo.getOriginalPrice());
            po.setConfiguration(bo.getConfiguration());
            po.setName(bo.getName());
            po.setInventory(bo.getInventory());
            po.setWeight(bo.getWeight());
            po.setDetail(bo.getDetail());
            goodsSkuPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("changeSku: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject getCategory(Long Id) {
        try{
            GoodsCategoryPoExample example=new GoodsCategoryPoExample();
            GoodsCategoryPoExample.Criteria criteria=example.createCriteria();
            criteria.andPidEqualTo(Id);
            List<GoodsCategoryPo> goodsCategoryPos=goodsCategoryPoMapper.selectByExample(example);
            return new ReturnObject<>(goodsCategoryPos);
        }
        catch (DataAccessException e){
            logger.error("getCategory: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject createCategory(Long Id, GoodsCategoryVo vo) {
        try{
            GoodsCategoryPo po=new GoodsCategoryPo();
            po.setName(vo.getName());
            po.setPid(Id);
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            goodsCategoryPoMapper.insert(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("createCategory: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject changeCategory(Long Id, GoodsCategoryVo vo) {
        try{
            GoodsCategoryPo po=goodsCategoryPoMapper.selectByPrimaryKey(Id);
            po.setName(vo.getName());
           goodsCategoryPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("changeCategory: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject deleteCategory(Long Id) {
        try{
            goodsCategoryPoMapper.deleteByPrimaryKey(Id);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("deleteCategory: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject<PageInfo<VoObject>> getBrands(Integer pageNum, Integer pageSize) {
        BrandPoExample example = new BrandPoExample();
        BrandPoExample.Criteria criteria=example.createCriteria();
        criteria.andNameIsNotNull();
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<BrandPo> brandPos = null;
        try {
            brandPos = brandPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(brandPos.size());
            for (BrandPo po : brandPos)
            {
                Brand brand = new Brand(po);
                ret.add(brand);
            }
            PageInfo<VoObject> goodsSkuPage = PageInfo.of(ret);
            return new ReturnObject<>(goodsSkuPage);

        }
        catch (DataAccessException e){
            logger.error("getSku: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject  getOneSpu(Long Id)
    {
        try {
            GoodsSpuPo goodsSpupo=goodsSpuPoMapper.selectByPrimaryKey(Id);
            GoodsSpu bo=new GoodsSpu(goodsSpupo);
            return new ReturnObject<>(bo);
        }
        catch (DataAccessException e){
            logger.error("getOneSpu: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject  getOneShareSpu(Long Id,Long sid)
    {
        try {
            GoodsSpuPo goodsSpupo=goodsSpuPoMapper.selectByPrimaryKey(Id);
            GoodsSpu bo=new GoodsSpu(goodsSpupo);
            return new ReturnObject<>(bo);
        }
        catch (DataAccessException e){
            logger.error("getOneShareSpu: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject createSpu(Long Id, GoodsSpuVo vo) {
        try{
            GoodsSpuPo po=new GoodsSpuPo();
            po.setShopId(Id);
            po.setName(vo.getName());
            po.setDetail(vo.getDetail());
            po.setSpec(vo.getSpec());
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            po.setDisabled((byte) 0);
            po.setGoodsSn(vo.getName()+Id+vo.getDetail());
            goodsSpuPoMapper.insert(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("createSpu: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject changeSpu(Long shopId,Long Id, GoodsSpuVo vo) {
        GoodsSpuPo po=goodsSpuPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        try{
            po.setName(vo.getName());
            po.setDetail(vo.getDetail());
            po.setSpec(vo.getSpec());
            po.setGmtModified(LocalDateTime.now());
            goodsSpuPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("changeSpu: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject deleteSpu(Long shopId,Long Id) {
        GoodsSpuPo po=goodsSpuPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        try{
            po.setDisabled((byte) 1);
            po.setGmtModified(LocalDateTime.now());
            goodsSpuPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("deleteSpu: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject onlineSpu(Long shopId,Long Id) {
        GoodsSpuPo po=goodsSpuPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        try{
            po.setGmtModified(LocalDateTime.now());
            goodsSpuPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("onlineSpu: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject offlineSpu(Long shopId,Long Id) {
        GoodsSpuPo po=goodsSpuPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        try{
            po.setGmtModified(LocalDateTime.now());
            goodsSpuPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("offlineSpu: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject createFloatPrice(Long shopId, Long id,FloatPrice bo,Long userId) {
        GoodsSkuPo po=goodsSkuPoMapper.selectByPrimaryKey(id);
        if(bo.getQuantity()>po.getInventory())
        {
            return new ReturnObject(ResponseCode.FLOAT_PRICE_QUANTITY_BIGGER);
        }
        try{
            FloatPricePo po1=new FloatPricePo();
            po1.setActivityPrice(bo.getActivityPrice());
            po1.setBeginTime(bo.getBeginTime());
            po1.setEndTime(bo.getEndTime());
            po1.setQuantity(bo.getQuantity());
            po1.setCreatedBy(userId);
            po1.setGmtCreate(LocalDateTime.now());
            po1.setGmtModified(LocalDateTime.now());
            po1.setValid((byte) 1);
            po1.setGoodsSkuId(id);
            floatPricePoMapper.insert(po1);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("createFloatPrice: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject invalidFloatPrice(Long shopId, Long id,Long userId) {
        FloatPricePo po=floatPricePoMapper.selectByPrimaryKey(id);
        GoodsSkuPo po1=goodsSkuPoMapper.selectByPrimaryKey(po.getGoodsSkuId());
        GoodsSpuPo po2=goodsSpuPoMapper.selectByPrimaryKey(po1.getGoodsSpuId());
        if(!Objects.equals(po2.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.FLOAT_PRICE_NOT_IN_SHOP);
        }
        try{
            po.setValid((byte) 0);
            po.setInvalidBy(userId);
            floatPricePoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("invalidFloatPrice: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject createBrand(Long Id, Brand bo) {
        try{
            BrandPo po = new BrandPo();
            po.setDetail(bo.getDetail());
            po.setImageUrl(bo.getImageUrl());
            po.setName(bo.getName());
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            brandPoMapper.insert(po);
            BrandCreateVo vo=new BrandCreateVo(po);
            return new ReturnObject<>(vo);
        }
        catch (DataAccessException e){
            logger.error("createBrand: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject changeBrand(Long Id, BrandVo vo) {
        try{
            BrandPo po = brandPoMapper.selectByPrimaryKey(Id);
            po.setGmtModified(LocalDateTime.now());
            po.setName(vo.getName());
            po.setDetail(vo.getDetail());
            brandPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("changeBrand: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject deleteBrand(Long Id) {
        try{
            brandPoMapper.deleteByPrimaryKey(Id);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("deleteBrand: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject addSpuToCategory(Long spuId,Long id) {
        try{
            GoodsCategoryPo po=goodsCategoryPoMapper.selectByPrimaryKey(id);
            if(Objects.equals(po.getPid(),null))
            {
                return new ReturnObject(ResponseCode.SPU_CANT_ADD_TO_FIRST_CATEGORY);
            }
            GoodsSpuPo po1=goodsSpuPoMapper.selectByPrimaryKey(spuId);
            po1.setCategoryId(id);
            goodsSpuPoMapper.updateByPrimaryKey(po1);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("addSpuToCategory: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject removeSpuToCategory(Long spuId,Long id) {
        try{
            GoodsSpuPo po=goodsSpuPoMapper.selectByPrimaryKey(spuId);
            if(!Objects.equals(po.getCategoryId(),id))
            {
                return new ReturnObject(ResponseCode.SPU_NO_BELONG_TO_CATEGORY);
            }
            po.setCategoryId(null);
            goodsSpuPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("removeSpuToCategory: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject addSpuToBrand(Long spuId,Long id) {
        try{
            GoodsSpuPo po1=goodsSpuPoMapper.selectByPrimaryKey(spuId);
            po1.setBrandId(id);
            goodsSpuPoMapper.updateByPrimaryKey(po1);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("addSpuToBrand: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject removeSpuToBrand(Long spuId,Long id) {
        try{
            GoodsSpuPo po1=goodsSpuPoMapper.selectByPrimaryKey(spuId);
            if(!Objects.equals(po1.getBrandId(),id))
            {
                return new ReturnObject(ResponseCode.SPU_NO_BELONG_TO_BRAND);
            }
            po1.setBrandId(null);
            goodsSpuPoMapper.updateByPrimaryKey(po1);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("removeSpuToBrand: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject uploadSkuImg(Long shopId, Long id, MultipartFile multipartFile) {
        ShopPo shopPo = shopPoMapper.selectByPrimaryKey(shopId);

        if (null == shopPo) {
            return new ReturnObject(ResponseCode.SPU_NO_BELONG_TO_BRAND);
        }

        ReturnObject returnObject = new ReturnObject();
        try {
            returnObject = ImgHelper.remoteSaveImg(multipartFile, 2, davUsername, davPassword, baseUrl);

            //文件上传错误
            if (returnObject.getCode() != ResponseCode.OK) {
                logger.debug(returnObject.getErrmsg());
                return returnObject;
            }

            GoodsSkuPo goodsSkuPo = goodsSkuPoMapper.selectByPrimaryKey(id);
            String oldImageUrl = goodsSkuPo.getImageUrl();
            goodsSkuPo.setImageUrl(returnObject.getData().toString());
            int count = goodsSkuPoMapper.updateByPrimaryKey(goodsSkuPo);

            //数据库更新失败，需删除新增的图片
            if (count == 0) {
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(), davUsername, davPassword, baseUrl);
                return new ReturnObject(ResponseCode.SHOP_SKU_IMAGE_FAIL);
            }

            //数据库更新成功需删除旧图片，未设置则不删除
            if (oldImageUrl != null) {
                ImgHelper.deleteRemoteImg(oldImageUrl, davUsername, davPassword, baseUrl);
            }
        } catch (IOException e) {
            logger.debug("uploadImg: I/O Error:" + baseUrl);
            return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
        }
        return returnObject;
    }

    public ReturnObject uploadSpuImg(Long shopId, Long id, MultipartFile multipartFile) {
        ShopPo shopPo = shopPoMapper.selectByPrimaryKey(shopId);

        if (null == shopPo) {
            return new ReturnObject(ResponseCode.SPU_NO_BELONG_TO_BRAND);
        }

        ReturnObject returnObject = new ReturnObject();
        try {
            returnObject = ImgHelper.remoteSaveImg(multipartFile, 2, davUsername, davPassword, baseUrl);

            //文件上传错误
            if (returnObject.getCode() != ResponseCode.OK) {
                logger.debug(returnObject.getErrmsg());
                return returnObject;
            }

            GoodsSpuPo goodsSpuPo = goodsSpuPoMapper.selectByPrimaryKey(id);
            String oldImageUrl = goodsSpuPo.getImageUrl();
            goodsSpuPo.setImageUrl(returnObject.getData().toString());
            int count = goodsSpuPoMapper.updateByPrimaryKey(goodsSpuPo);

            //数据库更新失败，需删除新增的图片
            if (count == 0) {
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(), davUsername, davPassword, baseUrl);
                return new ReturnObject(ResponseCode.SHOP_SKU_IMAGE_FAIL);
            }

            //数据库更新成功需删除旧图片，未设置则不删除
            if (oldImageUrl != null) {
                ImgHelper.deleteRemoteImg(oldImageUrl, davUsername, davPassword, baseUrl);
            }
        } catch (IOException e) {
            logger.debug("uploadImg: I/O Error:" + baseUrl);
            return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
        }
        return returnObject;
    }

    public ReturnObject uploadBrandImg(Long shopId, Long id, MultipartFile multipartFile) {
        ShopPo shopPo = shopPoMapper.selectByPrimaryKey(shopId);

        if (null == shopPo) {
            return new ReturnObject(ResponseCode.SPU_NO_BELONG_TO_BRAND);
        }
        ReturnObject returnObject = new ReturnObject();
        try {
            returnObject = ImgHelper.remoteSaveImg(multipartFile, 2, davUsername, davPassword, baseUrl);

            //文件上传错误
            if (returnObject.getCode() != ResponseCode.OK) {
                logger.debug(returnObject.getErrmsg());
                return returnObject;
            }

            BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
            String oldImageUrl = brandPo.getImageUrl();
            brandPo.setImageUrl(returnObject.getData().toString());
            int count = brandPoMapper.updateByPrimaryKey(brandPo);

            //数据库更新失败，需删除新增的图片
            if (count == 0) {
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(), davUsername, davPassword, baseUrl);
                return new ReturnObject(ResponseCode.SHOP_SKU_IMAGE_FAIL);
            }

            //数据库更新成功需删除旧图片，未设置则不删除
            if (oldImageUrl != null) {
                ImgHelper.deleteRemoteImg(oldImageUrl, davUsername, davPassword, baseUrl);
            }
        } catch (IOException e) {
            logger.debug("uploadImg: I/O Error:" + baseUrl);
            return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
        }
        return returnObject;
    }


}
