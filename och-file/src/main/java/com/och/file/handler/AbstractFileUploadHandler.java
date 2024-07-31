package com.och.file.handler;

import cn.hutool.core.date.DateUtil;
import com.och.common.annotation.FileUploadType;
import com.och.common.constant.SysSettingConfig;
import com.och.common.domain.file.LfsFileUploadVo;
import com.och.common.utils.MimeTypeUtils;
import com.och.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * @author danmo
 * @date 2023-11-01 15:26
 **/
@FileUploadType(value = "local")
public abstract class AbstractFileUploadHandler {

    @Autowired
    protected SysSettingConfig lfsSettingConfig;

    public abstract LfsFileUploadVo upload(MultipartFile file, Integer businessType) throws IOException;

    public Boolean checkFileFormat(String suffix){
        for (String str : MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION) {
            if (str.equalsIgnoreCase(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取上传文件地址
     * @param businessType
     * @return
     */
    public String getFileTempPath(Integer businessType){
        Date date = new Date();
        StringBuilder strbd =new StringBuilder();
        switch (businessType){
            case 1 -> {
                String sortUrl = lfsSettingConfig.getBaseProfile();
                //判断url是否为空，如果为空，使用默认
                if (StringUtils.isEmpty(sortUrl)) {
                    sortUrl = "/data/lfs/file";
                }
                strbd.append(sortUrl).append("/").append("temp").append("/").append("/").append(DateUtil.year(date)).append("/").append(DateUtil.month(date)).append("/").append(DateUtil.dayOfMonth(date)).append("/");
            }
            case 2 -> strbd.append("/").append("voice").append("/").append("/").append(DateUtil.year(date)).append("/").append(DateUtil.month(date)).append("/").append(DateUtil.dayOfMonth(date)).append("/");
            case 3 -> strbd.append("/").append("file").append("/").append("/").append(DateUtil.year(date)).append("/").append(DateUtil.month(date)).append("/").append(DateUtil.dayOfMonth(date)).append("/");
            case 4 -> strbd.append("/").append("video").append("/").append("/").append(DateUtil.year(date)).append("/").append(DateUtil.month(date)).append("/").append(DateUtil.dayOfMonth(date)).append("/");
            case 5 -> strbd.append("/").append("image").append("/").append("/").append(DateUtil.year(date)).append("/").append(DateUtil.month(date)).append("/").append(DateUtil.dayOfMonth(date)).append("/");
            default -> strbd.append("/").append("unknown").append("/").append("/").append(DateUtil.year(date)).append("/").append(DateUtil.month(date)).append("/").append(DateUtil.dayOfMonth(date)).append("/");
        }

        return strbd.toString();
    }
}
