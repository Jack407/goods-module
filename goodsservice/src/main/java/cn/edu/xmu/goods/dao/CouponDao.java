package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.CouponActivitySimpleRetVo;
import cn.edu.xmu.goods.model.vo.CouponRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ImgHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CouponDao {

    private static final Logger logger = LoggerFactory.getLogger(CouponDao.class);

    private static final String REDIS_COUPON_ACTIVITY_KEY = "couponActivity";
    private static final String REDIS_COUPON_ACTIVITY_TOTAL_KEY = "couponActivityTotal";
    private static final String REDIS_COUPON_ACTIVITY_USER_KEY = "couponActivityUser";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CouponActivityPoMapper couponActivityPoMapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CouponSkuPoMapper couponSkuPoMapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CouponPoMapper couponPoMapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GoodsSpuPoMapper goodsSpuPoMapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GoodsSkuPoMapper goodsSkuPoMapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ShopPoMapper shopPoMapper;

    @Value("${privilegeservice.dav.username}")
    private String davUsername;

    @Value("${privilegeservice.dav.password}")
    private String davPassword;

    @Value("${privilegeservice.dav.baseUrl}")
    private String baseUrl;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public ReturnObject createCouponAc( Long shopId, Long userId, CouponActivity bo)
    {
        CouponActivityPo couponActivityPo=new CouponActivityPo();
        couponActivityPo.setName(bo.getName());
        couponActivityPo.setBeginTime(bo.getBeginTime());
        couponActivityPo.setEndTime(bo.getEndTime());
        couponActivityPo.setCouponTime(bo.getCouponTime());
        couponActivityPo.setState((byte)1);
        couponActivityPo.setShopId(shopId);
        couponActivityPo.setQuantity(bo.getQuantity());
        couponActivityPo.setValidTerm(bo.getValidTerm());
        couponActivityPo.setStrategy(bo.getStrategy());
        couponActivityPo.setCreatedBy(userId);
        couponActivityPo.setModiBy(userId);
        couponActivityPo.setGmtCreate(LocalDateTime.now());
        couponActivityPo.setGmtModified(LocalDateTime.now());
        couponActivityPoMapper.insert(couponActivityPo);
        CouponActivitySimpleRetVo vo=new CouponActivitySimpleRetVo(couponActivityPo);
        return new ReturnObject<>(vo);
    }

    public ReturnObject getCoupons(Long id, Long userid) {
        CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
        if(!Objects.equals(couponActivityPo.getState(),(byte) 1))
        {
            return new ReturnObject(ResponseCode.COUPON_CANT_GET);
        }
        if (couponActivityPo.getQuantitiyType() == (byte) 0) {
            CouponPoExample example = new CouponPoExample();
            CouponPoExample.Criteria criteria = example.createCriteria();
            criteria.andActivityIdEqualTo(id);
            criteria.andCustomerIdEqualTo(userid);
            List<CouponPo> results = couponPoMapper.selectByExample(example);
            if (results.size() >= couponActivityPo.getQuantity()) {
                return new ReturnObject(ResponseCode.COUPON_IS_MAX);
            }
            if (couponActivityPo.getValidTerm() == (byte) 0) {
                CouponPo couponPo = new CouponPo();
                couponPo.setActivityId(id);
                couponPo.setName("双十一");
                couponPo.setCouponSn("111");
                couponPo.setBeginTime(couponActivityPo.getBeginTime());
                couponPo.setEndTime(couponActivityPo.getEndTime());
                couponPo.setCustomerId(userid);
                couponPoMapper.insert(couponPo);
            } else {
                CouponPo couponPo1 = new CouponPo();
                couponPo1.setActivityId(id);
                couponPo1.setName("双十一");
                couponPo1.setCouponSn("111");
                couponPo1.setBeginTime(LocalDateTime.now());
                couponPo1.setEndTime(LocalDateTime.now().plusDays((long) couponActivityPo.getValidTerm()));
                couponPo1.setCustomerId(userid);
                couponPoMapper.insert(couponPo1);
            }
            return new ReturnObject();
        } else {
            return receiveCoupons(id, userid);
        }
    }

    /**
     * 1.获取活动信息并缓存到redis中
     * 2.判断获取活动是否在进行中
     * 3.判断是否还有优惠券
     * 4.判断该用户是否领取过优惠券
     *
     * @param id
     * @param userId
     * @return
     */
    public synchronized ReturnObject receiveCoupons(Long id, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        String activityKey = REDIS_COUPON_ACTIVITY_KEY + id;
        String activityTotalKey = REDIS_COUPON_ACTIVITY_TOTAL_KEY + id;
        String activityUserKey = REDIS_COUPON_ACTIVITY_USER_KEY + id;

        //从redis中取出活动信息
        CouponActivityPo couponActivityPo = (CouponActivityPo) redisTemplate.opsForValue().get(activityKey);
        List<Long> userIds = (List<Long>) redisTemplate.opsForValue().get(activityUserKey);
        Integer total = (Integer) redisTemplate.opsForValue().get(activityTotalKey);
        //redis中不存在则从数据库取出放入redis
        if (null == couponActivityPo) {
            couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
            userIds = new ArrayList<>();
            total = couponActivityPo.getQuantity();
            redisTemplate.opsForValue().set(activityKey, couponActivityPo);
            redisTemplate.opsForValue().set(activityTotalKey, couponActivityPo.getQuantity());
            redisTemplate.opsForValue().set(activityUserKey, userIds);
        }
        //判断是否还有券
        if (total.compareTo(0) == 0) {
            return new ReturnObject(ResponseCode.COUPON_FINISH);
        }
        if (userIds.contains(userId)) {
            return new ReturnObject(ResponseCode.COUPON_REPEAT);
        }

        //写入领券记录表
        CouponPo couponPo = new CouponPo();
        couponPo.setActivityId(id);
        couponPo.setName("redis");
        couponPo.setCouponSn("111");
        couponPo.setCustomerId(userId);
        couponPo.setGmtCreate(LocalDateTime.now());
        couponPo.setState((byte) 0);
        if (Objects.equals(couponActivityPo.getValidTerm(), "0")) {
            couponPo.setBeginTime(couponActivityPo.getBeginTime());
            couponPo.setEndTime(couponActivityPo.getEndTime());
        } else {
            couponPo.setBeginTime(LocalDateTime.now());
            couponPo.setEndTime(LocalDateTime.now().plusDays((long) couponActivityPo.getValidTerm()));
        }
        couponPoMapper.insert(couponPo);
        //操作缓存数据
        userIds.add(userId);
        total = total - 1;
        redisTemplate.opsForValue().set(activityTotalKey, total);
        redisTemplate.opsForValue().set(activityUserKey, userIds);
        if (total == 0) {
            couponActivityPo.setQuantity(0);
            couponActivityPoMapper.updateByPrimaryKey(couponActivityPo);
        }
        CouponRetVo vo = new CouponRetVo(couponPo);
        return new ReturnObject<>(vo);
    }

    public ReturnObject<PageInfo<VoObject>> selectAllAc(Long shopId,Integer time, Integer pageNum, Integer pageSize) {
        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        if(!Objects.equals(null,shopId)) {
            criteria.andShopIdEqualTo(shopId);
        }
        //分页查询
        if(time==0)
        {
            criteria.andBeginTimeGreaterThan(LocalDateTime.now());
        }
        else if(time==1)
        {
            criteria.andBeginTimeBetween(LocalDateTime.now(),LocalDateTime.now().plusDays(1));
        }
        else if(time==2)
        {
            criteria.andBeginTimeLessThan(LocalDateTime.now());
            criteria.andEndTimeGreaterThan(LocalDateTime.now());
        }
        else if(time==3)
        {
            criteria.andEndTimeLessThan(LocalDateTime.now());
        }
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<CouponActivityPo> couponAcPo = null;
        try {
            couponAcPo = couponActivityPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(couponAcPo.size());
            for (CouponActivityPo po : couponAcPo) {
                CouponActivity couponActivity = new CouponActivity(po);
                ret.add(couponActivity);
            }
            PageInfo<VoObject> couponActivityPage = PageInfo.of(ret);
            return new ReturnObject<>(couponActivityPage);

        }
        catch (DataAccessException e){
            logger.error("selectAllAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject<PageInfo<VoObject>> selectAllInvalidAc(Long shopId, Integer pageNum, Integer pageSize) {
        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        //分页查询
        criteria.andStateEqualTo((byte)1);
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<CouponActivityPo> couponAcPo = null;
        try {
            couponAcPo = couponActivityPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(couponAcPo.size());
            for (CouponActivityPo po : couponAcPo) {
                CouponActivity couponActivity = new CouponActivity(po);
                ret.add(couponActivity);
            }
            PageInfo<VoObject> couponActivityPage = PageInfo.of(ret);
            return new ReturnObject<>(couponActivityPage);

        }
        catch (DataAccessException e){
            logger.error("selectAllInvalidAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject<PageInfo<VoObject>> getSkuInCouponAc(Long Id, Integer pageNum, Integer pageSize) {
        CouponSkuPoExample example = new CouponSkuPoExample();
        CouponSkuPoExample.Criteria criteria = example.createCriteria();
        criteria.andActivityIdEqualTo(Id);
        //分页查询
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<CouponSkuPo> couponSkuPo = null;
        try {
            couponSkuPo = couponSkuPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(couponSkuPo.size());
            for (CouponSkuPo po : couponSkuPo) {
                GoodsSkuPo goodsSkuPo=goodsSkuPoMapper.selectByPrimaryKey(po.getSkuId());
                GoodsSku goodsSku=new GoodsSku(goodsSkuPo);
                ret.add(goodsSku);
            }
            PageInfo<VoObject> couponActivityPage = PageInfo.of(ret);
            return new ReturnObject<>(couponActivityPage);

        }
        catch (DataAccessException e){
            logger.error("getSpuInCouponAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject  getOneCouponAc(Long Id, Long shopId)
    {
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_SHOP_NOT_MATCH);
        }
        else
        {
            try {
                CouponActivitySimpleRetVo vo=new CouponActivitySimpleRetVo(po);
                return new ReturnObject<>(vo);
            }
            catch (DataAccessException e){
                logger.error("getOneCouponAc: DataAccessException:" + e.getMessage());
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
            catch (Exception e) {
                // 其他Exception错误
                logger.error("other exception : " + e.getMessage());
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
            }
        }
    }

    public ReturnObject  changeCouponAc(Long Id, Long shopId,Long userId,CouponActivity bo)
    {
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_SHOP_NOT_MATCH);
        }
        if(!Objects.equals(po.getState(),(byte) 0))
        {
            return new ReturnObject(ResponseCode.AC_CANT_CHANGE);
        }
        else
        {
            try{
                po.setName(bo.getName());
                po.setQuantity(bo.getQuantity());
                po.setBeginTime(bo.getBeginTime());
                po.setEndTime(bo.getEndTime());
                po.setStrategy(bo.getStrategy());
                po.setGmtModified(LocalDateTime.now());
                po.setModiBy(userId);
                couponActivityPoMapper.updateByPrimaryKey(po);
                return new ReturnObject();
            }
            catch (DataAccessException e){
                logger.error("changeCouponAc: DataAccessException:" + e.getMessage());
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
            catch (Exception e) {
                // 其他Exception错误
                logger.error("other exception : " + e.getMessage());
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
            }
        }
    }

    public ReturnObject  offlineCouponAc(Long Id, Long shopId)
    {
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_SHOP_NOT_MATCH);
        }
        if(!Objects.equals(po.getState(),(byte) 0))
        {
            return new ReturnObject(ResponseCode.AC_CANT_CHANGE);
        }
        else
        {
            try{
                po.setState((byte)2);
                couponActivityPoMapper.updateByPrimaryKey(po);
                CouponPoExample example = new CouponPoExample();
                CouponPoExample.Criteria criteria = example.createCriteria();
                criteria.andActivityIdEqualTo(Id);
                List<CouponPo> couponPos=couponPoMapper.selectByExample(example);
                for(CouponPo po1:couponPos)
                {
                    if(po1.getState()==(byte)0 || po1.getState()==(byte)1)
                    {
                        po1.setState((byte)3);
                        couponPoMapper.updateByPrimaryKey(po1);
                    }
                }
                return new ReturnObject();
            }
            catch (DataAccessException e){
                logger.error("offlineCouponAc: DataAccessException:" + e.getMessage());
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
            }
            catch (Exception e) {
                // 其他Exception错误
                logger.error("other exception : " + e.getMessage());
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
            }
        }
    }

    public ReturnObject  addSkuInCouponAc(Long Id, Long shopId, CouponSku bo)
    {
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_SHOP_NOT_MATCH);
        }
        try{
            for(Long id:bo.getSkuIdList()) {
                GoodsSkuPo goodsSkuPo= goodsSkuPoMapper.selectByPrimaryKey(id);
                if (!Objects.equals(goodsSpuPoMapper.selectByPrimaryKey(goodsSkuPo.getGoodsSpuId()).getShopId(), shopId)) {
                    return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
                }
                CouponSkuPo po2 = new CouponSkuPo();
                po2.setActivityId(Id);
                po2.setSkuId(id);
                po2.setGmtCreate(LocalDateTime.now());
                po2.setGmtModified(LocalDateTime.now());
                couponSkuPoMapper.insert(po2);
            }
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("addSpuInCouponAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject  removeSkuInCouponAc(Long Id, Long shopId)
    {
        CouponSkuPo po=couponSkuPoMapper.selectByPrimaryKey(Id);
        CouponActivityPo po1=couponActivityPoMapper.selectByPrimaryKey(po.getActivityId());
        if(!Objects.equals(po1.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_SHOP_NOT_MATCH);
        }
        try{
            couponSkuPoMapper.deleteByPrimaryKey(Id);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("addSpuInCouponAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject<PageInfo<VoObject>> getUserCoupon(Long userId,Byte state,Integer pageNum, Integer pageSize) {
        CouponPoExample example = new CouponPoExample();
        CouponPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        //分页查询
        if(Objects.equals(state,(byte) 1) || Objects.equals(state,(byte) 2) || Objects.equals(state,(byte) 3))
        {
            criteria.andStateEqualTo(state);
        }
        //分页查询
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<CouponPo> couponPo = null;
        try {
            couponPo = couponPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(couponPo.size());
            for (CouponPo po : couponPo) {
                Coupon coupon=new Coupon(po);
                ret.add(coupon);
            }
            PageInfo<VoObject> couponPage = PageInfo.of(ret);
            return new ReturnObject<>(couponPage);

        }
        catch (DataAccessException e){
            logger.error("getUserCoupon: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject useCoupon(Long userId, Long Id)
    {
        CouponPo po=couponPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getCustomerId(),userId))
        {
            return new ReturnObject(ResponseCode.COUPON_USER_NO_MATCH);
        }
        if(LocalDateTime.now().isAfter(po.getBeginTime()) && LocalDateTime.now().isBefore(po.getEndTime()) && Objects.equals(po.getState(),(byte) 1))
        {
            return new ReturnObject(ResponseCode.COUPON_STATENOTALLOW);
        }
        try{
            po.setState((byte) 2);
            couponPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("useCoupon: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }



    public ReturnObject onlineCoupon(Long userId, Long Id,Long shopId)
    {
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getState(),(byte) 0))
        {
            return new ReturnObject(ResponseCode.AC_CANT_CHANGE);
        }
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_SHOP_NOT_MATCH);
        }
        try{
            po.setState((byte)1);
            couponActivityPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("useCoupon: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject offlineCoupon(Long userId, Long Id,Long shopId)
    {
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getState(),(byte) 1))
        {
            return new ReturnObject(ResponseCode.AC_CANT_CHANGE);
        }
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_SHOP_NOT_MATCH);
        }
        try{
            po.setState((byte)0);
            couponActivityPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("useCoupon: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject uploadAcImg(Long shopId, Long id, MultipartFile multipartFile) {
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

            CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
            String oldImageUrl = couponActivityPo.getImageUrl();
            couponActivityPo.setImageUrl(returnObject.getData().toString());
            int count = couponActivityPoMapper.updateByPrimaryKey(couponActivityPo);

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
