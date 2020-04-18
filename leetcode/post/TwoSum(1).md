## 题目

给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。

你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。

 

示例:

```
给定 nums = [2, 7, 11, 15], target = 9

因为 nums[0] + nums[1] = 2 + 7 = 9
所以返回 [0, 1]
```

## 解题思路

第一种暴力解法

```java
public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for (int i = 0; i<nums.length-1;i++) {
            for (int j = i+1; j<nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        return result;
    }
```

第二种 hash一轮循环解法

```java
public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        Map<Integer, Integer> map = new HashMap(nums.length);
        for (int i = 0; i<nums.length;i++) {
            int anV = target - nums[i];
            if (map.containsKey(anV)) {
                result[0] = i;
                result[1] = map.get(anV);
                break;
            }
            map.put(nums[i], i);
        }
        return result;
    }
```
