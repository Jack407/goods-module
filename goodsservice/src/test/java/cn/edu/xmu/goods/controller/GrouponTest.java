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
public class GrouponTest {
    private WebTestClient webClient;

    public GrouponTest(){
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
    public void getGrouponAllStates() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri("/groupon/groupons/states")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"name\":\"已新建\",\"code\":0}," +
                "{\"name\":\"被取消\",\"code\":1}],\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.post()
                .uri("/groupon/shops/1/spus/273/groupons")
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"id\":5,\"name\":null," +
                "\"goodsSpu\":{\"id\":273,\"name\":\"金和汇景•戴荣华•古彩洛神赋瓷瓶\"," +
                "\"goodsSn\":\"drh-d0001\",\"imageUrl\":" +
                "\"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\"," +
                "\"state\":4,\"gmtCreate\":\"2020-12-03 21:04:55\"," +
                "\"gmtModified\":\"2020-12-03 21:04:55\",\"disable\":4}," +
                "\"shop\":{\"id\":1,\"name\":\"Nike\"},\"strategy\":\"无\",\"state\":0," +
                "\"beginTime\":\"2021-12-07 11:57:39\",\"endTime\":\"2021-12-09 11:57:39\"}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-12 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.post()
                .uri("/groupon/shops/1/spus/273/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("916")
                .jsonPath("$.errmsg").isEqualTo("结束时间在开始时间之前")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":916,\"errmsg\":\"结束时间在开始时间之前\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"beginTime\":\"2021-12-03 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.post()
                .uri("/groupon/shops/1/spus/273/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("917")
                .jsonPath("$.errmsg").isEqualTo("开始时间已过")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":917,\"errmsg\":\"开始时间已过\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc4() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.post()
                .uri("/groupon/shops/1/spus/274/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("919")
                .jsonPath("$.errmsg").isEqualTo("该商品不在该店铺内")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":919,\"errmsg\":\"该商品不在该店铺内\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc5() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.post()
                .uri("/groupon/shops/2/spus/274/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("503")
                .jsonPath("$.errmsg").isEqualTo("departId不匹配")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":503,\"errmsg\":\"departId不匹配\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc6() throws Exception {
        String token = creatTestToken(1L, 10L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.post()
                .uri("/groupon/shops/10/spus/274/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("944")
                .jsonPath("$.errmsg").isEqualTo("没有该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createGrouponAc7() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.post()
                .uri("/groupon/shops/1/spus/1/groupons")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("945")
                .jsonPath("$.errmsg").isEqualTo("没有该商品")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":945,\"errmsg\":\"没有该商品\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.put()
                .uri("/groupon/shops/1/groupons/1")
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
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"beginTime\":\"2021-12-12 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.put()
                .uri("/groupon/shops/1/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("916")
                .jsonPath("$.errmsg").isEqualTo("结束时间在开始时间之前")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":916,\"errmsg\":\"结束时间在开始时间之前\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"beginTime\":\"2021-12-03 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.put()
                .uri("/groupon/shops/1/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("917")
                .jsonPath("$.errmsg").isEqualTo("开始时间已过")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":917,\"errmsg\":\"开始时间已过\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc4() throws Exception {
        String token = creatTestToken(1L, 2L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.put()
                .uri("/groupon/shops/2/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("924")
                .jsonPath("$.errmsg").isEqualTo("活动不属于该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":924,\"errmsg\":\"活动不属于该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc5() throws Exception {
        String token = creatTestToken(1L, 10L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.put()
                .uri("/groupon/shops/10/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("944")
                .jsonPath("$.errmsg").isEqualTo("没有该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc6() throws Exception {
        String token = creatTestToken(1L, 2L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.put()
                .uri("/groupon/shops/1/groupons/1")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("503")
                .jsonPath("$.errmsg").isEqualTo("departId不匹配")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":503,\"errmsg\":\"departId不匹配\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeGrouponAc7() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"beginTime\":\"2021-12-07 11:57:39\"," +
                "\"endTime\":\"2021-12-09 11:57:39\",\"strategy\":\"无\"}";
        byte[] ret = webClient.put()
                .uri("/groupon/shops/1/groupons/10")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("946")
                .jsonPath("$.errmsg").isEqualTo("没有该活动")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":946,\"errmsg\":\"没有该活动\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void cancelGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/groupon/shops/1/groupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void cancelGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.delete()
                .uri("/groupon/shops/2/groupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("924")
                .jsonPath("$.errmsg").isEqualTo("活动不属于该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":924,\"errmsg\":\"活动不属于该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void cancelGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 10L, 1000);
        byte[] ret = webClient.delete()
                .uri("/groupon/shops/10/groupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("944")
                .jsonPath("$.errmsg").isEqualTo("没有该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void cancelGrouponAc4() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/groupon/shops/2/groupons/1")
                .header("authorization",token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("503")
                .jsonPath("$.errmsg").isEqualTo("departId不匹配")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":503,\"errmsg\":\"departId不匹配\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void cancelGrouponAc5() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/groupon/shops/1/groupons/10")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("946")
                .jsonPath("$.errmsg").isEqualTo("没有该活动")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":946,\"errmsg\":\"没有该活动\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getGrouponActivity1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/groupons")
                        .queryParam("shopId",1L)
                        .queryParam("timeline",2L)
                        .queryParam("spuId",1L)
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
        String expectedString = "{\"errno\":0,\"data\":" +
                "{\"total\":2,\"pages\":1,\"pageSize\":2,\"page\":1," +
                "\"list\":[{\"id\":1,\"name\":\"双十一\",\"beginTime\":\"2020-12-05 11:57:39\"," +
                "\"endTime\":\"2020-12-09 11:57:39\"},{\"id\":3,\"name\":\"黑色星期五\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString, true);
    }

    @Test
    public void getGrouponActivity2() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/groupons")
                        .queryParam("shopId",2L)
                        .queryParam("timeline",0L)
                        .queryParam("spuId",2L)
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
        String expectedString = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1," +
                "\"page\":1,\"list\":[{\"id\":2,\"name\":\"双十二\"," +
                "\"beginTime\":\"2020-12-07 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString, true);
    }

    @Test
    public void getGrouponActivity3() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/groupons")
                        .queryParam("shopId",2L)
                        .queryParam("timeline",1L)
                        .queryParam("spuId",2L)
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
        String expectedString = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1," +
                "\"page\":1,\"list\":[{\"id\":2,\"name\":\"双十二\"," +
                "\"beginTime\":\"2020-12-07 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString, true);
    }

    @Test
    public void getGrouponActivity4() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/groupons")
                        .queryParam("shopId",2L)
                        .queryParam("timeline",3L)
                        .queryParam("spuId",2L)
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
        String expectedString = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1," +
                "\"page\":1,\"list\":[{\"id\":4,\"name\":\"儿童节\"," +
                "\"beginTime\":\"2020-06-01 11:57:39\",\"endTime\":\"2020-06-02 11:57:39\"}]}," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedString, responseString, true);
    }

    @Test
    public void getOneGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/spus/1/groupons")
                        .queryParam("state",(byte) 0)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("945")
                .jsonPath("$.errmsg").isEqualTo("没有该商品")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":945,\"errmsg\":\"没有该商品\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOneGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/10/spus/273/groupons")
                        .queryParam("state",(byte) 0)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("944")
                .jsonPath("$.errmsg").isEqualTo("没有该店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":944,\"errmsg\":\"没有该店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOneGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 2L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/2/spus/273/groupons")
                        .queryParam("state",(byte) 0)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("919")
                .jsonPath("$.errmsg").isEqualTo("该商品不在该店铺内")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":919,\"errmsg\":\"该商品不在该店铺内\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOneGrouponAc4() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/spus/273/groupons")
                        .queryParam("state",(byte) 0)
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
        String expectedResponse = "{\"errno\":0,\"data\":[{\"id\":5,\"name\":\"劳动节\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]," +
                "\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOneGrouponAc5() throws Exception {
        String token = creatTestToken(1L, 2L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/4/spus/273/groupons")
                        .queryParam("state",(byte) 0)
                        .build())
                .header("authorization",token)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("503")
                .jsonPath("$.errmsg").isEqualTo("departId不匹配")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":503,\"errmsg\":\"departId不匹配\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getShopGrouponAc1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/groupons")
                        .queryParam("state",(byte) 0)
                        .queryParam("spuId",273L)
                        .queryParam("startTime","2020-12-01 11:57:39")
                        .queryParam("endTime","2020-12-12 11:57:39")
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1," +
                "\"pageSize\":1,\"page\":1,\"list\":[{\"id\":5,\"name\":\"劳动节\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}" +
                ",\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getShopGrouponAc2() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/groupons")
                        .queryParam("state",(byte) 0)
                        .queryParam("spuId",273L)
                        .queryParam("startTime","null")
                        .queryParam("endTime","2020-12-12 11:57:39")
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1," +
                "\"pageSize\":1,\"page\":1,\"list\":[{\"id\":5,\"name\":\"劳动节\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}" +
                ",\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getShopGrouponAc3() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/groupon/shops/1/groupons")
                        .queryParam("state",(byte) 0)
                        .queryParam("spuId",273L)
                        .queryParam("startTime","2020-12-01 11:57:39")
                        .queryParam("endTime","null")
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1," +
                "\"pageSize\":1,\"page\":1,\"list\":[{\"id\":5,\"name\":\"劳动节\"," +
                "\"beginTime\":\"2020-12-05 11:57:39\",\"endTime\":\"2020-12-09 11:57:39\"}]}" +
                ",\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }





}
