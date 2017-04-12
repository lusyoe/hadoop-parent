package org.hadoop.domain;

/**
 * Sqoop输出格式
 * @author luhaoyuan <lusyoe@163.com>
 * @version 1.0.0
 * @date 2017年4月11日
 */
public enum ToFormat {

    /**
     * Comma separated text file
     */
    TEXT_FILE,
    
    /**
     * Sequence file
     */
    SEQUENCE_FILE,
    
    /**
     * Parquet file
     */
    PARQUET_FILE,
}
