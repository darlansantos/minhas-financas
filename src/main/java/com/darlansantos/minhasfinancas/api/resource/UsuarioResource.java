package com.darlansantos.minhasfinancas.api.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "usuarios")
public class UsuarioResource {
	
	@GetMapping
	public String helloWorld() {
		return "Hello world!";
	}

}
