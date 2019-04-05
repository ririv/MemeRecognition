package com.riri.emojirecognition.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StaticPagePathFinder {

    private ResourcePatternResolver resourcePatternResolver;

    @Autowired
    public StaticPagePathFinder(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public static class PagePaths {
        private String urlPath;
        private String viewFilePath;


        private PagePaths(String urlPath, String viewFilePath) {
            this.urlPath = urlPath;   //请求的资源路径
            this.viewFilePath = viewFilePath;  //实际的资源路径
        }

        public String getUrlPath() {
            return this.urlPath;
        }

        public String getViewFilePath() {
            return this.viewFilePath;
        }

        @Override
        public String toString() {
            return this.getUrlPath() + "; " + this.getViewFilePath();
        }
    }

    public List<PagePaths> findPath(){

        List<PagePaths> list = new ArrayList<>();

        try {
            Resource baseResource = resourcePatternResolver.getResource("classpath:/static");
            String baseResourcePath = baseResource.getURL().getPath();

            Resource[] pageResources = resourcePatternResolver.getResources("classpath:/static/**/*.html");

            for (Resource page : pageResources) {
                System.out.println(new PagePaths(buildUrlPath(baseResourcePath, page), buildViewFilePath(baseResourcePath, page)));
                list.add(new PagePaths(buildUrlPath(baseResourcePath, page), buildViewFilePath(baseResourcePath, page)));
            }
}
        catch (IOException e){
            e.printStackTrace();
        }

        return list;
    }

    private String buildUrlPath(String baseResourcePath,Resource pageResource) throws IOException {
        return pageResource.getURL().getPath().substring(baseResourcePath.length()).replace(".html", "");
    }

    private String buildViewFilePath(String baseResourcePath, Resource pageResource) throws IOException {
        return pageResource.getURL().getPath().substring(baseResourcePath.length());
    }

}
