package com.darlansantos.minhasfinancas.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.darlansantos.minhasfinancas.api.dto.LancamentoDTO;
import com.darlansantos.minhasfinancas.exception.RegraNegocioException;
import com.darlansantos.minhasfinancas.model.entity.Lancamento;
import com.darlansantos.minhasfinancas.model.entity.Usuario;
import com.darlansantos.minhasfinancas.model.entity.enums.StatusLancamento;
import com.darlansantos.minhasfinancas.model.entity.enums.TipoLancamento;
import com.darlansantos.minhasfinancas.service.LancamentoService;
import com.darlansantos.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping(value = "/api/lancamentos")
public class LancamentoResource {

	private LancamentoService lancamentoService;
	
	private UsuarioService usuarioService;

	public LancamentoResource(LancamentoService lancamentoService, UsuarioService usuarioService) {
		this.lancamentoService = lancamentoService;
		this.usuarioService = usuarioService;
	}
	
	@PostMapping
	public ResponseEntity<?> salvar (@RequestBody LancamentoDTO dto) {
		converter(dto);
		return null;
		
	}

	private Lancamento converter(LancamentoDTO dto) {
		Usuario usuario = usuarioService
				.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado."));
		
		Lancamento lancamento = Lancamento.builder()
				.descricao(dto.getDescricao())
				.ano(dto.getAno())
				.mes(dto.getMes())
				.valor(dto.getValor())
				.tipo(TipoLancamento.valueOf(dto.getTipo()))
				.status(StatusLancamento.valueOf(dto.getStatus()))
				.usuario(usuario)
				.build();
		
		return lancamento;
		
	}
		
}
