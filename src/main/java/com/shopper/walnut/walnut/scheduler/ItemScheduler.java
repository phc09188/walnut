package com.shopper.walnut.walnut.scheduler;

import com.shopper.walnut.walnut.exception.error.ItemNotFound;
import com.shopper.walnut.walnut.model.entity.BrandItem;
import com.shopper.walnut.walnut.model.entity.Item;
import com.shopper.walnut.walnut.model.status.ItemStatus;
import com.shopper.walnut.walnut.repository.BrandItemRepository;
import com.shopper.walnut.walnut.repository.ItemRepository;
import com.shopper.walnut.walnut.service.BrandService;
import com.shopper.walnut.walnut.service.CacheService;
import com.shopper.walnut.walnut.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ItemScheduler {
    private final BrandItemRepository brandItemRepository;
    private final ItemRepository itemRepository;
    private final BrandService brandService;
    private final ItemService itemService;
    private final CacheService cacheService;

    /**
     * brandItemService -> 물품등록하면 cache에 저장
     * 매일 자정 캐시를 통한 물품 업데이트
     * 유저 캐시 정보 전체 삭제
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void itemScheduling() throws MessagingException, IOException {
        log.info("itemScheduling is started");
        itemService.itemUpdate();
        cacheService.cacheInitialization();
    }


    /**
     * 1시간 마다
     * 물품 잔여수량 스케쥴러를 통해 업데이트 시켜주기
     * 만일 물품 수량이 0개가 되었을 경우 해당 Brand에 알림 메일 전송
     */
    @Scheduled(cron = "0 0 0/1 * * *")
    public void itemCountScheduling(){
        log.info("itemCountScheduling start");
        List<BrandItem> brandItemList = brandItemRepository.findAll();
        for(BrandItem brandItem : brandItemList){
            Optional<Item> optional = itemRepository.findByBrandItemId(brandItem.getBrandItemId());
            if(optional.isEmpty()){
                throw new ItemNotFound();
            }
            Item item = optional.get();
            if(item.getCnt() != brandItem.getCnt()){
                brandItem.setCnt(item.getCnt());
                brandItem.setTotalTake(item.getTotalTake());
                brandItem.setPayAmount(item.getPayAmount());
                brandItem.setSaleStatus(item.getSaleStatus());
                if(!item.getSaleStatus().equals(ItemStatus.ITEM_STATUS_ING)){
                    brandService.sendEmail(brandItem);
                }
            }
            brandItemRepository.save(brandItem);
        }
    }
}
