<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="br.com.goals.etrilhas.modelo.MapaItem" 
    %>
<%
MapaItem mapaItem = (MapaItem)request.getAttribute("mapaItem");
String id = request.getParameter("id");
if(id==null) id = mapaItem.getId()+"";
%>
<html>
<head>
	<title>MapaItem <%=request.getParameter("id")%></title>
</head>
<body>
	<h1>Definindo o Ponto cod: <%=request.getParameter("id")%></h1>
	<form action="definirMapaItem" method="post">
		<div>
			<input type="hidden" name="id" value="<%=id%>" />
			<label for="tipo">Tipo</label>
			<select name="tipo" id="tipo">
				<option value="Galeria">Galeria de Imagens</option>
				<option value="Foto360">Foto 360</option>
				<option value="Video">V&iacute;deo</option>
				<option value="MapaLinkMapa">Link para outro Mapa</option>
			</select>
		</div>
		<div>
			<label for="nome">Nome</label>
			<input type="text" name="nome" id="nome" value="<%=mapaItem.getNome()%>" />
		</div>
		<div>
			<label for="descricao">Descri&ccedil;&atilde;o</label>
			<textarea name="descricao" id="descricao"><%=mapaItem.getDescricao()%></textarea>
		</div>
		<div>
			<label for="icone">Icone</label>
			<input type="text" name="icone" id="icone" value="<%=mapaItem.getIcone()%>" />
		</div>
		<div>
			<input type="submit" value="Enviar" />
		</div>
	</form>
</body>
</html>