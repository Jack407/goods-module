package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.CouponActivityRetVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.dao.DataAccessException;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PresaleDao {

    private static final Logger logger = LoggerFactory.getLogger(PresaleDao.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    PresaleActivityPoMapper presaleActivityPoMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GoodsSpuPoMapper goodsSpuPoMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GoodsSkuPoMapper goodsSkuPoMapper;


    public ReturnObject<PageInfo<VoObject>> getPresaleActivity(Long shopId, Long timeline,Long skuId, Integer pageNum, Integer pageSize) {
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        if(!Objects.equals(shopId,0L))
        {
            criteria.andShopIdEqualTo(shopId);
        }
        if(!Objects.equals(skuId,0L))
        {
            criteria.andGoodsSkuIdEqualTo(skuId);
        }
        if(timeline==0L)
        {
            criteria.andBeginTimeGreaterThan(LocalDateTime.now());
        }
        else if(timeline==1L)
        {
            criteria.andBeginTimeBetween(LocalDateTime.now(),LocalDateTime.now().plusDays(1));
        }
        else if(timeline==2L)
        {
            criteria.andBeginTimeLessThan(LocalDateTime.now());
            criteria.andEndTimeGreaterThan(LocalDateTime.now());
        }
        else if(timeline==3L)
        {
            criteria.andEndTimeLessThan(LocalDateTime.now());
        }
        PageHelper.startPage(pageNum, pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        List<PresaleActivityPo> presaleAcPo = null;
        try {
            presaleAcPo = presaleActivityPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(presaleAcPo.size());
            for (PresaleActivityPo po : presaleAcPo) {
                PresaleActivity presaleActivity = new PresaleActivity(po);
                ret.add(presaleActivity);
            }
            PageInfo<VoObject> presaleActivityPage = PageInfo.of(ret);
            return new ReturnObject<>(presaleActivityPage);

        }
        catch (DataAccessException e){
            logger.error("getPresaleActivity: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject getOnePresaleAc(Long Id, Long shopId,Byte state) {
        GoodsSkuPo po=goodsSkuPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(goodsSpuPoMapper.selectByPrimaryKey(po.getGoodsSpuId()).getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        try{
            PresaleActivityPoExample example=new PresaleActivityPoExample();
            PresaleActivityPoExample.Criteria criteria=example.createCriteria();
            criteria.andGoodsSkuIdEqualTo(Id);
            if(Objects.equals(state,(byte)0)|| Objects.equals(state,(byte)1) || Objects.equals(state,(byte)2))
            {
                criteria.andStateEqualTo(state);
            }
            List<PresaleActivityPo> presaleActivityPos=presaleActivityPoMapper.selectByExample(example);
            return new ReturnObject<>(presaleActivityPos);
        }
        catch (DataAccessException e){
            logger.error("getOnePresaleAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject createPresaleAc(Long Id, Long shopId,Long userId,PresaleActivity bo) {
        GoodsSkuPo po=goodsSkuPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(goodsSpuPoMapper.selectByPrimaryKey(po.getGoodsSpuId()).getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.GOODS_NOT_IN_SHOP);
        }
        if(po.getInventory()>bo.getQuantity())
        {
            return new ReturnObject(ResponseCode.NO_SUFFICIENT_GOODS);
        }
        try{
            PresaleActivityPo presaleActivityPo=new PresaleActivityPo();
            presaleActivityPo.setAdvancePayPrice(bo.getAdvancePayPrice());
            presaleActivityPo.setBeginTime(bo.getBeginTime());
            presaleActivityPo.setEndTime(bo.getEndTime());
            presaleActivityPo.setPayTime(bo.getPayTime());
            presaleActivityPo.setGoodsSkuId(Id);
            presaleActivityPo.setQuantity(bo.getQuantity());
            presaleActivityPo.setName(bo.getName());
            presaleActivityPo.setRestPayPrice(bo.getRestPayPrice());
            presaleActivityPo.setShopId(shopId);
            presaleActivityPo.setGmtCreate(LocalDateTime.now());
            presaleActivityPo.setGmtModified(LocalDateTime.now());
            presaleActivityPo.setState((byte) 1);
            presaleActivityPoMapper.insert(presaleActivityPo);
            PresaleActivityRetVo vo=new PresaleActivityRetVo(presaleActivityPo);
            return new ReturnObject<>(vo);
        }
        catch (DataAccessException e){
            logger.error("createPresaleAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject changePresaleAc(Long Id, Long shopId,Long userId,PresaleActivity bo) {
        PresaleActivityPo po=presaleActivityPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_NOT_IN_SHOP);
        }
        if(!Objects.equals(bo.getQuantity(),null)) {
            if (bo.getQuantity() > goodsSkuPoMapper.selectByPrimaryKey(po.getGoodsSkuId()).getInventory()) {
                return new ReturnObject(ResponseCode.NO_SUFFICIENT_GOODS);
            }
        }
        try{
            po.setAdvancePayPrice(bo.getAdvancePayPrice());
            po.setBeginTime(bo.getBeginTime());
            po.setEndTime(bo.getEndTime());
            po.setPayTime(bo.getPayTime());
            po.setQuantity(bo.getQuantity());
            po.setName(bo.getName());
            po.setRestPayPrice(bo.getRestPayPrice());
            presaleActivityPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("changePresaleAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject cancelPresaleAc(Long Id, Long shopId) {
        PresaleActivityPo po=presaleActivityPoMapper.selectByPrimaryKey(Id);
        if(!Objects.equals(po.getShopId(),shopId))
        {
            return new ReturnObject(ResponseCode.ACTIVITY_NOT_IN_SHOP);
        }
        try{
            po.setState((byte) 0);
            presaleActivityPoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("changePresaleAc: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }
}
