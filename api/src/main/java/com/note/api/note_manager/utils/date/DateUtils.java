package com.note.api.note_manager.utils.date;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.GregorianCalendar;

@Component
@AllArgsConstructor
public class DateUtils {
    private final GregorianCalendar calendar = new GregorianCalendar();
    private final DateTypeEnumMapper dateTypeEnumMapper;

    public Date add(Date date, DateTypeEnum dateType, int amount) {
        this.calendar.setTime(date);
        this.calendar.add(dateTypeEnumMapper.dateTypeEnumToGregorianCalendarOperationField(dateType), amount);
        return this.calendar.getTime();
    }
}
