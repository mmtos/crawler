package localSupProJ.crawlingTarget;

import localSupProJ.parser.*;

import java.util.Arrays;
import java.util.List;

public enum OpenAPI {
    WEATHER(Arrays.asList("http://apis.data.go.kr/1360000/VilageFcstMsgService/getLandFcst"), new WeatherParser(),"b2dY8BdVpqoPeZJvuAM5qMjubwEA7Z9Lvo3x0TzZi0cZB9eolfVSuRJWfs71pzuvARH8rtdqMut%2FJIJKef%2F6ZA%3D%3D"),
    DUST_AIR(Arrays.asList("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"),new DustAirParser(),"b2dY8BdVpqoPeZJvuAM5qMjubwEA7Z9Lvo3x0TzZi0cZB9eolfVSuRJWfs71pzuvARH8rtdqMut%2FJIJKef%2F6ZA%3D%3D");


    private final List<String> urls;
    private final OpenAPIParser openAPIParser;
    private final String serviceKey;


    OpenAPI(List<String> urls, OpenAPIParser openAPIParser, String serviceKey) {
        this.urls = urls;
        this.openAPIParser = openAPIParser;
        this.serviceKey = serviceKey;
    }

    public List<String> getUrls() {
        return urls;
    }

    public OpenAPIParser getOpenAPIParser() {
        return openAPIParser;
    }

    public String getServiceKey() {
        return serviceKey;
    }
}
