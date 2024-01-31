package congestion.services;

import congestion.enums.Vehicle;
import congestion.rules.CityTollRules;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CongestionTaxCalculatorService {

  private final CityTollRulesService cityTollRulesService;

  private CityTollRules tollRules;

  @PostConstruct
  public void init() {
    // Defaults to Göteborg, make a smarter choice of relevant city rules later...
    tollRules = cityTollRulesService.loadCity("Göteborg");
  }

  public int getTax(Vehicle vehicle, LocalDateTime[] dates) {
    if (tollRules.isTollFreeVehicle(vehicle)) {
      return 0;
    }

    // Need to sort dates and split it into daily chunks to apply correct rules
    Collection<List<LocalDateTime>> dailyChunks = Arrays.stream(dates).sorted()
        .collect(Collectors.groupingBy(LocalDateTime::toLocalDate))
        .values();

    // Sum up the tolls for each day
    return dailyChunks.stream().map(this::getDailyFee).reduce(0, Integer::sum);
  }

  private int getDailyFee(List<LocalDateTime> localDateTimes) {
    int totalFee = 0;

    LocalDateTime bucketStartDateTime = null;
    int bucketMaxFee = 0;
    for (LocalDateTime date : localDateTimes) {
      int tollFee = tollRules.getTollFee(date);

      // Todo: Split this up to a less complicated structure
      if (bucketStartDateTime == null) {
        bucketStartDateTime = date;
        bucketMaxFee = tollFee;
      } else {
        // Is it within the single charge period, treat exact hit as a new period
        // (check requirements if this is correct)
        if (date.minusMinutes(tollRules.getSingleChargeMinutes())
            .isBefore(bucketStartDateTime)) {
          bucketMaxFee = Math.max(bucketMaxFee, tollFee);
        } else {
          totalFee += bucketMaxFee;
          // Start a new period
          bucketStartDateTime = date;
          bucketMaxFee = tollFee;
        }
      }
      // Also check if it is the last entry in the loop
      if (date == localDateTimes.get(localDateTimes.size() - 1) && bucketStartDateTime != null) {
        // Add what is left into totalFee
        totalFee += bucketMaxFee;
      }
    }

    // Make sure we don't pay more than the daily max
    return Math.min(totalFee, tollRules.getMaxDailyToll());
  }

}
