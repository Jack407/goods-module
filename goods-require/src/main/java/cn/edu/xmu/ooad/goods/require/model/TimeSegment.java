package cn.edu.xmu.ooad.goods.require.model;

import lombok.Data;

import java.sql.Time;

@Data
public class TimeSegment {

    private Long id;
    private String beginTime;
    private String endTime;
    private String gmtCreate;
    private String gmtModified;

    public TimeSegment(Long id, String beginTime,String endTime,String gmtCreate , String gmtModified)
    {
        this.id = id;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }


}
