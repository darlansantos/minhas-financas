package com.darlansantos.minhasfinancas.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.darlansantos.minhasfinancas.exception.RegraNegocioException;
import com.darlansantos.minhasfinancas.model.entity.repository.UsuarioRepository;
import com.darlansantos.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	private UsuarioService usuarioService;
	private UsuarioRepository usuarioRepository;
	
	@Before
	public void setUp() {
		usuarioRepository = Mockito.mock(UsuarioRepository.class);
		usuarioService = new UsuarioServiceImpl(usuarioRepository);
	}

	@Test(expected = Test.None.class) //Não lança exceção
	public void deveValidarEmail() {
		
		//cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação - execução
		usuarioService.validarEmail("email@email.com");	
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		//cenário
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao
		usuarioService.validarEmail("email@email.com");
	}

}
