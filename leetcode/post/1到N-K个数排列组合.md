## 题目 
给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。 

 示例: 

 输入: n = 4, k = 2
输出:
```
[
  [2,4],
  [3,4],
  [2,3],
  [1,2],
  [1,3],
  [1,4],
] 
```

## 解题思路

还是 回溯算法，本质上是深度优先搜索

虽然本题是只要求列出 全部组合，但其实如果题目要求列出全部排列 也是完全ok的

组合和排列的区别 不太了解的可以看下这里 [https://zhuanlan.zhihu.com/p/41855459](https://zhuanlan.zhihu.com/p/41855459)


组合
```java
// 组合
        public List<List<Integer>> combine(int n, int k) {
            List<List<Integer>> res = new ArrayList<>();
            if (n <= 0 || n < k) {
                return res;
            }
            helper(n, k, 1, new ArrayList<>(), res);
            return res;
        }
        
        private void helper(int n, int k, int start, ArrayList<Integer> item, List<List<Integer>> res) {
    // 个数满足k个 返回
            if (item.size() == k) {
                res.add(new ArrayList<>(item));
                return;
            }
            // 
            for (int i = start; i <= n; i++) {
                item.add(i);
                helper(n, k, i + 1, item, res);
                // 回溯完需要删除加入的元素，开始下一波循环
                item.remove(item.size() - 1);
            }
        }
````


排列
```java
public List<List<Integer>> permuta(int n, int k) {
            List<List<Integer>> res = new ArrayList<>();
            if (n <= 0 || n < k) {
                return res;
            }
            int[] ns = new int[n];
            // 转成int数组
            for (int i = 0; i<n; i++) {
                ns[i] = i+1;
            }
            helper(ns, k, 0, res);
            return res;
        }
        
        private void helper(int[] n, int k, int start, List<List<Integer>> res) {
            if (start == k) {
                List<Integer> item = new ArrayList<>();
                for (int i = 0; i<k; i++) {
                    item.add(n[i]);
                }
                res.add(item);
                return;
            }

            // 排列 本质上是通过start 与 后续节点一次交换，来获取所有可能
            // 但是交换完 完成回溯后 记得交换回来，因为完成回溯，意味着在此节点之后的所有可能排列都已经列举完了，所以需要开始新一轮交换
            for (int i = start; i<n.length; i++) {
                int temp = n[start];
                n[start] = n[i];
                n[i] = temp;
                helper2(n, k, start+1, res);
                n[i] = n[start];
                n[start] = temp;
            }

        }
``` 

