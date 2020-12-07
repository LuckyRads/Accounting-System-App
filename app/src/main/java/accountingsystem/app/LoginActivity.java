package accountingsystem.app;

import accountingsystem.app.rest.util.RestUtil;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    //region Private variables

    private EditText usernameField;

    private EditText passwordField;

    private Button loginBtn;

    //endregion

    //region Override methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.emailField);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(view -> login());
    }

    //endregion

    //region Functions

    public void login() {
        String requestBody = "{" +
                "\"username\": \"" + usernameField.getText() + "\"," +
                "\"password\": \"" + passwordField.getText() + "\"" +
                "}";
        LoginService loginService = new LoginService("/user/validate", "", requestBody);
        loginService.execute("POST");
    }

    public void continueIfValidated(String response) {
        if (response.equalsIgnoreCase("success")) {
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("loggedInUser", usernameField.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    //region Threads

    private final class LoginService extends AsyncTask<String, String, String> {

        private String requestUrl;
        private String urlParam;
        private String requestBody;

        public LoginService() {
        }

        public LoginService(String requestUrl, String urlParam) {
            this.requestUrl = requestUrl;
            this.urlParam = urlParam;
        }

        public LoginService(String requestUrl, String urlParam, String requestBody) {
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
            continueIfValidated(result);
        }

    }

    //endregion

}
