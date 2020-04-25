package org.example.spark.rdd.example01

import org.example.spark.util.ImplicitAspect._
import org.example.spark.util.ContextUtils

/**
 * 排序
 */
object SortApp {

  implicit val ord = Ordering[(Double, Int)].on[(String, Double, Int)](x => (-x._2, -x._3))

  implicit def Phone2Order(phone: Phone2): Ordered[Phone2] = new Ordered[Phone2] {
    override def compare(that: Phone2): Int = {
      -phone.price.compareTo(that.price)
    }
  }

  def main(args: Array[String]): Unit = {
    val sc = ContextUtils.getContext(this.getClass.getSimpleName)

    /**
     * 第一种排序
     */
    sc.parallelize(List("iphone11 5499 1000", "iphonexr 4799 3000", "iphone8 3499 3000", "huawei 3499 400"))
      .map(x => {
        val splits = x.split(" ")
        val name = splits(0)
        val price = splits(1).toDouble
        val amount = splits(2).toInt

        (name, price, amount)
      }).sortBy(x => (-x._2, -x._3)).printInfo()

    /**
     * 第二种排序 case class
     */
    sc.parallelize(List("huawei 3499 400", "iphone11 5499 1000", "iphonexr 4799 3000", "iphone8 3499 3000"))
      .map(x => {
        val splits = x.split(" ")
        val name = splits(0)
        val price = splits(1).toDouble
        val amount = splits(2).toInt

        Phone(name, price, amount)
      }).sortBy(x => x).printInfo()

    /**
     * 第三种 隐式转化
     */
    sc.parallelize(List("huawei 3499 400", "iphone11 5499 1000", "iphonexr 4799 3000", "iphone8 3499 3000"))
      .map(x => {
        val splits = x.split(" ")
        val name = splits(0)
        val price = splits(1).toDouble
        val amount = splits(2).toInt

        new Phone2(name, price, amount)
      }).sortBy(x => x).printInfo()

    /**
     * 第四种 隐式参数转化  Ordering[(Double,Int)].on
     */
    sc.parallelize(List("huawei 3499 400", "iphone11 5499 1000", "iphonexr 4799 3000", "iphone8 3499 3000"))
      .map(x => {
        val splits = x.split(" ")
        val name = splits(0)
        val price = splits(1).toDouble
        val amount = splits(2).toInt

        (name, price, amount)
      }).sortBy(x => x).printInfo()

    sc.stop()
  }

}

case class Phone(name: String, price: Double, amount: Int) extends Ordered[Phone] {

  override def compare(that: Phone): Int = {
    -this.price.compareTo(that.price)
  }

  override def toString: String = s"($name,$price,$amount)"
}


class Phone2(val name: String, val price: Double, val amount: Int) extends Serializable {
  override def toString: String = s"($name,$price,$amount)"
}