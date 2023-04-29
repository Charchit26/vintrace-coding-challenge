package com.winery.breakdowns.api.dto;

import java.util.List;

public class BreakdownResponse {
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