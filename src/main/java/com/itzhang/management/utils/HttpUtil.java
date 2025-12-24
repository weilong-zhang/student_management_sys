package com.itzhang.management.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class HttpUtil {

    /**
     * @param request
     * @return java.lang.String
     * @Description
     * @Author weiloong_zhang
     */
    public String getIp(HttpServletRequest request) {
        if (request == null) return "unknown";

        // 先从 X-Forwarded-For 获取（可能经过代理）
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.contains(",")) {
                ip = ip.split(",")[0]; // 多个 IP 取第一个
            }
            return ip.trim();
        }

        //再尝试 X-Real-IP
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        //最后使用 request.getRemoteAddr()
        return request.getRemoteAddr();
    }
}
