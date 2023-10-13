package br.com.coldigogeladeiras.jdbcinterface;

import java.util.List;

import br.com.coldigogeladeiras.modelo.Marca;
import com.google.gson.JsonObject;

public interface MarcaDAO {

	public boolean inserir(Marca marca);
	public boolean alterar(Marca marca);
	public boolean deletar(int id);
	public List<Marca> carregar();
	public Marca buscarPorId(int id);
	public List<JsonObject> buscar(String nome);
	public boolean alterarStatus(int id);
}
