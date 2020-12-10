package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CommentDao;
import cn.edu.xmu.goods.dao.PresaleDao;
import cn.edu.xmu.goods.model.vo.AuditVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private Logger logger = LoggerFactory.getLogger(CommentService.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CommentDao commentDao;


    public ReturnObject<PageInfo<VoObject>> getSkuComment(Long id, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = commentDao.getSkuComment(id, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject  auditComment(Long id, AuditVo vo)
    {
        return commentDao.auditComment(id,vo);
    }

    public ReturnObject<PageInfo<VoObject>> getUserComment(Long userId, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = commentDao.getUserComment(userId, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject<PageInfo<VoObject>> getAdComment(Byte state, Integer pageNum, Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = commentDao.getAdComment(state, pageNum, pageSize);
        return returnObject;
    }

    public ReturnObject  createComment(Long id,Long userId, CommentVo vo)
    {
        return commentDao.createComment(id,userId,vo);
    }

}
