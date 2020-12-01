package accountingsystem.app.rest.service;

import accountingsystem.app.rest.util.RestUtil;
import android.os.AsyncTask;

public class PeopleService extends AsyncTask<String, String, String> {

    private String requestUrl;
    private String urlParam;
    private String requestBody;
    private String response;

    public PeopleService() {
    }

    public PeopleService(String requestUrl, String urlParam) {
        this.requestUrl = requestUrl;
        this.urlParam = urlParam;
    }

    public PeopleService(String requestUrl, String urlParam, String requestBody) {
        this.requestUrl = requestUrl;
        this.urlParam = urlParam;
        this.requestBody = requestBody;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (strings[0] == "GET") {
            return RestUtil.executeGet(requestUrl, urlParam);
        } else if (strings[0] == "POST") {
            // TODO: post requests
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        response = result;
        System.out.println(result); // TODO: delete logs
    }

}
