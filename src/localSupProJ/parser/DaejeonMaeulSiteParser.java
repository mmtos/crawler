package localSupProJ.parser;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class DaejeonMaeulSiteParser implements SiteParser {
    static Logger log = LoggerFactory.getLogger(DaejeonMaeulSiteParser.class);

    @Override
    public ArrayList<HashMap<String, String>> parse(Document doc) throws Exception {
        log.info("parsing 코드가 없어요.");
        return null;
    }
}
