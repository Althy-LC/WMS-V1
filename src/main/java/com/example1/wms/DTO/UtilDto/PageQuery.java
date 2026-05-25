package com.example1.wms.DTO.UtilDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * @author Althy
 * @Create 2026/4/30 14:40
 * @Description
 */
@Data
public class PageQuery {
    @JsonIgnore
    private static Integer pageSize=10;

    @Min(value = 1,message = "页码非法")
    private Integer pageNum;

    public Integer getStart(){
        if(pageNum==null){
            pageNum=1;
        }
        return (pageNum-1)*pageSize;
    }
}
