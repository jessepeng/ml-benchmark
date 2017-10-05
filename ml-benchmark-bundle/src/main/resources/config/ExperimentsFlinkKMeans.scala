/**
  * Copyright (C) 2017 TU Berlin DIMA
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *         http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package config


import com.samskivert.mustache.Mustache
import com.typesafe.config.ConfigFactory
import org.peelframework.core.beans.data.{CopiedDataSet, DataSet, ExperimentOutput}
import org.peelframework.core.beans.experiment.ExperimentSequence.{Parameter, SimpleParameters}
import org.peelframework.core.beans.experiment.{ExperimentSequence, ExperimentSuite}
import org.peelframework.core.beans.system.Lifespan
import org.peelframework.dstat.beans.system.Dstat
import org.peelframework.flink.beans.experiment.FlinkExperiment
import org.peelframework.flink.beans.system.Flink
import org.peelframework.hadoop.beans.system.HDFS2
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import org.springframework.context.annotation.{Bean, Configuration}


@Configuration
class ExperimentsFlinkKMeans extends ApplicationContextAware {


  var ctx: ApplicationContext = null
  def setApplicationContext(ctx: ApplicationContext): Unit = {
    this.ctx = ctx
  }

  @Bean(name = Array("flink.kmeans.suite"))
  def `flink.kmeans.suite`: ExperimentSuite = new ExperimentSuite(
    for {
      topXXX /*     */ <- Seq("all")
      dimensions /* */ <- Seq(100)
      k /*          */ <- Seq(10)
      size             <- Seq("100g", "200g", "500g", "1t")

    } yield new FlinkExperiment(
      name = s"kmeans.flink.$topXXX.$k.$dimensions.$size",
      command =
        s"""
           |--class de.tuberlin.dima.mlbench.flink.kmeans.RUN                  \\
           |$${app.path.apps}/ml-benchmark-flink-jobs-1.0-SNAPSHOT.jar           \\
           |--inCentersPath=$${app.path.work}/benchmarks/$size/centers.dat  \\
           |--inInitPath=$${app.path.work}/benchmarks/$size/init.dat  \\
           |--inDataPath=$${app.path.work}/benchmarks/$size/data.dat  \\
           |--outputPath=$${app.path.work}/benchmarks/$size/output        \\
           |--iterations=$${scale.iterations}                                   \\
           |--degOfParall=$${system.default.config.parallelism.total}           \\
           |--numDimensions=$dimensions                                            \\
           |--method=DEFAULT                                            \\
           |--k=$k
          """.stripMargin.trim,
      config = ConfigFactory.parseString(
        s"""
           |system.default.config.slaves            = $${env.slaves.$topXXX.hosts}
           |system.default.config.parallelism.total = $${env.slaves.$topXXX.total.parallelism}
           |scale.iterations                        = 5
          """.stripMargin.trim),
      runs = 1,
      runner = ctx.getBean("flink-1.3.2", classOf[Flink]),
      systems = Set(ctx.getBean("dstat-0.7.2", classOf[Dstat])),
      inputs = Set(),
      outputs = Set()
    )
  )

  @Bean(name = Array("flink.kmeans.small"))
  def `flink.kmeans.small`: ExperimentSuite = new ExperimentSuite(
    for {
      topXXX /*     */ <- Seq("all")
      dimensions /* */ <- Seq(100)
      k /*          */ <- Seq(10)
      size             <- Seq("1M")

    } yield new FlinkExperiment(
      name = s"kmeans.flink.$topXXX.$k.$dimensions.$size",
      command =
        s"""
           |--class de.tuberlin.dima.mlbench.flink.kmeans.RUN                  \\
           |$${app.path.apps}/ml-benchmark-flink-jobs-1.0-SNAPSHOT.jar           \\
           |--inCentersPath=$${app.path.work}/benchmarks/$size/centers.dat  \\
           |--inInitPath=$${app.path.work}/benchmarks/$size/init.dat  \\
           |--inDataPath=$${app.path.work}/benchmarks/$size/data.dat  \\
           |--outputPath=$${app.path.work}/benchmarks/$size/output        \\
           |--iterations=$${scale.iterations}                                   \\
           |--degOfParall=$${system.default.config.parallelism.total}           \\
           |--numDimensions=$dimensions                                            \\
           |--method=DEFAULT                                            \\
           |--k=$k
          """.stripMargin.trim,
      config = ConfigFactory.parseString(
        s"""
           |system.default.config.slaves            = $${env.slaves.$topXXX.hosts}
           |system.default.config.parallelism.total = $${env.slaves.$topXXX.total.parallelism}
           |scale.iterations                        = 5
          """.stripMargin.trim),
      runs = 1,
      runner = ctx.getBean("flink-1.3.2", classOf[Flink]),
      systems = Set(ctx.getBean("dstat-0.7.2", classOf[Dstat])),
      inputs = Set(),
      outputs = Set()
    )
  )

}
