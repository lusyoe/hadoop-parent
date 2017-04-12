package org.hadoop.domain;

/**
 * Sqoop2 链接属性配置
 * @author luhaoyuan <lusyoe@163.com>
 * @version 1.0.0
 * @date 2017年4月11日
 */
public class LinkConfig {

    /**
     * 连接属性名
     */
    public static final String CONN_STRING = "linkConfig.connectionString";
    
    /**
     * jdbc属性名
     */
    public static final String JDBC_DRIVER = "linkConfig.jdbcDriver";

    /**
     * 数据库连接用户名 
     */
    public static final String USERNAME = "linkConfig.username";
    
    /**
     * 数据库连接密码
     */
    public static final String PASSWORD = "linkConfig.password";
    
    /**
     * hadoop 配置文件路径名
     */
    public static final String CONF_DIR = "linkConfig.confDir";
    
}
