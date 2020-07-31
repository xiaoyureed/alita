package io.github.xiaoyureed.alita.alitamvc.impl;

import io.github.xiaoyureed.alita.alitamvc.MvcThreadLocal;
import io.github.xiaoyureed.alita.alitamvc.Render;
import io.github.xiaoyureed.alita.alitamvc.consts.Constants;
import io.github.xiaoyureed.alita.alitamvc.exception.MvcContextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jsp render
 */
public class JspRender implements Render {

    public static final Logger log = LoggerFactory.getLogger(JspRender.class);

    @Override
    public void render(String view) {
        // get req and resp from threadLocal
        HttpServletRequest req = (HttpServletRequest) MvcThreadLocal.get(Constants.CONTEXT_KEY_REQUEST);
        HttpServletResponse resp = (HttpServletResponse) MvcThreadLocal.get(Constants.CONTEXT_KEY_RESPONSE);
        if (req == null) {
            throw new MvcContextException("MvcThreadLocal doesn't contain req!");
        }

        try {
            req.getRequestDispatcher(generatePath(view)).forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private String generatePath(String view) {
        return Constants.VIEW_PREFIX_DEFAULT + Constants.SLASH + view + Constants.VIEW_SUFFIX_DEFAULT;
    }
}
