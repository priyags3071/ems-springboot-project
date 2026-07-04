package com.ems.service;

import com.ems.model.Employee;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class PdfExportService {

    // Colors
    private static final BaseColor HEADER_BG    = new BaseColor(26, 29, 39);
    private static final BaseColor ROW_BG_EVEN  = new BaseColor(34, 38, 58);
    private static final BaseColor ROW_BG_ODD   = new BaseColor(22, 25, 35);
    private static final BaseColor ACCENT_COLOR  = new BaseColor(79, 142, 247);
    private static final BaseColor TEXT_WHITE    = new BaseColor(232, 234, 240);
    private static final BaseColor TEXT_MUTED    = new BaseColor(136, 145, 170);
    private static final BaseColor ACTIVE_COLOR  = new BaseColor(62, 207, 142);
    private static final BaseColor INACTIVE_COLOR= new BaseColor(239, 68, 68);
    private static final BaseColor LEAVE_COLOR   = new BaseColor(245, 158, 11);

    public byte[] exportEmployeesToPdf(List<Employee> employees) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 40, 30);
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();

        // ── Title Section ──────────────────────────────────────────────────
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, ACCENT_COLOR);
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, TEXT_MUTED);

        Paragraph title = new Paragraph("Employee Management System", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        document.add(title);

        Paragraph subtitle = new Paragraph(
            "Employee Report  |  Generated on: " + LocalDate.now() +
            "  |  Total Employees: " + employees.size(),
            subtitleFont
        );
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);

        // ── Separator Line ─────────────────────────────────────────────────
        LineSeparator line = new LineSeparator(1, 100, ACCENT_COLOR, Element.ALIGN_CENTER, -5);
        document.add(new Chunk(line));

        // ── Stats Row ──────────────────────────────────────────────────────
        long active   = employees.stream().filter(e -> e.getStatus() == Employee.EmployeeStatus.ACTIVE).count();
        long inactive = employees.stream().filter(e -> e.getStatus() == Employee.EmployeeStatus.INACTIVE).count();
        long onLeave  = employees.stream().filter(e -> e.getStatus() == Employee.EmployeeStatus.ON_LEAVE).count();

        PdfPTable statsTable = new PdfPTable(4);
        statsTable.setWidthPercentage(100);
        statsTable.setSpacingBefore(16);
        statsTable.setSpacingAfter(20);
        statsTable.setWidths(new float[]{1, 1, 1, 1});

        addStatCell(statsTable, String.valueOf(employees.size()), "Total Employees", ACCENT_COLOR);
        addStatCell(statsTable, String.valueOf(active),   "Active",   ACTIVE_COLOR);
        addStatCell(statsTable, String.valueOf(inactive), "Inactive", INACTIVE_COLOR);
        addStatCell(statsTable, String.valueOf(onLeave),  "On Leave", LEAVE_COLOR);

        document.add(statsTable);

        // ── Employee Table ─────────────────────────────────────────────────
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.5f, 2f, 1.5f, 2f, 1.5f, 1.2f, 1f});

        String[] headers = {"#", "Full Name", "Department", "Email", "Position", "Salary", "Status"};
        for (String h : headers) {
            addHeaderCell(table, h);
        }

        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            BaseColor rowBg = (i % 2 == 0) ? ROW_BG_EVEN : ROW_BG_ODD;

            addDataCell(table, String.valueOf(i + 1),                           rowBg, Element.ALIGN_CENTER);
            addDataCell(table, e.getFirstName() + " " + e.getLastName(),        rowBg, Element.ALIGN_LEFT);
            addDataCell(table, e.getDepartment(),                                rowBg, Element.ALIGN_LEFT);
            addDataCell(table, e.getEmail(),                                     rowBg, Element.ALIGN_LEFT);
            addDataCell(table, e.getPosition(),                                  rowBg, Element.ALIGN_LEFT);
            addDataCell(table, "$" + String.format("%,.0f", e.getSalary()),      rowBg, Element.ALIGN_RIGHT);

            // Status cell with color
            BaseColor statusColor = switch (e.getStatus()) {
                case ACTIVE   -> ACTIVE_COLOR;
                case INACTIVE -> INACTIVE_COLOR;
                case ON_LEAVE -> LEAVE_COLOR;
            };
            addStatusCell(table, e.getStatus().name().replace("_", " "), rowBg, statusColor);
        }

        document.add(table);

        // ── Footer ─────────────────────────────────────────────────────────
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, TEXT_MUTED);
        Paragraph footer = new Paragraph("This document is auto-generated by EMS Portal. Confidential.", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(16);
        document.add(footer);

        document.close();
        return out.toByteArray();
    }

    // ── Helper Methods ────────────────────────────────────────────────────

    private void addStatCell(PdfPTable table, String value, String label, BaseColor color) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(ROW_BG_EVEN);
        cell.setBorderColor(new BaseColor(46, 51, 80));
        cell.setPadding(12);

        Font valFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, color);
        Font lblFont = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, TEXT_MUTED);

        Paragraph val = new Paragraph(value, valFont);
        val.setAlignment(Element.ALIGN_CENTER);
        Paragraph lbl = new Paragraph(label, lblFont);
        lbl.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(val);
        cell.addElement(lbl);
        table.addCell(cell);
    }

    private void addHeaderCell(PdfPTable table, String text) {
        Font font = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, TEXT_WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(HEADER_BG);
        cell.setBorderColor(ACCENT_COLOR);
        cell.setBorderWidth(0.5f);
        cell.setPadding(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addDataCell(PdfPTable table, String text, BaseColor bg, int align) {
        Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, TEXT_WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "—", font));
        cell.setBackgroundColor(bg);
        cell.setBorderColor(new BaseColor(46, 51, 80));
        cell.setBorderWidth(0.3f);
        cell.setPadding(8);
        cell.setHorizontalAlignment(align);
        table.addCell(cell);
    }

    private void addStatusCell(PdfPTable table, String text, BaseColor bg, BaseColor textColor) {
        Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, textColor);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setBorderColor(new BaseColor(46, 51, 80));
        cell.setBorderWidth(0.3f);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}