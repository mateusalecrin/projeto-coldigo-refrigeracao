COLDIGO.marca = new Object();

$(document).ready(function() {
	
	COLDIGO.marca.buscar = function() {
		var valorBusca = $("#campoBuscaMarca").val();
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH + "marca/buscar",
			data: "valorBusca="+valorBusca,
			success: function(dados) {
				dados = JSON.parse(dados);
				$("#listaMarcas").html(COLDIGO.marca.exibir(dados));
			},
			error: function (info) {
				COLDIGO.exibirAviso("Erro ao consultar os contratos: "+ info.status + "-" + info.statusText);
			}
		}); 
	};
	
	COLDIGO.marca.buscar();
	
	COLDIGO.marca.excluir = function(id) {
		$.ajax({
			type: "DELETE",
			url: COLDIGO.PATH + "marca/excluir/"+id,
			success: function(msg){
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscar();
			},
			error: function(info) {
				COLDIGO.exibirAviso("Erro ao excluir marca: "+ info.status + " - " + info.statusText);
			}
		});
	},
	
	COLDIGO.marca.confirmarExclusao = function(id) {
		var modalConfirmaExclusao = {
			title: "Confirmar exclusão",
			height: 300,
			width: 550,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					COLDIGO.marca.excluir(id);
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			}
		};
		$("#modalConfirmaExclusao").dialog(modalConfirmaExclusao);
	};
	
	COLDIGO.marca.cadastrar = function() { 
		var marca = new Object();
		marca.nome = document.frmAddMarca.marca.value;
		if(marca.nome==""){
			COLDIGO.exibirAviso("Preencha todos os campos!");
		}else{
			$.ajax({
				type: "POST",
				url: COLDIGO.PATH + "marca/inserir",
				data: JSON.stringify(marca),
				success: function(msg) {
					COLDIGO.exibirAviso(msg);
					$("#addMarca").trigger("reset");
					COLDIGO.marca.buscar();
				},
				error: function(info) {
					COLDIGO.exibirAviso("Erro ao cadastrar uma nova marca: " + info.status + " - " + info.statusText);
				}
			});
		};
	};
	
	COLDIGO.marca.editar = function() {
		var marca = new Object();
		marca.id = document.frmEditaMarca.idMarca.value;
		marca.nome = document.frmEditaMarca.marca.value;
		$.ajax({
			type: "PUT",
			url: COLDIGO.PATH + "marca/alterar",
			data: JSON.stringify(marca),
			success: function(msg){
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscar();
				$("#modalEditaMarca").dialog("close");
			},
			error: function(info){
				COLDIGO.exibirAviso("Erro ao editar marca: "+ info.status + " - " + info.statusText);
			}
		});
	};
	
	COLDIGO.marca.alterarStatus = function(id, elemento) {
		console.log("marca.alterarStatus("+id+")");
		$.ajax({
			type: "PUT",
			url: COLDIGO.PATH + "marca/alterarStatus/"+id,
			success: function(msg) {
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscar();
			}, 
			error: function(info) {
				COLDIGO.exibirAviso("Erro ao alterar status: " + info.status + " - " + info.statusText);
				elemento.checked=!elemento.checked;
				console.log(info);
			}
		});
	};
	
	//Transforma os dados dos produtos recebidos do servidor em uma tabela HTML
	COLDIGO.marca.exibir = function(listaDeMarcas) {
		var tabelaMarcas = "<table>" +
		"<tr>" +
		"<th>Nome</th>" +
		"<th class='status'>Status</th>" +
		"<th class='acoes'>Ações</th>" +
		"</tr>";
		
		if (listaDeMarcas != undefined && listaDeMarcas.length > 0) {
			for (var i=0; i<listaDeMarcas.length; i++) {
				tabelaMarcas += "<tr>" +
				"<td>"+listaDeMarcas[i].nome+"</td>" +
				"<td>" +
					"<label class='switch'>" +
						"<input type='checkbox'" + (listaDeMarcas[i].status === 1 ? " checked" : "") + ">" +
						"<span class='slider round' onclick=\"COLDIGO.marca.alterarStatus('"+listaDeMarcas[i].id+"', this)\"></span>" +
					"</label>" +
				"</td>" +
				"<td>" +
					"<a onclick=\"COLDIGO.marca.exibirEdicao('"+listaDeMarcas[i].id+"')\"><img src='../../imgs/edit.png' alt='Editar registro'></a>" +
					"<a onclick=\"COLDIGO.marca.confirmarExclusao('"+listaDeMarcas[i].id+"')\"><img src='../../imgs/delete.png' alt='Excluir registro'></a>" +
				"</td>" +
				"</tr>";
				console.log("ID " + listaDeMarcas[i].id + " | " + listaDeMarcas[i].nome + " | " + listaDeMarcas[i].status);
			};
		} else if (listaDeMarcas == ""){
			tabelaMarcas += "<tr><td colspan='2'>Nenhum registro encontrado</td></tr>";
		};
		tabelaMarcas += "</table>";
		return tabelaMarcas;
	};
	
	COLDIGO.marca.exibirEdicao = function(id) {
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH + "marca/buscarPorId",
			data: "id="+id,
			success: function(marca) {
				
				document.frmEditaMarca.idMarca.value = marca.id;
				document.frmEditaMarca.marca.value = marca.nome;
				
				var modalEditaMarca = {
					title: "Editar Marca",
					height: 200,
					width: 550,
					modal: true,
					buttons: {
						"Salvar": function() {
							COLDIGO.marca.editar();
						},
						"Cancelar": function() {
							$(this).dialog("close");
						}
					},
					close: function(){
						//caso o usuário simplesmente feche a caixa de edição não deve acontecer nada
					}
				};
				$("#modalEditaMarca").dialog(modalEditaMarca);
			},
			error: function(info) {
				COLDIGO.exibirAviso("Erro ao buscar marca para edição: "+ info.status + " - " + info.statusText);
			}		
		});
	};
});