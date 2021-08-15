package com.valorin.configuration.languagefile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.valorin.Main;

public class LanguageFileLoader {

    private final Map<File, List<String>> lang;
    private final List<String> defaultLang;
    private List<File> customLangList;
    private final Map<String, List<String>> subDefaultLang;

    public LanguageFileLoader() {
        lang = new HashMap<>();
        defaultLang = new ArrayList<>();
        subDefaultLang = new HashMap<>();
        customLangList = addLanguages();

        loadDefaultLanguage();
        loadSubDefaultLanguage();

        copyChecker();
        customLangList = addLanguages();
        LanguageFileChecker();
        loadLang();
    }

    public void close() {
        lang.clear();
        defaultLang.clear();
        subDefaultLang.clear();
        customLangList.clear();
    }

    public List<File> getLanguagesList() {
        return customLangList;
    }

    public Map<File, List<String>> getLang() {
        return lang;
    }

    public List<String> getDefaultLang() {
        return defaultLang;
    }

    private void loadDefaultLanguage() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Main
                    .getInstance().getResource("DefaultLanguage.txt"), StandardCharsets.UTF_8));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                defaultLang.add(s);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<File> addLanguages() {// 获取所有语言文件
        List<File> fileList = new ArrayList<>();
        File file = new File("plugins/Dantiao/Languages");
        File[] files = file.listFiles();
        if (files == null) {
            return fileList;
        }
        for (File f : files) {
            if (f.isFile()) {
                if (f.getName().substring(f.getName().lastIndexOf(".") + 1)
                        .equals("txt")) {
                    fileList.add(f);
                }
            }
        }
        return fileList;
    }

    private void loadLang() {// 载入语言文件的内容
        lang.clear();
        List<File> fileList = customLangList;
        if (fileList == null) {
            return;
        }
        for (File file : fileList) {
            List<String> messages = new ArrayList<>();
            try {
                FileReader fr = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fr);
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    messages.add(s);
                }
                bufferedReader.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            lang.put(file, messages);
        }
    }

    private void copyChecker() {
        File textFile1 = new File("plugins/Dantiao/Languages/zh_CN.txt");
        File textFile2 = new File("plugins/Dantiao/Languages/zh_TW.txt");
        File textFile3 = new File("plugins/Dantiao/Languages/en.txt");
        File fileParent = textFile1.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        File[] fileList = {textFile1, textFile2, textFile3};
        for (File testFile : fileList) {
            if (!testFile.exists()) {
                try {
                    testFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void LanguageFileChecker() {
        List<File> fileList = customLangList;
        for (File file : fileList) {
            int count = 0;
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                Scanner scanner = new Scanner(fis);
                while (scanner.hasNextLine()) {
                    scanner.nextLine();
                    count++;
                }
                scanner.close();
                if (count < defaultLang.size()) {// 检测到行数缺失
                    loadSubDefaultLanguage();
                    List<String> mould = defaultLang; // 提示语模板
                    if (file.getName().equals("zh_CN.txt")
                            || file.getName().equals("zh_TW.txt")
                            || file.getName().equals("en.txt")) {
                        mould = subDefaultLang.get(file.getName().replace(
                                ".txt", ""));
                    }

                    List<String> logMessageList = new ArrayList<>();

                    try {
                        FileReader fr = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fr);
                        String s;
                        while ((s = bufferedReader.readLine()) != null) {
                            logMessageList.add(s);
                        }
                        bufferedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int i = logMessageList.size(); i < mould.size(); i++) {
                        logMessageList.add(mould.get(i));
                    }

                    try {
                        BufferedWriter writer = new BufferedWriter(
                                new FileWriter(file));
                        for (int i = 0; i < logMessageList.size(); i++) {
                            if (i == logMessageList.size() - 1) {
                                writer.write(logMessageList.get(i));
                            } else {
                                writer.write(logMessageList.get(i) + "\n");
                            }
                        }
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void loadSubDefaultLanguage() {
        String[] fileLocations = {"Languages/zh_CN.txt", "Languages/zh_TW.txt",
                "Languages/en.txt"};
        for (String fileLocation : fileLocations) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        Main.getInstance().getResource(fileLocation), StandardCharsets.UTF_8));
                String s;
                List<String> mList = new ArrayList<>();
                while ((s = bufferedReader.readLine()) != null) {
                    mList.add(s);
                }
                subDefaultLang.put(
                        fileLocation.replace("Languages/", "").replace(".txt", ""),
                        mList);
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
