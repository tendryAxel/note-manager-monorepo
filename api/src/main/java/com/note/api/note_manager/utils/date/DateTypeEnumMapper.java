package com.note.api.note_manager.utils.date;

import java.util.GregorianCalendar;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class DateTypeEnumMapper {
  public int dateTypeEnumToGregorianCalendarOperationField(DateTypeEnum dateType) {
    return switch (dateType) {
      case DAY -> GregorianCalendar.DATE;
    };
  }
}
