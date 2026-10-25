package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class BaseController {
    protected String getTraceId(HttpServletRequest request) {
        String traceId = (String) request.getAttribute("traceId");
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }
}
