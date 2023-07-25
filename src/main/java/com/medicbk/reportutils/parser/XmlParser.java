package com.medicbk.reportutils.parser;

import com.medicbk.reportutils.dto.ReportDto;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class XmlParser {

    public ReportDto extractInfoFromXml(String xmlContent) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlContent));
            Document document = builder.parse(is);
            XPath xPath = XPathFactory.newInstance().newXPath();

            XPathExpression idExpr = xPath.compile("/patient/@id");
            XPathExpression uuidExpr = xPath.compile("/patient/@uuid");

            String idAttribute = (String) idExpr.evaluate(document, XPathConstants.STRING);
            String uuidAttribute = (String) uuidExpr.evaluate(document, XPathConstants.STRING);

            //todo test ver, need to be checked
            List<Long> nosologyIds = new ArrayList<>();
            NodeList nodeList = document.getElementsByTagName("subgroup");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element subgroupElement = (Element) nodeList.item(i);
                String nosologyIdValue = subgroupElement.getAttribute("nosologyId");
                if (!nosologyIdValue.isBlank()) {
                    nosologyIds.add(Long.parseLong(nosologyIdValue));
                }
            }

            return ReportDto.builder()
                    .patientId(Long.parseLong(idAttribute))
                    .uuid(UUID.fromString(uuidAttribute))
                    .nosologyIds(nosologyIds)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
