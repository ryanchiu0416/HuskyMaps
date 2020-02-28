package huskymaps.handlers;

import spark.Request;
import spark.Response;

public class RedirectAPIHandler extends APIRouteHandler<Void, Boolean> {
    @Override
    protected Void parseRequest(Request request) {
        return null;
    }

    @Override
    protected Boolean processRequest(Void request, Response response) {
        response.redirect("/map.html", 301);
        return true;
    }

    @Override
    protected Object buildJsonResponse(Boolean result) {
        return true;
    }
}
