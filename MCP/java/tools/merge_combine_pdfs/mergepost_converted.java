/**
 * MCP Server function for Merge multiple PDFs together
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

class Post_MergeMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getPost_MergeHandler(MCPServer.APIConfig config) {
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
        if (args.containsKey("inlinePdf")) {
            queryParams.add("inlinePdf=" + args.get("inlinePdf"));
        }
        if (args.containsKey("urls")) {
            queryParams.add("urls=" + args.get("urls"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/post_merge" + queryString;
                
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
    
    public static MCPServer.Tool createPost_MergeTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> fileNameProperty = new HashMap<>();
        fileNameProperty.put("type", "string");
        fileNameProperty.put("required", false);
        fileNameProperty.put("description", "");
        properties.put("fileName", fileNameProperty);
        Map<String, Object> inlinePdfProperty = new HashMap<>();
        inlinePdfProperty.put("type", "string");
        inlinePdfProperty.put("required", false);
        inlinePdfProperty.put("description", "");
        properties.put("inlinePdf", inlinePdfProperty);
        Map<String, Object> urlsProperty = new HashMap<>();
        urlsProperty.put("type", "string");
        urlsProperty.put("required", true);
        urlsProperty.put("description", "");
        properties.put("urls", urlsProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "post_merge",
            "Merge multiple PDFs together",
            parameters
        );
        
        return new MCPServer.Tool(definition, getPost_MergeHandler(config));
    }
    
}