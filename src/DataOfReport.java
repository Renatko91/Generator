import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataOfReport {
    private int numberLenght;
    private int dateLenght;
    private int nameLenght;

    private ArrayList<String> numberList = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();

    public DataOfReport(String data, int numberLenght, int dateLenght, int nameLenght) {
        this.numberLenght = numberLenght;
        this.dateLenght = dateLenght;
        this.nameLenght = nameLenght;

        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);

        List<String[]> allRows = parser.parseAll(new File(data), "UTF-16");

        for(int i = 0; i < allRows.size(); i++) {
            String[] dataLine = allRows.get(i);

            for(int j = 0; j < dataLine.length; j++) {
                if(j == 0)
                    for(String s : formattingNumberList(dataLine[j])) {
                        numberList.add(s);
                    }
                else if(j == 1)
                    for(String s : formattingDateList(dataLine[j])) {
                        dateList.add(s);
                    }
                else
                    for(String s : formattingNameList(dataLine[j])) {
                        nameList.add(s);
                    }
            }
            numberList.add("");
            dateList.add("");
            nameList.add("");
        }
    }

    public ArrayList<String> getData1() {
        return numberList;
    }

    public ArrayList<String> getData2() {
        return dateList;
    }

    public ArrayList<String> getData3() {
        return nameList;
    }

    public ArrayList<String> formattingNumberList(String setting) {
        ArrayList<String> fNumberList = new ArrayList<>();
        setting = setting.trim();
        String transStr = "";

        while(true) {
            if (setting.length() > numberLenght) {
                transStr = setting.substring(0, numberLenght);
                fNumberList.add(transStr);
                setting = setting.substring(numberLenght);
            } else {
                fNumberList.add(setting);
                break;
            }
        }

        return fNumberList;
    }

    public ArrayList<String> formattingDateList(String setting) {
        ArrayList<String> fDateList = new ArrayList<>();
        setting = setting.trim();
        String transStr = "";

        if(setting.length() > dateLenght) {
            transStr = setting.substring(0, setting.lastIndexOf("/") + 1);
            fDateList.add(transStr);
            transStr = setting.substring(setting.lastIndexOf("/") + 1);
            fDateList.add(transStr);
        } else {
            fDateList.add(setting);
        }

        return fDateList;
    }

    public ArrayList<String> formattingNameList(String setting) {
        ArrayList<String> fNameList = new ArrayList<>();
        setting = setting.trim();
        StringBuffer sb = new StringBuffer(setting);

        if(sb.length() > nameLenght) {
            String s1 = sb.substring(0, nameLenght);
            String s2 = sb.substring(nameLenght);

            while(true) {
                if (s1.matches("(.{4})(\\s)(.{2})") ||
                        s1.matches("(.{5})(\\s)(.{1})") ||
                        s2.matches("(\\S{1})"))
                {
                    for (int j = 0; j < nameLenght - s1.lastIndexOf(' ') - 1; j++) {
                        sb.insert(s1.lastIndexOf(' '), " ");
                    }
                } else {
                    fNameList.add(s1.trim());
                }

                sb = new StringBuffer(sb.substring(nameLenght));

                if(sb.length() < nameLenght) {
                    if(sb.length() == 0) {
                        break;
                    }

                    fNameList.add(sb.substring(0, sb.length()).trim());
                    break;
                }
                s1 = sb.substring(0, nameLenght);
                s2 = sb.substring(nameLenght);
            }
        } else {
            fNameList.add(setting);
        }
        return fNameList;
    }
}
