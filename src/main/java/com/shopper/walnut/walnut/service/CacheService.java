package com.shopper.walnut.walnut.service;

import com.shopper.walnut.walnut.conponents.MailComponents;
import com.shopper.walnut.walnut.exception.error.EventException;
import com.shopper.walnut.walnut.model.constant.CacheKey;
import com.shopper.walnut.walnut.model.entity.Event;
import com.shopper.walnut.walnut.model.entity.EventListener;
import com.shopper.walnut.walnut.repository.EventListenerRepository;
import com.shopper.walnut.walnut.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {
    private final EventRepository eventRepository;
    private final EventListenerRepository eventListenerRepository;
    private final MailComponents mailComponents;

    public void cacheInitialization() throws MessagingException, IOException {
        searchNotStartEvent();
        cacheCleanUp();
    }

    public void searchNotStartEvent() throws MessagingException, IOException {
        List<Event> list = eventRepository.
                findAllBySignDateGreaterThanEqualAndSignDateLessThanEqual(
                        LocalDate.now().minusDays(1), LocalDate.now()
                );
        for (Event event : list) {
            eventStart(event.getEventId());
        }

    }

    @CacheEvict(key = "#eventId", value = CacheKey.EVENT_SUBMIT)
    public void eventStart(Long eventId) throws MessagingException, IOException {
        List<EventListener> members = eventListenerRepository.findAll();
        Optional<Event> optional = eventRepository.findById(eventId);
        if (optional.isEmpty()) {
            throw new EventException();
        }
        Event event = optional.get();
        String subject = "<p>[" + event.getBrand().getBrandName() + "]"
                + event.getSubject() + "</p>";
        for (EventListener listener : members) {
            mailComponents.sendMailWithFiles(listener.getUserEmail(), subject, event.getUrlFileName());
        }
    }

    @CacheEvict(allEntries = true)
    public void cacheCleanUp() {
    }


}
