package com.infosys.sqoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Value("${hadoop.hdfs.url}")
    private String hdfsUrl;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public Configuration config() {
        Configuration conf = new Configuration();

        conf.set("fs.defaultFS", hdfsUrl);;

        return conf;
    }
}