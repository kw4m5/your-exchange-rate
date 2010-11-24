package dtd.PHS.YourExchangeRates;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyXMLHandler extends DefaultHandler {

	ArrayList<String> currencies;
	ArrayList<Double> rates;
	String date;
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if (localName.equals("Cube")) {
			if (attributes.getIndex("time") != -1) {
				this.initData();
				this.date = attributes.getValue("time");
			} else if (attributes.getIndex("currency") != -1){
				this.currencies.add(attributes.getValue("currency"));
				this.rates.add(Double.parseDouble(attributes.getValue("rate")));
			}
		}		

	}

	private void initData() {
		this.currencies = new ArrayList<String>();
		this.rates = new ArrayList<Double>();		
	}
	
	public ArrayList<String> getCurrencies() {
		return this.currencies;
	}
	
	public ArrayList<Double> getRates() {
		return this.rates;
	}
	public String getDate() {
		return this.date;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}

