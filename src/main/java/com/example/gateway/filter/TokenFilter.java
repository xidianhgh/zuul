package com.example.gateway.filter;

import com.example.gateway.dao.GroupMemberMapper;
import com.example.gateway.entity.GroupMemberEntity;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TokenFilter extends ZuulFilter {

    @Override
    public String filterType() {
  /*
        pre：可以在请求被路由之前调用
        route：在路由请求时候被调用
        post：在route和error过滤器之后被调用
        error：处理请求时发生错误时被调用
        * */
        // 前置过滤器
        return FilterConstants.PRE_TYPE;    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
//        return true;
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        //只过滤OPTIONS 请求
        if(request.getMethod().equals(RequestMethod.OPTIONS.name())){

            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {

        //SpringCloud网关修改请求头使微服务获取客户端真实ip
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        String remoteAddr = request.getRemoteAddr();
//        ctx.getZuulRequestHeaders().put("HTTP_X_FORWARDED_FOR", remoteAddr);
//
//        System.out.println("China");
//        RequestContext currentContext = RequestContext.getCurrentContext();
//        String token = currentContext.getRequest().getHeader("TOKEN");
//        if (StringUtils.isEmpty(token)) {
//            System.out.println("token is null");
//        }

        //跨域
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        HttpServletRequest request = ctx.getRequest();
//        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Headers","authorization, content-type, Authorization, authentication");
        response.setHeader("Access-Control-Allow-Methods","POST,GET,DELETE,PUT");
        response.setHeader("Access-Control-Expose-Headers","X-forwared-port, X-forwarded-host");
        response.setHeader("Vary","Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
        //不再路由
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(200);
        return null;

    }
}
