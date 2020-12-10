package cn.edu.xmu.goods.service.impl;

import cn.edu.xmu.ooad.order.require.IShopService;
import cn.edu.xmu.ooad.order.require.models.ShopInfo;
import cn.edu.xmu.ooad.order.require.models.SkuInfo;

public class IShopServiceImpl implements IShopService {
    @Override
    public ShopInfo getShopInfo(Long shopId) {
        return null;
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        return null;
    }

    @Override
    public int decreaseStock(Long skuId, Integer count) {
        return 0;
    }

    @Override
    public int deleteFreightModel(Long freightModelId, Long shopId) {
        return 0;
    }
}
