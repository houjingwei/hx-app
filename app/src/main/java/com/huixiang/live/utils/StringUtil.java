package com.huixiang.live.utils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by hjw on 16/5/18.
 */
public class StringUtil
{

	private StringUtil()
	{

	}

	private static final int ASCII_CODE = 255;

	/**
	 * 获取省略字符
	 *
	 * @param s 字符串
	 * @param maxLength 最大长度
	 * @return 省略字符
	 */
	public static String getOmitString(String s, int maxLength)
	{

		int length = 0;
		int count = 0;
		if (null != s)
		{
			for (count = 0; count < s.length(); count++)
			{

				int ascii = Character.codePointAt(s, count);
				if (ascii >= 0 && ascii <= ASCII_CODE)
				{
					length++;
				}
				else
				{
					length += 2;
				}
				if (length > maxLength)
				{
					return s.substring(0, count) + "...";
				}
			}
		}
		return s;

	}

	public static int getRealLength(String s)
	{

		int length = 0;
		if (null != s)
		{
			for (int count = 0; count < s.length(); count++)
			{

				int ascii = Character.codePointAt(s, count);
				if (ascii >= 0 && ascii <= ASCII_CODE)
				{
					length++;
				}
				else
				{
					length += 2;
				}
			}
		}
		return length;
	}

	public static String cutString(String str, int length)
	{

		if (isNotEmpty(str) && str.length() > length)
		{
			return str.substring(0, length) + "...";
		}
		else
		{
			return str;
		}
	}

	public static String clearHtml(String str)
	{

		if (isNotEmpty(str))
		{
			return str.replaceAll("<[.[^<]]*>", "").replaceAll("[\\n|\\r]", "").replaceAll("&nbsp;", "");
		}
		else
		{
			return str;
		}
	}

	/**
	 * 获取字符串的实际长度(一个汉字占两个字节)
	 *
	 * @param str 字符串
	 * @return 字符串的实际长度
	 */
	public static int getStrActualLen(String str)
	{

		return str.replaceAll("[^\\x00-\\xff]", "**").length();
	}

	public static boolean isNotEmpty(String str)
	{

		if (null != str && !"".equals(str.trim()))
		{
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String str)
	{

		if (null == str || "".equals(str.trim()))
		{
			return true;
		}
		return false;
	}

	//判断是否含有中文
	public static boolean isHaveChin(String str)
	{

		if (null != str)
		{
			int length = str.length();
			int realLength = str.replaceAll("[^\\x00-\\xff]", "**").length();
			if (realLength > length)
			{
				return true;
			}
			return false;
		}
		else
		{
			return false;
		}

	}

	//是否是邮箱
	public static boolean isEmail(String str)
	{

		String checkEmail = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
		if (null != str)
		{
			return str.matches(checkEmail);
		}
		else
		{
			return false;
		}
	}

	//是否是数字
	public static boolean isNumber(String str)
	{

		String checkNumber = "^[0-9]+$";
		if (null != str)
		{
			return str.matches(checkNumber);
		}
		else
		{
			return false;
		}
	}

	//检测生日是否合法
	@SuppressWarnings("deprecation")
	public static boolean checkBirthday(String str)
	{

		String checkBirthday1 = "\\d{4}-\\d{2}-\\d{2}";
		String checkBirthday2 = "\\d{4}-\\d{2}-\\d{1}";
		String checkBirthday3 = "\\d{4}-\\d{1}-\\d{1}";
		String checkBirthday4 = "\\d{4}-\\d{1}-\\d{2}";
		str = str.replaceAll("/", "-");
		if (str.matches(checkBirthday1) || str.matches(checkBirthday2) || str.matches(checkBirthday3)
				|| str.matches(checkBirthday4))
		{
			String[] dataStr = str.split("-");
			Date test = new Date();
			Date today = new Date();
			test.setYear(Integer.parseInt(dataStr[0]) - 1900);
			test.setMonth(Integer.parseInt(dataStr[1]) - 1);
			test.setDate(Integer.parseInt(dataStr[2]));
			return today.after(test);
		}
		else
		{
			return false;
		}
	}

	/**
	 * 判断字符串是否非空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNullOrEmpty(String s) {
		if (null == s || "".equals(s)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 截取描述中包含的HTML代码
	 * @param htmlStr
	 * @return
	 */
    public static String delHTMLTag(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
        Pattern p_script= Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签
        Pattern p_style= Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签
        Pattern p_html= Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签
        htmlStr=htmlStr.replaceAll("&nbsp;",""); //过滤html标签
       return htmlStr.trim(); //返回文本字符串
    }
    
    public static String join(String join, String[] strAry){
    	if(null==strAry||0==strAry.length)
    		return "";
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<strAry.length;i++){
             if(i==(strAry.length-1)){
                 sb.append(strAry[i]);
             }else{
                 sb.append(strAry[i]).append(join);
             }
        }
       
        return new String(sb);
    }
    public static String join(String join, List<String> strList){
    	if(null==strList){
    		return "";
    	}
    	String[] strAry = (String[])strList.toArray(new String[strList.size()]);
    	return join(join,strAry);
    }

	public static boolean isNotNull(Object obj){
		try{
			if(null!=obj && obj instanceof String){
				return ((String) obj).trim().length()>0;
			}else{
				return false;
			}
		}catch (Exception e){
			return false;
		}
	}
    
}
