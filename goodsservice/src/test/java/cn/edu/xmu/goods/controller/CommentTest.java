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
public class CommentTest {
    private WebTestClient webClient;

    public CommentTest() {
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
     * 测试评论状态
     *
     *
     * @throws Exception
     */
    @Test
    public void getCommentAllStates() throws Exception {
        byte[] ret = webClient.get()
                .uri("/comment/comments/states")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo("0")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        String expectedResponse = "{\"errno\":0,\"data\":[{\"name\":\"未审核\",\"code\":0},{\"name\":\"未通过\",\"code\":2},{\"name\":\"评论成功\",\"code\":1}],\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);

    }

    /**
     * 买家查看自己的评价记录
     *
     *
     * @throws Exception
     */
    @Test
    public void getCommentMyself() throws Exception {
        String token = creatTestToken(1L, -1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/comment/comments")
                        .queryParam("page", 1)
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
    /**
     * 查看通过审核的评论
     *
     *
     * @throws Exception
     */
    @Test
    public void getComment() throws Exception {
        String token = creatTestToken(1L, -1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/comment/skus/1/comments")
                        .queryParam("page", 1)
                        .queryParam("pageSize",10)
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
    /**
     * 管理员查看未审核评论列表
     *
     *
     * @throws Exception
     */
    @Test
    public void getCommentAdmin() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/comment/shops/0/comments/all")
                        .queryParam("page", 1)
                        .queryParam("pageSize",2)
                        .queryParam("state", (byte)0)
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

    /**
     * 管理员审核评论
     *
     *
     * @throws Exception
     */
    @Test
    public void putCommentAdmin() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"conclusion\":\"true\"}";
        byte[] ret = webClient.put()
                .uri("/comment/shops/0/comments/2/confirm")
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

