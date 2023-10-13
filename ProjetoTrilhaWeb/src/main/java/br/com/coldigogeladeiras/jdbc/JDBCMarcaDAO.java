package br.com.coldigogeladeiras.jdbc;

import java.util.List;

import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import br.com.coldigogeladeiras.modelo.Marca;
import br.com.coldigogeladeiras.jdbcinterface.MarcaDAO;

public class JDBCMarcaDAO implements MarcaDAO {
	
	private Connection conexao;
	
	public JDBCMarcaDAO (Connection conexao) {
		this.conexao = conexao;
	}
	
	public boolean inserir(Marca marca) {
		String comando = "INSERT INTO marcas "
				+ "(id, nome) "
				+ "VALUES (?,?)";
		try {
			PreparedStatement pInsercao;			
			pInsercao = this.conexao.prepareStatement(comando);
			pInsercao.setInt(1, marca.getId());
			pInsercao.setString(2, marca.getNome());
			pInsercao.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	};
	
	public List<Marca> carregar() {
		String comando = "SELECT * FROM marcas WHERE status=? ORDER BY nome ASC";
		int marcasAtivas = 1;
		List<Marca> listMarcas = new ArrayList<Marca>();
		//Criação do objeto marca com valor null (ou seja, sem instanciá-lo)
		Marca marca = null;	
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, marcasAtivas);
			ResultSet rs = p.executeQuery();	
			while (rs.next()) {		
				//Criação de instância da classe Marca
				marca = new Marca();
				//Recebimento dos 2 dados retornados do BD para cada linha encontrada
				int id = rs.getInt("id");
				String nome = rs.getString("nome");
				int statusV = rs.getInt("status");
				marca.setId(id);
				marca.setNome(nome);
				marca.setStatus(statusV);	
				listMarcas.add(marca);
			};	
			} catch (Exception ex) {
				//Exibe a exceção no console
				ex.printStackTrace();
			}
		return listMarcas;	
	};
	
	public boolean deletar(int id) {
		String comando = "DELETE FROM marcas WHERE id = ?";
		PreparedStatement p;
		
		try {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int consultaExclusao (int id ) {
		String comandoExistencia = "SELECT id FROM marcas WHERE id = ?";
		String comandoVinculo = "SELECT id FROM produtos WHERE marcas_id = ?";
		int retornoConsultaExclusao = 0;
		
		try {
			PreparedStatement consultaExistencia = conexao.prepareStatement(comandoExistencia);
			consultaExistencia.setInt(1, id);
			ResultSet rs = consultaExistencia.executeQuery();			
			
			if(!rs.next()) {
				//A marca não existe no banco de dados.
				retornoConsultaExclusao = 0;
			}else {
				//A marca existe.
				PreparedStatement consultaVinculo = conexao.prepareStatement(comandoVinculo);
				consultaVinculo.setInt(1, id);
				ResultSet rsVinculo = consultaVinculo.executeQuery();
				
				if(rsVinculo.next()) {
					//A marca existe, porém possui produtos vinculados.
					retornoConsultaExclusao = 1;
				}else {
					//A marca existe e nao possui produtos vinculados.
					retornoConsultaExclusao = 2;
				}
			}
			return retornoConsultaExclusao; 
		}catch(SQLException e) {
			e.printStackTrace();
			return retornoConsultaExclusao = 3;
		}
	}
	
	public List<JsonObject> buscar(String nome) {
		
		String comando = "SELECT * FROM marcas ";
		
		if(!nome.equals("")) {
			comando += "WHERE nome LIKE '%" + nome + "%' ";
		}
		
		comando += "ORDER BY nome ASC";
		
		List<JsonObject> listaMarcas = new ArrayList<JsonObject>();
		JsonObject marca = null;
		
		try {
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			
			while (rs.next()) {
				
				int id = rs.getInt("id");
				String nomeMarca = rs.getString("nome");
				int status = rs.getInt("status");
				
				marca = new JsonObject();
				marca.addProperty("id", id);
				marca.addProperty("nome", nomeMarca);
				marca.addProperty("status", status);
				listaMarcas.add(marca);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return listaMarcas;
	}

	public Marca buscarPorId(int id) {
		String comando = "SELECT * FROM marcas WHERE marcas.id = ?";
		Marca marca = new Marca();
		
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				
				String nomeMarca = rs.getString("nome");
				
				marca.setId(id);
				marca.setNome(nomeMarca);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return marca;
	}
	
	public boolean alterar(Marca marca) {
		String comando = "UPDATE marcas SET nome=? WHERE id=?";
		PreparedStatement p;
		
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, marca.getNome());
			p.setInt(2, marca.getId());
			p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean alterarStatus(int id) {
		String consultaStatus = "SELECT status FROM marcas WHERE id=?";
		String comando = "UPDATE marcas SET status=? WHERE id=?";	
		try {
			PreparedStatement consulta = conexao.prepareStatement(consultaStatus);
			consulta.setInt(1, id);
			ResultSet rs = consulta.executeQuery();
			
			if(!rs.next()) {
				//A marca/status com esse id nao existe
				return false;
				
			} else {
				int statusMarca = rs.getInt("status");
				statusMarca = 1 - statusMarca;
				PreparedStatement altera = this.conexao.prepareStatement(comando);
				altera.setInt(1, statusMarca);
				altera.setInt(2, id);
				altera.executeUpdate();				
			}
			return true;			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}	
	};

	public boolean consultaDuplicidade(Marca marca) {
		String consultaInsercao = "SELECT COUNT(*) AS count FROM marcas WHERE nome=?";
		try {
			PreparedStatement pConsulta = this.conexao.prepareStatement(consultaInsercao);
			pConsulta.setString(1, marca.getNome());
			ResultSet rs = pConsulta.executeQuery();
			if(rs.next()) {
				int count = rs.getInt("count");
				if (count > 0) {
					//Marca já existe
					return false;
				};
			};
			return true;			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	};
};
