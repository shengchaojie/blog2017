package com.scj.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by shengcj on 2016/7/29.
 */
public class LoginStatusInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStatusInterceptor.class);

    /*@Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        String uid = null;
        if ((uid = session.getAttribute(CommonConstants.USER_ID_ENCODE).toString()) != null&&isUIDCorrect(uid)) {
            return true;
        }
        LOGGER.debug("登录拦截时,session信息不存在");

        Cookie[] cookies = request.getCookies();
        Cookie userCookie = null;
        for (Cookie cookie : cookies) {
            if (CommonConstants.USER_ID_ENCODE.equals(cookie.getName())) {
                userCookie = cookie;
                break;
            }
        }
        if (userCookie == null) {
            // TODO: 2016/7/29 可以把这些路径放到一个公共类中
            LOGGER.info("访问{}需要登录态",request.getRequestURI());
            response.sendRedirect(request.getContextPath() + "/user/view/login");
        } else {
            uid = userCookie.getValue();

            return isUIDCorrect(uid);
        }

        return false;
    }

    private boolean isUIDCorrect(String uid)
    {
        String[] params = uid.split("\\|");
        if (params.length >= 3) {
            String username = params[0];
            String expireTime = params[1];
            int hash = Integer.parseInt(params[2]);
            String password = userService.getUserByUsername(username).getPassword();

            if (hash == CommonUtil.generateUIDHash(username, password, expireTime, BlogContext.getSalt())) {
                return true;
            }
        }

        return false;
    }*/

}