package com.ttknp.api.dto;

import com.ttknp.api.entities.Gadget;
import com.ttknp.api.service.ModelService;
import com.ttknp.jdbccustomservice.jdbc.select.JdbcSelectHelper;
import com.ttknp.jdbccustomservice.jdbc.update.JdbcInsertUpdateDeleteHelper;
import com.ttknp.valiadationcustomservice.validation.ValidateHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.util.ResourceUtils;

@Service
public class GadgetDTO implements ModelService<Gadget> {

    private final JdbcSelectHelper<Gadget> jdbcSelectHelper;
    private final JdbcInsertUpdateDeleteHelper<Gadget> jdbcInsertUpdateDeleteHelper;
    private final JdbcTemplate jdbcTemplate;
    // private final JdbcReadSQLFileHelper jdbcReadSQLFileHelper;

    @Autowired
    public GadgetDTO(JdbcSelectHelper<Gadget> jdbcSelectHelper, JdbcInsertUpdateDeleteHelper<Gadget> jdbcInsertUpdateDeleteHelper, JdbcTemplate jdbcTemplate) {
        this.jdbcSelectHelper = jdbcSelectHelper;
        this.jdbcInsertUpdateDeleteHelper = jdbcInsertUpdateDeleteHelper;
        // this.jdbcReadSQLFileHelper = jdbcReadSQLFileHelper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Gadget> getAll() {
        return jdbcSelectHelper.selectAll(Gadget.class);
    }

    @Override
    public List<Gadget> getAllByKey(Object key) {
        return jdbcSelectHelper.selectAllWhereLikeAColumn(Gadget.class,"brand",key);
    }

    @Override
    public <U> List<U> getColumnByKey(Object key) {
        return (List<U>) jdbcSelectHelper.selectAllOnlyColumn(Gadget.class,String.class,key.toString());
    }

    @Override
    public Gadget getById(Object id) {
        try {
            if (ValidateHelperService.isNotEmptyString(id.toString())) {
                return jdbcSelectHelper.selectOne(Gadget.class, "gid", id);
            } else {
                throw new RuntimeException("Gadget id is empty");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteById(Object id) {
        try {
            if (ValidateHelperService.isNotEmptyString(id.toString())) {
                return jdbcInsertUpdateDeleteHelper.deleteOne(Gadget.class, "gid", id) > 0;
            }else {
                throw new RuntimeException("Gadget id is empty");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean update(Gadget gadget) {
        try {
            if (ValidateHelperService.isNotEmptyString(gadget.getGid())) {
                return jdbcInsertUpdateDeleteHelper.updateOne(Gadget.class, "gid", gadget) > 0;
            }else {
                throw new RuntimeException("Gadget id is empty");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean add(Gadget gadget) {
        try {
            if (ValidateHelperService.isNotEmptyString(gadget.getGid())) {
                return jdbcInsertUpdateDeleteHelper.insertOne(Gadget.class, gadget) > 0;
            }else {
                throw new RuntimeException("Gadget id is empty");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<String, byte[]> getGadgetHasMapReport(String fileType) {
        HashMap<String, byte[]> map = new HashMap<>(); // key is filename & value is file
        String fileName;
        if (fileType != null) {
            try {
                fileName = switch (fileType) {
                    case "CSV" -> "gadget_list.csv";  // Export to CSV
                    case "XLSX" -> "gadget_list.xlsx"; // Export to XLSX
                    case "HTML" -> "gadget_list.html"; // Export to HTML
                    case "XML" -> "gadget_list.xml"; // Export to XML
                    case "DOC" -> "gadget_list.doc"; // Export to DOC
                    case "PDF" -> "gadget_list.pdf";// Export to PDF
                    default -> "gadget_list.txt"; // Export to TXT
                };
                //  byte[] fileReport = ordersHistoryListJasperReportInBytesRootPath(fileType);
                byte[] fileReport = gadgetJasperReportInBytesFromRootPath(fileType);
                // log.debug("file report : {}", fileReport); // [102, 10, 54, 49, 57, 55, 51, 49, 48, 10, 37, 37, 69, 79, 70, 10 ,...]
                map.put(fileName, fileReport);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    @Override
    public HashMap<String, byte[]> getGadgetHasMapReport(String fileType,String key) {
        HashMap<String, byte[]> map = new HashMap<>(); // key is filename & value is file
        String fileName;
        if (fileType != null) {
            try {
                fileName = switch (fileType) {
                    case "CSV" -> "gadget_list.csv";  // Export to CSV
                    case "XLSX" -> "gadget_list.xlsx"; // Export to XLSX
                    case "HTML" -> "gadget_list.html"; // Export to HTML
                    case "XML" -> "gadget_list.xml"; // Export to XML
                    case "DOC" -> "gadget_list.doc"; // Export to DOC
                    case "PDF" -> "gadget_list.pdf";// Export to PDF
                    default -> "gadget_list.txt"; // Export to TXT
                };
                //  byte[] fileReport = ordersHistoryListJasperReportInBytesRootPath(fileType);
                byte[] fileReport = gadgetJasperReportInBytesFromRootPath(fileType,key);
                // log.debug("file report : {}", fileReport); // [102, 10, 54, 49, 57, 55, 51, 49, 48, 10, 37, 37, 69, 79, 70, 10 ,...]
                map.put(fileName, fileReport);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }


    private byte[] gadgetJasperReportInBytesFromRootPath(String fileType) throws Exception {
        String resourceTemplateClassPath = "classpath:report/gadget_list.jrxml";
        // 1. Create Required Parameters For mapping parameter tags
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("header","Gadget List By All");
        parameters.put("totalPrice", getTotalPrice());
        parameters.put("totalAmount",getTotalAmount());
        // 2. Create DataSource
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(getAll());
        // 2.2 Load Path Of Template
        String path = ResourceUtils.getFile(resourceTemplateClassPath).getAbsolutePath();
        // 3. Compile .jrmxl template, stored in JasperReport object
        JasperReport jasperReport = JasperCompileManager.compileReport(path);
        // 4. Fill Report - by passing complied .jrxml object, parameters, datasource
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);
        // 5.Export Report - by using JasperExportManager
        return exportJasperReportBytes(jasperPrint, fileType);
    }

    private byte[] gadgetJasperReportInBytesFromRootPath(String fileType,String brand) throws Exception {
        String resourceTemplateClassPath = "classpath:report/gadget_list.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("header","Gadget List By Brand : "+brand);
        parameters.put("totalPrice", getTotalPrice(brand));
        parameters.put("totalAmount",getTotalAmount(brand));
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(getAllByKey(brand));
        String path = ResourceUtils.getFile(resourceTemplateClassPath).getAbsolutePath();
        JasperReport jasperReport = JasperCompileManager.compileReport(path);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);
        return exportJasperReportBytes(jasperPrint, fileType);
    }

    // jasper Reports Util
    private byte[] exportJasperReportBytes(JasperPrint jasperPrint, String reportType) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        switch (reportType) {
            case "CSV":
                // Export to CSV
                JRCsvExporter csvExporter = new JRCsvExporter();
                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                csvExporter.exportReport();
                break;
            case "XLSX":
                // Export to XLSX
                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                xlsxExporter.exportReport();
                break;
            case "HTML":
                // Export to HTML
                HtmlExporter htmlExporter = new HtmlExporter();
                htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
                htmlExporter.exportReport();
                break;
            case "XML":
                // Export to XML
                JRXmlExporter xmlExporter = new JRXmlExporter();
                xmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xmlExporter.setExporterOutput(new SimpleXmlExporterOutput(outputStream));
                xmlExporter.exportReport();
                break;
            case "DOC":
                // Export to DOCX (RTF format)
                JRRtfExporter docxExporter = new JRRtfExporter();
                docxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                docxExporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                docxExporter.exportReport();
                break;
            case "PDF":
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                break;
            default:
                break;
        }
        return outputStream.toByteArray();
    }

    private Double getTotalPrice() {
        return jdbcTemplate.queryForObject("SELECT SUM(g.price * g.amount) AS totalPrice FROM it_shop.gadget AS g;",Double.class);
    }

    private Long getTotalAmount() {
        return jdbcTemplate.queryForObject("SELECT SUM( g.amount) AS totalAmount FROM it_shop.gadget AS g;",Long.class);
    }

    private Double getTotalPrice(String brand) {
        return jdbcTemplate.queryForObject("SELECT SUM(g.price * g.amount) AS totalPrice FROM it_shop.gadget AS g where g.brand = ?;",Double.class,brand);
    }

    private Long getTotalAmount(String brand) {
        return jdbcTemplate.queryForObject("SELECT SUM( g.amount) AS totalAmount FROM it_shop.gadget AS g where g.brand = ?;",Long.class,brand);
    }
}
