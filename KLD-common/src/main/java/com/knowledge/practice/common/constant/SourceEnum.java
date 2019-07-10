package com.knowledge.practice.common.constant;

import com.knowledge.practice.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 客服端来源
 */
public enum SourceEnum {
    /**
     * APP类型 包括ios和android
     */
    APP("ios", "android"),
    /**
     * IOS类型仅包含ios
     */
    IOS("ios"),
    /**
     * ANDROID类型仅包含android
     */
    ANDROID("android"),
    /**
     * WEB类型表示来自网页的请求
     */
    WEB("web");

    private List<String> nameList = new ArrayList<String>();

    private SourceEnum(String... names) {
        this.setNames(names);
    }

    public List<String> getName() {
        return nameList;
    }

    private void setNames(String... names) {
        if (names == null) {
            return;
        }
        this.nameList.clear();
        for (String name : names) {
            this.nameList.add(name);
        }
    }

    public boolean contains(String name) {
        if (StringUtils.isNull(name)) {
            return false;
        }
        return nameList.contains(name);
    }
}
