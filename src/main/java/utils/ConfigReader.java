package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private Properties prop;

    /**
     * 此方法用于初始化并加载 src/test/resources 路径下的 config.properties 文件
     * @return 返回加载后的 Properties 对象
     */
    public Properties init_prop() {
        prop = new Properties();
        try {
            // 确保文件路径与你的项目结构一致
            FileInputStream ip = new FileInputStream("./src/test/resources/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }
}