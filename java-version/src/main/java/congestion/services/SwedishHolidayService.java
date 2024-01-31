package congestion.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class SwedishHolidayService {
  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM.dd");

  /**
   * @return if a date is a public holiday
   */
  public boolean isPublicHoliday(LocalDate date) {
    // Replace this with a fetch from a holiday source, preferably a public one
    if (date.getYear() != 2013) {
      throw new IllegalArgumentException("Cannot handle years other than 2013");
    }
    return switch (date.format(dtf)) {
      case "01.01" -> true;
      case "03.28" -> true;
      case "03.29" -> true;
      case "04.01" -> true;
      case "04.30" -> true;
      case "05.01" -> true;
      case "05.08" -> true;
      case "05.09" -> true;
      case "06.05" -> true;
      case "06.21" -> true;
      case "11.01" -> true;
      case "12.24" -> true;
      case "12.25" -> true;
      case "12.26" -> true;
      case "12.31" -> true;
      default -> false;
    };
  }
}