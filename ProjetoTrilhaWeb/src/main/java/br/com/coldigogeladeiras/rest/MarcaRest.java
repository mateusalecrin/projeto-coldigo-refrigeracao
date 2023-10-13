package br.com.coldigogeladeiras.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.google.gson.JsonObject;
import br.com.coldigogeladeiras.bd.Conexao;
import br.com.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.com.coldigogeladeiras.modelo.Marca;

@Path("marca")
public class MarcaRest extends UtilRest {
	
	@GET
	@Path("/buscar")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@QueryParam("valorBusca") String nome) {
		
		try {
			
			List<JsonObject> listaMarcas = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscar(nome);
			conec.fecharConexao();
			
			String json = new Gson().toJson(listaMarcas);			
			return this.buildResponse(json);
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@DELETE
	@Path("/excluir/{id}")
	@Consumes("application/*")
	public Response excluir(@PathParam("id") int id) {
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			int consultaExclusao = jdbcMarca.consultaExclusao(id);
			String msg = "";
			
			switch(consultaExclusao) {
			case 0:
				msg = "A marca não existe.";
			break;
			case 1:
				msg = "A marca possui produtos vinculados. Consulte a tabela de produtos.";
			break;
			case 2:
				boolean retorno = jdbcMarca.deletar(id);				
				if(retorno) {
					msg += "Marca excluída com sucesso!";
				}else {
					msg += "Erro ao excluir marca.";
				}
			break;
			default:
				msg += "Algum erro durante a consulta de exclusão.";
			}
			
			conec.fecharConexao();
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}	

	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String marcaParam) {
		try {
			Marca marca = new Gson().fromJson(marcaParam, Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			String msg = "";
			
			boolean consulta = jdbcMarca.consultaDuplicidade(marca);
			
			if(consulta ) {
				boolean retorno = jdbcMarca.inserir(marca);				
				if(retorno) {
					msg = "Marca cadastrada com sucesso!";
				} else {
					msg = "Erro ao cadastrar produto.";
				};
			}else {
				msg = "Já existe marca com esse nome!";
			};
			
			conec.fecharConexao();
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	};
	
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String marcaParam) {
		try {
			Marca marca = new Gson().fromJson(marcaParam, Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			String msg = "";			
			boolean consulta = jdbcMarca.consultaDuplicidade(marca);
			if(consulta) {
				boolean retorno = jdbcMarca.alterar(marca);
				if (retorno) {
					msg = "Marca alterada com sucesso!";
				}else {
					msg = "Erro ao alterar marca.";
				};
			} else {
				msg = "Já existe uma marca com esse nome!";
			};
			conec.fecharConexao();
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@PUT
	@Path("/alterarStatus/{id}")
	@Consumes("application/*")
	public Response alterarStatus(@PathParam("id") int id) {
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			System.out.println("MarcaRest alterarStatus("+id+")");		
			
			boolean retorno = jdbcMarca.alterarStatus(id);
			String msg = "";
			
			if(retorno) {
				msg = "Status alterado com sucesso!";
			} else {
				msg = "Erro ao alterar status.";
			}
			
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/buscarPorId")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@QueryParam("id") int id) {
		
		try {
			Marca marca = new Marca();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			
			marca = jdbcMarca.buscarPorId(id);
			
			conec.fecharConexao();
			
			return this.buildResponse(marca);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/carregar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response carregar() {
		
		try {
			List<Marca> listaMarcas = new ArrayList<Marca>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.carregar();
			conec.fecharConexao();
			return this.buildResponse(listaMarcas);
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}
