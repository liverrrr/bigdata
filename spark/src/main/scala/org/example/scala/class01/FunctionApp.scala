package org.example.scala.class01

object FunctionApp {

  def main(args: Array[String]): Unit = {
    /**
      * 函数第一种定义：val/var 函数名称 = （参数列表） => {函数体}
      */
    val f1 = (a: Int, b: Int) => {
      a + b
    }

    /**
      * 函数第二种定义：val/var 函数名称：（输入参数类型） => 返回值类型  = （输入参数的引用） => {函数体}
      */
    val f2: (Int, Int) => Int = (a: Int, b: Int) => a + b

    /**
      * for 循环中的 yield 会把当前的元素记下来，保存在集合中，循环结束后将返回该集合。
      */
    val seq = for (v <- 1 to 5; w <- 6 to 10) yield v+w
    println(seq)
  }

  /**
    * 定义function
    * def function名(参数名:参数类型):返回类型={方法体}
    * 如果你不想返回，返回类型就可以是Unit
    * scala默认最后一行当做返回值，不需要写return
    * scala也可以不写返回类型，会自动根据返回值类型推导
    */
  def standard(x: Int, y: Int) = {
    x + y
  }

  /**
    * 函数柯里化定义
    */
  def haskellCurry(x: Int)(y: Int) = x + y

  /**
    * 大部分场景返回值可以Scala自身推导出来
    * 但是：对于递归调用的，一定要显式指明返回值类型
    */
  def recursion(num: Int): Int = {
    if (num <= 1) {
      1
    } else {
      num + recursion(num - 1)
    }
  }

  /**
    * scala支持默认参数
    * 当你调用function不传参数时，方法体中使用的是默认值
  */
  def defaultParameters(param: String = "default") = {
    println(param)
  }

  /**
    * scala支持可变参数
    * 只需要在参数后面加上*
    * 如果你要把集合传入，请在其后加上:_*
    * 例如：
    * variableParameters(List(1,2,3,4,5):_*)
    * variableParameters(1 to 10:_*)
    */
  def variableParameters(nums: Int*) = {
    var result = 0
    for (ele <- nums) {
      result += ele
    }
    result
  }


}
