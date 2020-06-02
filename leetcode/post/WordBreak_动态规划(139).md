## 题目

给定一个非空字符串 s 和一个包含非空单词列表的字典 wordDict，判定 s 是否可以被空格拆分为一个或多个在字典中出现的单词。 

 说明： 


 拆分时可以重复使用字典中的单词。 
 你可以假设字典中没有重复的单词。 


 示例 1： 

 输入: s = "leetcode", wordDict = ["leet", "code"]
输出: true
解释: 返回 true 因为 "leetcode" 可以被拆分成 "leet code"。


 示例 2： 

 输入: s = "applepenapple", wordDict = ["apple", "pen"]
输出: true
解释: 返回 true 因为 "applepenapple" 可以被拆分成 "apple pen apple"。
     注意你可以重复使用字典中的单词。


 示例 3： 

 输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
输出: false

## 解题思路

动态规划

状态转移方程  `dp[j] && dict.contains(s.substring(j, i))`

```java
class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) {
            return true;
        }

        Set<String> dict = new HashSet<>(wordDict);

        int length = s.length();

        // 存储从第1到i位的字符串是否满足条件 默认为false
        boolean[] dp = new boolean[length+1];

        // 空字符串 默认为true
        dp[0] = true;
        for (int i = 0; i <= length; i++) {
            for (int j = 0; j < i; j++) {
                // 前j-1个元素in dict且第j~i-1个元素构成的单词in dict，则判断dp[i] = true
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[length];

    }
}
```