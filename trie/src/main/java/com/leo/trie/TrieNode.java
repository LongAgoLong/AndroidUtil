package com.leo.trie;

import androidx.collection.ArrayMap;

import java.util.Map;

/**
 * 字典树节点，其中包含一个Map，该Map保存了所有子节点，key为节点对应的字符
 */
class TrieNode {
    private boolean end = false;
    private String replaceText;
    private final Map<Character, TrieNode> subNodes = new ArrayMap<>();

    public void addSubNode(Character key, TrieNode node) {
        subNodes.put(key, node);
    }

    public TrieNode getSubNode(Character key) {
        return subNodes.get(key);
    }

    public boolean isKeyWordEnd() {
        return end;
    }

    public String getReplaceText() {
        return replaceText;
    }

    public void setKeyWordEnd(String replaceText) {
        this.end = true;
        this.replaceText = replaceText;
    }
}