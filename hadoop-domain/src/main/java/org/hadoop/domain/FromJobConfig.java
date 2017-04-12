package org.hadoop.domain;

/**
 * Sqoop2 源job配置
 * @author luhaoyuan <lusyoe@163.com>
 * @version 1.0.0
 * @date 2017年4月11日
 */
public class FromJobConfig {

    /**
     * 源数据库名
     */
    public static final String SCHEMA_NAME = "fromJobConfig.schemaName";
    
    /**
     * 源表名
     */
    public static final String TABLE_NAME = "fromJobConfig.tableName";
    
    /**
     * 分割字段
     */
    public static final String PARITITION_COLUMN = "fromJobConfig.partitionColumn";
}
