package com.example1.wms.DTO;

import com.example1.wms.DTO.ValidGroups.AddGroups;
import com.example1.wms.DTO.ValidGroups.UpdateGroups;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Althy
 * @Create 2026/5/6 08:25
 * @Description
 */
@Data
public class DictDto {
    @NotBlank(message = "字典类型不得为空",groups = {AddGroups.class, UpdateGroups.class})
    @Length(min = 1,max = 20,message = "字典类型长度在1-20之间",groups = {AddGroups.class, UpdateGroups.class})
    private String dictType;
    @NotBlank(message = "字典值不得为空",groups = {AddGroups.class, UpdateGroups.class})
    private String dictCode;
    @NotBlank(message = "字典值名称不得为空",groups = AddGroups.class)
    private String dictName;
}
