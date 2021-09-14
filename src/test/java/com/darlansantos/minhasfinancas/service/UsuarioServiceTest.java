package com.darlansantos.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.darlansantos.minhasfinancas.exception.ErroAutenticacao;
import com.darlansantos.minhasfinancas.exception.RegraNegocioException;
import com.darlansantos.minhasfinancas.model.entity.Usuario;
import com.darlansantos.minhasfinancas.model.entity.repository.UsuarioRepository;
import com.darlansantos.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	private UsuarioService usuarioService;
	
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@Before
	public void setUp() {
		usuarioService = new UsuarioServiceImpl(usuarioRepository);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		
		//cenario
		String email = "email@email.com";
		String senha = "senha";		
		Usuario usuario = Usuario.builder().id(1L).email(email).senha(senha).build();		
		Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = usuarioService.autenticar(email, senha);
		
		//verificacao
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test(expected = ErroAutenticacao.class)
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		
		//cenario
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		usuarioService.autenticar("email@email.com", "senha");
	}
	
	@Test(expected = ErroAutenticacao.class)
	public void deveLancarErroQuandoSenhaNaoBater() {
		
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		usuarioService.autenticar(email, "123");
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
