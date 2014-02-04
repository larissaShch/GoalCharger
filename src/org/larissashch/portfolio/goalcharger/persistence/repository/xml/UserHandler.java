package org.larissashch.portfolio.goalcharger.persistence.repository.xml;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UserHandler extends DefaultHandler {
	
	private Map<Integer, String> data = new HashMap<Integer, String>();
	private int id;
	private String email;
	private String password;
	private String element;
	private String mainElementName;

	public UserHandler(String mainElementName){
		this.mainElementName = mainElementName;
	}

	@Override
	public void startDocument() throws SAXException{
		
	}
	
	@Override
	public void endDocument() throws SAXException{
		
	}
	
	@Override
	public void startElement(String nameSpace, String localName, String qName, Attributes attr){
		element = qName;
		
		//System.out.println("Start read element "+element);
		if (element.equals(mainElementName)){
			id = Integer.parseInt(attr.getValue("id"));
			email = attr.getValue("email");
		}
	}
	
	@Override
	public void endElement(String nameSpace, String localName, String qName) throws SAXException{
		//System.out.println("End read element "+element);
		element = "";
		if (element.equals(mainElementName)){
			id = -1;
		}
	}
	
	@Override
	public void characters(char []ch, int start, int end){
		//System.out.println("Element:"+element+", Characters:"+ch);
		if(element.equals("password")){
			password = new String (ch, start, end);
			//System.out.println("id:"+id+", "+email+password);
			data.put(id, email+password);
		}
		
		
	}
	
	public Map<Integer, String> getDate(){
		return data;
	}
}
