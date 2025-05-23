package com.och.system.domain.query.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author danmo
 * @date 2023-11-01 16:13
 **/
@Schema
@Data
public class VoiceFileAddQuery {

    /**
     *  主键ID
     */

    @Schema(description = "主键ID",hidden = true)
    private Long id;
    /**
     *  文件名称
     */
    @Schema(description = "文件名称",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "文件名称不能为空")
    private String name;

    /**
     *  1-手动上传 2-语音合成
     */
    @NotEmpty(message = "类型不能为空")
    @Schema(description = "类型 1-手动上传 2-语音合成",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer type;



    /**
     *  tts方式 1-腾讯 2-阿里 3-讯飞(type=9生效)
     */
    @Schema(description = "tts方式 1-腾讯 2-阿里 3-讯飞(type=2生效)")
    private Integer tts;


    /**
     *  合成文本
     */
    @Schema(description = "合成文本")
    private String speechText;


    /**
     * 文件ID
     */
    @Schema(description = "文件ID")
    private Long fileId;

}
