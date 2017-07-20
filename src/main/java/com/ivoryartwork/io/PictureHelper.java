package com.ivoryartwork.io;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/7/20
 */
public class PictureHelper {

    public static void main(String[] args) {
        int[] source = new int[110];
        //第一页
        source[0] = 151;
        source[1] = 106;
        source[2] = 159;
        source[3] = 133;
        source[4] = 101;
        source[5] = 115;
        source[6] = 177;
        source[7] = 130;
        source[8] = 66;
        source[9] = 85;
        source[10] = 98;
        source[11] = 164;
        //第二页 第一行
        source[12] = 84;
        source[13] = 104;
        source[14] = 209;
        source[15] = 122;
        source[16] = 144;
        source[17] = 124;
        source[18] = 77;
        source[19] = 35;
        source[20] = 10;
        source[21] = 8;
        source[22] = 149;
        //第二页 第二行
        source[23] = 165;
        source[24] = 138;
        source[25] = 167;
        source[26] = 73;
        source[27] = 70;
        source[28] = 195;
        source[29] = 129;
        source[30] = 172;
        source[31] = 49;
        //第二页 第三行
        source[32] = 74;
        source[33] = 128;
        source[34] = 108;
        source[35] = 33;
        source[36] = 38;
        source[37] = 25;
        source[38] = 173;
        source[39] = 187;
        source[40] = 176;
        //第二页 第四行
        source[41] = 181;
        source[42] = 183;
        source[43] = 107;
        source[44] = 153;
        source[45] = 4;
        source[46] = 118;
        source[47] = 197;
        source[48] = 87;
        source[49] = 141;
        //第二页 第五行
        source[50] = 182;
        source[51] = 208;
        source[52] = 32;
        source[53] = 105;
        source[54] = 126;
        source[55] = 24;
        source[56] = 185;
        source[57] = 131;
        source[58] = 168;
        //第二页 第六行
        source[59] = 81;
        source[60] = 136;
        source[61] = 47;
        source[62] = 3;
        source[63] = 89;
        source[64] = 134;
        source[65] = 154;
        source[66] = 163;
        source[67] = 142;
        //第二页 第七行
        source[68] = 37;
        source[69] = 83;
        source[70] = 90;
        source[71] = 121;
        source[72] = 11;
        source[73] = 180;
        source[74] = 120;
        source[75] = 18;
        source[76] = 82;
        //第三页 第一行
        source[77] = 127;
        source[78] = 76;
        source[79] = 150;
        source[80] = 52;
        source[81] = 113;
        source[82] = 54;
        source[83] = 158;
        source[84] = 155;
        source[85] = 109;
        //第三页 第二行
        source[86] = 91;
        source[87] = 44;
        source[88] = 140;
        source[89] = 157;
        source[90] = 57;
        source[91] = 178;
        source[92] = 102;
        source[93] = 96;
        source[94] = 188;
        //第三页 第三行
        source[95] = 95;
        source[96] = 147;
        source[97] = 213;
        source[98] = 1;
        source[99] = 189;
        source[100] = 71;
        source[101] = 166;
        source[102] = 211;
        source[103] = 143;
        //第三页 第四行
        source[104] = 135;
        source[105] = 103;
        source[106] = 116;
        source[107] = 160;
        source[108] = 97;
        source[109] = 100;
        String path = "E:\\Backup\\personal\\pictures\\00000000";
        File file = new File(path);
        File[] pics = file.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".jpg")) {
                    return true;
                }
                return false;
            }
        });
        int[] nums = new int[pics.length];
        for (int i = 0; i < pics.length; i++) {
            String name = pics[i].getName();
            String regix = "\\(([0-9]+)\\)";
            Pattern pattern = Pattern.compile(regix);
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                nums[i] = Integer.valueOf(matcher.group(1));
            }
        }
        Arrays.sort(nums);
        Arrays.sort(source);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != source[i]) {
                System.out.println("有照片不一致的");
            }
        }
    }
}
