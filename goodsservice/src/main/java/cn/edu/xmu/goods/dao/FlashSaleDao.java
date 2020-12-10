package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.*;
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
public class FlashSaleDao {

    private static final Logger logger = LoggerFactory.getLogger(FlashSaleDao.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    FlashSalePoMapper flashSalePoMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    FlashSaleItemPoMapper flashSaleItemPoMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GoodsSkuPoMapper goodsSkuPoMapper;


    public ReturnObject createFlashSale(Long id, FlashSale bo) {
        try{
            FlashSalePoExample example=new FlashSalePoExample();
            FlashSalePoExample.Criteria criteria=example.createCriteria();
            criteria.andTimeSegIdEqualTo(id);
            List<FlashSalePo> flashSalePos=flashSalePoMapper.selectByExample(example);
            for(FlashSalePo po1:flashSalePos)
            {
                if(Objects.equals(po1.getFlashDate().toLocalDate(),bo.getFlashDate().toLocalDate()))
                {
                    return new ReturnObject(ResponseCode.FLASH_SALE_DATE_CONFLICT);
                }
            }
            FlashSalePo po=new FlashSalePo();
            po.setFlashDate(bo.getFlashDate());
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            po.setTimeSegId(id);
            flashSalePoMapper.insert(po);
            FlashSaleRetVo vo = new FlashSaleRetVo(po);
            return new ReturnObject<>(vo);
        }
        catch (DataAccessException e){
            logger.error("createFlashSale: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }


    public ReturnObject deleteFlashSale(Long id) {
        try{
            flashSalePoMapper.deleteByPrimaryKey(id);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("createFlashSale: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject changeFlashSale(Long id, FlashSale bo) {
        try{
            FlashSalePo po=flashSalePoMapper.selectByPrimaryKey(id);
            FlashSalePoExample example=new FlashSalePoExample();
            FlashSalePoExample.Criteria criteria=example.createCriteria();
            criteria.andTimeSegIdEqualTo(po.getTimeSegId());
            List<FlashSalePo> flashSalePos=flashSalePoMapper.selectByExample(example);
            for(FlashSalePo po1:flashSalePos)
            {
                if(Objects.equals(po1.getFlashDate().toLocalDate(),bo.getFlashDate().toLocalDate()))
                {
                    return new ReturnObject(ResponseCode.FLASH_SALE_DATE_CONFLICT);
                }
            }
            po.setGmtModified(LocalDateTime.now());
            po.setFlashDate(bo.getFlashDate());
            flashSalePoMapper.updateByPrimaryKey(po);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("changeFlashSale: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject createFlashSaleItem(Long id, FlashSaleItemVo vo) {
        try{
            FlashSaleItemPo po=new FlashSaleItemPo();
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            po.setGoodsSkuId(vo.getSkuId());
            po.setPrice(vo.getPrice());
            po.setQuantity(vo.getQuantity());
            po.setSaleId(id);
            flashSaleItemPoMapper.insert(po);
            FlashSaleItemRetVo vo1=new FlashSaleItemRetVo(po);
            return new ReturnObject<>(vo1);
        }
        catch (DataAccessException e){
            logger.error("createFlashSaleItem: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }

    public ReturnObject deleteFlashSaleItem(Long fid,Long id) {
        try{
            FlashSaleItemPo po=flashSaleItemPoMapper.selectByPrimaryKey(id);
            if(!Objects.equals(fid,po.getSaleId()))
            {
                return new ReturnObject(ResponseCode.FLASH_SALE_ITEM_AND_FLASH_SALE_CONFLICT);
            }
            flashSaleItemPoMapper.deleteByPrimaryKey(id);
            return new ReturnObject();
        }
        catch (DataAccessException e){
            logger.error("deleteFlashSaleItem: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            logger.error("other exception : " + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
    }


}
