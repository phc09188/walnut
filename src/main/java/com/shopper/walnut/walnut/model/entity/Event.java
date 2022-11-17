package com.shopper.walnut.walnut.model.entity;

import com.shopper.walnut.walnut.model.input.EventInput;
import com.shopper.walnut.walnut.model.status.EventStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;

    private String fileName;
    private String urlFileName;

    private String subject;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate signDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId")
    private Brand brand;

    public static Event of(EventInput input, Brand brand) {
        return Event.builder()
                .fileName(input.getFileName())
                .urlFileName(input.getUrlFileName())
                .subject(input.getSubject())
                .content(input.getContent())
                .brand(brand)
                .status(EventStatus.REQ)
                .signDate(LocalDate.now())
                .endDate(input.getEndDate())
                .build();
    }
}
