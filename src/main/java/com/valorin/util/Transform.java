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
            throws IOException, SQLException {
        return new ByteArrayInputStream(yml
                .saveToString().getBytes(StandardCharsets.UTF_8));
    }

    public static Object streamToYaml(InputStream stream) throws IOException,
            ClassNotFoundException, SQLException {
        final byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        final String str = new String(bytes, StandardCharsets.UTF_8);
        final YamlConfiguration yml = new YamlConfiguration();
        try {
            yml.load(new StringReader(str));
            stream.close();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return yml;
    }

    public static Blob serialize(Object obj) throws IOException,
            SQLException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        String string = byteArrayOutputStream.toString("ISO-8859-1");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return new SerialBlob(string.getBytes(StandardCharsets.UTF_8));
    }

    public static Object serializeToObject(Blob blob) throws IOException,
            ClassNotFoundException, SQLException {
        String string = new String(blob.getBytes(1, (int) blob.length()),
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                string.getBytes(StandardCharsets.ISO_8859_1));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }
}
