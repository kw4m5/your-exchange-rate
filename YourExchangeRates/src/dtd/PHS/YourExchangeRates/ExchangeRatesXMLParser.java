package dtd.PHS.YourExchangeRates;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


public class ExchangeRatesXMLParser extends ExchangeRatesCollector {

	public ExchangeRatesXMLParser(String URLStr) {
		super(URLStr);
		try {
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			URL sourceUrl = new URL(this.URLStr);
			MyXMLHandler myXMLHandler = new MyXMLHandler();
			xr.setContentHandler(myXMLHandler);
			xr.parse(new InputSource(sourceUrl.openStream()));
			this.date = MyUtility.changeDateFormat(myXMLHandler.getDate());
			this.currencies = myXMLHandler.getCurrencies();
			this.rates = myXMLHandler.getRates();
		} catch (Exception e) {
			
		}
	}

}
