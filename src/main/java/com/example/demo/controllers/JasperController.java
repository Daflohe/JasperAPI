package com.example.demo.controllers;

import net.sf.jasperreports.engine.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.QuotationModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/Server/Reports/")
@CrossOrigin(origins = "*")

public class JasperController {
	public JasperController() {
		super();
	}

	@Autowired
	private pdfService service;
	// QuotationModel quotationModel = new QuotationModel();
	/* ENDPOINTS REPORTES */

	@GetMapping("/Quotation")
	public CompletableFuture<ResponseEntity<String>> createQuotation(@RequestParam String consecutive)
			throws IOException, JRException {
		CompletableFuture<String> response = service.QuotationReport(consecutive);
		return response.thenApply(result -> {
			if ("ERROR".equals(result)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error personalizado: " + result);
			} else {
				return ResponseEntity.ok(result);
			}
		});
	}
	
	/*@PostMapping("/Quotation")
	public CompletableFuture<ResponseEntity<String>> createQuotation(@RequestBody String consecutive)
			throws IOException, JRException {
		CompletableFuture<String> response = service.QuotationReport(consecutive);
		return response.thenApply(result -> {
			if ("ERROR".equals(result)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error personalizado: " + result);
			} else {
				return ResponseEntity.ok(result);
			}
		});
	}*/
}