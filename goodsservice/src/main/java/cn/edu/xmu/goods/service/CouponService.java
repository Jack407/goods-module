package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.bo.CouponSku;
import cn.edu.xmu.ooad.model.VoObject;
import com.github.pagehelper.PageInfo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.xmu.goods.dao.CouponDao;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import org.springframework.web.multipart.MultipartFile;


@Service
public class CouponService {

    private Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Autowired
    private CouponDao couponDao;

    public ReturnObject getCoupons( Long id,Long userId) {
        return couponDao.getCoupons(id,userId);
    }

    public ReturnObject createCouponAc( Long shopId,Long userId, CouponActivity bo){
        return couponDao.createCouponAc( shopId, userId,bo);
    }

    public ReturnObject<PageInfo<VoObject>> selectAllAc(Long shopId, Integer time, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = couponDao.selectAllAc(shopId,time, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject<PageInfo<VoObject>> selectAllInvalidAc(Long shopId, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = couponDao.selectAllInvalidAc(shopId, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject<PageInfo<VoObject>> getSkuInCouponAc(Long Id, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = couponDao.getSkuInCouponAc(Id, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject  getOneCouponAc(Long Id, Long shopId)
    {
        return couponDao.getOneCouponAc(Id,shopId);
    }

    public ReturnObject changeCouponAc(Long id, Long shopId, CouponActivity bo,Long userId){
        return couponDao.changeCouponAc(id, shopId, userId,bo);
    }

    public ReturnObject offlineCouponAc(Long id, Long shopId){
        return couponDao.offlineCouponAc(id, shopId);
    }

    public ReturnObject addSkuInCouponAc(Long Id, Long shopId, CouponSku bo){
        return couponDao.addSkuInCouponAc(Id, shopId,bo);
    }

    public ReturnObject removeSkuInCouponAc(Long Id, Long shopId){
        return couponDao.removeSkuInCouponAc(Id, shopId);
    }

    public ReturnObject<PageInfo<VoObject>> getUserCoupon(Long userId,Byte state, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = couponDao.getUserCoupon(userId,state, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject useCoupon(Long userId, Long Id){
        return couponDao.useCoupon(userId, Id);
    }

    public ReturnObject onlineCoupon(Long userId, Long Id, Long shopId){
        return couponDao.onlineCoupon(userId, Id, shopId);
    }

    public ReturnObject offlineCoupon(Long userId, Long Id, Long shopId){
        return couponDao.offlineCoupon(userId, Id, shopId);
    }

    public ReturnObject uploadAcImg(Long shopId, Long id, MultipartFile multipartFile) {
        return couponDao.uploadAcImg(shopId,id,multipartFile);
    }
}
