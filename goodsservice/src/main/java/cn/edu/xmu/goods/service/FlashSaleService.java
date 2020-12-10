package cn.edu.xmu.goods.service;


import cn.edu.xmu.goods.dao.CommentDao;
import cn.edu.xmu.goods.dao.FlashSaleDao;
import cn.edu.xmu.goods.dao.PresaleDao;
import cn.edu.xmu.goods.model.bo.FlashSale;
import cn.edu.xmu.goods.model.vo.AuditVo;
import cn.edu.xmu.goods.model.vo.FlashSaleItemVo;
import cn.edu.xmu.goods.model.vo.FlashSaleVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlashSaleService {

    private Logger logger = LoggerFactory.getLogger(FlashSaleService.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private FlashSaleDao flashSaleDao;

    public ReturnObject createFlashSale(Long id, FlashSale bo)
    {
        return flashSaleDao.createFlashSale(id,bo);
    }

    public ReturnObject deleteFlashSale(Long id)
    {
        return flashSaleDao.deleteFlashSale(id);
    }

    public ReturnObject changeFlashSale(Long id, FlashSale bo)
    {
        return flashSaleDao.changeFlashSale(id,bo);
    }

    public ReturnObject createFlashSaleItem(Long id, FlashSaleItemVo vo)
    {
        return flashSaleDao.createFlashSaleItem(id,vo);
    }

    public ReturnObject deleteFlashSaleItem(Long fid,Long id)
    {
        return flashSaleDao.deleteFlashSaleItem(fid,id);
    }

}
