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
import org.peelframework.core.beans.system.Lifespan
import org.peelframework.dstat.beans.system.Dstat
import org.peelframework.hadoop.beans.system.HDFS2
import org.peelframework.spark.beans.system.Spark
import org.peelframework.flink.beans.system.Flink
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.context.{ApplicationContext, ApplicationContextAware}

@Configuration
class systems extends ApplicationContextAware {

  /* the enclosing application context */
  var ctx: ApplicationContext = null

  def setApplicationContext(ctx: ApplicationContext): Unit = {
    this.ctx = ctx
  }

  // ---------------------------------------------------
  // Systems
  // ---------------------------------------------------


  @Bean(name = Array("flink-1.3.2"))
  def `flink-1.3.1`: Flink = new Flink(
    version = "1.3.2",
    configKey = "flink",
    lifespan = Lifespan.EXPERIMENT,
    mc = ctx.getBean(classOf[Mustache.Compiler])
  )
}
