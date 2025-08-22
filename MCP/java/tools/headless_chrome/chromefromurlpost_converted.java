/**
 * MCP Server function for Convert URL to PDF
 */

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Function;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

class Post_Chrome_UrlMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPost_Chrome_UrlHandler(MCPServer.APIConfig config) {
        return (request) -> {
            try {
                Map<String, Object> args = request.getArguments();
                if (args == null) {
                    return new MCPServer.MCPToolResult("Invalid arguments object", true);
                }
                
                List<String> queryParams = new ArrayList<>();
        if (args.containsKey("fileName")) {
            queryParams.add("fileName=" + args.get("fileName"));
        }
        if (args.containsKey("url")) {
            queryParams.add("url=" + args.get("url"));
        }
        if (args.containsKey("inlinePdf")) {
            queryParams.add("inlinePdf=" + args.get("inlinePdf"));
        }
        if (args.containsKey("options")) {
            queryParams.add("options=" + args.get("options"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/post_chrome_url" + queryString;
                
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + config.getApiKey())
                    .header("Accept", "application/json")
                    .GET()
                    .build();
                
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() >= 400) {
                    return new MCPServer.MCPToolResult("API error: " + response.body(), true);
                }
                
                // Pretty print JSON
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.body());
                String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
                
                return new MCPServer.MCPToolResult(prettyJson);
                
            } catch (IOException | InterruptedException e) {
                return new MCPServer.MCPToolResult("Request failed: " + e.getMessage(), true);
            } catch (Exception e) {
                return new MCPServer.MCPToolResult("Unexpected error: " + e.getMessage(), true);
            }
        };
    }
    
    public static MCPServer.Tool createPost_Chrome_UrlTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> fileNameProperty = new HashMap<>();
        fileNameProperty.put("type", "string");
        fileNameProperty.put("required", false);
        fileNameProperty.put("description", "");
        properties.put("fileName", fileNameProperty);
        Map<String, Object> urlProperty = new HashMap<>();
        urlProperty.put("type", "string");
        urlProperty.put("required", true);
        urlProperty.put("description", "");
        properties.put("url", urlProperty);
        Map<String, Object> inlinePdfProperty = new HashMap<>();
        inlinePdfProperty.put("type", "string");
        inlinePdfProperty.put("required", false);
        inlinePdfProperty.put("description", "");
        properties.put("inlinePdf", inlinePdfProperty);
        Map<String, Object> optionsProperty = new HashMap<>();
        optionsProperty.put("type", "string");
        optionsProperty.put("required", false);
        optionsProperty.put("description", "");
        properties.put("options", optionsProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "post_chrome_url",
            "Convert URL to PDF",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPost_Chrome_UrlHandler(config));
    }
    
}