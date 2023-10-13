package br.com.coldigogeladeiras.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.coldigogeladeiras.modelo.Produto;

public interface ProdutoDAO {
	
	public boolean inserir(Produto produto);
	public boolean alterar(Produto produto);
	public boolean deletar(int id);
	public List<JsonObject> buscaPorNome(String nome);
	public Produto buscarPorId(int id);
}
