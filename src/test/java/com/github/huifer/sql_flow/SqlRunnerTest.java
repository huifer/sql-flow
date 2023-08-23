package com.github.huifer.sql_flow;

import com.zaxxer.hikari.util.DriverDataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlRunnerTest {

  @Test
  public void test() throws SQLException {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    DriverDataSource root = new DriverDataSource(
        "jdbc:mysql://192.168.1.11:3306/sql-flow?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false",
        "com.mysql.cj.jdbc.Driver", new Properties(), "root", "YouCon123!");
    jdbcTemplate.setDataSource(root);

    // 创建事务工厂
    JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();

    // 创建环境
    Environment environment = new Environment("development", transactionFactory, root);

    // 创建配置
    Configuration configuration = new Configuration(environment);
    configuration.addMapper(SqlMapper.class);

    // 构建 SqlSessionFactory
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

    // 创建 SqlSession
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      // 构建查询参数
      Map<String, Object> params = new HashMap<>();
      params.put("id", 1); // 设置您要查询的ID值

      SqlMapper mapper = sqlSession.getMapper(SqlMapper.class);
      Object sql = mapper.sql(1);
      System.out.println();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  interface SqlMapper {

    @Select("""
         SELECT *
                                 FROM table_a as a
                                 WHERE 1=1
                                   and 
                                   a.id = #{id} 
        """)
    List<Map<String,Object>> sql(Integer id);
  }
}
