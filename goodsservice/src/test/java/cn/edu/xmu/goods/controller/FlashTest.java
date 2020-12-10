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
public class FlashTest {
    private WebTestClient webClient;

    public FlashTest(){
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
    public void createFlashSale() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        String json = "{\"flash_date\":\"2021-12-07 11:57:39\"}";
        byte[] ret = webClient.post()
                .uri("/flashsale/timesegments/1/flashsales")
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
}
