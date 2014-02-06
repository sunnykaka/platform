package com.rootrip.platform.common.web.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author liubin
 *
 */
public class ValidateCodeServlet extends HttpServlet {


	public ValidateCodeServlet() {
		super();
        width = 60;
        height = 20;
        codeCount = 4;
        x = 0;
    }

    public void init() throws ServletException {
        String strWidth = getInitParameter("width");
        String strHeight = getInitParameter("height");
        String strCodeCount = getInitParameter("codeCount");
        try
        {
            if(strWidth != null && strWidth.length() != 0)
                width = Integer.parseInt(strWidth);
            if(strHeight != null && strHeight.length() != 0)
                height = Integer.parseInt(strHeight);
            if(strCodeCount != null && strCodeCount.length() != 0)
                codeCount = Integer.parseInt(strCodeCount);
        }
        catch(NumberFormatException numberformatexception) { }
        x = width / (codeCount + 1);
        fontHeight = height - 2;
        codeY = height - 4;
    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        BufferedImage buffImg = new BufferedImage(width, height, 1);
        Graphics2D g = buffImg.createGraphics();
        Random random = new Random();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        Font font = new Font("Times New Roman", 1, fontHeight);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        g.setColor(Color.LIGHT_GRAY);
        for(int i = 0; i < 80; i++)
        {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuffer randomCode = new StringBuffer();
        int red = 0;
        int green = 0;
        int blue = 0;
        for(int i = 0; i < codeCount; i++)
        {
            String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
            red = random.nextInt(100);
            green = random.nextInt(20);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, i * this.x + this.x / 2, codeY);
            randomCode.append(strRand);
        }

        HttpSession session = request.getSession();
        session.setAttribute("validateCode", randomCode.toString());
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setContentType("image/jpeg");
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
    }

    private static final long serialVersionUID = 0x5365350e92735a2bL;
    private int width;
    private int height;
    private int codeCount;
    private int x;
    private int fontHeight;
    private int codeY;
    char codeSequence[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
        'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', 
        '4', '5', '6', '7', '8', '9'
    };
}
