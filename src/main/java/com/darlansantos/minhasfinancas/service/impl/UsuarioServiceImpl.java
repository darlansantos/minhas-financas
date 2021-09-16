package com.darlansantos.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.darlansantos.minhasfinancas.exception.ErroAutenticacao;
import com.darlansantos.minhasfinancas.exception.RegraNegocioException;
import com.darlansantos.minhasfinancas.model.entity.Usuario;
import com.darlansantos.minhasfinancas.model.repository.UsuarioRepository;
import com.darlansantos.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository usuarioRepository;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return usuarioRepository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = usuarioRepository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return usuarioRepository.findById(id);
	}

}
