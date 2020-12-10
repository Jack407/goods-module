package cn.edu.xmu.ooad.goods.require.model;
import lombok.Data;

@Data
public class Comment {

    private Long customer_id;
    private Long sku_id;


    public Comment(Long customer_id, Long sku_id)
    {
        this.customer_id = customer_id;
        this.sku_id = sku_id;
    }

}
