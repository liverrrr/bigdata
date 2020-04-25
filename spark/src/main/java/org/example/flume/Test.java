package org.example.flume;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        File parentDir = new File("D:\\tmp");

//        List<File> result = Lists.newArrayList();
//
//        getAllFiles(result,parentDir);
//
//        for (File file:result){
//            System.out.println(file);
//        }


        try (DirectoryStream<Path> stream = Files.newDirectoryStream(parentDir.toPath())) {
            for (Path entry : stream) {
                System.out.println(entry);
            }
        } catch (IOException e) {
            logger.error("I/O exception occurred while listing parent directory. " +
                    "Files already matched will be returned. " + parentDir.toPath(), e);
        }


    }

    private static void getAllFiles(List<File> result, File parentDir){
        File[] files = parentDir.listFiles();
        assert files != null;
        for (File file:files){
            if (file.isDirectory()){
                getAllFiles(result,file);
            }
            result.add(file);
        }
    }

}
