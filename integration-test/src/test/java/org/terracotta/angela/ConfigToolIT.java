/*
 * Copyright Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terracotta.angela;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.terracotta.angela.client.ClusterFactory;
import org.terracotta.angela.client.ConfigTool;
import org.terracotta.angela.client.Tsa;
import org.terracotta.angela.client.config.ConfigurationContext;
import org.terracotta.angela.client.support.junit.AngelaOrchestratorRule;
import org.terracotta.angela.common.ToolExecutionResult;
import org.terracotta.angela.common.distribution.Distribution;
import org.terracotta.angela.common.net.PortAllocator;
import org.terracotta.angela.common.tcconfig.TerracottaServer;
import org.terracotta.angela.common.topology.Topology;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.terracotta.angela.client.config.custom.CustomConfigurationContext.customConfigurationContext;
import static org.terracotta.angela.client.support.hamcrest.AngelaMatchers.successful;
import static org.terracotta.angela.common.TerracottaConfigTool.configTool;
import static org.terracotta.angela.common.distribution.Distribution.distribution;
import static org.terracotta.angela.common.dynamic_cluster.Stripe.stripe;
import static org.terracotta.angela.common.provider.DynamicConfigManager.dynamicCluster;
import static org.terracotta.angela.common.tcconfig.TerracottaServer.server;
import static org.terracotta.angela.common.topology.LicenseType.TERRACOTTA_OS;
import static org.terracotta.angela.common.topology.PackageType.KIT;
import static org.terracotta.angela.common.topology.Version.version;

public class ConfigToolIT {

  PortAllocator.PortReservation reservation;
  int[] ports;

  @Rule
  public AngelaOrchestratorRule angelaOrchestratorRule = new AngelaOrchestratorRule();

  @Before
  public void setUp() {
    reservation = angelaOrchestratorRule.getPortAllocator().reserve(4);
    ports = reservation.stream().toArray();
  }

  @After
  public void tearDown() {
    reservation.close();
  }

  @Test
  public void testFailingConfigToolCommand() throws Exception {
    TerracottaServer server = server("server-1")
        .tsaPort(ports[0])
        .tsaGroupPort(ports[1])
        .configRepo("terracotta1/repository")
        .logs("terracotta1/logs")
        .metaData("terracotta1/metadata")
        .failoverPriority("availability");
    Distribution distribution = distribution(version("3.9-SNAPSHOT"), KIT, TERRACOTTA_OS);
    ConfigurationContext configContext = customConfigurationContext()
        .tsa(context -> context.topology(new Topology(distribution, dynamicCluster(stripe(server)))))
        .configTool(context -> context.configTool(configTool("config-tool")).distribution(distribution));

    try (ClusterFactory factory = angelaOrchestratorRule.newClusterFactory("ConfigToolTest::testFailingClusterToolCommand", configContext)) {
      Tsa tsa = factory.tsa();
      tsa.startAll();
      ConfigTool configTool = factory.configTool();

      ToolExecutionResult result = configTool.executeCommand("non-existent-command");
      assertThat(result, is(not(successful())));
    }
  }

  @Test
  public void testValidConfigToolCommand() throws Exception {
    TerracottaServer server = server("server-1")
        .tsaPort(ports[0])
        .tsaGroupPort(ports[1])
        .configRepo("terracotta1/repository")
        .logs("terracotta1/logs")
        .metaData("terracotta1/metadata")
        .failoverPriority("availability");
    Distribution distribution = distribution(version("3.9-SNAPSHOT"), KIT, TERRACOTTA_OS);
    ConfigurationContext configContext = customConfigurationContext()
        .tsa(context -> context.topology(new Topology(distribution, dynamicCluster(stripe(server)))))
        .configTool(context -> context.configTool(configTool("config-tool")).distribution(distribution));

    try (ClusterFactory factory = angelaOrchestratorRule.newClusterFactory("ConfigToolTest::testValidConfigToolCommand", configContext)) {
      Tsa tsa = factory.tsa();
      tsa.startAll();
      ConfigTool configTool = factory.configTool();

      ToolExecutionResult result = configTool.executeCommand("get", "-s", "localhost:" + ports[0], "-c", "offheap-resources");
      System.out.println("######Result: " + result);
    }
  }
}