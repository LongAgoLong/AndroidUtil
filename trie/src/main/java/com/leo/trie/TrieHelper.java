package com.leo.trie;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public class TrieHelper {
    private static volatile TrieHelper mInstance;
    private final TrieNode rootNode = new TrieNode();

    private TrieHelper() {
    }

    public static TrieHelper getInstance() {
        if (mInstance == null) {
            synchronized (TrieHelper.class) {
                if (mInstance == null) {
                    mInstance = new TrieHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 对每一个敏感词进行遍历，将该关键字的每个字添加到字典树的每个结点上
     *
     * @param lineTxt     被替换的文本【不带空格】
     * @param replaceText 更正文本
     */
    public void addWord(@NonNull String lineTxt, @NonNull String replaceText) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (null == subNode) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;
            // 如果字符遍历到头则将该节点标记为尾部节点
            if (i == lineTxt.length() - 1) {
                tempNode.setKeyWordEnd(replaceText);
            }
        }
    }

    public String filter(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        StringBuilder sb = new StringBuilder(); // 存放过滤后文本的StringBuilder
        int begin = 0; // 单词遍历起点指针
        int position = 0; // 文本字符遍历指针
        TrieNode tempNode = rootNode; // 对应字典树上的遍历指针
        int length = text.length();
        // 对文本的每个字符进行遍历
        while (position < length) {
            Character c = text.charAt(position);

            // 如果当前字符是干扰符号，则直接跳过；
            // 如果字典树遍历还未开始则将头指针begin++，并且将当前字符添加到暂存区sb中
            if (isSymbol(c)) {
                position++;
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                continue;
            }

            TrieNode subNode;
            if (null != (subNode = tempNode.getSubNode(c))) {
                // 如果当前Node下面包含有c字符对应的Node，则将position指针移动一位
                tempNode = subNode;
                position++;
                // 如果包含c字符的subNode已经是结尾Node，则表示begin到position的字符串是敏感词，需要替换为"***"
                // 同时将字典树的指针tempNode回归到根结点
                if (subNode.isKeyWordEnd()) {
                    sb.append(subNode.getReplaceText());
                    tempNode = rootNode;
                    begin = position;
                }
                if (position == length) {
                    for (int i = begin; i < position; i++) {
                        sb.append(text.charAt(i));
                    }
                    break;
                }
            } else {
                // 如果当前Node下不包含c字符对应的Node，则position指针移动一位，并将begin与position同步
                // 同时将字典树的指针tempNode回归到根结点
                // 将begin到position之间的字符串添加到暂存区sb
                position++;
                for (int i = begin; i < position; i++) {
                    sb.append(text.charAt(i));
                }
                begin = position;
                tempNode = rootNode;
            }
        }
        return sb.toString();
    }

    /**
     * 是否干扰符号
     *
     * @param c
     * @return
     */
    private boolean isSymbol(Character c) {
        return Character.isWhitespace(c);
    }
}
