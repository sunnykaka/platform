package com.rootrip.platform.tag;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.apache.taglibs.standard.tag.common.core.Util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Reader;

/**
 * 重写JSTL的c:out标签，保留原功能的基础上增加四个功能：<br/>
 * <li>1、增加空格转换&nbsp;的属性(conBlank)：将传入的文本含有空格的转化为&nbsp; </li>
 * <li>2、增加去除HTML标签的属性(eraseHtml)：擦除文本中<XX>标签;</li>
 * <li>3、增加\n转换<br/>的属性(conBr)：将传入的文本中含有\n的转化为&lt;br/&gt;</li>
 * <li>4、增加字符串截断属性(size)：按字节截断，如果有格式化HTMl代码，仅截断HTML代码中的文字，HTML标签保留，默认为0不处理<br/>
 * 截断字符串时补足字符属性(ellipsis)：截断字符串时 会扣除本属性长度，默认为"..."，size大于0小于本属性长度时，显示完整本串。</li>
 * @author liubin
 * @date 2012-11-9
 *
 */
@SuppressWarnings("serial")
public class OutTag extends OutSupport {
    // 按字节截断字符串数
    private int mnSize;

    // 截断字符串后追加字符串
    private String msEllipsis;

    // 是否将\n转换为<br/>
    private boolean mbConBr;

    // 是否擦除html标签
    private boolean mbEraseHtml;

    // 是否替换空格
    private boolean mbConBlank;

    // 局部变量
    private boolean needBody;

    // 局部变量表示是否开始截断，用于保留HTML标签
    private boolean tbStartcut;

    // 局部变量，表示是否已经截断完毕
    private boolean tbCutend;

    /**
     * 输出值，由标签读入的属性
     *
     * @param value 输出值
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 默认值，由标签读入的属性
     *
     * @param def 默认值
     */
    public void setDefault(String def) {
        this.def = def;
    }

    /**
     * 是否格式化html标签，由标签读入的属性
     *
     * @param escapeXml 是否格式化html标签
     */
    public void setEscapeXml(boolean escapeXml) {
        this.escapeXml = escapeXml;
    }

    /**
     * 截断长度，由标签读入的属性
     *
     * @param size 截断长度
     */
    public void setSize(int size) {
        this.mnSize = size;
    }

    /**
     * 截断追加字符串，由标签读入的属性
     *
     * @param ellipsis 截断追加字符串
     */
    public void setEllipsis(String ellipsis) {
        this.msEllipsis = ellipsis;
    }

    /**
     * 是否转换回车，由标签读入的属性
     *
     * @param conBr 是否转换回车
     */
    public void setConBr(boolean conBr) {
        this.mbConBr = conBr;
    }

    /**
     * 是否替换空格，由标签读入的属性
     *
     * @param conBlank 是否替换空格
     */
    public void setConBlank(boolean conBlank) {
        this.mbConBlank = conBlank;
    }

    /**
     * 是否擦除html标签，由标签读入的属性
     *
     * @param eraseHtml 是否擦除html标签
     */
    public void setEraseHtml(boolean eraseHtml) {
        this.mbEraseHtml = eraseHtml;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.apache.taglibs.standard.tag.common.core.OutSupport#release()
      */

    public void release() {
        super.release();
        this.mnSize = 0;
        this.msEllipsis = "...";
        this.mbConBr = false;
        this.mbEraseHtml = false;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.apache.taglibs.standard.tag.common.core.OutSupport#doStartTag()
      */
    public int doStartTag() throws JspException {
        needBody = false; // reset state related to 'default'

        // 如果没有传入追加字符串，使用默认值
        if (msEllipsis == null) {
            msEllipsis = "...";
        }
        this.bodyContent = null; // clean-up body (just in case container is
        // pooling tag handlers)
        try {
            // print value if available; otherwise, try 'default'
            if (value != null) {
                outValue(pageContext, escapeXml, value);
                return SKIP_BODY;
            } else {
                // if we don't have a 'default' attribute, just go to the body
                if (def == null) {
                    needBody = true;
                    return EVAL_BODY_BUFFERED;
                }

                // if we do have 'default', print it
                outValue(pageContext, escapeXml, def);
                return SKIP_BODY;
            }
        } catch (IOException ex) {
            throw new JspException(ex.toString(), ex);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see org.apache.taglibs.standard.tag.common.core.OutSupport#doEndTag()
      */
    public int doEndTag() throws JspException {
        try {
            if (!needBody) {
                return EVAL_PAGE; // nothing more to do
            }

            // trim and print outValue the body
            if (bodyContent != null && bodyContent.getString() != null) {
                outValue(pageContext, escapeXml, bodyContent.getString());
            }
            return EVAL_PAGE;
        } catch (IOException ex) {
            throw new JspException(ex.toString(), ex);
        }
    }

    /**
     * 根据条件将obj处理后输出
     *
     * @param poPageContext pagecontext
     * @param pbEscapeXml   是否转换XMLchar
     * @param poObj         带输出的对象
     * @throws IOException 从reader中读取数据异常
     */
    private void outValue(PageContext poPageContext, boolean pbEscapeXml,
                          Object poObj) throws IOException {
        JspWriter toJspWriter = poPageContext.getOut();
        // 如果保留数大于0，将允许截断处理的标志置true
        boolean tbCut = mnSize > 0;
        if (tbCut) {
            // 开始处理时处置两个标志
            tbCutend = false;// 尚未截断完毕
            tbStartcut = true;// 开始截断
        }
        if (!pbEscapeXml) {
            StringBuffer tsBuffer = new StringBuffer();
            // write chars as is
            if (poObj instanceof Reader) {
                Reader toReader = (Reader) poObj;
                char[] toBuf = new char[4096];
                int tnCount;
                // 从流中读入全部字符
                while ((tnCount = toReader.read(toBuf, 0, 4096)) != -1) {
                    tsBuffer.append(toBuf, 0, tnCount);
                }
            } else {
                tsBuffer.append(poObj.toString());
            }
            String tsText = tsBuffer.toString();
            if (mbEraseHtml) {
                // 如果去除HTML标签 使用正则替换所有的<X>标签，忽略单一'<','>',空“<>”
                tsText = tsText.replaceAll("</?\\w+.*?>", "");
            }
            if (tbCut) {
                // 开始截断
                tsText = cutString(tsText.toCharArray(),
                    tsText.toCharArray().length);
            }
            if (mbConBlank) {
                // 如果需要转换空格，替换后输出
                tsText = tsText.replaceAll(" ", "&nbsp;");
            }
            if (mbConBr) {
                // 如果需要转换回车，替换后输出
                tsText = tsText.replaceAll("\r", "");
                tsText = tsText.replaceAll("\n", "<br/>");
            }
            toJspWriter.write(tsText);
        } else {
            // 转换XMLchars 不处理替换空格的操作
            // escape XML chars
            if (poObj instanceof Reader) {
                Reader toReader = (Reader) poObj;
                char[] toBuf = new char[4096];
                int tnCount;
                while ((tnCount = toReader.read(toBuf, 0, 4096)) != -1) {
                    if (tbCut) {
                        // 截断处理
                        String tsText = cutString(toBuf, tnCount);
                        outEscapedXml(tsText.toCharArray(), tsText.length(),
                            toJspWriter);
                    } else {
                        outEscapedXml(toBuf, tnCount, toJspWriter);
                    }
                }
            } else {
                String tsText = poObj.toString();
                if (tbCut) {
                    // 截断处理
                    tsText = cutString(tsText.toCharArray(), tsText
                        .toCharArray().length);
                }
                outEscapedXml(tsText.toCharArray(), tsText.length(),
                    toJspWriter);
            }
        }
    }

    /**
     * 输出并替换char数组
     *
     * @param poBuffer    缓冲
     * @param pnLength    长度
     * @param poJspWriter jspWriter
     * @throws IOException IO异常
     */
    private void outEscapedXml(char[] poBuffer, int pnLength,
                               JspWriter poJspWriter) throws IOException {
        int tnStart = 0;
        for (int i = 0; i < pnLength; ++i) {
            char tsC = poBuffer[i];
            if (tsC <= Util.HIGHEST_SPECIAL) {
                char[] tsEscaped = Util.specialCharactersRepresentation[tsC];
                if (tsEscaped != null) {
                    // add unescaped portion
                    if (tnStart < i) {
                        poJspWriter.write(poBuffer, tnStart, i - tnStart);
                    }
                    // add tsEscaped xml
                    poJspWriter.write(tsEscaped);
                    tnStart = i + 1;
                } else if (mbConBr && tsC == '\n') {
                    // 如果读入的字符是回车，并且允许转换的时候输出<br/>
                    // 首先输出回车前剩余的部分
                    if (tnStart < i) {
                        poJspWriter.write(poBuffer, tnStart, i - tnStart);
                    }
                    // 修改偏移值
                    tnStart = i + 1;
                    poJspWriter.write("<br/>");
                } else if (mbConBr && tsC == '\r') {
                    // 换行不予操作
                    // 首先输出回车前剩余的部分
                    if (tnStart < i) {
                        poJspWriter.write(poBuffer, tnStart, i - tnStart);
                    }
                    // 修改偏移值
                    tnStart = i + 1;
                } else if (mbConBlank && tsC == ' ') {
                    // 首先输出回车前剩余的部分
                    if (tnStart < i) {
                        poJspWriter.write(poBuffer, tnStart, i - tnStart);
                    }
                    // 修改偏移值
                    tnStart = i + 1;
                    poJspWriter.write("&nbsp;");
                }
            }
        }
        // add rest of unescaped portion
        if (tnStart < pnLength) {
            poJspWriter.write(poBuffer, tnStart, pnLength - tnStart);
        }
    }

    /**
     * 截取字符串
     *
     * @param poBuf    字符流缓冲
     * @param pnLength 字符流缓冲中有效长度
     * @return IO异常
     */
    private String cutString(char[] poBuf, int pnLength) {
        StringBuffer toBuffer = new StringBuffer();
        int sublength = msEllipsis.getBytes().length;
        if (!escapeXml) {
            for (int i = 0; i < pnLength; ++i) {
                char tsC = poBuf[i];
                switch (poBuf[i]) {
                    case '<':
                        // 如果没有设置去除HTML标签的情况下，保留<>中所有字符
                        if (!mbEraseHtml) {
                            toBuffer.append(tsC);
                        }
                        tbStartcut = false;
                        break;
                    case '>':
                        // 如果没有设置去除HTML标签的情况下，保留<>中所有字符
                        if (!mbEraseHtml) {
                            toBuffer.append(tsC);
                        }
                        tbStartcut = true;
                        break;
                    default:
                        // 如果没有设置去除HTML标签的情况下，保留<>中所有字符
                        if (!mbEraseHtml && !tbStartcut) {
                            toBuffer.append(tsC);
                            break;
                        }
                        // 截取完毕不予处理以下方法
                        if (tbCutend) {
                            break;
                        }
                        // 如果允许截取，但是剩余字符长度已经小于补充字符的长度，补充的字符串
                        if (mnSize <= sublength) {
                            toBuffer.append(msEllipsis);
                            tbCutend = true;
                            break;
                        }
                        if (tsC < 128) {
                            toBuffer.append(tsC);
                            --mnSize;
                        } else {
                            // 如果是非ASCII码，采用双字节方式读入
                            --mnSize;
                            --mnSize;
                            // 读入前判断如果加上之后是否仍有空间增加补充的字符串
                            if (mnSize >= sublength) {
                                toBuffer.append(tsC);
                            } else {
                                toBuffer.append(msEllipsis);
                                tbCutend = true;
                            }
                        }
                        break;
                }
            }
        } else {
            for (int i = 0; i < pnLength; ++i) {
                char tsC = poBuf[i];
                if (!tbCutend) {
                    // 如果允许截取，但是剩余字符长度已经小于补充字符的长度，补充的字符串
                    if (mnSize <= sublength) {
                        toBuffer.append(msEllipsis);
                        tbCutend = true;
                    } else if (tsC < 128) {
                        --mnSize;
                        toBuffer.append(tsC);
                    } else {
                        // 如果是非ASCII码，采用双字节方式读入
                        --mnSize;
                        --mnSize;
                        // 读入前判断如果加上之后是否仍有空间增加补充的字符串
                        if (mnSize >= sublength) {
                            toBuffer.append(tsC);
						} else {
							toBuffer.append(msEllipsis);
							tbCutend = true;
						}
					}
				}
			}
		}
		return toBuffer.toString();
	}
}
