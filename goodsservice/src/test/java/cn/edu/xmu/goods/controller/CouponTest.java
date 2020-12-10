package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsserviceApplication;
import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.vo.AuditVo;
import cn.edu.xmu.goods.model.vo.CouponActivityVo;
import cn.edu.xmu.goods.model.vo.GrouponActivityVo;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.*;
import java.util.Base64;

@SpringBootTest(classes = GoodsserviceApplication.class)   //标识本类是一个SpringBootTest
public class CouponTest {

    private WebTestClient webClient;

    public CouponTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        return token;
    }

    @Test
    public void getCouponAllStates() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri("/coupon/coupons/states")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"name\":\"未领取\",\"code\":0}," +
                "{\"name\":\"已领取\",\"code\":1}," +
                "{\"name\":\"已使用\",\"code\":2},{\"name\":\"已失效\"," +
                "\"code\":3}],\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createCouponAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"name\":\"双十一\",\"quantity\":10,\"quantityType\":0" +
                ",\"validTerm\":10,\"couponTime\":\"2021-12-07 11:57:39\"," +
                "\"beginTime\":\"2021-12-20 11:57:39\"," +
                "\"endTime\":\"2021-12-21 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.post()
                .uri("/coupon/shops/1/couponactivities")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"id\":1,\"name\":\"双十一\"}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getCouponAc() throws Exception {
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/coupon/couponactivities")
                        .queryParam("shopId",1L)
                        .queryParam("time",0)
                        .queryParam("page",1)
                        .queryParam("pageSize",2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1,\"page\":1," +
                "\"list\":[{\"id\":2,\"name\":\"双十二\"," +
                "\"beginTime\":\"2021-12-20 11:57:39\",\"endTime\":\"2021-12-21 11:57:39\"," +
                "\"couponTime\":\"2021-12-07 11:57:39\",\"shopId\":1,\"quantity\":10," +
                "\"validTerm\":10,\"imageUrl\":null,\"strategy\":\"无\",\"createdBy\":1," +
                "\"modifyBy\":1,\"gmtCreated\":\"2020-12-07 22:20:50\"," +
                "\"gmtModified\":\"2020-12-07 22:20:50\",\"quantityType\":null," +
                "\"state\":0}]},\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void selectAllInvalidAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/coupon/shops/1/couponactivities/invalid")
                        .queryParam("page",1)
                        .queryParam("pageSize",2)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1," +
                "\"page\":1,\"list\":[{\"id\":2,\"name\":\"双十二\",\"beginTime\":" +
                "\"2021-12-20 11:57:39\",\"endTime\":\"2021-12-21 11:57:39\",\"couponTime\":" +
                "\"2021-12-07 11:57:39\",\"shopId\":1,\"quantity\":10,\"validTerm\":10," +
                "\"imageUrl\":null,\"strategy\":\"无\",\"createdBy\":1,\"modifyBy\":1," +
                "\"gmtCreated\":\"2020-12-07 22:20:50\",\"gmtModified\":\"2020-12-07 22:20:50\",\"" +
                "quantityType\":null,\"state\":1}]},\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getSkuInCouponAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/coupon/couponactivities/1/skus")
                        .queryParam("page",1)
                        .queryParam("pageSize",2)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":2,\"pages\":1,\"pageSize\":2" +
                ",\"page\":1,\"list\":[{\"id\":273,\"goodsSpuId\":273,\"skuSn\":null,\"name\":\"+\"" +
                ",\"originalPrice\":980000,\"configuration\":null,\"weight\":10,\"imageUrl\":" +
                "\"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\"inventory\"" +
                ":1,\"detail\":null,\"disabled\":4,\"gmtCreated\":\"2020-12-07T13:48:44\"," +
                "\"gmtModified\":\"2020-12-07T13:48:44\"},{\"id\":274,\"goodsSpuId\":274,\"skuSn\":" +
                "null,\"name\":\"+\",\"originalPrice\":850,\"configuration\":null,\"weight\":4," +
                "\"imageUrl\":\"http://47.52.88.176/file/images/201612/file_5861cd259e57a.jpg\"," +
                "\"inventory\":99,\"detail\":null,\"disabled\":4,\"gmtCreated\":" +
                "\"2020-12-07T13:48:44\",\"gmtModified\":\"2020-12-07T13:48:44\"}]},\"errmsg\":" +
                "\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOneCouponAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri("/coupon/shops/1/couponactivities/2")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.print(responseString);
    }

    @Test
    public void changeCouponAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"name\":\"双十一\",\"quantity\":10," +
                "\"beginTime\":\"2021-12-20 11:57:39\"," +
                "\"endTime\":\"2021-12-21 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.put()
                .uri("/coupon/shops/1/couponactivities/2")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.print(responseString);
    }

    @Test
    public void offlineCouponAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/coupon/shops/1/couponactivities/2")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.print(responseString);
    }

    @Test
    public void addSkuInCouponAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"skuIdList\":[273]}";
        byte[] ret = webClient.post()
                .uri("/coupon/shops/1/couponactivities/2/skus")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.print(responseString);
    }

    @Test
    public void removeSkuInCouponAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/coupon/shops/1/couponskus/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.print(responseString);
    }

    @Test
    public void getUserCoupon() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/coupon/coupons")
                        .queryParam("state",(byte) 1)
                        .queryParam("page",1)
                        .queryParam("pageSize",2)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.print(responseString);
    }

    @Test
    public void useCoupon() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.put()
                .uri("/coupon/coupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.print(responseString);
    }

    @Test
    public void returnCoupon() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.put()
                .uri("/coupon/shops/1/coupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        System.out.print(responseString);
    }

}
