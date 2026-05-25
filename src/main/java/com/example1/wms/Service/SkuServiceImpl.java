package com.example1.wms.Service;

import com.example1.wms.BuniessException;
import com.example1.wms.Util.CodeBulider;
import com.example1.wms.DTO.SkuDto;
import com.example1.wms.Mapper.SkuMapper;
import com.example1.wms.POJO.SKU;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Althy
 * @Create 2026/5/3 18:08
 * @Description
 */
@Slf4j
@Service
public class SkuServiceImpl implements SkuService{
@Autowired
    SkuMapper skuMapper;
@Autowired
    SequenceService sequenceService;

    //新增商品
    @Override
    @Transactional
    public void add(SkuDto skuDto) {
        CodeBulider codeBulider = new CodeBulider();
        Long seq = sequenceService.nextCode("SKU");
        skuDto.setSkuCode(codeBulider.getnewSKUcode(seq));

       // System.out.println(skuDto.toString());

        SKU sku = new SKU();
        BeanUtils.copyProperties(skuDto,sku);
        skuMapper.add(sku);

        sequenceService.update("SKU");

    }

    //按商品编码查指定商品
    @Override
    public SkuDto selectcode(String skuCode, Boolean isAdmin) {
        SKU sku = skuMapper.code(skuCode,isAdmin);

        SkuDto skuDto = new SkuDto();
        BeanUtils.copyProperties(sku, skuDto);
        skuDto.setStatusdesc(skuDto.getStatus().getDesc());

        return  skuDto;
    }

    //按商品名称查指定商品
    @Override
    public List<SkuDto> selectName(String skuName, Boolean isAdmin) {
        List<SKU> skus = skuMapper.name(skuName,isAdmin);

            List<SkuDto> skuDtos = skus.stream().map(T->  {
                SkuDto skuDto = new SkuDto();
                BeanUtils.copyProperties(T, skuDto);
                skuDto.setStatusdesc(skuDto.getStatus().getDesc());
                return skuDto;
             }).toList();

       return skuDtos;
    }

    //按商品条码查指定商品
    @Override
    public SkuDto selectbarcode(String barcode, Boolean isAdmin) {
        SKU sku = skuMapper.barcode(barcode,isAdmin);

        SkuDto skuDto = new SkuDto();
        BeanUtils.copyProperties(sku,skuDto);
        skuDto.setStatusdesc(skuDto.getStatus().getDesc());

        return skuDto;
    }


    //删除商品
    @Override
    public void delete(String skuCode) {
        int result = skuMapper.delete(skuCode);
        if(result == 0){
            throw new BuniessException(404,"待删除商品不存在");
        }
    }

    //修改商品
    @Override
    public void put(SkuDto skuDto) {


        SKU sku = new SKU();
        BeanUtils.copyProperties(skuDto,sku);
        int result = skuMapper.put(sku);

        if(result == 0){
            log.warn(sku.toString());
            throw new BuniessException(404,"待修改的商品不存在");
        }
    }


}
