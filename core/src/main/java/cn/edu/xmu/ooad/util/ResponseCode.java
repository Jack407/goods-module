package cn.edu.xmu.ooad.util;

/**
 * 返回的错误码
 * @author Ming Qiu
 */
public enum ResponseCode {
    OK(0,"成功"),
    /***************************************************
     *    系统级错误
     **************************************************/
    INTERNAL_SERVER_ERR(500,"服务器内部错误"),
    //所有需要登录才能访问的API都可能会返回以下错误
    AUTH_INVALID_JWT(501,"JWT不合法"),
    AUTH_JWT_EXPIRED(502,"JWT过期"),

    //以下错误码提示可以自行修改
    //--------------------------------------------
    FIELD_NOTVALID(503,"字段不合法"),
    //所有路径带id的API都可能返回此错误
    RESOURCE_ID_NOTEXIST(504,"操作的资源id不存在"),
    RESOURCE_ID_OUTSCOPE(505,"操作的资源id不是自己的对象"),
    FILE_NO_WRITE_PERMISSION(506,"目录文件夹没有写入的权限"),
    RESOURCE_FALSIFY(507, "信息签名不正确"),
    IMG_FORMAT_ERROR(508,"图片格式不正确"),
    IMG_SIZE_EXCEED(509,"图片大小超限"),

    //--------------------------------------------


    /***************************************************
     *    其他模块错误码
     **************************************************/
    ADDRESS_OUTLIMIT(601,"达到地址簿上限"),
    REGION_OBSOLETE(602,"地区已废弃"),
    ADVERTISEMENT_OUTLIMIT(603,"达到时段广告上限"),
    TIMESEG_CONFLICT(604,"时段冲突"),
    SHAREACT_CONFLICT(605,"分享活动时段冲突"),
    ORDERITEM_NOTSHARED(606,"订单明细无分享记录"),
    FLASHSALE_OUTLIMIT(607,"达到时段秒杀上限"),
    ADVERTISEMENT_STATENOTALLOW(608,"广告状态禁止"),
    AFTERSALE_STATENOTALLOW(609,"售后单状态禁止"),
    /***************************************************
     *    权限模块错误码
     **************************************************/
    AUTH_INVALID_ACCOUNT(700, "用户名不存在或者密码错误"),
    AUTH_ID_NOTEXIST(701,"登录用户id不存在"),
    AUTH_USER_FORBIDDEN(702,"用户被禁止登录"),
    AUTH_NEED_LOGIN(704, "需要先登录"),
    AUTH_NOT_ALLOW(705,"无权限访问"),
    USER_NAME_REGISTERED( 731,"用户名已被注册"),
    EMAIL_REGISTERED(732, "邮箱已被注册"),
    MOBILE_REGISTERED(733,"电话已被注册"),
    ROLE_REGISTERED(736, "角色名已存在"),
    USER_ROLE_REGISTERED(737, "用户已拥有该角色"),
    PASSWORD_SAME(741,"不能与旧密码相同"),
    URL_SAME(742,"权限url与RequestType重复"),
    PRIVILEGE_SAME(743,"权限名称重复"),
    PRIVILEGE_BIT_SAME(744,"权限位重复"),
    EMAIL_WRONG(745,"与系统预留的邮箱不一致"),
    MOBILE_WRONG(746,"与系统预留的电话不一致"),
    USERPROXY_CONFLICT(747,"同一时间段有冲突的代理关系"),
    EMAIL_NOTVERIFIED(748,"Email未确认"),
    MOBILE_NOTVERIFIED(749,"电话号码未确认"),
    USERPROXY_BIGGER(750,"开始时间要小于失效时间"),
    USERPROXY_SELF(751,"自己不可以代理自己"),
    /***************************************************
     *    订单模块错误码
     **************************************************/
    ORDER_STATENOTALLOW(801,"订单状态禁止"),
    FREIGHTNAME_SAME(802,"运费模板名重复"),
    REGION_SAME(803,"运费模板中该地区已经定义"),
    REFUND_MORE(804,"退款金额超过支付金额"),
    /***************************************************
     *    商品模块错误码
     **************************************************/
    SKU_NOTENOUGH(900,"商品规格库存不够"),
    SKUSN_SAME(901,"商品规格重复"),
    SKUPRICE_CONFLICT(902,"商品浮动价格时间冲突"),
    USER_NOTBUY(903,"用户没有购买此商品"),
    COUPONACT_STATENOTALLOW(904,"优惠活动状态禁止"),
    COUPON_STATENOTALLOW(905,"优惠卷状态禁止"),
    PRESALE_STATENOTALLOW(906,"预售活动状态禁止"),
    GROUPON_STATENOTALLOW(907,"团购活动状态禁止"),
    USER_HASSHOP(908,"用户已经有店铺"),
    COUPON_NOTBEGIN(909,"未到优惠卷领取时间"),
    COUPON_FINISH(910,"优惠卷领罄"),
    COUPON_END(911,"优惠卷活动终止"),
    COUPON_ACTIVITY_TYPE_ERROR(912,"优惠活动的类型不为总数控制类型"),
    COUPON_IS_MAX(913,"用户所领取的优惠券已经达到了上限"),
    COUPON_IS_OUT(914,"用户所领取的优惠券已经领完"),
    SPU_SHOP_NOT_MATCH(915,"所选择商品不在所选择的店铺内"),
    BEGIN_AFTER_END(916,"结束时间在开始时间之前"),
    BEGIN_BEFORE_NOW(917,"开始时间已过"),
    ACTIVITY_SHOP_NOT_MATCH(918,"优惠活动与店铺不一致"),
    GOODS_NOT_IN_SHOP(919,"该商品不在该店铺内"),
    CANT_ADD_SPU_TO_ACTIVITY(920,"只能给从未上线的优惠活动中移除商品"),
    COUPON_USER_NO_MATCH(921,"该优惠券不属于此用户"),
    COUPON_CANT_RETURN(922,"优惠券无法退回"),
    NO_SUFFICIENT_GOODS(923,"预售量大于库存量"),
    ACTIVITY_NOT_IN_SHOP(924,"活动不属于该店铺"),
    SHOP_CANT_CHANGE(925,"当前店铺信息无法修改"),
    USER_CANT_AUDIT(926,"当前用户无审核权限"),
    SHOP_CANT_AUDIT(927,"当前店铺无法审批"),
    SHOP_CANT_ONLINE(928,"当前店铺无法上线"),
    SHOP_CANT_OFFLINE(929,"当前店铺无法下线"),
    COMMENT_CANT_AUDIT(930,"当前评论无法审批"),
    SPU_CANT_ONLINE(931,"当前商品无法上架"),
    SPU_CANT_OFFLINE(932,"当前商品无法下架"),
    FLOAT_PRICE_QUANTITY_BIGGER(933,"价格浮动的库存量大于sku的库存量"),
    FLOAT_PRICE_NOT_IN_SHOP(934,"价格浮动不在当前商店内"),
    NO_PRI_TO_BRAND(935,"无品牌的权限"),
    NO_PRI_TO_CATEGORY(936,"无分类的权限"),
    SPU_CANT_ADD_TO_FIRST_CATEGORY(937,"商品不能加入第一级分类"),
    SPU_NO_BELONG_TO_CATEGORY(938,"当前商品不属于此分类"),
    SPU_NO_BELONG_TO_BRAND(939,"当前商品不属于此品牌"),
    FLASH_SALE_DATE_CONFLICT(940,"秒杀活动时间冲突"),
    FLASH_SALE_ITEM_AND_FLASH_SALE_CONFLICT(941,"秒杀活动与当前秒杀项中的活动不一致"),
    SHOP_SKU_IMAGE_FAIL(942,"SKU图片上传失败"),
    NO_PRI_TO_SYSTEM(943,"没有权限"),
    NO_THIS_SHOP(944,"没有该店铺"),
    NO_THIS_SPU(945,"没有该商品"),
    NO_THIS_ACTIVITY(946,"没有该活动"),
    COUPON_REPEAT(947,"不能重复领券"),
    AC_CANT_CHANGE(948,"优惠活动无法修改"),
    COUPON_CANT_GET(949,"优惠券无法领取");

    private int code;
    private String message;
    ResponseCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(){
        return message;
    }

}
