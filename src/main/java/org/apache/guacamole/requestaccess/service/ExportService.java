package org.apache.guacamole.requestaccess.service;

import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.guacamole.requestaccess.db.RequestMapper;
import org.apache.guacamole.requestaccess.model.Request;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportService {

    @Inject
    private RequestMapper requestMapper;

    public Response exportRequestsAsCsv() {
        List<Request> requests = requestMapper.selectAll();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             Writer writer = new OutputStreamWriter(out);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Requestor", "Target Environment", "Reason", "Requested Time", "Submission Time", "Status"))) {

            for (Request request : requests) {
                csvPrinter.printRecord(
                        request.getUser().getIdentifier(),
                        request.getEnvironment().getEnvironmentName(),
                        request.getReason(),
                        request.getEstimatedTime() + " minutes",
                        request.getSubmissionTime(),
                        request.getRequestStatus()
                );
            }

            csvPrinter.flush();
            return Response.ok(out.toByteArray(), "text/csv")
                    .header("Content-Disposition", "attachment; filename=\"requests.csv\"")
                    .build();

        } catch (IOException e) {
            return Response.serverError().build();
        }
    }

    public Response exportRequestsAsXlsx() {
        List<Request> requests = requestMapper.selectAll();
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Requests");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Requestor", "Target Environment", "Reason", "Requested Time", "Submission Time", "Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data
            int rowNum = 1;
            for (Request request : requests) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(request.getUser().getIdentifier());
                row.createCell(1).setCellValue(request.getEnvironment().getEnvironmentName());
                row.createCell(2).setCellValue(request.getReason());
                row.createCell(3).setCellValue(request.getEstimatedTime() + " minutes");
                row.createCell(4).setCellValue(request.getSubmissionTime().toString());
                row.createCell(5).setCellValue(request.getRequestStatus());
            }

            workbook.write(out);
            return Response.ok(out.toByteArray(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "attachment; filename=\"requests.xlsx\"")
                    .build();

        } catch (IOException e) {
            return Response.serverError().build();
        }
    }

}
