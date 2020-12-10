package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;
import cn.edu.xmu.goods.model.vo.ShopVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
public class Shop {

    private Long id;
    private String name;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public enum State {
        UNAUDITED((byte) 0, "未审核"),
        OFFLINE((byte) 1, "未上线"),
        ONLINE((byte) 2, "上线"),
        CLOSED((byte) 3, "关闭"),
        DENY((byte) 4, "审核未通过");

        private Byte code;
        private String description;

        State(Byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
    private State state;

    public Shop(ShopVo vo)
    {
        this.name=vo.getName();
    }
}
