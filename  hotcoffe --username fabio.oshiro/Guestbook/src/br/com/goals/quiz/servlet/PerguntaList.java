package br.com.goals.quiz.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.quiz.vo.Pergunta;
import br.com.goals.template.Template;

public class PerguntaList extends BaseServletQuiz{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Template template = getTemplate(request);
		if(request.getParameter("delId")!=null){
			quizFacade.apagarPergunta(request.getParameter("delId"));
		}
		if(request.getParameter("updId")!=null){
			response.sendRedirect("PerguntaAdd?id=" + request.getParameter("updId"));
		}else{
			List<Pergunta> list = quizFacade.listarPerguntas();
			template.encaixaResultSet(list);
			response.setContentType("text/html");
			response.getWriter().write(template.toString());
		}
	}
}
