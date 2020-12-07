package accountingsystem.app.ui;

import accountingsystem.app.R;
import accountingsystem.app.rest.util.RestUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserFragment extends Fragment {

    //region Private variables

    private long userId;

    private TextInputEditText emailField;

    private TextInputEditText passwordField;

    private TextInputEditText nameField;

    private Button updateBtn;

    //endregion

    //region Functions

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        emailField = root.findViewById(R.id.emailField);
        passwordField = root.findViewById(R.id.passwordField);
        nameField = root.findViewById(R.id.nameField);

        updateBtn = root.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(view -> updateUserInfo());

        loadUserInfo();

        return root;
    }

    public void loadUserInfo() {
        TextView loggedInUserType = getActivity().findViewById(R.id.navHeaderUserType);
        UserService userService = null;
        if (loggedInUserType.getText().toString().equalsIgnoreCase("person")) {
            userService = new UserService("/person/people");
        } else {
            userService = new UserService("/company/companies");
        }
        userService.execute("GET");
    }

    public void setUserInfo(String response) {
        TextView loggedInUser = getActivity().findViewById(R.id.navHeaderUser);
        try {
            JSONArray responseArray = new JSONArray(response);

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject object = (JSONObject) responseArray.get(i);
                if (loggedInUser.getText().equals(object.get("email"))) {
                    emailField.setText(object.get("email").toString());
                    passwordField.setText(object.get("password").toString());
                    nameField.setText(object.get("name").toString());
                    userId = Long.parseLong(object.get("id").toString());
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Unable to parse user info.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUserInfo() {
        String requestBody = "{" +
                "\"email\": \"" + emailField.getText() + "\"," +
                "\"password\": \"" + passwordField.getText() + "\"," +
                "\"name\": \"" + nameField.getText() + "\"" +
                "}";
        TextView loggedInUserType = getActivity().findViewById(R.id.navHeaderUserType);
        UserService userService = new UserService("/" + loggedInUserType.getText().toString().toLowerCase(), "/" + userId, requestBody);
        userService.execute("POST");
    }

    //endregion

    //region Threads

    private final class UserService extends AsyncTask<String, String, String> {

        private String requestUrl;
        private String urlParam;
        private String requestBody;

        public UserService() {
        }

        public UserService(String requestUrl) {
            this.requestUrl = requestUrl;
            this.urlParam = "";
        }

        public UserService(String requestUrl, String urlParam) {
            this.requestUrl = requestUrl;
            this.urlParam = urlParam;
        }

        public UserService(String requestUrl, String urlParam, String requestBody) {
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
                setUserInfo(result);
            } else {
                Toast.makeText(getActivity(), "Account information updated successfully.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //endregion

}