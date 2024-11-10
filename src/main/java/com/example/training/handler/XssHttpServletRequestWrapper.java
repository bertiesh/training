package com.example.training.handler;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Xuxinyuan
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request)
    {
        super(request);
    }

    public  String level="none";

    @Override
    public String[] getParameterValues(String name)
    {
        String[] values = super.getParameterValues(name);
        if (values != null)
        {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++)
            {
                //防xss攻击和过滤前后空格
                //Jsoup.clean(strHTML, Whitelist.none().addTags("div"));
                //1)： none()
                //该API会清除所有HTML标签，仅保留文本节点。
                //2)： simpleText()
                //该API仅会保留b, em, i, strong, u 标签，除此之外的所有HTML标签都会被清除。
                //3)： basic()
                //该API会保留 a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, span, strike, strong, sub, sup, u, ul 和其适当的属性标签，除此之外的所有HTML标签都会被清除，且该API不允许出现图片(img tag)。另外该API中允许出现的超链接中可以允许其指定http, https, ftp, mailto 且在超链接中强制追加rel=nofollow属性。
                //4)： basicWithImages()
                //该API在保留basic()中允许出现的标签的同时也允许出现图片(img tag)和img的相关适当属性，且其src允许其指定 http 或 https。
                //5)： relaxed()
                //该API仅会保留 a, b, blockquote, br, caption, cite, code, col, colgroup, dd, div, dl, dt, em, h1, h2, h3,
                // h4, h5, h6, i, img, li, ol, p, pre, q, small, span, strike, strong, sub, sup, table, tbody, td, tfoot,
                // th, thead, tr, u, ul 标签，除此之外的所有HTML标签都会被清除，且在超链接中不会强制追加rel=nofollow属性。
                switch (level) {
                    case "none":
                        escapseValues[i] = Jsoup.clean(values[i], Whitelist.none()).trim();
                        break;
                    case "simpleText":
                        escapseValues[i] = Jsoup.clean(values[i], Whitelist.simpleText()).trim();
                        break;
                    case "basic":
                        escapseValues[i] = Jsoup.clean(values[i], Whitelist.basic()).trim();
                        break;
                    case "basicWithImages":
                        escapseValues[i] = Jsoup.clean(values[i], Whitelist.basicWithImages()).trim();
                        break;
                    default:  //relaxed
                        escapseValues[i] = Jsoup.clean(values[i], Whitelist.relaxed()).trim();
                        break;
                }

            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }
}
