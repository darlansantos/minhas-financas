package com.darlansantos.minhasfinancas.api.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.darlansantos.minhasfinancas.api.dto.UsuarioDTO;
import com.darlansantos.minhasfinancas.exception.ErroAutenticacao;
import com.darlansantos.minhasfinancas.exception.RegraNegocioException;
import com.darlansantos.minhasfinancas.model.entity.Usuario;
import com.darlansantos.minhasfinancas.service.LancamentoService;
import com.darlansantos.minhasfinancas.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper; 

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioResourceTest {

	private static final org.springframework.http.MediaType JSON = org.springframework.http.MediaType.APPLICATION_JSON;

	private static final String API = "/api/usuarios/";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UsuarioService usuarioService;
	
	@MockBean
	private LancamentoService lancamentoService;
	
	@Test
	public void usuarioResourceTest() throws Exception {
		
		// Cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();		
		Usuario usuario = Usuario.builder().id(1L).email(email).senha(senha).build();	
		Mockito.when(usuarioService.autenticar(email, senha)).thenReturn(usuario);	
		String json = new ObjectMapper().writeValueAsString(dto);
		
		// Execucao e Verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
		;	
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		
		// Cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();					
		Mockito.when(usuarioService.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);	
		String json = new ObjectMapper().writeValueAsString(dto);
		
		// Execucao e Verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
		;	
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception {
		
		// Cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();		
		Usuario usuario = Usuario.builder().id(1L).email(email).senha(senha).build();	
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);	
		String json = new ObjectMapper().writeValueAsString(dto);
		
		// Execucao e Verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API)
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
		;	
	}
	
	@Test
	public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
		
		// Cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();			
		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		String json = new ObjectMapper().writeValueAsString(dto);
		
		// Execucao e Verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API)
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
		;	
	}

}
