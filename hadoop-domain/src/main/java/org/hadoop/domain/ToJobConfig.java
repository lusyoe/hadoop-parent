package org.hadoop.domain;

/**
 * Sqoop2 目标job配置
 * @author luhaoyuan <lusyoe@163.com>
 * @version 1.0.0
 * @date 2017年4月11日
 */
public class ToJobConfig {
    
    /**
     * Job输出目录
     */
    public static final String OUT_DIR = "toJobConfig.outputDirectory";
    
    /**
     * Job输出格式
     */
    public static final String OUT_FORMAT = "toJobConfig.outputFormat";

    /**
     * 追加模式，输出目录可不为空
     */
    public static final String APPEND_MODE = "toJobConfig.appendMode";
    
    
}
