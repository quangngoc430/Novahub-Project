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
            requestArrayList.add(new Request("\\/api\\/users", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/users\\/(\\d+)", new String[]{Method.PUT.name(), Method.DELETE.name()}));
            requestArrayList.add(new Request("\\/api\\/users\\/(\\d+)", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/users\\/me", new String[]{Method.GET.name(), Method.PUT.name()}));

            // categories
            requestArrayList.add(new Request("\\/api\\/categories", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/categories", new String[]{Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/categories\\/(\\d+)", new String[]{Method.PUT.name(), Method.DELETE.name()}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)\\/skills", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/categories\\/(\\d+)\\/skills", new String[]{Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)\\/skills\\/(\\d+)", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/categories\\/(\\d+)\\/skills\\/(\\d+)", new String[]{Method.PUT.name(), Method.DELETE.name()}));

            // skills
            requestArrayList.add(new Request("\\/api\\/skills", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/skills", new String[]{Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/skills\\/(\\d+)", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/skills\\/(\\d+)", new String[]{Method.PUT.name(), Method.DELETE.name()}));
            requestArrayList.add(new Request("\\/api\\/skills\\/(\\d+)\\/users", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/skills\\/me", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/skills\\/me\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));
            requestArrayList.add(new Request("\\/api\\/users\\/(\\d+)\\/skills", new String[]{Method.GET.name()}));

            // issues
            requestArrayList.add(new Request("\\/api\\/admin\\/issues", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/issues\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));
            requestArrayList.add(new Request("\\/api\\/admin\\/issues\\/(\\d+)\\/action", new String[]{Method.GET.name()}));
            requestArrayList.add(new Request("\\/api\\/issues\\/me", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/issues\\/me\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));

            // day-off
            requestArrayList.add(new Request("\\/api\\/day-offs", new String[]{Method.GET.name(), Method.POST.name()}));
            requestArrayList.add(new Request("\\/api\\/day-offs\\/(\\d+)", new String[]{Method.GET.name(), Method.PUT.name(), Method.DELETE.name()}));

            // day-of-type
            requestArrayList.add(new Request("\\/api\\/day-off-types", new String[]{Method.POST.name(), Method.PUT.name()}));
            requestArrayList.add(new Request("\\/api\\/day-off-types\\/(\\d+)", new String[]{Method.GET.name(), Method.DELETE.name()}));
        }

        return requestArrayList;
    }

}
