package accountingsystem.app.ui.people;

import accountingsystem.app.R;
import accountingsystem.app.rest.service.PeopleService;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class PeopleFragment extends Fragment {

    private ListView peopleList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_people, container, false);

        peopleList = root.findViewById(R.id.peopleList);

        List<String> arrayList = new ArrayList();
        arrayList.add("aaa");
        arrayList.add("bbb");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        arrayAdapter.add("aaa");
        arrayAdapter.add("b11111111");
        arrayAdapter.notifyDataSetChanged();
        peopleList.setAdapter(arrayAdapter);

        PeopleService peopleService = new PeopleService("/person/people", "");
        peopleService.execute("GET");

        return root;
    }

    public void populatePeopleList() {

    }

}