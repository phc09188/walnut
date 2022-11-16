package com.shopper.walnut.walnut.model.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
public class EventInput {
    private long brandId;
    private String fileName;
    private String urlFileName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String subject;
    private String content;
}
