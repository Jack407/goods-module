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
public class PreSaleTest {

    private WebTestClient webClient;

    public PreSaleTest(){
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
    public void getPresaleAllStates() throws Exception {
        byte[] ret = webClient.get()
                .uri("/presale/presales/states")
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
    public void getPresaleActivity1() throws Exception {
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/presale/presales")
                        .queryParam("shopId",1L)
                        .queryParam("timeline",2L)
                        .queryParam("skuId",273L)
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":1,\"pages\":1,\"pageSize\":1," +
                "\"page\":1,\"list\":[{\"id\":1,\"name\":\"双十一\",\"beginTime\":" +
                "\"2020-12-05 11:57:39\",\"payTime\":\"2020-12-20 11:57:39\",\"endTime\":" +
                "\"2020-12-25 11:57:39\",\"shopId\":1,\"goodsSkuId\":273,\"quantity\":10," +
                "\"advancePayPrice\":1,\"restPayPrice\":1,\"gmtCreated\":\"2020-12-05 11:57:39\"" +
                ",\"gmtModified\":\"2020-12-05 11:57:39\",\"state\":0}]},\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void getOnePresaleAc1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/presale/shops/1/skus/273/presales")
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
        System.out.print(responseString);
    }

    @Test
    public void createPresaleAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"name\":\"双十二\",\"advancePayPrice\":5,\"restPayPrice\":10," +
                "\"quantity\":1,\"payTime\":\"2021-12-20 11:57:39\",\"beginTime\":\"2021-12-15 " +
                "11:57:39\"," + "\"endTime\":\"2021-12-25 11:57:39\"}";
        byte[] ret = webClient.post()
                .uri("/presale/shops/1/skus/273/presales")
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
    public void changePresaleAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"name\":\"双十二\",\"advancePayPrice\":5,\"restPayPrice\":10," +
                "\"quantity\":1,\"payTime\":\"2021-12-20 11:57:39\",\"beginTime\":\"2021-12-15 " +
                "11:57:39\"," + "\"endTime\":\"2021-12-25 11:57:39\"}";
        byte[] ret = webClient.put()
                .uri("/presale/shops/1/presales/1")
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
    public void cancelPresaleAc() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/presale/shops/1/presales/1")
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
