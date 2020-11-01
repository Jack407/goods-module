package cn.edu.xmu.privilege;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ming Qiu
 **/
@SpringBootApplication
@MapperScan("cn.edu.xmu.privilege.mapper")
public class PrivilegeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrivilegeServiceApplication.class, args);
    }

}
