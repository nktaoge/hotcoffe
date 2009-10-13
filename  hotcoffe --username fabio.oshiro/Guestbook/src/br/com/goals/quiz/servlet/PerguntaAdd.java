package br.com.goals.quiz.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.quiz.vo.Pergunta;
import br.com.goals.template.RequestUtil;
import br.com.goals.template.Template;

public class PerguntaAdd extends BaseServletQuiz {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			Pergunta pergunta;
			Template template = getTemplate(request);
			if(request.getParameter("id")!=null){
				pergunta = quizFacade.findPergunta(request.getParameter("id"));
				System.out.println("opcoes.size = " + pergunta.getOpcoes().size());
			}else{
				pergunta = new Pergunta();
			}
			template.setForm("pergunta", pergunta);
			template.setForm("opcoes", pergunta.getOpcoes());
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(template.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Pergunta pergunta = new Pergunta();
			if(request.getParameter("Pergunta.id")!=null){
				pergunta = quizFacade.findPergunta(request.getParameter("Pergunta.id"));
				System.out.println("atualizando pergunta id " + pergunta.getId());
				System.out.println("opcoes = " + pergunta.getOpcoes().size());
			}
			RequestUtil.setParseLong(true);
			RequestUtil.request(request, pergunta);
			RequestUtil.request(request, pergunta.getOpcoes());
			quizFacade.salvar(pergunta);
			Template template = getTemplate(request);
			template.setForm("pergunta", pergunta);
			template.setForm("opcoes", pergunta.getOpcoes());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().write(template.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
