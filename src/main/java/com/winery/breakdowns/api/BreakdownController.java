package com.winery.breakdowns.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        if (t.equals("year")) {
            return buildYearBreakdown(cds);
        } else if (t.equals("region")) {
            return buildRegionBreakdown(cds);
        } else if (t.equals("variety")) {
            return buildVarietyBreakdown(cds);
        } else if (t.equals("year-variety")) {
            return buildYearVarietyBreakdown(cds);
        }

        throw new IllegalArgumentException("Received invalid type: " + t);
    }

    public BreakdownResponse buildYearBreakdown(Stream<WineDto.ComponentDto> cds) {
        List<BreakdownResponse.Breakdown> b = cds
                .map(o -> new BreakdownResponse.Breakdown(String.valueOf(o.percentage), String.valueOf(o.year)))
                .collect(Collectors.toList());
        return new BreakdownResponse("year", b);
    }

    public BreakdownResponse buildVarietyBreakdown(Stream<WineDto.ComponentDto> cds) {
        List<BreakdownResponse.Breakdown> b = cds
                .map(o -> new BreakdownResponse.Breakdown(String.valueOf(o.percentage), o.variety))
                .collect(Collectors.toList());
        return new BreakdownResponse("variety", b);
    }

    public BreakdownResponse buildRegionBreakdown(Stream<WineDto.ComponentDto> cds) {
        List<BreakdownResponse.Breakdown> b = cds
                .map(o -> new BreakdownResponse.Breakdown(String.valueOf(o.percentage), o.region))
                .collect(Collectors.toList());
        return new BreakdownResponse("region", b);
    }

    public BreakdownResponse buildYearVarietyBreakdown(Stream<WineDto.ComponentDto> cds) {
        List<BreakdownResponse.Breakdown> b = cds
                .map(o -> new BreakdownResponse.Breakdown(String.valueOf(o.percentage), String.format("%d-%s", o.year, o.variety)))
                .collect(Collectors.toList());
        return new BreakdownResponse("year-variety", b);
    }

    public static class BreakdownResponse {
        public String breakdownType;
        public List<Breakdown> breakdown;

        public BreakdownResponse(String bt, List<Breakdown> b) {
            this.breakdownType = bt;
            this.breakdown = b;
        }

        public static class Breakdown {
            public String percentage;
            public String key;

            public Breakdown(String p, String k) {
                this.percentage = p;
                this.key = k;
            }
        }
    }

    public static class WineDto {
        public String lotCode;
        public double volume;
        public String description;
        public String tankCode;
        public String productState;
        public String ownerName;
        public List<ComponentDto> components;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public WineDto(
                @JsonProperty("lotCode") String lc,
                @JsonProperty("volume") double v,
                @JsonProperty("description") String d,
                @JsonProperty("tankCode") String tc,
                @JsonProperty("productState") String ps,
                @JsonProperty("ownerName") String on,
                @JsonProperty("components") List<ComponentDto> c
        ) {
            this.lotCode = lc;
            this.volume = v;
            this.description = d;
            this.tankCode = tc;
            this.productState = ps;
            this.ownerName = on;
            this.components = c;
        }

        public static class ComponentDto {
            public double percentage;
            public int year;
            public String variety;
            public String region;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public ComponentDto(
                    @JsonProperty("percentage") double p,
                    @JsonProperty("year") int y,
                    @JsonProperty("variety") String v,
                    @JsonProperty("region") String r
            ) {
                this.percentage = p;
                this.year = y;
                this.variety = v;
                this.region = r;
            }
        }
    }

}
