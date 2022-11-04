package com.shopper.walnut.walnut.scheduler;

import com.shopper.walnut.walnut.exception.ItemException;
import com.shopper.walnut.walnut.exception.error.ErrorCode;
import com.shopper.walnut.walnut.model.constant.CacheKey;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.input.BrandItemInput;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.service.BrandItemService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ItemScheduler {
    private final BrandItemRepository brandItemRepository;
    private final ItemRepository itemRepository;

    /**
     * brandItemService -> 물품등록하면 cache에 저장
     * 10분 마다 전체상품 Item entity에 업데이트하고 싶음
     * cache에 있는 정보를 받아오는 방법은 없을까?
     * 스케쥴러는 변수를 받아올 수 없다고 한다...
     * @param
     */
    @CacheEvict(value = CacheKey.ITEM_SCHEDULE, allEntries = true)
    @Scheduled(cron = "0 0/10 * * * *")
    public void itemScheduling(){
        log.info("itemScheduling is started");
        List<BrandItem> brandItemList = brandItemRepository.findAll();
        for(BrandItem brandItem : brandItemList){
            boolean result = itemRepository.existsByBrandIdAndBrandItemId(brandItem.getBrand().getBrandId(),brandItem.getBrandItemId());
            if(!result){
                Item item  = Item.of(brandItem);
                itemRepository.save(item);
            }
        }
    }

    /**
     * 물품 잔여수량 스케쥴러를 통해 업데이트 시켜주기
     * 만일 물품 수량이 0개가 되었을 경우 해당 Brand에 알림 메일 전송
     */
    @Scheduled(cron = "10 * * * * *")
    public void itemCountScheduling(){
        log.info("itemCountScheduling start");
        List<BrandItem> brandItemList = brandItemRepository.findAll();
        for(BrandItem brandItem : brandItemList){
            Optional<Item> optional = itemRepository.findByBrandItemId(brandItem.getBrandItemId());
            if(optional.isEmpty()){
                throw new ItemException(ErrorCode.ITEM_NOT_FOUND);
            }
            if(optional.get().getCnt() != brandItem.getCnt()){
                if(optional.get().getCnt() ==0){
                    /**
                     * Brand에 알림 전송
                     */
                }
                brandItem.setCnt(optional.get().getCnt());
                brandItemRepository.save(brandItem);
            }
        }
    }
}