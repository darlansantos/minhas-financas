package com.darlansantos.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	public ResponseEntity<?> buscar(		
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam(value = "usuario") Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if (usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody LancamentoDTO dto) {
		
		try {
			Lancamento entidade = converter(dto);
			entidade = lancamentoService.salvar(entidade);
			return new ResponseEntity<>(entidade, HttpStatus.CREATED);			
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity<?> autualizar(@PathVariable("id") long id, @RequestBody LancamentoDTO dto) {
		Optional<Lancamento> obj = lancamentoService.obterPorId(id);
		return obj.map(entidade -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entidade.getId());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);				
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
			new ResponseEntity<>("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));	
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deletar(@PathVariable("id") long id) {
		return lancamentoService.obterPorId(id).map(entidade -> {
			lancamentoService.deletar(entidade);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> 
		new ResponseEntity<>("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
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
