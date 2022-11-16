package com.shopper.walnut.walnut.controller;

import com.shopper.walnut.walnut.exception.error.BrandNotFound;
import com.shopper.walnut.walnut.exception.error.EventException;
import com.shopper.walnut.walnut.model.constant.CacheKey;
import com.shopper.walnut.walnut.model.entity.Brand;
import com.shopper.walnut.walnut.model.entity.Event;
import com.shopper.walnut.walnut.model.input.EventInput;
import com.shopper.walnut.walnut.repository.BrandRepository;
import com.shopper.walnut.walnut.repository.EventRepository;
import com.shopper.walnut.walnut.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/brand/main")
public class EventController {
    private final FileUtil fileUtil;
    private final BrandRepository brandRepository;
    private final EventRepository eventRepository;


    /** 이벤트 리스트 **/
    @Cacheable(key = "#model.asMap()", value = CacheKey.EVENTLIST)
    @GetMapping("/eventList")
    public String eventList(Model model, @AuthenticationPrincipal User logInUser){
        Optional<Brand> optional =  brandRepository.findByBrandLoginId(logInUser.getUsername());
        if(optional.isEmpty()){
            throw new BrandNotFound();
        }
        Brand brand = optional.get();
        List<Event> events =  eventRepository.findAllByBrand(brand);
        model.addAttribute("brand", brand);
        model.addAttribute("events",events);
        return "/brand/main/eventList";
    }
    /** 추가 폼 **/
    @GetMapping("/event/addForm")
    public String eventAddForm(Model model, @RequestParam Long brandId){
        Optional<Brand> optional = brandRepository.findById(brandId);
        if(optional.isEmpty()){
            throw new BrandNotFound();
        }
        model.addAttribute("brand", optional.get());
        return "/brand/main/eventAdd";
    }

    /** 추가 !! **/
    @CacheEvict(value = CacheKey.EVENTLIST, allEntries = true)
    @PostMapping("/event/add.do")
    public String eventAdd(EventInput input, MultipartFile file){
        Optional<Brand> optional = brandRepository.findById(input.getBrandId());
        if(optional.isEmpty()){
            throw new BrandNotFound();
        }
        Brand brand = optional.get();
        String detail =  fileUtil.FileUrl(file, "event");
        String[] details = detail.split(",");
        input.setFileName(details[0]);
        input.setUrlFileName(details[1]);
        Event event = Event.of(input, brand);
        eventRepository.save(event);

        return "redirect:/brand/main/eventList";
    }

    /** 삭제 **/
    @CacheEvict(value = CacheKey.EVENTLIST, allEntries = true)
    @PostMapping("/event/delete.do")
    public String eventDelete(@RequestParam Long eventId){
        Optional<Event> optional =  eventRepository.findById(eventId);
        if(optional.isEmpty()){
            throw new EventException();
        }
        Event event = optional.get();
        eventRepository.delete(event);
        return "redirect:/brand/main/eventList";
    }
}
