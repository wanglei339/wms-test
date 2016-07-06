package com.lsh.wms.web.tag;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.html.ExportTagToContentRule;
import org.sitemesh.tagprocessor.State;

public class MySitemeshTagRule implements TagRuleBundle {

    public void install(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
        defaultState.addRule("mymeta", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("mymeta"), false));
        defaultState.addRule("mylink", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("mylink"), false));
        defaultState.addRule("myscript", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("myscript"), false));
    }

    public void cleanUp(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {

    }

}
