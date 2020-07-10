package com.zhboy.ycwwz.base_library.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.zhboy.ycwwz.base_library.widgets.SizeLabel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static android.text.Html.fromHtml;

/**
 * @author : HaoBoy
 * @date : 2018/8/17
 * @description : stirng相关Utils类
 **/
public class StringUtils {

    /**
     * 只允许字母、数字和汉字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {

        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判断是否位纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 正则表达式校验邮箱
     *
     * @param emaile 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    public static boolean checkEmail(String emaile) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }

    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(double v, int scale) {
        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }

    /**
     * 中文检测
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 将半角符转为全角， 解决textview换行参差不齐
     *
     * @param input
     * @return
     */
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    /*将json转成map*/
    public static HashMap<String, String> parseJSON2Map(String jsonStr) {
        return getIntGson().fromJson(jsonStr, HashMap.class);
    }

    /**
     * 解决gson默认将int转换为double
     *
     * @return
     */
    public static Gson getIntGson() {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue())
                            return new JsonPrimitive(src.longValue());
                        return new JsonPrimitive(src);
                    }
                }).create();
        return gson;
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     * 199/198/166/148/146
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
//        String regExp = "^((13[0-9])|(19[8-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(14[6-8])|(166))\\d{8}$";
        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 隐藏手机号中间四位
     *
     * @param mobile
     * @return
     */
    public static String hidPhoneMiddle(String mobile) {

        if (isPhoneLegal(mobile)) {
            String maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
            return maskNumber;
        }
        return mobile;
    }

    public static String getName() {
        Random random = new Random();
        String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
                "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
                "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
                "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
                "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季"};
        int index = random.nextInt(Surname.length - 1);
        String name = Surname[index]; //获得一个随机的姓氏
        return name + "**";
    }

    /**
     * 返回手机号码
     */
    private static String getTel() {
        String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,178,187,177,176,198,199,175,130,131,132,155,156,133,153".split(",");
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static String getFuCardPrizeName() {
        Random random = new Random();
        String[] Surname = {"食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液", "电热水壶1个", "微波炉1个",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液", "电热水壶1个", "微波炉1个",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液",
                "食用油4桶", "卫生纸2提", "3袋大米", "20次抽奖机会", "6袋洗衣液"};
        int index = random.nextInt(Surname.length - 1);
        String name = Surname[index]; //获得一个礼品
        return name;
    }

    /**
     * 返回地址
     */
    public static String getFuCardInfo() {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 30; i++) {
//            sb.append(getName()).append("集福卡").append(20 + (int) (Math.random() * 100)).append("张").append("兑换").append(getFuCardPrizeName()).append("     ");
            sb.append(getName()).append("兑换").append(getFuCardPrizeName()).append("     ");

        }
        return sb.toString();
    }


    /**
     * 返回地址
     */
    private static String getAddress() {
        Random random = new Random();
        String[] address = "河南,河北,北京,天津,江苏,山东,山西,陕西,安徽,湖北,湖南,黑龙江,吉林,辽宁,广东".split(",");
        int index = random.nextInt(address.length - 1);
        String ad = address[index]; //获得一个随机的姓氏

        return ad;
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0 || "".equalsIgnoreCase(s) || "null".equalsIgnoreCase(s);
    }

    public static boolean isNotBlankAndEmpty(String str) {
        if (str != null && !str.equals("null") && str.length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 对去assests下面的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 设置html文本信息
     *
     * @param money       传入金额 比如: 1999
     * @param productName 传入金额 比如: 电动自行车
     */
    public static SpannableString setSbsMoney(String money, String productName) {
        SpannableString spannableString = new SpannableString("价值￥" + money + productName);
        AbsoluteSizeSpan asb = new AbsoluteSizeSpan(10, true);//true表示单位为dip，若为false则表示px
        spannableString.setSpan(asb, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        AbsoluteSizeSpan asb2 = new AbsoluteSizeSpan(20, true);//true表示单位为dip，若为false则表示px
        spannableString.setSpan(asb2, 3, money.length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6440")), 2, money.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 设置html文本信息
     *
     * @param content 传入奖品描述 比如: 1999元电动自己自行车
     */
    public static SpannableString setSbsSnatchCard(String content) {
        String text = "1礼品卡价值￥" + content;
        SpannableString spannableString = new SpannableString(text);
        AbsoluteSizeSpan asb = new AbsoluteSizeSpan(20, true);//true表示单位为dip，若为false则表示px
        spannableString.setSpan(asb, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        AbsoluteSizeSpan asb2 = new AbsoluteSizeSpan(14, true);//true表示单位为dip，若为false则表示px
        spannableString.setSpan(asb2, 1, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        //设置中划线
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString.setSpan(strikethroughSpan, 4, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6440")), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 设置多样式文本
     *
     * @param textView  textView
     * @param content   具体内容
     * @param sizeLabel 设置字体大小
     */
    public static void setHtmlTextView(TextView textView, String content, SizeLabel sizeLabel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(fromHtml(content, Html.FROM_HTML_MODE_COMPACT, null, sizeLabel));
        } else {
            textView.setText(fromHtml(content, null, sizeLabel));
        }
    }

    /**
     * 设置多样式文本
     *
     * @param textView textView
     * @param content  具体内容
     */
    public static void setHtmlTextView(TextView textView, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(fromHtml(content));
        }
    }


    /**
     * 设置多样式文本
     *
     * @param content 具体内容
     */
    public static Spanned getHtmlText(String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return fromHtml(content, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(content);
        }
    }

    private static void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
                                         final URLSpan urlSpan, final Context mContext) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {

            public void onClick(View view) {
                //Do something with URL here.
                String url = urlSpan.getURL();

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
//            public void updateDrawState(TextPaint ds) {
//                //设置颜色
//                ds.setColor(Color.argb(255, 54, 92, 124));
//                //设置是否要下划线
//                ds.setUnderlineText(false);
//            }

        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    public static CharSequence getClickableHtml(Context mContext, TextView tv, String html) {
        Spanned spannedHtml;
        spannedHtml = fromHtml(html);
        tv.setText(spannedHtml);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span, mContext);
        }
        return clickableHtmlBuilder;
    }

}
