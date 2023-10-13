function validaFaleConosco() {
	var nome = document.frmfaleconosco.txtnome.value;
	var expRegNome = new RegExp("^[A-zÀ-ü]{3,}([ ]{1}[A-zÀ-ü]{2,})+$");

	if (!expRegNome.test(nome)) {
		alert("Preencha o campo Nome corretamente!")
		document.frmfaleconosco.txtnome.focus();
		return false;
	}

	var fone = document.frmfaleconosco.txtfone.value;
	var expRegFone = new RegExp("^[(]{1}[1-9]{2}[)]{1}[0-9]{4,5}[-]{1}[0-9]{4}$");

	if (!expRegFone.test(fone)) {
		alert("Preencha o campo Telefone corretamente.");
		document.frmfaleconosco.txtfone.focus();
		return false;
	}

	if (document.frmfaleconosco.txtemail.value=="") {
		alert("Preencha o campo E-mail.");
		document.frmfaleconosco.txtemail.focus();		
		return false;
	}

	if (document.frmfaleconosco.selmotivo.value=="ES") {
		alert("Selecione o motivo.");
		document.frmfaleconosco.selmotivo.focus();
		return false;
	}
	
	if ((document.frmfaleconosco.selmotivo.value=="PR") && (document.frmfaleconosco.selproduto.value=="")) {
		alert ("Selecione o produto a se obter informações.");
		document.frmfaleconosco.selproduto.focus();
		return false;
	}
	
	if (document.frmfaleconosco.txtcomentario.value=="") {
		alert("Preencha o campo comentário.");
		document.frmfaleconosco.txtcomentario.focus();
		return false;
	}
	
	return true;
}

function verificaMotivo(motivo) {
	// Capturamos a estrutura da div cujo ID é a opcaoProduto na variável elemento
	var elemento = document.getElementById("opcaoProduto")
	
	// Se o valor da variável motivo for "PR"
	if (motivo=="PR"){
		// Criamos um elemento (tag) <select> e guardamos na variável homônima
		var select = document.createElement("select");
		// Setamos nesse novo select o atributo 'name' com o valor 'selproduto'
		select.setAttribute("name", "selproduto");
		// Conteúdo atual da variável select:
		// <select name="selproduto"></select>
		
		// Criamos um elemento (tag) <option> e guardamos na variável homônima
		var option = document.createElement("option");
		// Setamos nesse novo option o atributo 'value' com o valor vazio
		option.setAttribute("value", "");
		// Criamos um nó de texto "Escolha" e gravamos na variável 'texto'
		var texto = document.createTextNode("Escolha");
		// Colocamos o nó de texto criado como "filho" da tag option criada
		option.appendChild(texto);
		// Conteúdo atual da variável option:
		// <option value="">Escolha</option>
		
		// Colocamos o option criado como "Filho" da tag select criada
		select.appendChild(option);
		// Conteúdo atual da variável select:
		// <select name="selproduto"><option value="">Escolha</option></select>
		
		// Criamos um elemento (tag) <option> e guardamos na variável homônima
		var option = document.createElement("option");
		// Setamos nesse novo option o atributo 'value' com o valor "FR"
		option.setAttribute("value", "FR");
		// Criamos um nó de texto "Freezer" e gravamos na variável 'texto'
		var texto = document.createTextNode("Freezer");
		// Colocamos o nó de texto criado como "filho" da tag option criada
		option.appendChild(texto);
		// Conteúdo atual da variável option:
		// <option value ="FR">Freezer</option>
		
		// Colocamos o option criado como "filho" da tag select criada
		select.appendChild(option);
		// Conteúdo atual da variável select:
		// <select name="selproduto">
		// <option value="">Escolha</option>
		// <option value="FR">Freezer</option>
		// </select>
		
		// Criamos um elemento (tag) <option> e guardamos na variável homônima
		var option = document.createElement("option");
		// Setamos nesse novo option o atributo 'value' com o valor "GE"
		option.setAttribute("value", "GE");
		// Criamos um nó de texto "Geladeira" e gravamos na variável 'texto'
		var texto = document.createTextNode("Geladeira");
		// Colocamos o nó de texto criado como "filho" da tag option criada
		option.appendChild(texto);
		// Conteúdo atual da variável option:
		// <option value="GE">Geladeira</option>
		
		// Colocamos o option criado como "filho" da tag select criada
		select.appendChild(option);
		// Conteúdo atual da variável select:
		// <select name="selproduto">
		// <option value="">Escolha</option>
		// <option value="FR">Freezer</option>
		// <option value="GR">Geladeira</option>
		// </select>
		
		// Colocamos o select criado como "filho" da tag div capturada no início da função
		elemento.appendChild(select);
		// Se o valor da variável motivo não for "PR"...
	} else {
		// Se a div possuir algum "primeiro filho"
		if (elemento.firstChild)
		//Removemos ele
		elemento.removeChild(elemento.firstChild);
	}
}
// Fim da função verificaMotivo()

// Assim que o documento HTML for carregado por completo...
$(document).ready(function() {
	// Carrega cabeçalho, menu e rodapé aos respectivos locais
	$("header").load("/ProjetoTrilhaWeb/pages/site/general/cabecalho.html");
	$("nav").load("/ProjetoTrilhaWeb/pages/site/general/menu.html");
	$("footer").load("/ProjetoTrilhaWeb/pages/site/general/rodape.html");	
});