package com.guac.request.portal.rest;

import com.guac.request.portal.dao.RequestDAO;
import com.guac.request.portal.model.AccessRequest;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.UserContext;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AccessRequestResource {

    private final RequestDAO requestDAO;
    private final UserContext userContext;

    public AccessRequestResource(RequestDAO requestDAO, UserContext userContext) {
        this.requestDAO = requestDAO;
        this.userContext = userContext;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitAccessRequest(AccessRequest request) throws GuacamoleException {
        request.setUsername(userContext.self().getIdentifier());
        request.setRequestId(UUID.randomUUID().toString());
        request.setRequestDate(new Date());
        request.setStatus("PENDING");
        requestDAO.submitAccessRequest(request);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccessRequest> getAllAccessRequests() throws GuacamoleException {
        return requestDAO.getAllAccessRequests();
    }

    @POST
    @Path("/{requestId}/approve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveAccessRequest(@PathParam("requestId") String requestId) throws GuacamoleException {
        requestDAO.approveAccessRequest(requestId);
        return Response.ok().build();
    }

    @POST
    @Path("/{requestId}/deny")
    @Produces(MediaType.APPLICATION_JSON)
    public Response denyAccessRequest(@PathParam("requestId") String requestId) throws GuacamoleException {
        requestDAO.denyAccessRequest(requestId);
        return Response.ok().build();
    }

    @GET
    @Path("/export/csv")
    @Produces("text/csv")
    public Response exportAccessRequestsAsCsv() throws GuacamoleException {
        List<AccessRequest> requests = requestDAO.getAllAccessRequests();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(baos)) {
            // Write CSV header
            writer.write("Request ID,Username,Target Environment,Reason,Estimated Time (hours),Request Date,Status\n");
            // Write data
            for (AccessRequest request : requests) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%d\",\"%s\",\"%s\"\n",
                        request.getRequestId(),
                        request.getUsername(),
                        request.getTargetEnvironment(),
                        request.getReasonForRequest(),
                        request.getEstimatedTime(),
                        request.getRequestDate().toString(),
                        request.getStatus()));
            }
        } catch (IOException e) {
            throw new GuacamoleException("Failed to generate CSV.", e);
        }

        return Response.ok(baos.toByteArray())
                .header("Content-Disposition", "attachment; filename=\"requests.csv\"")
                .build();
    }

    @GET
    @Path("/export/xlsx")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportAccessRequestsAsXlsx() throws GuacamoleException {
        List<AccessRequest> requests = requestDAO.getAllAccessRequests();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Access Requests");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Request ID", "Username", "Target Environment", "Reason", "Estimated Time (hours)", "Request Date", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Data rows
            int rowNum = 1;
            for (AccessRequest request : requests) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(request.getRequestId());
                row.createCell(1).setCellValue(request.getUsername());
                row.createCell(2).setCellValue(request.getTargetEnvironment());
                row.createCell(3).setCellValue(request.getReasonForRequest());
                row.createCell(4).setCellValue(request.getEstimatedTime());
                row.createCell(5).setCellValue(request.getRequestDate().toString());
                row.createCell(6).setCellValue(request.getStatus());
            }

            workbook.write(baos);
        } catch (IOException e) {
            throw new GuacamoleException("Failed to generate XLSX.", e);
        }

        return Response.ok(baos.toByteArray())
                .header("Content-Disposition", "attachment; filename=\"requests.xlsx\"")
                .build();
    }
}
