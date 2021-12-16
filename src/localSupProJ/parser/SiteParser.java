package localSupProJ.parser;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;

public interface SiteParser {
    ArrayList<HashMap<String, String>> parse(Document doc) throws Exception;
}
