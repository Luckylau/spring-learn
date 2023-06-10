package luckylau.spring.session.security;

import luckylau.spring.session.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * @Author luckylau
 * @Date 2021/9/25
 */
public class DefaultAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public static final String POST = "POST";

    public DefaultAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!POST.equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            throw new AuthenticationServiceException(
                    "Authentication contentType not found");
        }
        MediaType mediaType = MediaType.parseMediaType(contentType);
        if (!MediaType.APPLICATION_JSON.includes(mediaType)) {
            throw new AuthenticationServiceException(
                    "Authentication contentType only supported: " + MediaType.APPLICATION_JSON.toString());
        }

        String body = getRequestBody(request);
        Map<String, String> jsonObject = JsonUtils.jsonToMap(body, String.class, String.class);
        if (jsonObject == null) {
            throw new AuthenticationServiceException(
                    "Authentication params error");
        }
        String username = jsonObject.get("username");
        String password = jsonObject.get("password");

        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            throw new AuthenticationServiceException("用户名或密码不能为空");
        }

        username = username.trim();
        password = password.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * 用户名和密码都是requestBody中取出
     *
     * @param request
     * @return
     */
    private String getRequestBody(HttpServletRequest request) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            logger.error("httpServletRequest get body error", e);
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("httpServletRequest close io error", e);
                }
            }
        }
        return sb.toString();

    }

    private void setDetails(HttpServletRequest request,
                            UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
