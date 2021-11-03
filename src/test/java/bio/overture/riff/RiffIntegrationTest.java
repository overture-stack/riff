package bio.overture.riff;

import bio.overture.riff.model.ShortenRequest;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Slf4j
public class RiffIntegrationTest extends AbstractTest {
    private static boolean setupDone = false;

    private static String accessToken = "";
    private static String userId = UUID.randomUUID().toString();
    private static String riffId = "";
    private static String riffIdForDeleteTest = "";

    @PostConstruct
    void setup(){
        if (setupDone) {
            return;
        }
        accessToken = AuthUtils.createRsaToken(userId, keycloak.getFirstMappedPort());
        val req = new ShortenRequest();
        req.setAlias("Alias");
        req.setContent(ImmutableMap.of("longUrl", "value"));
        req.setSharedPublicly(false);

        val req2 = new ShortenRequest();
        req2.setAlias("Alias 2");
        req2.setContent(ImmutableMap.of("longUrl", "value 2"));
        req2.setSharedPublicly(false);

        val resp = service.makeRiff(userId, req);
        val resp2 = service.makeRiff(userId, req2);
        riffId = resp.getId();
        riffIdForDeleteTest = resp2.getId();

        setupDone = true;
    }

    @Test
    @DisplayName("GET api documentation does not require a token")
    void getApiDoc() throws Exception {
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/v2/api-docs")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);

        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("OPTION on user riff does not require a token")
    void userRiffPreflight() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.options("/riff/user/" + userId)
        ).andReturn();

        String[] allowedMethods = ((String)result.getResponse().getHeaderValue("Allow")).split(",");
        this.assertArraysEqualIgnoreOrder(new String[]{"GET","HEAD","OPTIONS"}, allowedMethods);

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    @DisplayName("GET user riff returns 401 if no token")
    void userRiffWithoutToken() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/riff/user/" + userId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(401, status);
    }

    @Test
    @DisplayName("GET user riff returns 200 if valid token")
    void userRiffWithToken() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/riff/user/" + userId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    @DisplayName("OPTION on riff does not require a token")
    void riffPreflight() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.options("/riff/" + riffId)
        ).andReturn();

        String[] allowedMethods = ((String)result.getResponse().getHeaderValue("Allow")).split(",");
        this.assertArraysEqualIgnoreOrder(new String[]{"GET","HEAD","DELETE","PUT","OPTIONS"}, allowedMethods);

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    @DisplayName("GET riff does not require a token")
    void getRiffWithoutToken() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/riff/" + riffId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    @DisplayName("GET riff return 404 if riff does not exist")
    void getRiffNotExisting() throws Exception{
        String unknownRiffId = riffId + 50;
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/riff/" + unknownRiffId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(404, status);
    }

    @Test
    @DisplayName("OPTION on shorten does not require a token")
    void riffShortenPreflight() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.options("/riff/shorten")
        ).andReturn();

        String[] allowedMethods = ((String)result.getResponse().getHeaderValue("Allow")).split(",");
        this.assertArraysEqualIgnoreOrder(new String[]{"GET","HEAD","DELETE","PUT","POST","OPTIONS"}, allowedMethods);

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    @DisplayName("POST riff returns 401 if no token")
    void postRiffWithoutToken() throws Exception {
        JSONObject content = new JSONObject();
        content.put("something", "some value");

        JSONObject body = new JSONObject();
        body.put("alias", "newRiff");
        body.put("content", content);
        body.put("sharedPublicly", true);

        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.post("/riff/shorten")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toJSONString())
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(401, status);
    }

    @Test
    @DisplayName("POST riff returns 400 if no body")
    void postRiffWithoutBody() throws Exception {
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.post("/riff/shorten")
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(400, status);
    }

    @Test
    @DisplayName("POST riff returns 200 if valid token and valid body")
    void postRiffWithTokenAndBody() throws Exception {
        JSONObject content = new JSONObject();
        content.put("something", "some value");

        JSONObject body = new JSONObject();
        body.put("alias", "newRiff");
        body.put("content", content);
        body.put("sharedPublicly", true);

        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.post("/riff/shorten")
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toJSONString())
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    @DisplayName("PUT riff returns 401 if no token")
    void putRiffWithoutToken() throws Exception {
        JSONObject content = new JSONObject();
        content.put("something", "some value");

        JSONObject body = new JSONObject();
        body.put("alias", "newRiff");
        body.put("content", content);
        body.put("sharedPublicly", true);

        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.put("/riff/" + riffId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toJSONString())
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(401, status);
    }

    @Test
    @DisplayName("PUT riff returns 400 if no body")
    void putRiffWithoutBody() throws Exception {
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.put("/riff/" + riffId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(400, status);
    }

    @Test
    @DisplayName("PUT riff returns 404 if riff id does not exist")
    void putRiffNotExistingRiffId() throws Exception {
        JSONObject content = new JSONObject();
        content.put("something", "some value");

        JSONObject body = new JSONObject();
        body.put("alias", "newRiff");
        body.put("content", content);
        body.put("sharedPublicly", true);

        String unknownRiffId = riffId + 100;

        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.put("/riff/" + unknownRiffId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toJSONString())
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(404, status);
    }

    @Test
    @DisplayName("PUT riff returns 200 if riff id exists, token and body are valid")
    void putRiffExistingRiffIdWithBodyAndToken() throws Exception {
        JSONObject content = new JSONObject();
        content.put("longUrl", "value updated");

        JSONObject body = new JSONObject();
        body.put("alias", "newRiff");
        body.put("content", content);
        body.put("sharedPublicly", true);

        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.put("/riff/" + riffId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toJSONString())
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    @DisplayName("DELETE riff returns 401 if no token")
    void deleteRiffWithoutToken() throws Exception {
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.delete("/riff/" + riffIdForDeleteTest)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(401, status);
    }

    @Test
    @DisplayName("DELETE riff returns 200 with body = false if riff id does not exist")
    void deleteRiffNotExistingRiffId() throws Exception {
        String unknownRiffId = riffIdForDeleteTest + 200;

        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.delete("/riff/" + unknownRiffId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);

        String response = result.getResponse().getContentAsString();
        Assertions.assertEquals("false", response);
    }

    @Test
    @DisplayName("DELETE riff returns 200 with body = true if riff id exists")
    void deleteRiffExistingRiffId() throws Exception {
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.delete("/riff/" + riffIdForDeleteTest)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);

        String response = result.getResponse().getContentAsString();
        Assertions.assertEquals("true", response);
    }

    @Test
    @DisplayName("OPTION on redirect to riff does not require a token")
    void riffRedirectionPreflight() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.options("/s/" + riffId)
        ).andReturn();

        String[] allowedMethods = ((String)result.getResponse().getHeaderValue("Allow")).split(",");
        this.assertArraysEqualIgnoreOrder(new String[]{"GET","HEAD","OPTIONS"}, allowedMethods);

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    @DisplayName("GET redirect to riff does not require a token and returns 302")
    void getRiffRedirectionWithoutToken() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/s/" + riffId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(302, status);
    }

    @Test
    @DisplayName("GET redirect to riff return 404 if riff does not exist")
    void getRiffRedirectionNotExisting() throws Exception{
        String unknownRiffId = riffId + 50;
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/s/" + unknownRiffId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(404, status);
    }

    @Test
    @DisplayName("Routes require token by default")
    void requestNotExistingRouteWithoutToken() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/notExistingRoute")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(401, status);
    }

    @Test
    @DisplayName("Request not existing route with token returns 404")
    void requestNotExistingRouteWithToken() throws Exception{
        MvcResult result = super.mvc.perform(
                MockMvcRequestBuilders.get("/notExistingRoute")
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = result.getResponse().getStatus();
        Assertions.assertEquals(404, status);
    }

}
