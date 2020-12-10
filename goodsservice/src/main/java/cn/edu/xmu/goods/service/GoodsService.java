package cn.edu.xmu.goods.service;


import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.dao.PresaleDao;
import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.vo.BrandVo;
import cn.edu.xmu.goods.model.vo.GoodsCategoryVo;
import cn.edu.xmu.goods.model.vo.GoodsSpuVo;
import cn.edu.xmu.ooad.model.VoObject;
import com.github.pagehelper.PageInfo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class GoodsService {

    private Logger logger = LoggerFactory.getLogger(GoodsService.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private GoodsDao goodsDao;

    public ReturnObject<PageInfo<VoObject>> getSku(Long shopId, String skuSn,Long spuId,String spuSn, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = goodsDao.getSku(shopId,skuSn,spuId,spuSn, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject  getOneSku(Long Id)
    {
        return goodsDao.getOneSku(Id);
    }

    public ReturnObject  addSkuToSpu(Long Id, Long shopId, Long userId, GoodsSku bo)
    {
        return goodsDao.addSkuToSpu(Id,shopId,userId,bo);
    }

    public ReturnObject  deleteSku(Long Id, Long shopId)
    {
        return goodsDao.deleteSku(Id,shopId);
    }

    public ReturnObject  changeSku(Long Id, Long shopId, GoodsSku bo)
    {
        return goodsDao.changeSku(Id,shopId,bo);
    }

    public ReturnObject  getCategory(Long Id)
    {
        return goodsDao.getCategory(Id);
    }

    public ReturnObject  createCategory(Long Id, GoodsCategoryVo vo)
    {
        return goodsDao.createCategory(Id,vo);
    }

    public ReturnObject  changeCategory(Long Id, GoodsCategoryVo vo)
    {
        return goodsDao.changeCategory(Id,vo);
    }

    public ReturnObject  deleteCategory(Long Id)
    {
        return goodsDao.deleteCategory(Id);
    }

    public ReturnObject<PageInfo<VoObject>> getBrands(Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = goodsDao.getBrands( pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject  getOneSpu(Long Id)
    {
        return goodsDao.getOneSpu(Id);
    }

    public ReturnObject  getOneShareSpu(Long Id,Long sid)
    {
        return goodsDao.getOneShareSpu(Id,sid);
    }

    public ReturnObject  createSpu(Long Id, GoodsSpuVo vo)
    {
        return goodsDao.createSpu(Id,vo);
    }

    public ReturnObject  changeSpu(Long shopId, Long Id, GoodsSpuVo vo)
    {
        return goodsDao.changeSpu(shopId,Id,vo);
    }

    public ReturnObject  deleteSpu(Long shopId, Long Id)
    {
        return goodsDao.deleteSpu(shopId,Id);
    }

    public ReturnObject  onlineSpu(Long shopId, Long Id)
    {
        return goodsDao.onlineSpu(shopId,Id);
    }

    public ReturnObject  offlineSpu(Long shopId, Long Id)
    {
        return goodsDao.offlineSpu(shopId,Id);
    }

    public ReturnObject  createFloatPrice(Long shopId,Long id, FloatPrice bo,Long userId)
    {
        return goodsDao.createFloatPrice(shopId,id,bo,userId);
    }

    public ReturnObject  invalidFloatPrice(Long shopId,Long id,Long userId)
    {
        return goodsDao.invalidFloatPrice(shopId,id,userId);
    }

    public ReturnObject  createBrand(Long id, Brand bo)
    {
        return goodsDao.createBrand(id,bo);
    }

    public ReturnObject  changeBrand(Long id, BrandVo vo)
    {
        return goodsDao.changeBrand(id,vo);
    }

    public ReturnObject  deleteBrand(Long id)
    {
        return goodsDao.deleteBrand(id);
    }

    public ReturnObject  addSpuToCategory(Long spuId,Long id)
    {
        return goodsDao.addSpuToCategory(spuId,id);
    }

    public ReturnObject  removeSpuToCategory(Long spuId,Long id)
    {
        return goodsDao.removeSpuToCategory(spuId,id);
    }

    public ReturnObject  addSpuToBrand(Long spuId,Long id)
    {
        return goodsDao.addSpuToBrand(spuId,id);
    }

    public ReturnObject  removeSpuToBrand(Long spuId,Long id)
    {
        return goodsDao.removeSpuToBrand(spuId,id);
    }

    public ReturnObject uploadSkuImg(Long shopId, Long id, MultipartFile multipartFile) {
        return goodsDao.uploadSkuImg(shopId,id,multipartFile);
    }

    public ReturnObject uploadSpuImg(Long shopId, Long id, MultipartFile multipartFile) {
        return goodsDao.uploadSpuImg(shopId,id,multipartFile);
    }

    public ReturnObject uploadBrandImg(Long shopId, Long id, MultipartFile multipartFile) {
        return goodsDao.uploadBrandImg(shopId,id,multipartFile);
    }

}
