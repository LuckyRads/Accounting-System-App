package accountingsystem.app.ui;

import accountingsystem.app.R;
import accountingsystem.app.rest.util.RestUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TransactionFragment extends Fragment {

    //region Private variables

    private ListView transactionList;

    private Long categoryId;

    //endregion

    //region Functions

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        categoryId = getArguments().getLong("categoryId");
        System.out.println("tarans " + categoryId);

        transactionList = root.findViewById(R.id.transactionList);

        loadTransactions();

        return root;
    }

    public void loadTransactions() {
        TransactionService transactionService = new TransactionService("/transaction/transactions", "/" + categoryId);
        transactionService.execute("GET");
    }

    public void populateTransactionList(String response) {
        System.out.println(response);
//        try {
//            TextView loggedInUser = getActivity().findViewById(R.id.navHeaderUser);
//            categories = new ArrayList<>();
//            ArrayList<String> categoriesString = new ArrayList<String>();
//            JSONArray responseArray = new JSONArray(response);
//
//            for (int i = 0; i < responseArray.length(); i++) {
//                JSONObject object = (JSONObject) responseArray.get(i);
//                Category category = new Category();
//
//                category.setId(Long.parseLong(object.get("id").toString()));
//                category.setName(object.get("name").toString());
//                category.setDescription(object.get("description").toString());
//
//                String responsiblePeopleString = object.get("responsiblePeople").toString();
//                responsiblePeopleString = responsiblePeopleString.substring(1, responsiblePeopleString.length() - 1);
//
//                boolean loggedInUserResponsible = false;
//
//                for (String responsiblePersonEmail : responsiblePeopleString.split(",")) {
//                    if (loggedInUserResponsible) break;
//                    if (responsiblePersonEmail.contains(loggedInUser.getText().toString())) {
//                        categories.add(category);
//                        categoriesString.add(category.getName());
//                        loggedInUserResponsible = true;
//                    }
//                }
//            }
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, categoriesString);
//            categoriesList.setAdapter(adapter);
//
//            categoriesList.setOnItemClickListener((parent, view, position, id) -> {
//                String categoryName = categoriesList.getItemAtPosition(position).toString();
//                for (Category category : categories) {
//                    if (category.getName().equalsIgnoreCase(categoryName)) {
//
//                    }
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "Unable to parse category info.", Toast.LENGTH_SHORT).show();
//        }
    }

    //endregion

    //region Threads

    private final class TransactionService extends AsyncTask<String, String, String> {

        private String requestUrl;
        private String urlParam;
        private String requestBody;

        public TransactionService() {
        }

        public TransactionService(String requestUrl) {
            this.requestUrl = requestUrl;
            this.urlParam = "";
        }

        public TransactionService(String requestUrl, String urlParam) {
            this.requestUrl = requestUrl;
            this.urlParam = urlParam;
        }

        public TransactionService(String requestUrl, String urlParam, String requestBody) {
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
                populateTransactionList(result);
            } else {

            }
        }

    }

    //endregion

}
