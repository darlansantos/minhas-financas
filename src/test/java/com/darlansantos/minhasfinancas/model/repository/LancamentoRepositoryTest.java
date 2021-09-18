package com.darlansantos.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.darlansantos.minhasfinancas.model.entity.Lancamento;
import com.darlansantos.minhasfinancas.model.enums.StatusLancamento;
import com.darlansantos.minhasfinancas.model.enums.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		
		lancamento = lancamentoRepository.save(lancamento);
		
		assertThat(lancamento.getId()).isNotNull();		
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		lancamentoRepository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoInexistente).isNull();
	}

	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		lancamento.setAno(2021);
		lancamento.setDescricao("Teste Atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		lancamentoRepository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2021);
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizar");
		assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEPersistirUmLancamento();		
		
		Optional<Lancamento> lancamentoEncontrado = lancamentoRepository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	private Lancamento criarEPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		return lancamento;
	}

	private Lancamento criarLancamento(	) {
		Lancamento lancamento = Lancamento.builder()
								  	.ano(2021)
								  	.mes(9)
								  	.descricao("Lancamento qualquer")
								  	.valor(BigDecimal.valueOf(10))
								  	.tipo(TipoLancamento.RECEITA)
								  	.status(StatusLancamento.PENDENTE)
								  	.build();
		return lancamento;
	}

}
