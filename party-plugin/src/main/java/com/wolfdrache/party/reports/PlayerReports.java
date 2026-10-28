package com.wolfdrache.party.reports;

import java.util.ArrayList;
import java.util.List;

import com.wolfdrache.serverapi.reports.ReportModel;

public class PlayerReports {
    public final List<ReportModel> madeReports = new ArrayList<>();
    public final List<ReportModel> receivedReportsOpen = new ArrayList<>();
    public final List<ReportModel> receivedReportsClosed = new ArrayList<>();

    public void closeReport(ReportModel report) {
        receivedReportsOpen.remove(report);
        receivedReportsClosed.add(report);
    }
}
