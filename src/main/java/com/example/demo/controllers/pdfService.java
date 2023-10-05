package com.example.demo.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;


@Service
public class pdfService {
	/*CLASE DE CONEXION A BD*/
    public Conect conect;	
    @Async
    public CompletableFuture<String> QuotationReport(String consecutive) throws JRException, IOException {
    	/*STRING PARA BD*/
    	conect = new Conect("jdbc:mysql://localhost/asoftydb");
    	/*URL DE ARCHIVO JRXML GENERADO EN JASPER*/
        File file = ResourceUtils.getFile("C:/Users/programador1/JaspersoftWorkspace/MyReports/Blank_A4.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        /*PARAMETROS UTILIZADOS EN JASPER =>(NOMBRE DE PARAMETRO, VALOR A ENVIAR)*/
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Consecutive", consecutive);
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conect.getConnection());//, dataSource);
        
        /*EXPORTE DIRECTO, (GENERA DOWNLOAD EN NAVEGADOR)*/
        //JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
        
        /*GENERACION DE FILE EN BYTE[]*/
        JRPdfExporter pdfExporter = new JRPdfExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        pdfExporter.exportReport();
        byte[] fileBytes = byteArrayOutputStream.toByteArray();
        if (jasperPrint.getPages().isEmpty()) {
        	return CompletableFuture.completedFuture("ERROR"); 
        }
        if(fileBytes.length > 1) {
        	// Convertir el arreglo de bytes a Base64
            String base64File = Base64.getEncoder().encodeToString(fileBytes);
        	return CompletableFuture.completedFuture(base64File);
        }else {
        	return CompletableFuture.completedFuture("ERROR");            
        }       
    }
 
}
