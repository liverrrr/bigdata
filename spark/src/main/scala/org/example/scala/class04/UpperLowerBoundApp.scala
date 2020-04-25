package org.example.scala.class04

object UpperLowerBoundApp {


  /**
    * <: => 上界
    * >: => 下界
    */
  def maxValue[T <: Ordered[T]](x: T, y: T): T = {
    if (x.compareTo(y) > 0) x else y
  }

  /**
    * 视图界定
    * 内部是发生了隐式转化
    */
  def maxValue2[T <% Ordered[T]](x: T, y: T): T = {
    if (x.compareTo(y) > 0) x else y
  }

  /**
    * 上下文界定
    * 外部隐式转化
    */
  def maxValue3[T: Ordering](x: T, y: T)(implicit ordering: Ordering[T]): T = {
    if (ordering.compare(x, y) > 0) x else y
  }

  def main(args: Array[String]): Unit = {
    val user1 = new User("zhangsan", 18)
    val user2 = new User("lisi", 30)
    println(maxValue(user1, user2))

    println("--------------------------------------------------------------")

    /**
      * scala的Int并没继承Ordered
      * 如果没有视图界定，会报Int不属于Ordered的错误
      */
    println(maxValue2(10, 13))

    println("--------------------------------------------------------------")

    implicit val ordering=new Ordering[User2]{
      override def compare(x: User2, y: User2): Int = x.age-y.age
    }

    val user3 = new User2("zhangsan", 18)
    val user4 = new User2("lisi", 30)

    println(maxValue3(user3,user4))

  }

}

class User(val name: String, val age: Int) extends Ordered[User] {
  override def compare(that: User): Int = this.age - that.age
  override def toString: String = name + "\t" + age
}

class User2(val name:String,val age:Int){
  override def toString: String = name + "\t" + age
}


