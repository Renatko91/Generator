import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class Generator {
    static int height = 0;

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        SettingReport setting = new SettingReport(args[0]);
        DataOfReport data = new DataOfReport(args[1]);
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(args[2], false), "UTF-16");
        //FileWriter fw = new FileWriter(args[2]);

        ArrayList<String> numberList = data.getData1();
        ArrayList<String> dateList = data.getData2();
        ArrayList<String> nameList = data.getData3();

        while(true) {
            if(height == 0) {
                reportContent(fw, setting,
                        setting.getSettingName(2),
                        setting.getSettingName(3),
                        setting.getSettingName(4)
                );
                repSep(fw, setting.getSettingValue("Ширина"));
            }

            int max = Math.max(checkL(nameList),
                    Math.max(checkL(dateList),
                            checkL(numberList)
                    )
            );

            if(height + max + 1 < setting.getSettingValue("Высота")) {
                for (int i = 0; i < max; i++) {
                    reportContent(fw, setting,
                            checkL(numberList) > i ? data.getData1().get(i) : "",
                            checkL(dateList) > i ? data.getData2().get(i) : "",
                            checkL(nameList) > i ? data.getData3().get(i) : ""
                    );
                }
                removeList(data.getData1(), checkL(numberList));
                removeList(data.getData2(), checkL(dateList));
                removeList(data.getData3(), checkL(nameList));
            } else {
                fw.write("~");
                fw.write("\n");
                height = 0;
            }


            if(checkL(numberList) == 0) {
                break;
            }

            if(height != 0 && height + 2 < setting.getSettingValue("Высота")) {
                repSep(fw, setting.getSettingValue("Ширина"));
            }
        }

        fw.close();
    }

    public static void removeList(ArrayList<String> list, int amount) {
        for(int i = 0; i < amount + 1; i++){
            list.remove(0);
        }
    }

    public static Integer checkL(ArrayList<String> inane) {
        int index = 0;
        for(String s : inane) {
            if(s.isEmpty()) {
                index = inane.indexOf(s);
            }
        }
        return index;
    }

    public static void reportContent(OutputStreamWriter fw,
                                     SettingReport setting,
                                     String value1,
                                     String value2,
                                     String value3
    ) throws IOException {
        String text = String.format("| %s | %s | %s |",
                space(value1, setting.getSettingValue("Номер")),
                space(value2, setting.getSettingValue("Дата")),
                space(value3, setting.getSettingValue("ФИО"))
        );

        fw.write(text + "\n");
        height++;
    }

    public static void repSep(OutputStreamWriter fw, int width) throws IOException {
        for(int i = 0; i < width; i++) {
            fw.write("-");
        }
        fw.write("\n");
        height++;
    }

    public static String space(String set, int count){
        for(int i = set.length(); i < count; i++) {
            set += " ";
        }
        return set;
    }
}
