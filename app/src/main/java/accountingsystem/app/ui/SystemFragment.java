package accountingsystem.app.ui;

import accountingsystem.app.R;
import accountingsystem.app.rest.util.RestUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Properties;

public class SystemFragment extends Fragment {

    //region Private variables

    private TextInputEditText companyInput;

    private TextInputEditText createdAtInput;

    private TextInputEditText versionInput;

    private Button updateBtn;

    //endregion

    //region Functions

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_system, container, false);

        companyInput = root.findViewById(R.id.companyInput);
        createdAtInput = root.findViewById(R.id.createdAtInput);
        versionInput = root.findViewById(R.id.versionInput);

        updateBtn = root.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(view -> updateSystemInfo());

        loadSystemInfo();

        return root;
    }

    public void loadSystemInfo() {
        SystemService systemService = new SystemService("/accounting-system");
        systemService.execute("GET");
    }

    public void setSystemInfo(String response) {
        System.out.println(response);
        Gson parser = new Gson();
        Properties data = parser.fromJson(response, Properties.class);

        String company = (String) data.get("company");
        String dateCreated = (String) data.get("dateCreated");
        String version = (String) data.get("version");

        companyInput.setText(company);
        createdAtInput.setText(dateCreated);
        versionInput.setText(version);
    }

    public void updateSystemInfo() {
        String requestBody = "{" +
                "\"company\": \"" + companyInput.getText() + "\"," +
                "\"dateCreated\": \"" + createdAtInput.getText() + "\"," +
                "\"version\": \"" + versionInput.getText() + "\"" +
                "}";
        SystemService systemService = new SystemService("/accounting-system", "", requestBody);
        systemService.execute("POST");
    }

    //endregion

    //region Threads

    private final class SystemService extends AsyncTask<String, String, String> {

        private String requestUrl;
        private String urlParam;
        private String requestBody;

        public SystemService() {
        }

        public SystemService(String requestUrl) {
            this.requestUrl = requestUrl;
            this.urlParam = "";
        }

        public SystemService(String requestUrl, String urlParam) {
            this.requestUrl = requestUrl;
            this.urlParam = urlParam;
        }

        public SystemService(String requestUrl, String urlParam, String requestBody) {
            this.requestUrl = requestUrl;
            this.urlParam = urlParam;
            this.requestBody = requestBody;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings[0] == "GET") {
                return RestUtil.executeGet(requestUrl, urlParam);
            } else if (strings[0] == "POST") {
                return RestUtil.executePost(requestUrl, urlParam, requestBody);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.contains("{")) {
                setSystemInfo(result);
            } else {
                Toast.makeText(getActivity(), "System information updated successfully.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //endregion

}