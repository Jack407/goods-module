package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@ApiModel(description = "评论简单视图对象")
public class CommentSimpleRetVo {

    @ApiModelProperty(value = "评论id")
    private Long id;

    @ApiModelProperty(value = "评论内容")
    private String content;


    public CommentSimpleRetVo(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
    }

}
