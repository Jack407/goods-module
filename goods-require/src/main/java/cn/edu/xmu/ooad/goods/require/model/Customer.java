package cn.edu.xmu.ooad.goods.require.model;

import lombok.Data;

@Data
public class Customer {

    private Long id;
    private String user_name;
    private String real_name;

    public Customer(Long id, String user_name, String real_name)
    {
        this.id = id;
        this.user_name = user_name;
        this.real_name = real_name;
    }
}
