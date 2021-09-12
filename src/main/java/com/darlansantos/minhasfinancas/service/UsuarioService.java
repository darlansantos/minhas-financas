package com.darlansantos.minhasfinancas.service;

import com.darlansantos.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticat(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);

}
