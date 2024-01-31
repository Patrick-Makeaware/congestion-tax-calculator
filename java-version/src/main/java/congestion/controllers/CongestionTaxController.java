package congestion.controllers;

import congestion.enums.Vehicle;
import congestion.services.CongestionTaxCalculatorService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("taxes")
public class CongestionTaxController {
  private final CongestionTaxCalculatorService service;

  @GetMapping
  public int getCongestionTax(@RequestParam Vehicle vehicle,
      @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime[] dates) {
    return service.getTax(vehicle, dates);
  }

}
