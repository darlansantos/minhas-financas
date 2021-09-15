package com.darlansantos.minhasfinancas.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.darlansantos.minhasfinancas.api.dto.LancamentoDTO;
import com.darlansantos.minhasfinancas.service.LancamentoService;

@RestController
@RequestMapping(value = "/api/lancamentos")
public class LancamentoResource {

	private LancamentoService lancamentoService;

	public LancamentoResource(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}
	
	@PostMapping
	public ResponseEntity<?> salvar (@RequestBody LancamentoDTO dto) {
		return null;
	}
	
}
