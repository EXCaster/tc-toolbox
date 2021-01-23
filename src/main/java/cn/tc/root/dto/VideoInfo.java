package cn.tc.root.dto;

import lombok.Data;

/**
 * 视频信息实体
 * Created by EXCaster on 2019/12/18
 */
@Data
public class VideoInfo {
    private Integer width;
    private Integer height;
    private Long size;
    private Double duration;
    private String path;
    private Integer bitrate;
    private Float framerate;

    public VideoInfo getData(Integer width, Integer height, Long size, Double duration, String path,
                             Integer bitrate, Float framerate) {
        this.width = width;
        this.height = height;
        this.size = size;
        this.duration = duration;
        this.bitrate = bitrate;
        this.path = path;
        this.framerate = framerate;
        return this;
    }
}
