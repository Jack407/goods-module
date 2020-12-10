package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.GrouponDao;
import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.GrouponActivityRetVo;
import cn.edu.xmu.goods.model.vo.ShopRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.github.pagehelper.PageInfo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class GrouponService {

    private Logger logger = LoggerFactory.getLogger(PresaleService.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private GrouponDao grouponDao;

    public ReturnObject<PageInfo<VoObject>> getGrouponActivity(Long shopId, Long timeline, Long spuId,  Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = grouponDao.getGrouponActivity(shopId, timeline, spuId, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject<PageInfo<VoObject>> getShopGrouponActivity(Long shopId, Byte state, Long spuId,LocalDateTime startTime, LocalDateTime endTime, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = grouponDao.getShopGrouponActivity(shopId, state, spuId,startTime,endTime, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject getOneGrouponAc(Long Id, Long shopId, Byte state) {
        ReturnObject<List<GrouponActivityPo>> returnObject = grouponDao.getOneGrouponAc(Id, shopId, state);
        if (Objects.equals(returnObject.getCode(), ResponseCode.OK)) {
            List<GrouponActivityRetVo> grouponActivityRetVos = new ArrayList<>();
            for (GrouponActivityPo po : returnObject.getData()) {
                GrouponActivityRetVo vo = new GrouponActivityRetVo(po);
                grouponActivityRetVos.add(vo);
            }
            return new ReturnObject<>(grouponActivityRetVos);
        } else {
            return returnObject;
        }
    }

    public ReturnObject createGrouponAc(Long Id, Long shopId, Long userId, GrouponActivity bo) {
        return grouponDao.createGrouponAc(Id, shopId, userId, bo);
    }

    public ReturnObject changeGrouponAc(Long Id, Long shopId, Long userId, GrouponActivity bo) {
        return grouponDao.changeGrouponAc(Id, shopId, userId, bo);
    }

    public ReturnObject cancelGrouponAc(Long Id, Long shopId) {
        return grouponDao.cancelGrouponAc(Id, shopId);
    }
}
