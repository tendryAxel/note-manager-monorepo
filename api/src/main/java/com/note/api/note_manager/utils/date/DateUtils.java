package com.note.api.note_manager.utils.date;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DateUtils {
  private final GregorianCalendar calendar = new GregorianCalendar();
  private final DateTypeEnumMapper dateTypeEnumMapper;

  public Date add(Date date, DateTypeEnum dateType, int amount) {
    this.calendar.setTime(date);
    this.calendar.add(
        dateTypeEnumMapper.dateTypeEnumToGregorianCalendarOperationField(dateType), amount);
    return this.calendar.getTime();
  }

  public Date add(Date date, Duration duration) {
    this.calendar.setTime(date);
    this.calendar.add(Calendar.SECOND, (int) duration.getSeconds());
    return this.calendar.getTime();
  }
}
