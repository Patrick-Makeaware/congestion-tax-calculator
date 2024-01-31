package congestion.services;

import congestion.rules.CityTollRules;
import congestion.rules.GothenburgTollRules;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CityTollRulesService {
  private final GothenburgTollRules gothenburgTollRules;

  public CityTollRules loadCity(String city) {
    // Replace this switch to fetch data from a data store somewhere
    return switch (city) {
      case "Göteborg" -> gothenburgTollRules;

      default -> throw new IllegalStateException("Unexpected city: " + city);
    };
  }
}
