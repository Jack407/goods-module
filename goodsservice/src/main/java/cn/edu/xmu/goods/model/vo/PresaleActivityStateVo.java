package cn.edu.xmu.goods.model.vo;


import cn.edu.xmu.goods.model.bo.PresaleActivity;
import lombok.Data;

/**
 * 预售活动状态VO
 * @author 李狄翰
 * @date 2020/11/10 18:41
 */
@Data
public class PresaleActivityStateVo {

    private Long Code;

    private String name;
    public PresaleActivityStateVo(PresaleActivity.State state){
        Code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }
}
