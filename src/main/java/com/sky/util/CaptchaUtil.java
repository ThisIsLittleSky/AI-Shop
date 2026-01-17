package com.sky.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 图片验证码生成工具类
 */
@Component
public class CaptchaUtil {

    @Autowired
    private RedisUtil redisUtil;

    // Redis key前缀
    private static final String CAPTCHA_KEY_PREFIX = "captcha:";
    
    // 验证码过期时间（5分钟）
    private static final long CAPTCHA_EXPIRE_MINUTES = 5;
    
    // 验证码字符集（排除容易混淆的字符：0、O、I、1、l）
    private static final String CODE_CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
    
    // 验证码图片宽度
    private static final int WIDTH = 120;
    
    // 验证码图片高度
    private static final int HEIGHT = 40;
    
    // 验证码字符数量
    private static final int CODE_LENGTH = 4;
    
    private final Random random = new Random();

    /**
     * 生成验证码并返回Base64编码的图片
     * 
     * @return CaptchaResult 包含验证码唯一标识和Base64图片
     */
    public CaptchaResult generateCaptcha() {
        // 生成唯一标识
        String captchaId = UUID.randomUUID().toString().replace("-", "");
        
        // 生成验证码字符串
        String code = generateCode();
        
        // 生成验证码图片
        BufferedImage image = createImage(code);
        
        // 将图片转换为Base64
        String base64Image = imageToBase64(image);
        
        // 存储验证码到Redis（5分钟过期）
        String redisKey = CAPTCHA_KEY_PREFIX + captchaId;
        redisUtil.set(redisKey, code.toLowerCase(), CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
        
        return new CaptchaResult(captchaId, base64Image);
    }

    /**
     * 生成验证码并返回图片字节数组
     * 
     * @return CaptchaByteResult 包含验证码唯一标识和图片字节数组
     */
    public CaptchaByteResult generateCaptchaBytes() {
        // 生成唯一标识
        String captchaId = UUID.randomUUID().toString().replace("-", "");
        
        // 生成验证码字符串
        String code = generateCode();
        
        // 生成验证码图片
        BufferedImage image = createImage(code);
        
        // 将图片转换为字节数组
        byte[] imageBytes = imageToBytes(image);
        
        // 存储验证码到Redis（5分钟过期）
        String redisKey = CAPTCHA_KEY_PREFIX + captchaId;
        redisUtil.set(redisKey, code.toLowerCase(), CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
        
        return new CaptchaByteResult(captchaId, imageBytes);
    }

    /**
     * 验证验证码是否正确
     * 
     * @param captchaId 验证码唯一标识
     * @param inputCode 用户输入的验证码
     * @return true表示验证码正确，false表示验证码错误或已过期
     */
    public boolean verifyCaptcha(String captchaId, String inputCode) {
        if (captchaId == null || inputCode == null) {
            return false;
        }
        
        String redisKey = CAPTCHA_KEY_PREFIX + captchaId;
        Object storedCode = redisUtil.get(redisKey);
        
        if (storedCode == null) {
            // 验证码不存在或已过期
            return false;
        }
        
        // 比较验证码（不区分大小写）
        boolean isValid = storedCode.toString().equalsIgnoreCase(inputCode.trim());
        
        // 验证成功后删除验证码（一次性使用）
        if (isValid) {
            redisUtil.delete(redisKey);
        }
        
        return isValid;
    }

    /**
     * 生成随机验证码字符串
     */
    private String generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
        }
        return code.toString();
    }

    /**
     * 创建验证码图片
     */
    private BufferedImage createImage(String code) {
        // 创建BufferedImage对象
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 设置抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 绘制干扰线
        drawInterferenceLines(g);
        
        // 绘制验证码字符
        drawCode(g, code);
        
        // 绘制噪点
        drawNoise(g);
        
        g.dispose();
        return image;
    }

    /**
     * 绘制验证码字符
     */
    private void drawCode(Graphics2D g, String code) {
        int charWidth = WIDTH / (CODE_LENGTH + 1);
        Font font = new Font("Arial", Font.BOLD, 28);
        g.setFont(font);
        
        for (int i = 0; i < code.length(); i++) {
            // 随机颜色
            g.setColor(getRandomColor());
            
            // 字符位置（上下随机偏移）
            int x = charWidth * (i + 1) - 10;
            int y = HEIGHT / 2 + random.nextInt(10) - 5;
            
            // 旋转角度
            double angle = (random.nextDouble() - 0.5) * 0.5;
            g.rotate(angle, x, y);
            
            // 绘制字符
            g.drawString(String.valueOf(code.charAt(i)), x, y);
            
            // 恢复旋转
            g.rotate(-angle, x, y);
        }
    }

    /**
     * 绘制干扰线
     */
    private void drawInterferenceLines(Graphics2D g) {
        // 绘制3-5条干扰线
        int lineCount = 3 + random.nextInt(3);
        for (int i = 0; i < lineCount; i++) {
            g.setColor(getRandomColor());
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 绘制噪点
     */
    private void drawNoise(Graphics2D g) {
        // 绘制20-30个噪点
        int noiseCount = 20 + random.nextInt(11);
        for (int i = 0; i < noiseCount; i++) {
            g.setColor(getRandomColor());
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g.fillOval(x, y, 2, 2);
        }
    }

    /**
     * 获取随机颜色（深色系，保证在白色背景上清晰可见）
     */
    private Color getRandomColor() {
        int r = random.nextInt(150);  // 0-150
        int g = random.nextInt(150);
        int b = random.nextInt(150);
        return new Color(r, g, b);
    }

    /**
     * 将BufferedImage转换为Base64字符串
     */
    private String imageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
    }

    /**
     * 将BufferedImage转换为字节数组
     */
    private byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
    }

    /**
     * 验证码结果类（Base64格式）
     */
    public static class CaptchaResult {
        private String captchaId;      // 验证码唯一标识
        private String base64Image;    // Base64编码的图片

        public CaptchaResult(String captchaId, String base64Image) {
            this.captchaId = captchaId;
            this.base64Image = base64Image;
        }

        public String getCaptchaId() {
            return captchaId;
        }

        public String getBase64Image() {
            return base64Image;
        }
    }

    /**
     * 验证码结果类（字节数组格式）
     */
    public static class CaptchaByteResult {
        private String captchaId;  // 验证码唯一标识
        private byte[] imageBytes; // 图片字节数组

        public CaptchaByteResult(String captchaId, byte[] imageBytes) {
            this.captchaId = captchaId;
            this.imageBytes = imageBytes;
        }

        public String getCaptchaId() {
            return captchaId;
        }

        public byte[] getImageBytes() {
            return imageBytes;
        }
    }
}

