package com.darlansantos.minhasfinancas.service;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		
		// Execucao e Verificacao
		catchThrowableOfType(() -> lancamentoService.atualizar(lancamentoASalvar), NullPointerException.class);
		Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);
	}
	
}
