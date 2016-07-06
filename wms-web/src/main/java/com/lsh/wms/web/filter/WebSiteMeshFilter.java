package com.lsh.wms.web.filter;

import com.lsh.wms.web.tag.MySitemeshTagRule;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class WebSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.addTagRuleBundles(new MySitemeshTagRule());
    }

}
