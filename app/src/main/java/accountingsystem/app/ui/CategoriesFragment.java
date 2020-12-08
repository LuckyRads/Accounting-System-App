package accountingsystem.app.ui;

import accountingsystem.app.R;
import accountingsystem.app.model.Category;
import accountingsystem.app.model.Transaction;
import accountingsystem.app.model.TransactionType;
import accountingsystem.app.rest.util.RestUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    //region Private variables

    private ListView categoriesList;

    private List<Category> categories;

    private ListView transactionList;

    private List<Transaction> transactions;

    //endregion

    //region Functions

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        categoriesList = root.findViewById(R.id.categoriesList);
        transactionList = root.findViewById(R.id.transactionList);

        loadCategories();

        return root;
    }

    public void loadCategories() {
        CategoryService categoryService = new CategoryService("/category/categories");
        categoryService.execute("GET");
    }

    public void populateCategoriesList(String response) {
        try {
            TextView loggedInUser = getActivity().findViewById(R.id.navHeaderUser);
            categories = new ArrayList<>();
            ArrayList<String> categoriesString = new ArrayList<String>();
            JSONArray responseArray = new JSONArray(response);

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject object = (JSONObject) responseArray.get(i);
                Category category = new Category();

                category.setId(Long.parseLong(object.get("id").toString()));
                category.setName(object.get("name").toString());
                category.setDescription(object.get("description").toString());

                String responsiblePeopleString = object.get("responsiblePeople").toString();
                responsiblePeopleString = responsiblePeopleString.substring(1, responsiblePeopleString.length() - 1);

                boolean loggedInUserResponsible = false;

                for (String responsiblePersonEmail : responsiblePeopleString.split(",")) {
                    if (loggedInUserResponsible) break;
                    if (responsiblePersonEmail.contains(loggedInUser.getText().toString())) {
                        categories.add(category);
                        categoriesString.add(category.getName());
                        loggedInUserResponsible = true;
                    }
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, categoriesString);
            categoriesList.setAdapter(adapter);

            categoriesList.setOnItemClickListener((parent, view, position, id) -> {
                String categoryName = categoriesList.getItemAtPosition(position).toString();
                for (Category category : categories) {
                    if (category.getName().equalsIgnoreCase(categoryName)) {
                        loadTransactions(category.getId());
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Unable to parse category info.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadTransactions(long id) {
        TransactionService transactionService = new TransactionService("/transaction/transactions", "/" + id);
        transactionService.execute("GET");
    }

    public void populateTransactionList(String response) {
        try {
            transactions = new ArrayList<>();
            ArrayList<String> transactionsString = new ArrayList<String>();
            JSONArray responseArray = new JSONArray(response);

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject object = (JSONObject) responseArray.get(i);
                Transaction transaction = new Transaction();

                transaction.setId(Long.parseLong(object.get("id").toString()));
                transaction.setName(object.get("name").toString());
                transaction.setAmount(Double.parseDouble(object.get("amount").toString()));
                transaction.setDate(LocalDate.parse(object.get("date").toString()));
                transaction.setReceiver(object.get("receiver").toString());
                transaction.setSender(object.get("sender").toString());
                if (object.get("transactionType").toString().equalsIgnoreCase("EXPENSE")) {
                    transaction.setTransactionType(TransactionType.EXPENSE);
                } else {
                    transaction.setTransactionType(TransactionType.INCOME);
                }

                transactions.add(transaction);
                transactionsString.add(transaction.getTransactionType() + ": " + transaction.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, transactionsString);
            transactionList.setAdapter(adapter);

            transactionList.setOnItemClickListener((parent, view, position, id) -> {
                String transactionName = transactionList.getItemAtPosition(position).toString().split(":")[1].trim();
                for (Transaction transaction : transactions) {
                    if (transaction.getName().contains(transactionName)) {
                        String toastMessage = "Amount: " + transaction.getAmount() + "; Receiver: " + transaction.getReceiver() +
                                "; Sender: " + transaction.getSender() + "; Date:" + transaction.getDate();
                        Toast.makeText(this.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Unable to parse transaction info.", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    //region Threads

    private final class CategoryService extends AsyncTask<String, String, String> {

        private String requestUrl;
        private String urlParam;
        private String requestBody;

        public CategoryService() {
        }

        public CategoryService(String requestUrl) {
            this.requestUrl = requestUrl;
            this.urlParam = "";
        }

        public CategoryService(String requestUrl, String urlParam) {
            this.requestUrl = requestUrl;
            this.urlParam = urlParam;
        }

        public CategoryService(String requestUrl, String urlParam, String requestBody) {
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
                populateCategoriesList(result);
            } else {

            }
        }

    }

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
                System.out.println("else on post request");
            }
        }

    }

    //endregion

}
