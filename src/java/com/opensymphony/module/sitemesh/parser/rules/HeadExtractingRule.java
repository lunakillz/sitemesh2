package com.opensymphony.module.sitemesh.parser.rules;

import com.opensymphony.module.sitemesh.html.BlockExtractingRule;
import com.opensymphony.module.sitemesh.html.util.CharArray;

public class HeadExtractingRule extends BlockExtractingRule {

    private final CharArray head;

    public HeadExtractingRule(CharArray head) {
        this.head = head;
    }

    protected CharArray createBuffer() {
        return head;
    }

}