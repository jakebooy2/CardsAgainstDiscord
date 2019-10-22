package me.tfkjake.cad.misc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Properties {

    private java.util.Properties properties;

    public Properties() {
        java.util.Properties prop = new java.util.Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        properties = prop;
    }

    public String getString(String path) {
        return properties.getProperty(path);
    }

    public int getInt(String path) {
        return Integer.parseInt(properties.getProperty(path));
    }

    public java.util.Properties getProperties() {
        return properties;
    }

}
