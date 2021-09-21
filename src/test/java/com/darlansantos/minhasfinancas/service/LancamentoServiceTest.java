package com.darlansantos.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.darlansantos.minhasfinancas.exception.RegraNegocioException;
import com.darlansantos.minhasfinancas.model.entity.Lancamento;
import com.darlansantos.minhasfinancas.model.enums.StatusLancamento;
import com.darlansantos.minhasfinancas.model.repository.LancamentoRepository;
import com.darlansantos.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.darlansantos.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
		
	@SpyBean
	private LancamentoServiceImpl lancamentoService;
	
	@MockBean
	private LancamentoRepository lancamentoRepository;

	@Test
	public void deveSalvarUmLancamento() {
		
		// Cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(lancamentoService).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		// Execucao
		Lancamento lancamento = lancamentoService.salvar(lancamentoASalvar);
		
		// Verificacao
		assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		
		// Cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(lancamentoService).validar(lancamentoASalvar);
		
		// Execucao e Verificacao
		catchThrowableOfType(() -> lancamentoService.salvar(lancamentoASalvar), RegraNegocioException.class);
		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		
		// Cenario	
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(lancamentoService).validar(lancamentoSalvo);
		
		Mockito.when(lancamentoRepository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		// Execucao
		lancamentoService.atualizar(lancamentoSalvo);
		
		// Verificacao
		Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		
		// Cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		// Execucao e Verificacao
		catchThrowableOfType(() -> lancamentoService.atualizar(lancamento), NullPointerException.class);
		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamento);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		
		// Cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1L);
		
		// Execuxao
		lancamentoService.deletar(lancamento);
		
		// Verificacao
		Mockito.verify(lancamentoRepository).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		
		// Cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		// Execucao
		catchThrowableOfType(() -> lancamentoService.deletar(lancamento), NullPointerException.class);
		
		// Verificacao
		Mockito.verify(lancamentoRepository, Mockito.never()).delete(lancamento);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deveFiltrarLancamentos() {
		
		// Cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1L);
		
		List<Lancamento> lista = Arrays.asList(lancamento); 
		Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		// Execucao
		List<Lancamento> resultado = lancamentoService.buscar(lancamento);
		
		// Verificacoes
		assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);		
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		
		// Cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1L);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(lancamentoService).atualizar(lancamento);
		
		// Execucao
		lancamentoService.atualizarStatus(lancamento, novoStatus);
		
		// Verificacoes
		assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(lancamentoService).atualizar(lancamento);
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		
		// Cenario
		Long id = 1L;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamento));
		
		// Execucao
		Optional<Lancamento> resultado = lancamentoService.obterPorId(id);
		
		// Verificacao
		assertThat(resultado.isPresent()).isTrue();		
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		
		// Cenario
		Long id = 1L;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());
		
		// Execucao
		Optional<Lancamento> resultado = lancamentoService.obterPorId(id);
		
		// Verificacao
		assertThat(resultado.isPresent()).isFalse();		
	}
	
}
