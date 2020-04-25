package org.example.datastream.sink

import java.sql.{Connection, PreparedStatement}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.example.domain.Domain.Access
import org.example.util.MySQLUtil

class MySQLRichSink extends RichSinkFunction[Access]{

  private var conn:Connection = _
  private var state:PreparedStatement = _
  private val sql = "INSERT INTO access_log (time, domain,flow) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE flow = ?"
  override def open(parameters: Configuration): Unit = {
    val url = "jdbc:mysql://localhost:3306/g7"
    val user = "ruoze"
    val password = "ruozedata"
    conn = MySQLUtil.getConnection(url,user,password)
  }

  override def close(): Unit = {
    MySQLUtil.close(conn,state)
  }

  override def invoke(value: Access, context: SinkFunction.Context[_]): Unit = {
    state = conn.prepareStatement(sql)
    state.setString(1,value.time)
    state.setString(2,value.domain)
    state.setInt(3,value.flow)
    state.setInt(4,value.flow)
    state.executeUpdate()
  }


}
