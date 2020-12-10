package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.PresaleDao;
import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopRetVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.AuditVo;
import cn.edu.xmu.ooad.model.VoObject;
import com.github.pagehelper.PageInfo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ShopService {

    private Logger logger = LoggerFactory.getLogger(ShopService.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ShopDao shopDao;

    public ReturnObject  createShop(Shop shop)
    {
        ReturnObject<ShopPo> returnObject=shopDao.createShop(shop);
        if(Objects.equals(returnObject.getCode(),ResponseCode.OK))
        {
            return new ReturnObject<>(new ShopRetVo(returnObject.getData()));
        }
        else
        {
            return returnObject;
        }
    }

    public ReturnObject  changeShop(Shop shop,Long id)
    {
        return shopDao.changeShop(shop,id);
    }

    public ReturnObject  closeShop(Long id)
    {
        return shopDao.closeShop(id);
    }

    public ReturnObject  auditShop( Long id,AuditVo vo)
    {
        return shopDao.auditShop(id,vo);
    }

    public ReturnObject  onlineShop(Long id)
    {
        return shopDao.onlineShop(id);
    }

    public ReturnObject  offlineShop(Long id)
    {
        return shopDao.offlineShop(id);
    }
}
