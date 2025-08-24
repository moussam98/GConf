package com.ensao.gi4.controller;

import com.ensao.gi4.dto.DocumentMetadataDto;
import com.ensao.gi4.service.api.DocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/documents")
record DocumentController(DocumentService documentService)  {
	
	@GetMapping("/{id}/content")
	public ResponseEntity<ByteArrayResource> getDocumentFile(@PathVariable Long id){
		return documentService.findById(id)
				.map(document ->
						ResponseEntity
								.ok()
								.contentType(MediaType.parseMediaType(document.getFileType()))
								.header(HttpHeaders.CONTENT_DISPOSITION,
										"attachment:filename=\"" + document.getFilename() + "\"" )
								.body(new ByteArrayResource(document.getData())))
				.orElse(ResponseEntity.notFound().build());


		
	}
	
	@GetMapping("/{id}/metadata")
	public ResponseEntity<DocumentMetadataDto> getDocument(@PathVariable Long id){
        return documentService.findDocumentMetadataById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteDocument(@PathVariable Long id){
		return ResponseEntity.ok(documentService.deleteById(id));
	}

}
