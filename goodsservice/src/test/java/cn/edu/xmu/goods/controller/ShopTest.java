package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsserviceApplication;
import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.vo.AuditVo;
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
public class ShopTest {
    private WebTestClient webClient;

    public ShopTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        return token;
    }



    /**
     * 测试返回商铺状态
     *
     *
     * @throws Exception
     */
    @Test
    public void getShopAllStates() throws Exception {
        byte[] ret = webClient.get()
                .uri("/shop/shops/states")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"name\":\"未审核\",\"code\":0},{\"name\":\"未上线\",\"code\":1},{\"name\":\"上线\",\"code\":2},{\"name\":\"关闭\",\"code\":3},{\"name\":\"审核未通过\",\"code\":4}],\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 测试新建商店
     *
     *
     * @throws Exception
     */
    @Test
    public void createShop1() throws Exception {
        String token = creatTestToken(1L, -1L, 1000);
        String json = "{\"name\":\"星巴克\"}";
        byte[] ret = webClient.post()
                .uri("/shop/shops")
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"id\":5,\"name\":\"星巴克\"," +
                "\"state\":0},\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createShop2() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"name\":\"星巴克\"}";
        byte[] ret = webClient.post()
                .uri("/shop/shops")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("908")
                .jsonPath("$.errmsg").isEqualTo("用户已经有店铺")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":908,\"errmsg\":\"用户已经有店铺\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeShop1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"name\":\"麦当劳\"}";
        byte[] ret = webClient.put()
                .uri("/shop/shops/1")
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
    public void changeShop2() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"name\":\"麦当劳\"}";
        byte[] ret = webClient.put()
                .uri("/shop/shops/2")
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
        String expectedResponse = "{\"errno\":503,\"errmsg\":\"departId不匹配\"}";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void changeShop3() throws Exception {
        String token = creatTestToken(1L, 10L, 1000);
        String json = "{\"name\":\"麦当劳\"}";
        byte[] ret = webClient.put()
                .uri("/shop/shops/10")
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
    public void closeShop1() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/shop/shops/1")
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
    public void closeShop2() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/shop/shops/3")
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
    public void closeShop3() throws Exception {
        String token = creatTestToken(1L, 10L, 1000);
        byte[] ret = webClient.delete()
                .uri("/shop/shops/10")
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
    public void closeShop4() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.delete()
                .uri("/shop/shops/3")
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
    public void auditShop1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = webClient.put()
                .uri("/shop/shops/0/newshops/1/audit")
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
    public void auditShop2() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = webClient.put()
                .uri("/shop/shops/1/newshops/1/audit")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("926")
                .jsonPath("$.errmsg").isEqualTo("当前用户无审核权限")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":926,\"errmsg\":\"当前用户无审核权限\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void auditShop3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = webClient.put()
                .uri("/shop/shops/0/newshops/2/audit")
                .header("authorization",token)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("927")
                .jsonPath("$.errmsg").isEqualTo("当前店铺无法审批")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":927,\"errmsg\":\"当前店铺无法审批\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void auditShop4() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = webClient.put()
                .uri("/shop/shops/0/newshops/10/audit")
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
    public void auditShop5() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = webClient.put()
                .uri("/shop/shops/0/newshops/1/audit")
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
    public void onlineShop1() throws Exception {
        String token = creatTestToken(1L, 3L, 1000);
        byte[] ret = webClient.put()
                .uri("/shop/shops/3/onshelves")
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
    public void onlineShop2() throws Exception {
        String token = creatTestToken(1L, 10L, 1000);
        byte[] ret = webClient.put()
                .uri("/shop/shops/10/onshelves")
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
    public void onlineShop3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.put()
                .uri("/shop/shops/1/onshelves")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("928")
                .jsonPath("$.errmsg").isEqualTo("当前店铺无法上线")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":928,\"errmsg\":\"当前店铺无法上线\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void onlineShop4() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.put()
                .uri("/shop/shops/3/onshelves")
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
    public void offlineShop1() throws Exception {
        String token = creatTestToken(1L, 4L, 1000);
        byte[] ret = webClient.put()
                .uri("/shop/shops/4/offshelves")
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
    public void offlineShop2() throws Exception {
        String token = creatTestToken(1L, 10L, 1000);
        byte[] ret = webClient.put()
                .uri("/shop/shops/10/offshelves")
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
    public void offlineShop3() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.put()
                .uri("/shop/shops/1/offshelves")
                .header("authorization",token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("929")
                .jsonPath("$.errmsg").isEqualTo("当前店铺无法下线")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":929,\"errmsg\":\"当前店铺无法下线\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void offlineShop4() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.put()
                .uri("/shop/shops/1/offshelves")
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




}
