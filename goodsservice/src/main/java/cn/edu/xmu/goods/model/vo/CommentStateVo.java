package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Comment;
import lombok.Data;

/**
 * 优惠券状态VO
 * @author 李狄翰
 * @date 2020/11/10 18:41
 */
@Data
public class CommentStateVo {

    private Long Code;

    private String name;
    public CommentStateVo(Comment.State state){
        Code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }
}
