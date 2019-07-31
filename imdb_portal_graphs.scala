import java.time.LocalDate

import edu.drexel.cs.dbgroup.portal.representations.OneGraph
import org.apache.spark.sql.SparkSession
import edu.drexel.cs.dbgroup.portal.{Interval, TEdge}
import edu.drexel.cs.dbgroup.portal.representations.VEGraph
import edu.drexel.cs.dbgroup.portal.representations.OneGraphColumn
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import scala.util.matching.Regex
import edu.drexel.cs.dbgroup.portal.ProgramContext


object GraphOps {
  case class CrimeType(primaryType: String)

  //Helper Functions

  def IdtoLong(id: String, key: Int): Long = {
    val result = id.slice(2, id.length)
    return (result.toLong + key * 1000000000)

  }

  def ismovieId(id: Any): Long = {
    val numberPattern: Regex = "tt[0-9]+".r
    val newid = id.toString
    numberPattern.findFirstMatchIn(newid) match {
      case Some(_) => return IdtoLong(newid, 1)
      case None => return 0;
    }
  }

  def dateFormat(id: String): String = {
    return id + "-01-01"
  }

  def isStartYear(id: Any): String = {
    val numberPattern: Regex = "18|19|20\\d\\d".r
    val newid = id.toString
    numberPattern.findFirstMatchIn(newid) match {
      case Some(_) => return dateFormat(newid)
      case None => return "0";
    }
  }

  def isEndYear(id: Any): String = {
    val numberPattern: Regex = "18|19|20\\d\\d".r
    val newid = id.toString

    if (newid.length > 5) {
      return "0";
    }
    numberPattern.findFirstMatchIn(newid) match {
      case Some(_) =>
        val temp = newid.toInt + 1;
        return dateFormat(temp.toString)
      case None => return "0";
    }
  }

  def ispersonId(id: Any): Long = {
    val numberPattern: Regex = "nm[0-9]+".r
    val newid = id.toString

    numberPattern.findFirstMatchIn(newid) match {
      case Some(_) => return IdtoLong(newid, 2)
      case None => return 0;
    }
  }

  def main(args: Array[String]): Unit = {

    //Movie Nodes Intilization
    val spark = SparkSession.builder().appName("test").master("local").getOrCreate()
    ProgramContext.setContext(spark.sparkContext)

    //Load from csv file from local /data/
    val movie_df = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("/data/movies.csv")
    val rows: org.apache.spark.rdd.RDD[org.apache.spark.sql.Row] = movie_df.rdd
    val lines = rows.map(s => (ismovieId(s(0)), isStartYear(s(5)), isEndYear(s(5))))
    val clean_movie_df = lines.filter(x => (x._1 != 0) && (x._2 != "0" && x._2.length == 10))
    //format according to template given of Users
    val movieNode = clean_movie_df.map(s => (s._1, (Interval(LocalDate.parse(s._2), LocalDate.parse(s._3)), "movieNode")))

    //PersonNode Initilization
    val df2 = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("/home/madhur/data/principle.csv")
    val rows2: org.apache.spark.rdd.RDD[org.apache.spark.sql.Row] = df2.rdd
    val lines2 = rows2.map(s => (ispersonId(s(0)), isStartYear(s(2)), isEndYear(s(2))))
    val newRDD2 = lines2.filter(x => (x._1 != 0) && (x._2 != "0" && x._2.length ==10))
    val personNode = newRDD2.map(s=> (s._1,(Interval(LocalDate.parse(s._2), LocalDate.parse(s._3)),"personNode")))

    
    //Union of person nOde and Movie Node to create all node RDD
    val allNodes = movieNode.union(personNode)
    val users: RDD[(VertexId, (Interval, String))] = movieNode

    //Edges Initilization
    val dfEdges = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("/home/madhur/data/shortedges.csv")
    val rowsEdges: org.apache.spark.rdd.RDD[org.apache.spark.sql.Row] = dfEdges.rdd
    val linesEdges = rowsEdges.map(s => ((ispersonId(s(0)) * 2 + ismovieId(s(1)) * 3), ispersonId(s(0)), ismovieId(s(1)), isStartYear(s(2)), isEndYear(s(2))))
    val newRDDEdges = linesEdges.filter(x => (x._1 != 0) && (x._2 != 0) && (x._3 != 0) && (x._4 != "0" && x._4.length == 10))
    //format according to template of friendships
    val edges = newRDDEdges.map(s => TEdge[Int](s._1, s._2, s._3, Interval(LocalDate.parse(s._4), LocalDate.parse(s._5)), 1))
    val friendships: RDD[(TEdge[Int])] = edges

    //create VEGraph
    //val VEG = VEGraph.fromRDDs(users,friendships, "Default", StorageLevel.MEMORY_ONLY_SER)
    val Onegraph = OneGraph.fromRDDs(users, friendships, "Default", StorageLevel.MEMORY_ONLY_SER)

    // Graph Operations
    val connComp = Onegraph.connectedComponents()
    val triCount = Onegraph.triangleCount
    val coeff = Onegraph.clusteringCoefficient
    val vertices = Onegraph.vertices

    // Convert the RDD to DF

    
    //val newedges = vertices.map(s => s._2._2._2)
    //val newrdd = newedges.map(s =>s)
    //val newrdd = newedges.map({ case (primaryType: String) => CrimeType(primaryType.trim) })
    //val dfWithSchema = spark.createDataFrame(newedges).toDF("eed","vertexId")
    //val count = newedges.groupBy(s => s)



    //val dfWithoutSchema = spark.createDataFrame(newrdd).toDF("vertexId")
    //val tp =dfWithoutSchema.groupBy("vertexId").count()
    //println(tp)
  
    //.coalesce(1).write.csv("/data/connectedComponenetsportal.csv")

  }
}