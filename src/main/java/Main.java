import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        PageRankAlgorithm pga = new PageRankAlgorithm("https://datalaboratory.one");
        pga.crawl();

        PageRankMatrix pgm = new PageRankMatrix(pga.paths, pga.linkList.size());
        pgm.doAlgorithm(3);

        BigDecimal[] authority = pgm.getaMatrix();
        BigDecimal[] hubbiness = pgm.gethMatrix();

        for (int j = 0; j < pga.linkList.size(); j++) {
            pga.linkList.get(j).setAuthority(authority[j]);
            pga.linkList.get(j).setHubbiness(hubbiness[j]);
        }

        List<LinkModel> topAuthorities = pga.linkList.stream()
                .sorted(Comparator.comparing(LinkModel::getAuthority)).collect(Collectors.toList());

        List<LinkModel> topHubs = pga.linkList.stream()
                .sorted(Comparator.comparing(LinkModel::getHubbiness)).collect(Collectors.toList());

        System.out.println("Top authorities");
        for (LinkModel linkModel : topAuthorities) {
            System.out.println(linkModel.getAuthority() + " - " + linkModel.getUrl());
        }

        System.out.println("Top hubs");
        for (LinkModel linkModel : topHubs) {
            System.out.println(linkModel.getHubbiness() + " - " + linkModel.getUrl());
        }
    }
}
