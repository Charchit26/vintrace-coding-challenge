package com.winery.breakdowns.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winery.breakdowns.api.models.BreakdownResponse;
import com.winery.breakdowns.api.models.Component;
import com.winery.breakdowns.api.models.WineDto;
import com.winery.breakdowns.api.services.BreakdownService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URL;
import java.util.Comparator;
import java.util.Objects;

@RestController
@RequestMapping("/api/breakdown")
public class BreakdownController {
    private static final String YEAR = "year";
    private static final String VARIETY = "variety";
    private static final String REGION = "region";
    private static final String YEAR_VARIETY = "year-variety";

    private final BreakdownService breakdownService;

    public BreakdownController(BreakdownService breakdownService) {
        this.breakdownService = breakdownService;
    }

    @RequestMapping(value = "/year/{lotCode}", method = RequestMethod.GET)
    public ResponseEntity<BreakdownResponse> getBreakdownByYear(@PathVariable("lotCode") String lc) {
        WineDto wineDto = loadWineDto(lc);
        BreakdownResponse response = breakdownService.build(YEAR, wineDto.getComponents().stream().sorted(Comparator.comparing(Component::getPercentage).reversed()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/variety/{lotCode}", method = RequestMethod.GET)
    public ResponseEntity<BreakdownResponse> getBreakdownByVariety(@PathVariable("lotCode") String lc) {
        WineDto wineDto = loadWineDto(lc);
        BreakdownResponse response = breakdownService.build(VARIETY, wineDto.getComponents().stream().sorted(Comparator.comparing(Component::getPercentage).reversed()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/region/{lotCode}", method = RequestMethod.GET)
    public ResponseEntity<BreakdownResponse> getBreakdownByRegion(@PathVariable("lotCode") String lc) {
        WineDto wineDto = loadWineDto(lc);
        BreakdownResponse response = breakdownService.build(REGION, wineDto.getComponents().stream().sorted(Comparator.comparing(Component::getPercentage).reversed()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/year-variety/{lotCode}", method = RequestMethod.GET)
    public ResponseEntity<BreakdownResponse> getBreakdownByYearAndVariety(@PathVariable("lotCode") String lc) {
        WineDto wineDto = loadWineDto(lc);
        BreakdownResponse response = breakdownService.build(YEAR_VARIETY, wineDto.getComponents().stream().sorted(Comparator.comparing(Component::getPercentage).reversed()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private WineDto loadWineDto(String lotCode) {
        ClassLoader cl = getClass().getClassLoader();
        URL resourceUrl = cl.getResource(lotCode + ".json");
        try {
            return Objects.requireNonNull(new ObjectMapper().readValue(resourceUrl, WineDto.class));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        }
    }
}
