package cn.edu.xmu.ooad.goods.require;

import cn.edu.xmu.ooad.goods.require.model.Comment;

public interface ICommentService {


    /**
     * 根据时间段id 获取一个 时间段的具体信息
     */
    Comment getComment(Long orderItemId);
}
