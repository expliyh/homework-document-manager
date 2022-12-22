package top.expli;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class knives {
    public static String random(){
        int maxi = knives.mihomo.length;
        int si = ((int) (Math.random()*1000))%maxi;
        return mihomo[si];
    }
    public static @NotNull Boolean is_knive(String str){
        if(!knives.sorted){
            Arrays.sort(knives.mihomo);
            knives.sorted = true;
        }
        return Arrays.binarySearch(mihomo, str) != -1;
    }
    private static boolean sorted = false;
    private static final String[] mihomo = {
            "我果然，没在被神明注视着啊……",
            "坎瑞亚没有亡国，对吗？\n直到今天，你们仍是我的荣耀。",
            "就像是我记得稻妻的一切一样\n只要你记得我，我就能永远的活着。"
    };
}
