package cn.tc.root.util;

import cn.tc.root.dto.VideoInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 媒体处理
 * Created by EXCaster on 2021/01/21
 */
@Component
@Slf4j
public class mediaProcess {

    public VideoInfo getVideoInfo(String path) {
        MediaInfo MI = new MediaInfo();
        File file = new File(path);
        if (MI.Open(path) < 1) {
            log.error("open fail: {} , exists:{}, length:{}", path, file.exists(), file.length());
            MI.Close();
            MI.dispose();
        }

        String size = MI.Get(MediaInfo.StreamKind.General, 0, "FileSize");
        String width = MI.Get(MediaInfo.StreamKind.Video, 0, "Width",
                MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
        String height = MI.Get(MediaInfo.StreamKind.Video, 0, "Height",
                MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
        String bitRate = MI.Get(MediaInfo.StreamKind.Video, 0, "BitRate",
                MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
        String frameRate = MI.Get(MediaInfo.StreamKind.Video, 0, "FrameRate",
                MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
        String duration = MI.Get(MediaInfo.StreamKind.Video, 0, "Duration",
                MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
        log.info("forMediaInfo: size:{}, width:{}, height:{}, bitRate:{}, frameRate:{}, duration:{}", size,
                width, height, bitRate, frameRate, duration);
        if (StringUtils.isBlank(bitRate)) {
            bitRate = MI.Get(MediaInfo.StreamKind.Video, 0, "BitRate_Nominal",
                    MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
            if (StringUtils.isBlank(bitRate)) {
                bitRate = MI.Get(MediaInfo.StreamKind.Video, 0, "BitRate_Mode",
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
            }
        }

        if (StringUtils.isBlank(bitRate)) {
            bitRate = "200000";
        }

        if (StringUtils.isBlank(duration) || Double.parseDouble(duration) < 1000) {
            duration = MI.Get(MediaInfo.StreamKind.Video, 0, "TimeCode_FirstFrame",
                    MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
            if (StringUtils.isBlank(duration) || Double.parseDouble(duration) < 1000) {
                duration = MI.Get(MediaInfo.StreamKind.General, 0, "Duration",
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
            }
        }

        if (duration == null) {
            duration = "5000";
        }

        if (StringUtils.isBlank(frameRate) || Float.parseFloat(frameRate) < 0) {
            try {
                frameRate = MI.Get(MediaInfo.StreamKind.Video, 0, "FrameRate_Mode",
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);

                if (StringUtils.isBlank(frameRate) || Float.parseFloat(frameRate) < 0) {
                    frameRate = MI.Get(MediaInfo.StreamKind.Video, 0, "FrameRate_Nominal",
                            MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
                }
            } catch (Exception e) {
                frameRate = MI.Get(MediaInfo.StreamKind.Video, 0, "FrameRate_Nominal",
                        MediaInfo.InfoKind.Text, MediaInfo.InfoKind.Name);
            }
        }

        MI.Close();
        MI.dispose();
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.getData(Integer.parseInt(width), Integer.parseInt(height), Long.parseLong(size),
                Double.parseDouble(duration)/1000, path, Integer.parseInt(bitRate), Float.parseFloat(frameRate));
        return videoInfo;
    }


}
