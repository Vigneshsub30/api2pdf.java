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

class Get_Wkhtmltopdf_UrlMCPTool {
    
    public static Function<MCPServer.MCPRequest, MCPServer.MCPToolResult> getGet_Wkhtmltopdf_UrlHandler(MCPServer.APIConfig config) {
        return (request) -> {
            try {
                Map<String, Object> args = request.getArguments();
                if (args == null) {
                    return new MCPServer.MCPToolResult("Invalid arguments object", true);
                }
                
                List<String> queryParams = new ArrayList<>();
        if (args.containsKey("url")) {
            queryParams.add("url=" + args.get("url"));
        }
        if (args.containsKey("output")) {
            queryParams.add("output=" + args.get("output"));
        }
                
                String queryString = queryParams.isEmpty() ? "" : "?" + String.join("&", queryParams);
                String url = config.getBaseUrl() + "/api/v2/get_wkhtmltopdf_url" + queryString;
                
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
    
    public static MCPServer.Tool createGet_Wkhtmltopdf_UrlTool(MCPServer.APIConfig config) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> urlProperty = new HashMap<>();
        urlProperty.put("type", "string");
        urlProperty.put("required", true);
        urlProperty.put("description", "Url of the page to convert to PDF. Must start with http:// or https://.");
        properties.put("url", urlProperty);
        Map<String, Object> outputProperty = new HashMap<>();
        outputProperty.put("type", "string");
        outputProperty.put("required", false);
        outputProperty.put("description", "Specify output=json to receive a JSON output. Defaults to PDF file.");
        properties.put("output", outputProperty);
        parameters.put("properties", properties);
        
        MCPServer.ToolDefinition definition = new MCPServer.ToolDefinition(
            "get_wkhtmltopdf_url",
            "Convert URL to PDF",
            parameters
        );
        
        return new MCPServer.Tool(definition, getGet_Wkhtmltopdf_UrlHandler(config));
    }
    
}