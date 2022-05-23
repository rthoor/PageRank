import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class PageRankAlgorithm {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.119 YaBrowser/22.3.0.2430 Yowser/2.5 Safari/537.36";
    private static final String REFERRER = "https://www.google.com";
    private static final String urlPattern = "(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])";
    private static final List<String> banned = Arrays.asList("wp-login", "download");

    public ArrayList<LinkModel> linkList = new ArrayList<>();
    public ArrayList<Pair<Integer, Integer>> paths = new ArrayList<>();

    private String host;

    public PageRankAlgorithm(String host) {
        this.host = host;
    }

    public void crawl() {
        LinkModel main = new LinkModel(host);
        linkList.add(main);
        getLinks(host);
    }

    public void getLinks(String url){
        if (!isUrlContainsBannedWord(url)) {
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent(USER_AGENT)
                        .referrer(REFERRER)
                        .header("Content-Type", "text/html; charset=UTF-8")
                        .timeout(10000)
                        .get();
                Elements links = doc.select("a");
                LinkModel main = new LinkModel(url);
                for (Element link : links) {
                    String href = link.attr("href");
                    if (href.matches(urlPattern)) {
                        LinkModel linkModel = new LinkModel(href);
                        if (!linkList.contains(linkModel) && href.contains(host) && !href.contains("download")) {
                            linkList.add(linkModel);
                            getLinks(href);
                            int index = linkList.indexOf(linkModel);
                            if(index != -1) {
                                paths.add(new Pair<>(linkList.indexOf(main), index));
                            }
                        } else if (linkList.contains(linkModel)) {
                            paths.add(new Pair<>(linkList.indexOf(main), linkList.indexOf(linkModel)));
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUrlContainsBannedWord(String url) {
        for (String ban : banned) {
            if (url.contains(ban)) return true;
        }
        return false;
    }

}
