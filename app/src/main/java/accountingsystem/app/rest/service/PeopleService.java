package accountingsystem.app.rest.service;

import accountingsystem.app.rest.util.RestUtil;
import android.os.AsyncTask;

public class PeopleService extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            return RestUtil.executeGet("/person/people", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while getting data from web";
        }
    }

}
