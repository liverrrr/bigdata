package org.example.spark.rdd

import org.example.spark.util.ContextUtils
import org.example.spark.util.ImplicitAspect._
import org.apache.spark.SparkConf

object TransformationApp {

  def main(args: Array[String]): Unit = {
    val sc = ContextUtils.getContext(this.getClass.getSimpleName)


    /**
      * parallelize
      * local 模式下分区数就是本机的core
      * 其他模式下就是2和总core其中最大那个
      */
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6))

    /**
      * map  对每条数据处理
      * mapPartitions 对每个分区的数据处理   =>  经典场景：mysql connect
      * mapPartitionsWithIndex 对每个带索引的分区进行处理
      */
    rdd.map(_ * 2)
    rdd.mapPartitions(x => x.map(_ * 2))
    rdd.mapPartitionsWithIndex((index, partition) => {
      partition.map(x => s"分区$index,数据$x")
    }).printInfo(1)

    /**
      * mapValues   对k,v类型的RDD中的V进行操作
      */
    val a = sc.parallelize(List(("ruoze", 30), ("Jespon", 18)))
    a.mapValues(_ + 1).printInfo(1)


    /**
      * flatMap = flatter + map
      */
    val b = sc.parallelize(List(List(1, 2, 3, 4), List(1, 3, 5, 7)))
    b.flatMap(x => x.map(_ * 2)).printInfo(1)

    sc.parallelize(1 to 10).map(1 to _).printInfo(1)
    sc.parallelize(1 to 10).flatMap(1 to _).printInfo(1)

    /**
      * glom 将分区的数据放在集合中 返回类型Array(Array(xx), Array(xx))
      */
    sc.parallelize(1 to 20).glom().printInfo(1)

    /**
      * sample 从原rdd取样，长度为传入参数fraction决定，数据是否重复由withReplacement决定
      */
    sc.parallelize(1 to 20).sample(false, 0.4, 5).printInfo(1)

    /**
      * filter 过滤不符合条件的
      */
    sc.parallelize(1 to 20).filter(x => x > 6 && x % 2 == 0).printInfo(1)

    /**
      * union  两个类型相同的rdd的合集
      * intersection 交集
      * subtract 差集
      */
    val one = sc.parallelize(List(1,3,5,7,9))
    val two = sc.parallelize(List(1,2,3,4,5))
    one.union(two)
    one.intersection(two)
    one.subtract(two)

    /**
      * distinct 去重
      * 如果加上numPartitions参数那么就会重新按照hash分区
      */
    one.union(two).distinct().mapPartitionsWithIndex((index,partition)=>{
      partition.map(x=>s"分区$index，数据$x")
    }).printInfo(1)
    // distinct底层实现
    one.union(two).map((_,null)).reduceByKey((x,y)=>x).map(_._1).printInfo(1)

    /**
      * 两者都有shuffle过程
      * reduceByKey 底层实现会有combine过程
      * groupByKey 没有
      *
      * groupBy 自定义分组规则
      * 底层还是使用了groupByKey
      */
    sc.parallelize(List(("a",1),("b",2),("c",3),("a",99))).reduceByKey(_+_).printInfo(1)
    sc.parallelize(List(("a",1),("b",2),("c",3),("a",99))).groupByKey().mapValues(x=>x.sum).printInfo(1)
    sc.parallelize(List(("a",1),("b",2),("c",3),("a",99))).groupBy(_._1).mapValues(x=>x.map(_._2).sum).printInfo(1)

    /**
      * sortBy 自定义排序规则 底层还是使用了sortByKey
      * sortByKey 只能对key排序
      */
    sc.parallelize(List(("ruoze",30),("J哥",18),("星星",60))).sortBy(-_._2).printInfo(1)
    sc.parallelize(List(("ruoze",30),("J哥",18),("星星",60))).map(x=>(x._2,x._1)).sortByKey(false).map(x=>(x._2,x._1)).printInfo(1)

    val address = sc.parallelize(List(("若泽","北京"),("J哥","上海"),("仓老师","杭州"),("星星","杭州")))
    val person = sc.parallelize(List(("若泽","30"),("J哥","18"),("星星","60")))

    /**
      * join底层就是使用了cogroup
      * RDD[K,V]
      *
      * 根据key进行关联，返回两边RDD的记录，没关联上的是空
      * join返回值类型  RDD[(K, (Option[V], Option[W]))]
      * cogroup返回值类型  RDD[(K, (Iterable[V], Iterable[W]))]
      */
    address.join(person).printInfo()
    address.leftOuterJoin(person).printInfo()
    address.rightOuterJoin(person).printInfo()
    address.fullOuterJoin(person).printInfo()
    address.cogroup(person).printInfo()


    sc.stop()
  }


}
