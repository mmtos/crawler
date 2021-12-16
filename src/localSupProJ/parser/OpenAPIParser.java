package localSupProJ.parser;

import localSupProJ.crawlingTarget.OpenAPI;

import java.util.ArrayList;
import java.util.HashMap;

public interface OpenAPIParser {
    ArrayList<HashMap<String, String>> parse(OpenAPI api) throws Exception;
}
