package com.example1.wms.Util;

import com.example1.wms.DTO.InspectDto;
import com.example1.wms.DTO.UtilDto.PathSortDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Althy
 * @Create 2026/5/16 01:13
 * @Description S型路径排序类
 *      实现逻辑：
 *          基于库位编码的存储结构，
 *              仓库编码-库区编码-货架-层数-货位
 *            首先拆分库位编码为仓库，库区，货架，层数，货位
 *                  将同一库区业务提出，整合入同一库区类型
 *        盘点规则：
 *           在按照同库区优先于同类型库区优先于其他类型库区原则（仅适用于盘点业务使用）
 *              库区类型优先级-> 不合格区<存储区
 *
 *        通用规则：
 *           同库区同货架同层 ： 货架奇数 货位升序 ， 偶数 货位降序
 *           层数统一降序，货架奇数升序，偶数降序，形成s型拣货，货架升序排序
 *           areaType 3存储区 4不合格区
 *
 */
public class SPathSortUtil {
    public static Map<Long,Integer> inventory(List<PathSortDto> pathSortDtos){
          List<PathSortDto> sortlist = pathSortDtos.stream()
                                                    .sorted(SPathSortUtil::compare)
                                                    .collect(Collectors.toList());

          Map<Long, Integer> map = new HashMap<>();
          //id+序号组成map
          for (int i=0;i<sortlist.size();i++){
              map.put(sortlist.get(i).getId(),i);
        }
          return map;
    }

    private static int compare(PathSortDto T1,PathSortDto T2){
        //按照库区类型区分优先级
            int result = Integer.compare(T1.getAreaType(),T2.getAreaType());
             if(result != 0){
                 return result;
             }

        //获取T1的货架号，层数，位号
        String locationcode = T1.getLocationCode();
        int shelf1 = Integer.parseInt(locationcode.split("-")[2]);
        int layer1 = Integer.parseInt(locationcode.split("-")[3]);
        int bin1   = Integer.parseInt(locationcode.split("-")[4]);

        //获取T2的货架号，层数，位号
         locationcode = T2.getLocationCode();
        int shelf2 = Integer.parseInt(locationcode.split("-")[2]);
        int layer2 = Integer.parseInt(locationcode.split("-")[3]);
        int bin2   = Integer.parseInt(locationcode.split("-")[4]);

       //按货架号
         result = Integer.compare(shelf1,shelf2);
         if(result != 0){
             return  result;
         }

       //按照层数
        result = Integer.compare(layer2,layer1);
        if(result != 0){
            return  result;
        }

        //按照货位号
        result = shelf2 % 2 == 1 ? Integer.compare(bin1,bin2)
                        : Integer.compare(bin2,bin1);
        return result;
    }
}
