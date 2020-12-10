package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.GoodsserviceApplication;
import cn.edu.xmu.goods.dao.GoodsDao;
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
public class GoodsTest {

    private WebTestClient webClient;

    public GoodsTest(){
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
    public void getFlashDetail() throws Exception {
        File file = new File("."+File.separator + "src" + File.separator + "test" + File.separator+"resources" + File.separator +"timg.png");
        System.out.println("文件长度："+file.length());
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("img", Base64.getDecoder().decode(Base64.getEncoder().encode(data)))
                .header("Content-Disposition", "form-data; name=img; filename=image.jpg");
        byte[] ret = webClient.post()
                .uri("/goods/shops/1/skus/1/uploadImg")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo("OK")
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
        String responseString = new String(ret, "UTF-8");
        //endregion
        String expectedResponse = "{\"code\":\"OK\",\"errmsg\":\"成功\",\"data\":[{\"id\":1,\"saleId\":null,\"goodsSkuId\":null,\"price\":10,\"quantity\":10,\"gmtCreated\":\"2020-12-01T22:32:08\",\"gmtModified\":null,\"goodsSku\":{\"id\":273,\"goodsSpuId\":273,\"skuSn\":null,\"name\":\"+\",\"originalPrice\":980000,\"configuration\":null,\"weight\":10,\"imageUrl\":\"http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg\",\"inventory\":1,\"detail\":null,\"disabled\":4,\"gmtCreated\":\"2020-11-28T17:42:17\",\"gmtModified\":\"2020-11-28T17:42:17\"}},{\"id\":2,\"saleId\":null,\"goodsSkuId\":null,\"price\":15,\"quantity\":15,\"gmtCreated\":\"2020-12-01T22:32:41\",\"gmtModified\":null,\"goodsSku\":{\"id\":274,\"goodsSpuId\":274,\"skuSn\":null,\"name\":\"+\",\"originalPrice\":850,\"configuration\":null,\"weight\":4,\"imageUrl\":\"http://47.52.88.176/file/images/201612/file_5861cd259e57a.jpg\",\"inventory\":99,\"detail\":null,\"disabled\":4,\"gmtCreated\":\"2020-11-28T17:42:17\",\"gmtModified\":\"2020-11-28T17:42:17\"}}]}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void createBrand() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"name\":\"杨国福\",\"detail\":\"麻辣烫\"}";
        byte[] ret = webClient.post()
                .uri("/goods/shops/0/brands")
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"id\":120,\"name\":\"杨国福\",\"detail\":\"麻辣烫\",\"imageUrl\":null,\"gmtCreated\":null,\"gmtModified\":null},\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    @Test
    public void changeBrand1() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        String json = "{\"name\":\"哈哈\",\"detail\":\"哈哈\"}";
        byte[] ret = webClient.put()
                .uri("/goods/shops/0/brands/71")
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
    public void deleteBrand() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.delete()
                .uri("/goods/shops/0/brands/71")
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
    public void getBrands() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/goods/brands")
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
        String expectedResponse = "{\"errno\":0,\"data\":{\"total\":2,\"pages\":1,\"pageSize\":2,\"page\":1,\"list\":[{\"id\":71,\"name\":\"戴荣华\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"},{\"id\":72,\"name\":\"范敏祺\",\"detail\":null,\"imageUrl\":null,\"gmtCreated\":\"2020-12-07T13:48:45\",\"gmtModified\":\"2020-12-07T13:48:45\"}]},\"errmsg\":\"成功\"}\n";
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void addSpuToBrand() throws Exception {
        String token = creatTestToken(1L, 0L, 1000);
        byte[] ret = webClient.post()
                .uri("/goods/shops/0/spus/273/brands/1")
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
    public void removeSpuToBrand() throws Exception {
        String token = creatTestToken(1L, 1L, 1000);
        byte[] ret = webClient.delete()
                .uri("/goods/shops/1/spus/273/brands/71")
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
}