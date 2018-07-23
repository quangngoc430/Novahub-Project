package vn.novahub.helpdesk.config;

import vn.novahub.helpdesk.model.Request;

import java.util.ArrayList;

public class RequestsNeedAuthencationToken {

    private static ArrayList<Request> requestArrayList;

    private enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }
    
    
    public static ArrayList<Request> get() {
        if(requestArrayList == null) {
            requestArrayList = new ArrayList<>();

            // roles
            requestArrayList.add(new Request("\\/api\\/roles\\/(\\d+)", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/roles", new String[]{Method.GET.name()}));
            // users
            requestArrayList.add(new Request("\\/api\\/users", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/users\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));
            requestArrayList.add(new Request("\\/api\\/users\\/me", new String[]{Method.GET.name(), Method.PUT.name()}));
            // categories
            requestArrayList.add(new Request("\\/api\\/categories", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)\\/skills", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)\\/skills\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));
            // skills
            requestArrayList.add(new Request("\\/api\\/skills", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/skills\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));
            requestArrayList.add(new Request("\\/api\\/skills\\/(\\d+)\\/users", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/users\\/me\\/skills", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/users\\/me\\/skills\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));

        }

        return requestArrayList;
    }

}
