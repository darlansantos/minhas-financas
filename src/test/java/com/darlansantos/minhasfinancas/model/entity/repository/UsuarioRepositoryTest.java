package com.darlansantos.minhasfinancas.model.entity.repository;

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

import com.darlansantos.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	private TestEntityManager entityManager; 

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {	
		
		//cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
		entityManager.persist(usuario);
		
		//ação - execução
		boolean result = usuarioRepository.existsByEmail("usuario@gmail.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {	
		
		//cenário
		
		//ação - execução
		boolean result = usuarioRepository.existsByEmail("usuario@gmail.com");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}

}
