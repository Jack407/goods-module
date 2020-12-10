package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.GrouponActivity;
import lombok.Data;

/**
 * 团购活动状态VO
 * @author 李狄翰
 * @date 2020/11/10 18:41
 */
@Data
public class GrouponActivityStateVo {

    private Long Code;

    private String name;
    public GrouponActivityStateVo(GrouponActivity.State state){
        Code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }
}
