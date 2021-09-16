package com.darlansantos.minhasfinancas.model.repository;

import java.util.Optional;

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
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private TestEntityManager entityManager; 

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {	
		
		//cenário	
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//ação - execução
		boolean result = usuarioRepository.existsByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {	
		
		//cenário
		
		//ação - execução
		boolean result = usuarioRepository.existsByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}
	
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		
		//cenario
		Usuario usuario = criarUsuario();
		
		//acao
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		//Verificacao
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		//cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		//acao
		Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");
				
		//verificacao
		Assertions.assertThat(result.isPresent()).isTrue(); 
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		
		//cenario

		//acao
		Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");
				
		//verificacao
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	private static Usuario criarUsuario() {
		return  Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com") 
				.senha("senha")
				.build();	
	}

}
