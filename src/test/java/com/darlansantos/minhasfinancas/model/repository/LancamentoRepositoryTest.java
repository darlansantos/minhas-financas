package com.darlansantos.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
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
		
		Assertions.assertThat(lancamento.getId()).isNotNull();		
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		lancamentoRepository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoInexistente).isNull();
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
