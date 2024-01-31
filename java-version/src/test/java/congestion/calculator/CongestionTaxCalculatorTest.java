package congestion.calculator;

import congestion.CongestionTaxApplication;
import congestion.services.CongestionTaxCalculatorService;
import congestion.enums.Vehicle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(classes = CongestionTaxApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class CongestionTaxCalculatorServiceTest {

  private final CongestionTaxCalculatorService calculator;
  private final TestRestTemplate restTemplate;

  @Value(value = "${local.server.port}")
  private int port;

  private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  // Scribbled test dates
  private final LocalDateTime[] testDates = new LocalDateTime[]{
      LocalDateTime.parse("2013-01-14 21:00:00", dtf),
      LocalDateTime.parse("2013-01-15 21:00:00", dtf),
      LocalDateTime.parse("2013-02-07 06:23:27", dtf),
      LocalDateTime.parse("2013-02-07 15:27:00", dtf),
      LocalDateTime.parse("2013-02-08 06:27:00", dtf),
      LocalDateTime.parse("2013-02-08 06:20:27", dtf),
      LocalDateTime.parse("2013-02-08 14:35:00", dtf),
      LocalDateTime.parse("2013-02-08 15:29:00", dtf),
      LocalDateTime.parse("2013-02-08 15:47:00", dtf),
      LocalDateTime.parse("2013-02-08 16:01:00", dtf),
      LocalDateTime.parse("2013-02-08 16:48:00", dtf),
      LocalDateTime.parse("2013-02-08 17:49:00", dtf),
      LocalDateTime.parse("2013-02-08 18:29:00", dtf),
      LocalDateTime.parse("2013-02-08 18:35:00", dtf),
      LocalDateTime.parse("2013-03-26 14:25:00", dtf),
      LocalDateTime.parse("2013-03-28 14:07:27", dtf),
  };

  @Autowired
  CongestionTaxCalculatorServiceTest(CongestionTaxCalculatorService calculator,
      TestRestTemplate restTemplate) {
    this.calculator = calculator;
    this.restTemplate = restTemplate;
  }

  @Test
  void getTax_taxableVehicle_restCall() {
    // GIVEN

    // WHEN
    Integer tax = restTemplate.getForObject("http://localhost:" + port +
        "/taxes?vehicle=Car&dates=2013-03-19T14:07:27,2013-03-19T16:01:13", Integer.class);

    // THEN
    Assertions.assertEquals(26, tax);
  }

  @Test
  void getTax_taxableVehicle_restCall_incorrectOrder() {
    // GIVEN

    // WHEN
    Integer tax = restTemplate.getForObject("http://localhost:" + port +
        "/taxes?vehicle=Car&dates=2013-03-19T16:01:13,2013-03-19T14:07:27", Integer.class);

    // THEN
    Assertions.assertEquals(26, tax);
  }

  @Test
  void getTax_taxableVehicle_restCall_noTaxVehicle() {
    // GIVEN

    // WHEN
    Integer tax = restTemplate.getForObject("http://localhost:" + port +
        "/taxes?vehicle=Motorcycle&dates=2013-03-19T16:01:13,2013-03-19T14:07:27", Integer.class);

    // THEN
    Assertions.assertEquals(0, tax);
  }

  @Test
  void getTax_taxableVehicle() {
    // GIVEN

    // WHEN
    int tax = calculator.getTax(Vehicle.Car, testDates);

    // THEN
    Assertions.assertEquals(89, tax);
  }
}