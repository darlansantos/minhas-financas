package com.darlansantos.minhasfinancas.model.entity.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.darlansantos.minhasfinancas.model.entity.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {	
		
		//cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
		usuarioRepository.save(usuario);
		
		//ação - execução
		boolean result = usuarioRepository.existsByEmail("usuario@gmail.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {	
		
		//cenário
		usuarioRepository.deleteAll();
		
		//ação - execução
		boolean result = usuarioRepository.existsByEmail("usuario@gmail.com");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}

}
