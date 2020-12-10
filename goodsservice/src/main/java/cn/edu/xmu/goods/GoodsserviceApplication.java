package cn.edu.xmu.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.goods"})
@MapperScan("cn.edu.xmu.goods.mapper")
public class GoodsserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsserviceApplication.class, args);
    }

}
