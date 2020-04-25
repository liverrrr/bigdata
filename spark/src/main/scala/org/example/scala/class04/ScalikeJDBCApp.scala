package org.example.scala.class04

import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs

object ScalikeJDBCApp {

  def select = {
    val infoes = DB.readOnly(implicit session => {
      SQL("select * from city_info").map(rs => {
        CityInfo(
          rs.int("city_id"),
          rs.string("city_name"),
          rs.string("area")
        )
      }).list().apply()
    })
    infoes.foreach(println)
  }

  def insert = {
    DB.autoCommit(implicit session => {
      SQL("insert into city_info(city_id,city_name,area) values(?,?,?)")
        .bind(11, "HANGZHOU", "SC")
        .update()
        .apply()
    })
  }

  def update: Int = {
    DB.autoCommit(implicit session => {
      SQL("update city_info set area=? where city_name=?")
        .bind("WC", "HANGZHOU")
        .update()
        .apply()
    })
  }

  def delete: Int = {
    DB.autoCommit(implicit session => {
      SQL("delete from city_info where city_id=?")
        .bind(11)
        .update()
        .apply()
    })
  }

  def main(args: Array[String]): Unit = {
    DBs.setupAll()

//    select
    insert
//    update
//    delete

    DBs.close()
  }



}

case class CityInfo(cityId:Int,cityName:String,area:String)
