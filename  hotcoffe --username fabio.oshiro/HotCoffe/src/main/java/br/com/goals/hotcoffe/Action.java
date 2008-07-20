package br.com.goals.hotcoffe;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

public abstract class Action {
	String templateFileName;
	Template template;
	HttpServletRequest request;
	String message="";
	public void process() {
		//System.out.println(this.getClass().getSimpleName() + ".process()");
		template = new Template(this.getClass());
		// recolocar todos os valores de formulario
		template.mergeRequest(request);
		try {
			// popular os objetos
			initEntity();
			// chamar a acao abstrata
			execute();
			// colocar as mensagens
			template.setArea("message",message);
		} catch (HotCoffeException e) {
			//caso ocorra um erro
			template.setArea("message",e.getMessage().replace("\n","<br />"));
		}
	}
	/**
	 * Inicia as entidades encontradas na acao 
	 * @throws HotCoffeException
	 */
	private void initEntity() throws HotCoffeException {
		Field fields[] = this.getClass().getDeclaredFields();
		String errorMessage = "";
		for (Field field : fields) {
			// verificar se o field é um Entity
			if (!field.getType().getName().equals(Model.class.getName()))
				continue;
			try {
				boolean accessible = field.isAccessible();
				if (!accessible)
					field.setAccessible(true);
				Model model = (Model) field.get(this);
				if (model == null)
					continue;
				
				// popular os campos deste entity
				for (Attribute attribute : model.getAttributeList()) {
					System.out.print(model.getName() + "." + attribute.name);
					System.out.println("\n\t" + attribute.className);
					String htmlInputName = model.getName() + "." + attribute.name;
					// resgatar o valor, converter aff e setar no entity
					String value = request.getParameter(htmlInputName);
					
					try {
						validateRequest(attribute,value);
					} catch (Exception e) {
						errorMessage+=e.getMessage()+"\n";
					}
				}
				
				if (!accessible)
					field.setAccessible(false);
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(!errorMessage.equals("")){
				throw new HotCoffeException(errorMessage);
			}
		}
	}

	public abstract void execute();

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	private static void validateRequest(Attribute attribute, String value) throws HotCoffeException {
		if (value == null) {
			return;
		}
		try {
			if (attribute.className.equals("java.lang.String")) {
				// verificar o size
				if(attribute.size<value.length()) 
					throw new HotCoffeException(Messages.getString(attribute.parent.getName()+"."+attribute.name+".SizeError"));
				attribute.value = value;
			} else if (attribute.className.equals("java.lang.Double")) {
				attribute.value = Double.valueOf(value);
			} else if (attribute.className.equals("java.lang.Long")) {
				attribute.value = Long.valueOf(value);
			} else if (attribute.className.equals("java.lang.Integer")) {
				attribute.value = Integer.valueOf(value);
			} else if (attribute.className.equals("java.sql.Timestamp")) {
				// TODO verificar a conversao
				attribute.value = java.sql.Timestamp.valueOf(value);
			} else if (attribute.className.equals("java.lang.Float")) {
				attribute.value = Float.valueOf(value);
			} else if (attribute.className.equals("java.sql.Date")) {
				java.util.Date date = new SimpleDateFormat(Messages.getString("dateFormat")).parse(value);
				attribute.value = new java.sql.Date(date.getTime());
			} else if (attribute.className.equals("java.math.BigDecimal")) {
				attribute.value = java.math.BigDecimal.valueOf(Double.valueOf(value));
			} else if (attribute.className.equals("java.lang.Boolean")) {
				if (value != null && (value.equals("1") || value.equals("true") || value.equals("on"))) {
					attribute.value = true;
				} else {
					attribute.value = false;
				}
			}else{
				System.err.println("Type not recognized '" + attribute.className + "'!");
			}
			
		} catch (Exception e) {
			throw new HotCoffeException(Messages.getString(attribute.parent.getName()+"."+attribute.name+".TypeError"));
		}
	}
}
