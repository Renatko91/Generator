import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingReport {
    private LinkedHashMap<String, Integer> mapWithSettings = new LinkedHashMap<>();

    public SettingReport(String setting) throws IOException, SAXException, ParserConfigurationException {
        mapWithSettings.put("Ширина",
                Integer.parseInt(
                        getXMLvalue(setting, "page", "width", 0)
                )
        );

        mapWithSettings.put("Высота",
                Integer.parseInt(
                        getXMLvalue(setting, "page", "height", 0)
                )
        );

        for(int i = 0; i < 3; i++) {
            mapWithSettings.put(
                    getXMLvalue(setting, "column", "title", i),
                    Integer.parseInt(
                            getXMLvalue(setting, "column", "width", i)
                    )
            );
        }
    }

    public Integer getSettingValue(String settingValue) {
        return mapWithSettings.get(settingValue);
    }

    public String getSettingName(int number) {
        int count = 0;
        String settingName = "";
        for(Map.Entry<String, Integer> setting : mapWithSettings.entrySet()) {
            if(count++ == number) {
                settingName = setting.getKey();
            }
        }
        return settingName;
    }

    public String getXMLvalue(String setting,
                                     String tag,
                                     String name,
                                     int index
    ) throws ParserConfigurationException,
            IOException,
            SAXException {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(setting));

            Node pageSettings = document.getElementsByTagName(tag).item(index);
            Element pageSetting = (Element) pageSettings;

            return pageSetting
                    .getElementsByTagName(name)
                    .item(0)
                    .getTextContent();
    }
}
