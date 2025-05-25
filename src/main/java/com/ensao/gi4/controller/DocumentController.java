package com.ensao.gi4.controller;

import com.ensao.gi4.model.Document;
import com.ensao.gi4.service.api.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/document")
@AllArgsConstructor
public class DocumentController {
	
	public final DocumentService documentService; 
	
	@GetMapping("/{documentId}")
	public ResponseEntity<ByteArrayResource> getDocumentFile(@PathVariable Long documentId){
		
		Optional<Document> documentOptional = documentService.findById(documentId); 
		if (documentOptional.isPresent()) {
		
			Document document = documentOptional.get();
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(document.getFileType()))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + document.getFilename() + "\"" )
					.body(new ByteArrayResource(document.getData())); 
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		}
		
	}
	
	@GetMapping("/content/{id}")
	public ResponseEntity<Document> getDocument(@PathVariable Long id){
		Optional<Document> documentOptional = documentService.findById(id);

        return documentOptional
				.map(document -> ResponseEntity.ok()
				.body(document)).orElseGet(() -> ResponseEntity.badRequest().build());
	}

}
