package congestion.rules;

import congestion.services.SwedishHolidayService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GothenburgTollRules implements CityTollRules {
  private final SwedishHolidayService swedishHolidayService;

  @AllArgsConstructor
  @Getter
  private static class IntervalFee {
    private final LocalTime from;
    private final int fee;
  }

  private static final List<IntervalFee> intervalFees = Arrays.stream(new IntervalFee[]{
      new IntervalFee(LocalTime.of(0, 0), 0),
      new IntervalFee(LocalTime.of(6, 0), 8),
      new IntervalFee(LocalTime.of(6, 30), 13),
      new IntervalFee(LocalTime.of(7, 0), 18),
      new IntervalFee(LocalTime.of(8, 0), 13),
      new IntervalFee(LocalTime.of(8, 30), 8),
      new IntervalFee(LocalTime.of(15, 0), 13),
      new IntervalFee(LocalTime.of(15, 30), 18),
      new IntervalFee(LocalTime.of(17, 0), 13),
      new IntervalFee(LocalTime.of(18, 0), 8),
      new IntervalFee(LocalTime.of(18, 30), 0),
  }).toList();

  @Override
  public boolean isTollFreeDate(LocalDate date) {
    if (date.getMonthValue() == 7) {
      // July is toll-free
      return true;
    }
    if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
      // Weekends are toll-free
      return true;
    }
    if (swedishHolidayService.isPublicHoliday(date)) {
      // Is a swedish holiday and toll-free
      return true;
    }
    if (swedishHolidayService.isPublicHoliday(date.plusDays(1))) {
      // Is a day before a swedish holiday and toll-free
      return true;
    }

    return false;
  }

  @Override
  public int getTollFee(LocalDateTime dateTime) {
    if (isTollFreeDate(dateTime.toLocalDate())) {
      return 0;
    }

    int finalFee = 0;

    // Change this later to a more performant method
    for (IntervalFee fee : intervalFees) {
      if (dateTime.toLocalTime().isAfter(fee.from)) {
        finalFee = fee.fee;
      } else {
        break;
      }
    }

    return finalFee;
  }

  @Override
  public int getMaxDailyToll() {
    return 60;
  }
}
