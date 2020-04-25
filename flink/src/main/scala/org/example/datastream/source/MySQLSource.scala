package org.example.datastream.source

import java.sql.{Connection, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.example.domain.Domain.City
import org.example.util.MySQLUtil

class MySQLSource extends RichParallelSourceFunction[City]{

  private var conn:Connection = _
  private var state:PreparedStatement = _

  override def open(parameters: Configuration): Unit = {
    val url = "jdbc:mysql://localhost:3306/g7"
    val user = "ruoze"
    val password = "ruozedata"
    conn = MySQLUtil.getConnection(url,user,password)
  }

  override def close(): Unit = {
    MySQLUtil.close(conn,state)
  }

  override def run(ctx: SourceFunction.SourceContext[City]): Unit = {
    val sql = "select * from city_info"
    state = conn.prepareStatement(sql)
    val rs = state.executeQuery()
    while(rs.next()){
      val id = rs.getInt(1)
      val name = rs.getString(2)
      val area = rs.getString(3)
      ctx.collect(City(id,name,area))
    }
  }

  override def cancel(): Unit = {}
}
