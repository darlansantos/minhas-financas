package com.darlansantos.minhasfinancas.model.entity.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.darlansantos.minhasfinancas.model.entity.Usuario;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Test
	void deveVerificarAExistenciaDeUmEmail() {	
		
		//cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
		usuarioRepository.save(usuario);
		
		//ação - execução
		boolean result = usuarioRepository.existsByEmail("usuario@gmail.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	void erroAoVerificarAExistenciaDeUmEmail() {	
		
		//cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
		usuarioRepository.save(usuario);
		
		//ação - execução
		boolean result = usuarioRepository.existsByEmail("usuario@gmail.com.br");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}

}
