package com.example1.wms.DTO;

import com.example1.wms.DTO.Enum.InventoryCheckStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.time.LocalDateTime;

/**
 * @author Althy
 * @Create 2026/5/14 19:16
 * @Description
 */
@Data
public class InventoryCheckDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderNo;
    @NotBlank(message = "仓库编码不得为空")
    private String warehouseCode;
    @NotBlank(message = "创建者不得为空")
    private String createUser;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InventoryCheckStatusEnum status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String desc;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;
}
