package com.wolfdrache.party.reports;

import java.util.HashSet;
import java.util.Set;

public class ReportCategory {
    public final String name;
    public final Set<String> reasons = new HashSet<>(
        Set.of("custom")
    );

    public ReportCategory(String name) {
        this.name = name.replace(" ", "_").toLowerCase();
    }

    public ReportCategory(String name, Set<String> reasons) {
        this.name = name.replace(" ", "_").toLowerCase();
        this.reasons.addAll(reasons.stream().map(s -> s.replace(" ", "_").toLowerCase()).toList());
    }
}
