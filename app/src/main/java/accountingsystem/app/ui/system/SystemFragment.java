package accountingsystem.app.ui.system;

import accountingsystem.app.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;

public class SystemFragment extends Fragment {

    private TextInputEditText companyInput;

    private TextInputEditText createdAtInput;

    private TextInputEditText versionInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_system, container, false);

        companyInput = root.findViewById(R.id.companyInput);
        createdAtInput = root.findViewById(R.id.createdAtInput);
        versionInput = root.findViewById(R.id.versionInput);

        companyInput.setText("kompanija");

        return root;
    }

}