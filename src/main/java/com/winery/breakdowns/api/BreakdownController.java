package com.winery.breakdowns.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winery.breakdowns.api.dto.BreakdownResponse;
import com.winery.breakdowns.api.dto.WineDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/breakdown")
public class BreakdownController {

    @RequestMapping(value = "/year/{lotCode}", method = RequestMethod.GET)
    public ResponseEntity<BreakdownResponse> getBreakdownByYear(@PathVariable("lotCode") String lc) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        URL r1 = cl.getResource(lc + ".json");
        WineDto w = new ObjectMapper().readValue(r1, WineDto.class);

        if (w == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        }

        if (!Arrays.asList("year", "variety", "region", "year-variety").contains("year")) {
            throw new IllegalArgumentException("Received wrong type: " + "year");
        }

        BreakdownResponse r = build("year", w.components.stream().sorted(Comparator.comparing((WineDto.ComponentDto c) -> c.percentage).reversed()));

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/variety/{lotCode}", method = RequestMethod.GET)
    public ResponseEntity<BreakdownResponse> getBreakdownByVariety(@PathVariable("lotCode") String lc) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        URL r1 = cl.getResource(lc + ".json");
        WineDto w = new ObjectMapper().readValue(r1, WineDto.class);

        if (w == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        }

        if (!Arrays.asList("year", "variety", "region", "year-variety").contains("variety")) {
            throw new IllegalArgumentException("Received wrong type: " + "variety");
        }

        BreakdownResponse r = build("variety", w.components.stream().sorted(Comparator.comparing((WineDto.ComponentDto c) -> c.percentage).reversed()));

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/region/{lotCode}", method = RequestMethod.GET)
    public ResponseEntity<BreakdownResponse> getBreakdownByRegion(@PathVariable("lotCode") String lc) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        URL r1 = cl.getResource(lc + ".json");
        WineDto w = new ObjectMapper().readValue(r1, WineDto.class);

        if (w == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        }

        if (!Arrays.asList("year", "variety", "region", "year-variety").contains("region")) {
            throw new IllegalArgumentException("Received wrong type: " + "region");
        }

        BreakdownResponse r = build("region", w.components.stream().sorted(Comparator.comparing((WineDto.ComponentDto c) -> c.percentage).reversed()));

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @RequestMapping(value = "/year-variety/{lotCode}", method = RequestMethod.GET)
    public ResponseEntity<BreakdownResponse> getBreakdownByYearAndVariety(@PathVariable("lotCode") String lc) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        URL r1 = cl.getResource(lc + ".json");
        WineDto w = new ObjectMapper().readValue(r1, WineDto.class);

        if (w == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        }

        if (!Arrays.asList("year", "variety", "region", "year-variety").contains("year-variety")) {
            throw new IllegalArgumentException("Received wrong type: " + "year-variety");
        }

        BreakdownResponse r = build("year-variety", w.components.stream().sorted(Comparator.comparing((WineDto.ComponentDto c) -> c.percentage).reversed()));

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    public BreakdownResponse build(String t, Stream<WineDto.ComponentDto> cds) {
        switch (t) {
            case "year":
                return buildYearBreakdown(cds);
            case "region":
                return buildRegionBreakdown(cds);
            case "variety":
                return buildVarietyBreakdown(cds);
            case "year-variety":
                return buildYearVarietyBreakdown(cds);
        }

        throw new IllegalArgumentException("Received invalid type: " + t);
    }

    public BreakdownResponse buildYearBreakdown(Stream<WineDto.ComponentDto> cds) {
        Map<String, Double> totalPercentageByYear = cds
                .collect(Collectors.groupingBy(component -> String.valueOf(component.year),
                        Collectors.summingDouble(component -> component.percentage)));

        List<BreakdownResponse.Breakdown> b = totalPercentageByYear.entrySet().stream()
                .map(o -> new BreakdownResponse.Breakdown(String.valueOf(o.getValue()), String.valueOf(o.getKey())))
                .collect(Collectors.toList());
        return new BreakdownResponse("year", b);
    }

    public BreakdownResponse buildVarietyBreakdown(Stream<WineDto.ComponentDto> cds) {
        Map<String, Double> totalPercentageByVariety = cds
                .collect(Collectors.groupingBy(component -> component.variety,
                        Collectors.summingDouble(component -> component.percentage)));

        List<BreakdownResponse.Breakdown> b = totalPercentageByVariety.entrySet().stream()
                .map(o -> new BreakdownResponse.Breakdown(String.valueOf(o.getValue()), o.getKey()))
                .collect(Collectors.toList());
        return new BreakdownResponse("variety", b);
    }

    public BreakdownResponse buildRegionBreakdown(Stream<WineDto.ComponentDto> cds) {
        Map<String, Double> totalPercentageByRegion = cds
                .collect(Collectors.groupingBy(component -> component.region,
                        Collectors.summingDouble(component -> component.percentage)));

        List<BreakdownResponse.Breakdown> b = totalPercentageByRegion.entrySet().stream()
                .map(o -> new BreakdownResponse.Breakdown(String.valueOf(o.getValue()), o.getKey()))
                .collect(Collectors.toList());
        return new BreakdownResponse("region", b);
    }

    public BreakdownResponse buildYearVarietyBreakdown(Stream<WineDto.ComponentDto> cds) {
        Map<String, Double> totalPercentageByYearVariety = cds
                .collect(Collectors.groupingBy(component -> String.format("%d-%s", component.year, component.variety),
                        Collectors.summingDouble(component -> component.percentage)));

        List<BreakdownResponse.Breakdown> b = totalPercentageByYearVariety.entrySet().stream()
                .map(o -> new BreakdownResponse.Breakdown(String.valueOf(o.getValue()), o.getKey()))
                .collect(Collectors.toList());
        return new BreakdownResponse("year-variety", b);
    }

}
