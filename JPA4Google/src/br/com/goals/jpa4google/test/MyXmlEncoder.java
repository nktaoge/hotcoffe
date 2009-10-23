package br.com.goals.jpa4google.test;

import java.beans.XMLEncoder;
import java.io.OutputStream;

public class MyXmlEncoder extends XMLEncoder{

	public MyXmlEncoder(OutputStream out) {
		super(out);
	}

}
