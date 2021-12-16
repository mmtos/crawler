package localSupProJ.crawlingTarget;

import localSupProJ.parser.DaejeonMaeulSiteParser;
import localSupProJ.parser.SiteParser;
import localSupProJ.parser.SeoulMaeulSiteParser;

public enum TargetSite {
    DAEJEON_MAEUL("https://www.daejeonmaeul.kr/app/contest/index?md_id=business_contest", new DaejeonMaeulSiteParser(),"get"),
    SEOUL_MAEUL_1("http://www.seoulmaeul.org/programs/user/support/list.asp?tabgbn=4",new SeoulMaeulSiteParser(), "get");


    private final String url;
    private final SiteParser siteParser;
    private final String httpMethod;

    TargetSite(String url, SiteParser siteParser, String httpMethod) {
        this.url = url;
        this.siteParser = siteParser;
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public SiteParser getSiteParser() {
        return siteParser;
    }

    public String getHttpMethod() {
        return httpMethod;
    }
}
