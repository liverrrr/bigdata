package org.example.datastream.source

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.example.domain.Domain.CityInfo
import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs

class ScalikeSource extends RichSourceFunction[CityInfo]{

  override def run(ctx: SourceFunction.SourceContext[CityInfo]): Unit = {

    DBs.setupAll()
    DB.readOnly(implicit session => {
      SQL("select * from city_info").map(rs => {
        val cityInfo = CityInfo(
          rs.int("city_id"),
          rs.string("city_name"),
          rs.string("area")
        )
        ctx.collect(cityInfo)
      }).list().apply()
    })
    DBs.close()
  }

  override def cancel(): Unit = {

  }
}
