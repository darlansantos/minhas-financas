package com.darlansantos.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import lombok.Data;

@Data
@Entity
@Table(name = "lancamento", schema = "financas")
public class Lancamento {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "descricao")
	String descricao;
	
	@Column(name = "ano")
	private Integer ano;
	
	@Column(name = "mes")
	private Integer mes;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "data_Cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataCadastro;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

}
