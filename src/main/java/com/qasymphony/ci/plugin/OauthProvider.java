package com.qasymphony.ci.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.qasymphony.ci.plugin.utils.ClientRequestException;
import com.qasymphony.ci.plugin.utils.HttpClientUtils;
import com.qasymphony.ci.plugin.utils.JsonUtils;
import com.qasymphony.ci.plugin.utils.ResponseEntity;
import org.apache.commons.httpclient.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author trongle
 * @version 10/23/2015 5:22 PM trongle $
 * @since 1.0
 */
public class OauthProvider {
  private static final Logger LOG = Logger.getLogger(OauthProvider.class.getName());
  private static final String HEADER_AUTH = "Authorization";

  private OauthProvider() {

  }

  /**
   * Get access token from apiKey
   *
   * @param url
   * @param apiKey
   * @return
   */
  public static String getAccessToken(String url, String apiKey) {
    StringBuilder sb = new StringBuilder()
      .append(url)
      .append("/oauth/token?grant_type=refresh_token")
      .append("&refresh_token=").append(apiKey);
    Map<String, String> headers = new HashMap<>();
    headers.put(HEADER_AUTH, "Basic amVua2luczo=");
    try {
      ResponseEntity entity = HttpClientUtils.post(sb.toString(), headers, null);
      if (HttpStatus.SC_OK != entity.getStatusCode()) {
        LOG.log(Level.WARNING, String.format("Cannot get access token from:%s, %s", url, entity.toString()));
        return null;
      }
      JsonNode node = JsonUtils.readTree(entity.getBody());
      if (null == node) {
        LOG.log(Level.WARNING, "Cannor extract access token from:" + entity.getBody());
        return null;
      }
      return JsonUtils.getText(node, "access_token");
    } catch (ClientRequestException e) {
      return null;
    }
  }

  /**
   * Build header with get access token from refresh token
   *
   * @param url
   * @param apiKey
   * @param headers
   * @return
   */
  public static Map<String, String> buildHeaders(String url, String apiKey, Map<String, String> headers) {
    String accessToken = getAccessToken(url, apiKey);
    return buildHeaders(accessToken, headers);
  }

  /**
   * Build headers with access token
   *
   * @param accessToken
   * @param headers
   * @return
   */
  public static Map<String, String> buildHeaders(String accessToken, Map<String, String> headers) {
    Map<String, String> map = new HashMap<>();
    //appSecretKey is refresh token, so we packed with Bearer in header when build headers to qTest
    map.put(HEADER_AUTH, "Bearer " + accessToken);
    map.put("Content-Type", "application/json");
    if (null != headers && headers.size() > 0) {
      map.putAll(headers);
    }
    return map;
  }
}
