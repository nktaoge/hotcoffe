package br.com.goals.quiz.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.goals.quiz.QuizFacade;
import br.com.goals.quiz.vo.Opcao;
import br.com.goals.quiz.vo.Pergunta;
import br.com.goals.template.RequestUtil;
import br.com.goals.template.Template;

public class PerguntaAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static QuizFacade quizFacade = new QuizFacade();
	public Template getTemplate(HttpServletRequest request) throws IOException{
    	Template template = new Template();
    	template.setTemplateFile(this.getClass().getSimpleName()+".html");
    	return template;
    }
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			Pergunta pergunta;
			Template template = getTemplate(request);
			if(request.getParameter("id")!=null){
				pergunta = quizFacade.findPergunta(request.getParameter("id"));
				for(Opcao op:pergunta.getOpcoes()){
					System.out.println(op.getTexto());
				}
			}else{
				pergunta = new Pergunta();
			}
			template.setForm("pergunta", pergunta);
			response.getWriter().write(template.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Pergunta pergunta = new Pergunta();
		try {
			RequestUtil.setParseLong(true);
			RequestUtil.request(request, pergunta);
			quizFacade.salvar(pergunta);
			
			Opcao opcao = new Opcao();
			opcao.setCorreta(false);
			opcao.setTexto("Minha opcao");
			opcao.setPergunta(pergunta);
			//pergunta.getOpcoes().add(opcao);
			quizFacade.salvar(opcao);
			
			System.out.println("texto = " + pergunta.getTxtTexto());
			Template template = getTemplate(request);
			template.setForm("pergunta", pergunta);
			response.getWriter().write(template.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
