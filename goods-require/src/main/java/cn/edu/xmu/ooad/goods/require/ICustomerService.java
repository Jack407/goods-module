package cn.edu.xmu.ooad.goods.require;

import cn.edu.xmu.ooad.goods.require.model.Customer;

public interface ICustomerService {


    /**
     * 根据顾客的id 返回顾客的具体信息
     */
    Customer getCustomer(Long customerId);
}
