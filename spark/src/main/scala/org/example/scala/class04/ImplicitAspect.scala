package org.example.scala.class04

import java.io.File

object ImplicitAspect {

  /**
    *
    * 多个隐式类型转化最好放在一个Object里便于随时使用
    *
    * 格式：
    * implicit def AToB(A):B=new B(...)
    */

  implicit def fileToRichFile(file: File):RichFile=new RichFile(file)
  implicit def manToSuperMan(man:Man):SuperMan=new SuperMan(man.name)
}
