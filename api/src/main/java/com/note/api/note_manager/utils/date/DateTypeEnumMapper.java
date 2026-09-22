package com.note.api.note_manager.utils.date;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.GregorianCalendar;

@Component
@NoArgsConstructor
public class DateTypeEnumMapper {
    public int dateTypeEnumToGregorianCalendarOperationField(DateTypeEnum dateType) {
        return switch (dateType) {
            case DAY -> GregorianCalendar.DATE;
        };
    }
}
