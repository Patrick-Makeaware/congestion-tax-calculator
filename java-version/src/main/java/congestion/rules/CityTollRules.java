package congestion.rules;

import congestion.enums.Vehicle;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CityTollRules {

  /**
   * @return true if a vehicle is toll-free. Defaults to all but "Car".
   */
  default boolean isTollFreeVehicle(Vehicle vehicle) {
    assert vehicle != null : "Vehicle must not be null";
    return switch (vehicle) {
      case Car -> false;
      case Motorcycle, Tractor, Emergency, Diplomat, Foreign, Military -> true;
      // Default case if Vehicle enum gets added types
      default -> throw new IllegalArgumentException("Unknown vehicle type" + vehicle);
    };
  }

  /**
   * @return if date is toll-free.
   */
  boolean isTollFreeDate(LocalDate date);

  /**
   * @return number of minutes that counts as a single charge. Defaults to 60.
   */
  default int getSingleChargeMinutes() {
    return 60;
  }

  /**
   * @return toll-fee for a specific date and time. Checks for toll-free dates internally.
   */
  int getTollFee(LocalDateTime dateTime);

  int getMaxDailyToll();
}
