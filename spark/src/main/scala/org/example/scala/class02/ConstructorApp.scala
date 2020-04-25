package org.example.scala.class02

object ConstructorApp {

  def main(args: Array[String]): Unit = {
    val person = new Person("张三",18)
    println(person.job)

    val student = new Student("李四",23,"中文")
    println(student)
    println(student.major)
  }
}

/**
  * 主构造器 类名(参数名:参数类型)
  * 如果你想在外面调用Person的name和age必须在之前加val或者var,默认是私有的
  */
class Person(val name:String,val age:Int){
  println("进入Person主构造器")

  var job:String = _  // _ 占位符  前提是一定要明确是什么数据类型
  val sex="男"


  /**
    * 附属构造器的名称 this
    * 每个附属构造器的第一行必须调用主构造器或者其他的附属构造器
    */
  def this(name:String,age:Int,job:String){
    this(name,age)
    this.job=job
  }

  println("执行完Person主构造器")
}

/**
  * override是子类重写父类中的属性或者方法的修饰符
  * new 子类会调用父类的构造
  * 子类构造器只能在父类主构造器上添加属性
  */
class Student(name:String,age:Int,val major:String) extends Person(name,age){
  println("进入Student构造器")


  override val sex="女"

  override def toString: String = {
      this.name+"\t"+this.age+"\t"+this.job+"\t"+this.sex+"\t"+this.major
  }

  println("执行完Student构造器")
}
