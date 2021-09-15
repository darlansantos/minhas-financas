package com.darlansantos.minhasfinancas.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.darlansantos.minhasfinancas.model.entity.Lancamento;
import com.darlansantos.minhasfinancas.model.entity.enums.StatusLancamento;
import com.darlansantos.minhasfinancas.model.entity.repository.LancamentoRepository;
import com.darlansantos.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private LancamentoRepository lancamentoRepository;
	
	public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
		this.lancamentoRepository = lancamentoRepository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		return lancamentoRepository.save(lancamento);
	}

	@Override
	public Lancamento atualizar(Lancamento lancamento) {
		return null;
	}

	@Override
	public void deletar(Lancamento lancamento) {

	}

	@Override
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		return null;
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {

	}

}
