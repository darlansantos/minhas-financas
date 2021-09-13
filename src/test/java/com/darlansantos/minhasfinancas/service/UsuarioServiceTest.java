package com.darlansantos.minhasfinancas.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.darlansantos.minhasfinancas.exception.RegraNegocioException;
import com.darlansantos.minhasfinancas.model.entity.Usuario;
import com.darlansantos.minhasfinancas.model.entity.repository.UsuarioRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Test(expected = Test.None.class) //Não lança exceção
	public void deveValidarEmail() {
		
		//cenário
		usuarioRepository.deleteAll();
		
		//ação - execução
		usuarioService.validarEmail("email@email.com");	
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		//cenário
		Usuario usuario = Usuario.builder().nome("Usuario").email("email@email.com").build();
		usuarioRepository.save(usuario);
		
		//acao
		usuarioService.validarEmail("email@email.com");
	}

}
