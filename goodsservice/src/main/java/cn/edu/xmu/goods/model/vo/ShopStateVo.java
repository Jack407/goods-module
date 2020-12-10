package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Shop;
import lombok.Data;

/**
 * 商店状态VO
 * @author 李狄翰
 * @date 2020/11/10 18:41
 */
@Data
public class ShopStateVo {

    private Long Code;

    private String name;
    public ShopStateVo(Shop.State state){
        Code=Long.valueOf(state.getCode());
        name=state.getDescription();
    }
}
