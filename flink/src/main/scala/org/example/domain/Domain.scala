package org.example.domain

object Domain {

  case class WC(word:String,count:Int)
  case class Access(time:String,domain:String,flow:Int)
  case class City(id:Int,name:String,area:String)
  case class Url(id:Long, url:String, count:Long)
  case class CityInfo(id:Int,cityName:String,area:String)
}
