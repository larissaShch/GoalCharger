package org.larissashch.portfolio.goalcharger.persistence.repository.xml;

import org.larissashch.portfolio.goalcharger.model.entity.Administrator;
import org.larissashch.portfolio.goalcharger.model.entity.ApplicationProperties;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.model.entity.User;


import org.larissashch.portfolio.goalcharger.persistence.repository.UserRepository;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class InXMLUserRepository implements UserRepository {

	private String customerFileName;
	private String administratorFileName;
	private Integer customerLastId;
	private Integer administratorLastId;
	private Document customerDocument;
	private Document administratorDocument;

	public InXMLUserRepository(boolean isTest) {
		String testName = "";
		if (isTest) {
			testName = "Test";
		}
		customerFileName = ApplicationProperties.DB_PATH + testName + "Customer.xml";
		administratorFileName = ApplicationProperties.DB_PATH + testName + "Administrator.xml";
		
		File file;
		file = new File(customerFileName);
		if(file.length()==0){
			file.delete();
		}
		customerDocument = this
				.createXMLDocument(customerFileName, "customers");
		
		file = new File(administratorFileName);
		if(file.length()==0){
			file.delete();
		}
		administratorDocument = this.createXMLDocument(administratorFileName,
				"administrators");

		if (!new File(customerFileName).exists()) {
			customerLastId = 0;
			this.writeInXML(customerDocument, customerFileName);
		} else {
			customerLastId = this.getMaxLastId(customerDocument);
		}

		

		if (!new File(administratorFileName).exists()) {
			administratorLastId = 0;
			this.writeInXML(administratorDocument, administratorFileName);
		} else {
			administratorLastId = this.getMaxLastId(administratorDocument);
		}

	}

	
	private int getMaxLastId(Document document) {
		int lastId = 0;

		if (document.getDocumentElement().getChildNodes().item(1) != null) {

			NodeList nodeList = document
					.getElementsByTagName(document.getDocumentElement()
							.getChildNodes().item(1).getNodeName());

			if (nodeList != null) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					NamedNodeMap namedNodeMap = node.getAttributes();
					Node nodeAttr = namedNodeMap.getNamedItem("id");
					if (nodeAttr != null) {

						if (Integer.parseInt(nodeAttr.getTextContent()) > lastId) {
							lastId = Integer.parseInt(nodeAttr.getTextContent());
						}

					}
				}
				return lastId;
			}
		}
		return 0;
	}

	private Document createXMLDocument(String fileName, String rootElementName) {
		String exceptionMessage = "Error Message: \nMethod: createXMLDocument(String fileName, String rootElementName)\nFile name: "
				+ fileName + "\nRoot  element name: " + rootElementName + "\n";
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();

			Document document;
			if (!(new File(fileName).exists())) {
				document = documentBuilder.newDocument();
				Element rootElement = document.createElement(rootElementName);
				document.appendChild(rootElement);
				this.writeInXML(document, fileName);

				return document;
			}

			document = documentBuilder.parse(new File(fileName));

			document.getDocumentElement().normalize();

			return document;

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(exceptionMessage);
		} finally {
		}

	}

	private void writeInXML(Document document, String fileName) {
		synchronized (document) {
			String exceptionMessage = "Error Message: \nMethod: writeInXML(Document document, String fileName)\nFile name: "
					+ fileName + "\n";
			try {
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer;

				transformer = transformerFactory.newTransformer();

				DOMSource domSource = new DOMSource(document);
				StreamResult streamResult = new StreamResult(new File(fileName));

				transformer.transform(domSource, streamResult);
			} catch (TransformerException e) {
				e.printStackTrace();
				throw new RuntimeException(exceptionMessage);
			}
		}
	}

	private Element createNewElement(Document document, Element rootElement,
			String newChildElementName, String value) {
		if (newChildElementName != null) {
			if (value != null) {
				Element element = document.createElement(newChildElementName);
				element.appendChild(document.createTextNode(value));
				rootElement.appendChild(element);
				return element;
			}
			Element element = document.createElement(newChildElementName);
			rootElement.appendChild(element);
			return element;
		}
		return null;
	}

	private void createNewAttribute(Document document, Element element,
			String newAttributeName, String value) {
		if (value != null) {
			Attr attr = document.createAttribute(newAttributeName);
			attr.setValue(value);
			element.setAttributeNode(attr);
		}
	}

	private Node getElement(Document document, String attributeName,
			String value) {
		if (document.getDocumentElement().getChildNodes().item(1) != null) {
			NodeList nodeList = document
					.getElementsByTagName(document.getDocumentElement()
							.getChildNodes().item(1).getNodeName());

			if (nodeList != null) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					NamedNodeMap namedNodeMap = node.getAttributes();
					Node nodeAttr = namedNodeMap.getNamedItem(attributeName);
					if (nodeAttr != null) {

						if (nodeAttr.getTextContent().toString().equals(value)) {
							return node;
						}

					}
				}
			}
		}

		return null;
	}

	private void removeElement(Document document, Node root, Node node) {
		if (node != null) {
			NodeList nodeList = document.getElementsByTagName(node
					.getNodeName());

			if (nodeList != null) {

				for (int i = 0; i < nodeList.getLength(); i++) {

					if (nodeList.item(i) == node) {
						root.removeChild(nodeList.item(i));
					}
				}
			}
			
		}

	}

	private <T extends User> void save(Document document, T user) {

		if (this.getElement(document, "id", Integer.toString(user.getId())) != null) {


			this.removeElement(document, document.getDocumentElement(), this
					.getElement(document, "id", Integer.toString(user.getId())));

		}

		if (user.getClass().getSimpleName().toString().toLowerCase()
				.equals("customer")) {
			synchronized (customerDocument) {
				Element customerElement = this
						.createNewElement(customerDocument,
								customerDocument.getDocumentElement(),
								"customer", null);
				this.saveUser(customerDocument, customerElement, user);
				Customer customer = (Customer) user;


				if (customer.isTesterAccountFlag()) {
					this.createNewElement(customerDocument, customerElement,
							"testeraccountflag", "true");
				}
				if (customer.isAutoDeleteFlag()) {
					this.createNewElement(customerDocument, customerElement,
							"autodeleteflag", "true");
				}
				this.writeInXML(customerDocument, customerFileName);
			}

		}
		if (user.getClass().getSimpleName().toString().toLowerCase()
				.equals("administrator")) {
			synchronized (administratorDocument) {
				Element administratorElement = this.createNewElement(
						administratorDocument,
						administratorDocument.getDocumentElement(),
						"administrator", null);
				this.saveUser(administratorDocument, administratorElement, user);
				//Administrator administrator = (Administrator) user;
				
				this.writeInXML(administratorDocument, administratorFileName);
			}
		}

	}

	private void saveUser(Document document, Element element, User user) {
		this.createNewAttribute(document, element, "id",
				Integer.toString(user.getId()));
		this.createNewAttribute(document, element, "email", user.getEmail());
		this.createNewElement(document, element, "firstname",
				user.getFirstName());
		this.createNewElement(document, element, "lastname", user.getLastName());
		this.createNewElement(document, element, "password", user.getPassword());

		this.createNewElement(document, element, "birthdate",
				this.convertDateToString(user.getBirthDate()));

		this.createNewElement(document, element, "createdate",
				this.convertDateToString(user.getCreateDate()));

		//Element userElement;


	}

	@SuppressWarnings("unchecked")
	private <T extends User> T read(Document document, int id) {
		Node node = this.getElement(document, "id", Integer.toString(id));
		Element element = (Element) node;

		if (node != null) {
			if (node.getNodeName().equals("customer")) {
				Customer user = new Customer();


				if (element.getElementsByTagName("testeraccountflag").item(0) != null) {

					user.setTesterAccountFlag(true);
				}
				if (element.getElementsByTagName("autodeleteflag").item(0) != null) {

					user.setAutoDeleteFlag(true);
				}

				return (T) this.<Customer> readUser(element, user);
			}
			if (node.getNodeName().equals("administrator")) {
				Administrator user = new Administrator();

				
				return (T) this.<Administrator> readUser(element, user);
			}
		}
		return null;
	}

	private <T extends User> T readUser(Element element, T user) {
		Node node = (Node) element;
		NamedNodeMap namedNodeMap = node.getAttributes();
		Node nodeAttr = namedNodeMap.getNamedItem("id");
		if (nodeAttr != null) {
			user.setId(Integer.parseInt(nodeAttr.getTextContent()));
		}

		namedNodeMap = node.getAttributes();
		nodeAttr = namedNodeMap.getNamedItem("email");
		if (nodeAttr != null) {
			user.setEmail(nodeAttr.getTextContent());
		}

		user.setFirstName(element.getElementsByTagName("firstname").item(0)
				.getTextContent());
		user.setLastName(element.getElementsByTagName("lastname").item(0)
				.getTextContent());
		user.setPassword(element.getElementsByTagName("password").item(0)
				.getTextContent());

		user.setBirthDate(this.convertStringToDate(element
				.getElementsByTagName("birthdate").item(0).getTextContent()));

		user.setCreateDate(this.convertStringToDate(element
				.getElementsByTagName("createdate").item(0).getTextContent()));
		

		return user;
	}

	private String convertDateToString(Date date) {
		if (date != null) {
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(date);

			StringBuilder result = new StringBuilder();

			result.append(calendar.get(Calendar.DAY_OF_MONTH));
			result.append('.');
			result.append(calendar.get(Calendar.MONTH) + 1);
			result.append('.');
			result.append(calendar.get(Calendar.YEAR));

			return result.toString();
		}
		return null;
	}

	private Date convertStringToDate(String dateString) {
		String messageException = "Error Message: \nMethod: convertStringToDate(String dateString); \ndateString: "
				+ dateString;

		if (dateString.length() > 0) {
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("dd.MM.yyyy");
			Date date;
			try {
				date = format.parse(dateString);
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(messageException);
			}

		}

		return null;
	}

	@Override
	public void saveCustomer(Customer customer) {
		synchronized(customerLastId){
			if(customer.getId()==0){
				customerLastId++;
				customer.setId(customerLastId);
			}
			if(customer.getId()>customerLastId){
				customerLastId = customer.getId();
			}
		}
		synchronized (customerDocument) {
			this.<Customer> save(customerDocument, customer);
		}

	}

	@Override
	public Customer readCustomer(int id) {
		synchronized (customerDocument) {
			return this.read(customerDocument, id);
		}
	}

	@Override
	public void deleteCustomer(int id) {
		synchronized (customerDocument) {
			this.removeElement(customerDocument,
					customerDocument.getDocumentElement(),
					this.getElement(customerDocument, "id", Integer.toString(id)));
			this.writeInXML(this.customerDocument, this.customerFileName);
		}
	}

	@Override
	public void saveAdministrator(Administrator administrator) {
		synchronized(administratorLastId){
			if(administrator.getId()==0){
				administratorLastId++;
				administrator.setId(administratorLastId);
			}
			if(administrator.getId()>administratorLastId){
				administratorLastId = administrator.getId();
			}
		}
		synchronized (administratorDocument) {
			this.<Administrator> save(administratorDocument, administrator);
		}

	}

	@Override
	public Administrator readAdministrator(int id) {
		synchronized (administratorDocument) {
			return this.read(administratorDocument, id);
		}
	}

	@Override
	public void deleteAdministrator(int id) {
		synchronized (administratorDocument) {
			this.removeElement(
					administratorDocument,
					administratorDocument.getDocumentElement(),
					this.getElement(administratorDocument, "id",
							Integer.toString(id)));
			this.writeInXML(this.administratorDocument,
					this.administratorFileName);
		}
	}

	@Override
	public int getCustomerCount() {
		synchronized (customerDocument) {
			
			if(customerDocument.getDocumentElement().getChildNodes().item(1)!=null){
				NodeList nodeList = customerDocument.getElementsByTagName(customerDocument.getDocumentElement().getChildNodes().item(1).getNodeName());
				return nodeList.getLength();
			}
			return 0;
		}
	}

	@Override
	public int getAdministratorCount() {
		synchronized (administratorDocument) {
			if(administratorDocument.getDocumentElement().getChildNodes().item(1)!=null){
				NodeList nodeList = administratorDocument
						.getElementsByTagName(administratorDocument
								.getDocumentElement().getChildNodes().item(1)
								.getNodeName());
	
				return nodeList.getLength();
			}
			return 0;
		}
	}


	@Override
	public <T extends User> void saveUser(T user) {
		if(user.getUserType().equalsIgnoreCase("Customer")){
			this.saveCustomer((Customer)user);
		}
		if(user.getUserType().equalsIgnoreCase("Administrator")){
			this.saveAdministrator((Administrator)user);
		}
		
	}




}

