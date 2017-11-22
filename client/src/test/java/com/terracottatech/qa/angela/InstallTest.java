package com.terracottatech.qa.angela;

import org.junit.Test;

import com.terracottatech.qa.angela.client.ClientControl;
import com.terracottatech.qa.angela.client.TsaControl;
import com.terracottatech.qa.angela.common.distribution.Distribution;
import com.terracottatech.qa.angela.common.tcconfig.License;
import com.terracottatech.qa.angela.common.tcconfig.TcConfig;
import com.terracottatech.qa.angela.common.topology.LicenseType;
import com.terracottatech.qa.angela.common.topology.PackageType;
import com.terracottatech.qa.angela.common.topology.Topology;

import java.util.concurrent.Future;

import static com.terracottatech.qa.angela.common.topology.Version.version;

/**
 * @author Aurelien Broszniowski
 */

public class InstallTest {

  @Test
  public void testRemoteInstall() throws Exception {

    TcConfig tcConfig = new TcConfig(version("10.1.0-SNAPSHOT"), "/terracotta/10/tc-config-a.xml");

    Topology topology = new Topology("1", Distribution.distribution(version("10.1.0-SNAPSHOT"), PackageType.KIT, LicenseType.TC_DB), tcConfig);
    License license = new License("/terracotta/10/TerracottaDB101_license.xml");

    try (TsaControl control = new TsaControl(topology, license)) {
      control.installAll();
      control.startAll();
      control.licenseAll();

      System.out.println("---> Wait for 3 sec");
      Thread.sleep(3000);

      try (ClientControl clientControl = control.clientControl("localhost")) {
        Future<Void> f = clientControl.submit((context) -> System.out.println("hello dudes 5"));
        f.get();
      }

      System.out.println("---> Stop");
      control.stopAll();
    }
  }
}
