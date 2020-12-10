package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.PresaleDao;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.vo.GrouponActivityRetVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.github.pagehelper.PageInfo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class PresaleService {

    private Logger logger = LoggerFactory.getLogger(PresaleService.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PresaleDao presaleDao;


    public ReturnObject<PageInfo<VoObject>> getPresaleActivity(Long shopId, Long timeline,Long skuId, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = presaleDao.getPresaleActivity(shopId,timeline,skuId, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject  getOnePresaleAc(Long Id, Long shopId,Byte state)
    {
        ReturnObject<List<PresaleActivityPo>> returnObject = presaleDao.getOnePresaleAc(Id, shopId, state);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            List<PresaleActivityRetVo> presaleActivityRetVos = new ArrayList<>();
            for (PresaleActivityPo po : returnObject.getData()) {
                PresaleActivityRetVo vo = new PresaleActivityRetVo(po);
                presaleActivityRetVos.add(vo);
            }
            return new ReturnObject<>(presaleActivityRetVos);
        } else {
            return returnObject;
        }
    }

    public ReturnObject  createPresaleAc(Long Id, Long shopId, Long userId, PresaleActivity bo)
    {
        return presaleDao.createPresaleAc(Id,shopId,userId,bo);
    }

    public ReturnObject  changePresaleAc(Long Id, Long shopId, Long userId, PresaleActivity bo)
    {
        return presaleDao.changePresaleAc(Id,shopId,userId,bo);
    }

    public ReturnObject  cancelPresaleAc(Long Id, Long shopId)
    {
        return presaleDao.cancelPresaleAc(Id,shopId);
    }

}
