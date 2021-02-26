package com.valorin.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;

public class Transform {
    public static InputStream yamlToStream(YamlConfiguration yml)
            throws IOException, SerialException, SQLException {
        ByteArrayInputStream stream = new ByteArrayInputStream(yml
                .saveToString().getBytes(StandardCharsets.UTF_8));
        return stream;
    }

    public static Object streamToYaml(InputStream stream) throws IOException,
            ClassNotFoundException, SQLException {
        final byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        final String str = new String(bytes, StandardCharsets.UTF_8);
        final YamlConfiguration yml = new YamlConfiguration();
        try {
            yml.load((Reader) new StringReader(str));
            stream.close();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return yml;
    }

    public static Blob serialize(Object obj) throws IOException,
            SerialException, SQLException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        String string = byteArrayOutputStream.toString("ISO-8859-1");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Blob blob = new SerialBlob(string.getBytes("UTF-8"));
        return blob;
    }

    public static Object serializeToObject(Blob blob) throws IOException,
            ClassNotFoundException, SQLException {
        String string = new String(blob.getBytes(1, (int) blob.length()),
                "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                string.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }
}
