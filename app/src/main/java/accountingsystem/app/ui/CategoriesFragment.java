package accountingsystem.app.ui;

import accountingsystem.app.R;
import accountingsystem.app.model.Category;
import accountingsystem.app.rest.util.RestUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    //region Private variables

    private ListView categoriesList;

    private List<Category> categories;

    //endregion

    //region Functions

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        categoriesList = root.findViewById(R.id.categoriesList);

        loadCategories();

        return root;
    }

    public void loadCategories() {
        CategoryService categoryService = new CategoryService("/category/categories");
        categoryService.execute("GET");
    }

    public void populateCategoriesList(String response) {
        try {
            categories = new ArrayList<>();
            ArrayList<String> categoriesString = new ArrayList<String>();
            JSONArray responseArray = new JSONArray(response);

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject object = (JSONObject) responseArray.get(i);
                Category category = new Category();

                category.setId(Long.parseLong(object.get("id").toString()));
                category.setName(object.get("name").toString());
                category.setDescription(object.get("description").toString());

                categories.add(category);
                categoriesString.add(category.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, categoriesString);
            categoriesList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Unable to parse category info.", Toast.LENGTH_SHORT).show();
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

    //endregion

}
