package vn.novahub.helpdesk.filter;

import vn.novahub.helpdesk.model.Request;

import java.util.ArrayList;

public class RequestsNeedAuthencationToken {

    private static ArrayList<Request> requestArrayList;

    public static ArrayList<Request> get() {
        if(requestArrayList == null) {
            requestArrayList = new ArrayList<>();

            // roles
            requestArrayList.add(new Request("\\/api\\/roles\\/(\\d+)", new String[]{"GET", "POST"}));
            requestArrayList.add(new Request("\\/api\\/roles", new String[]{"GET"}));
            // users
            requestArrayList.add(new Request("\\/api\\/users", new String[]{"GET", "POST"}));
            requestArrayList.add(new Request("\\/api\\/users\\/(\\d+)", new String[]{"GET", "PUT", "DELETE"}));
            requestArrayList.add(new Request("\\/api\\/users\\/me", new String[]{"GET", "PUT"}));
            // categories
            requestArrayList.add(new Request("\\/api\\/categories", new String[]{"GET", "POST"}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)", new String[]{"GET", "PUT", "DELETE"}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)\\/skills", new String[]{"GET", "POST"}));
            requestArrayList.add(new Request("\\/api\\/categories\\/(\\d+)\\/skills\\/(\\d+)", new String[]{"GET", "PUT", "DELETE"}));
            // skills
            requestArrayList.add(new Request("\\/api\\/skills", new String[]{"GET", "POST"}));
            requestArrayList.add(new Request("\\/api\\/skills\\/(\\d+)", new String[]{"GET", "PUT", "DELETE"}));
            requestArrayList.add(new Request("\\/api\\/skills\\/(\\d+)\\/users", new String[]{"GET"}));
            requestArrayList.add(new Request("\\/api\\/users\\/me\\/skills", new String[]{"GET", "POST"}));
            requestArrayList.add(new Request("\\/api\\/users\\/me\\/skills\\/(\\d+)", new String[]{"GET", "PUT", "DELETE"}));

        }

        return requestArrayList;
    }

}
