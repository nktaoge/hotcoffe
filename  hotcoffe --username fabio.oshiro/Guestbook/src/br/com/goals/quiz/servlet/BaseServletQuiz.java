package br.com.goals.quiz.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import br.com.goals.quiz.QuizFacade;
import br.com.goals.template.Template;

public abstract class BaseServletQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static QuizFacade quizFacade = new QuizFacade();
	protected Template getTemplate(HttpServletRequest request) throws IOException{
    	Template template = new Template();
    	template.setTemplateFile(this.getClass().getSimpleName()+".html");
    	return template;
    }
}
