package com.terracottatech.qa.angela.common.tcconfig.holders;

import com.terracottatech.qa.angela.common.tcconfig.ServerSymbolicName;
import com.terracottatech.qa.angela.common.tcconfig.TerracottaServer;
import com.terracottatech.qa.angela.common.tcconfig.TsaStripeConfig;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Terracotta config for Terracotta 4.0.x
 * <p>
 * 8   -> 4.0.x
 *
 * @author Aurelien Broszniowski
 */
public class TcConfig8Holder extends TcConfigHolder {

  public TcConfig8Holder(final TcConfig8Holder tcConfig8Holder) {
    super(tcConfig8Holder);
  }

  public TcConfig8Holder(final InputStream tcConfigInputStream) {
    super(tcConfigInputStream);
  }

  @Override
  protected NodeList getServersList(Document tcConfigXml, XPath xPath) throws XPathExpressionException {
    return (NodeList) xPath.evaluate("//*[name()='servers']//*[name()='server']", tcConfigXml.getDocumentElement(), XPathConstants.NODESET);
  }

  @Override
  public void updateSecurityRootDirectoryLocation(String securityRootDirectory) {
    throw new UnsupportedOperationException("security-root-directory configuration is not available in TcConfig8");
  }

  @Override
  public void updateDataDirectory(final String rootId, final String newlocation) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public void updateHostname(final String serverName, final String hostname) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public void updateServerGroupPort(Map<ServerSymbolicName, Integer> proxiedPorts) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public void updateServerTsaPort(Map<ServerSymbolicName, Integer> proxiedPorts) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public List<TerracottaServer> retrieveGroupMembers(String serverName, boolean updateProxy) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public Map<ServerSymbolicName, Integer> retrieveTsaPorts(final boolean updateForProxy) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public void addOffheap(String resourceName, String size, String unit) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addDataDirectory(List<TsaStripeConfig.TsaDataDirectory> tsaDataDirectoryList) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public Map<String, String> getDataDirectories() {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public void addPersistencePlugin(String persistenceDataName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> getPluginServices() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAuditDirectoryLocation(final File kitDir, final int stripeId) {
    throw new UnsupportedOperationException("Unimplemented");
  }
}
